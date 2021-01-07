package clientServer;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/*
 * Le thread client ouvre les stream I/O pour un client, demande son nom,
 * informe les clients connectés sur le server des nouvelles connexions.
 * Tant que l'on recoit des données, on les transmets aux autres clients. 
 * Quand un client quitte le chat, le thread informe les autres clients et se termine.
 */
class ClientThread extends Thread {

  private DataInputStream dataInputStream = null;
  private BufferedReader inputStream = null;
  private PrintStream outputStream = null;
  private Socket clientSocket = null;
  private final ClientThread[] threads;
  private int maxClients;

  public ClientThread(Socket clientSocket, ClientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    maxClients = threads.length;
  }

  public void run() {
    int maxClients = this.maxClients;
    ClientThread[] threads = this.threads;

    try {
    	dataInputStream = new DataInputStream(clientSocket.getInputStream());
    	inputStream = new BufferedReader(new InputStreamReader(dataInputStream)); //on place dataInputStream dans un bufferedReader car DataInputStream.readLine() est deprecated
    	outputStream = new PrintStream(clientSocket.getOutputStream());
      
      /* On demande le nom au client */
      outputStream.println("Entrez votre nom: ");
      String name = inputStream.readLine().trim();
      outputStream.println("* Salut " + name + " ! * \nPour quitter le chat, tapez simplement bye \n");
      for (int i = 0; i < maxClients; i++) {
        if (threads[i] != null && threads[i] != this) {
          threads[i].outputStream.println("* " + name + " vient d'entrer dans le chat *");
        }
      }
      while (true) {
        String line = inputStream.readLine();
        if (line.startsWith("bye")) {
          break;
        }
        for (int i = 0; i < maxClients; i++) {
          if (threads[i] != null) {
            threads[i].outputStream.println(name + " : " + line);
          }
        }
      }
      for (int i = 0; i < maxClients; i++) {
        if (threads[i] != null && threads[i] != this) {
          threads[i].outputStream.println(name + " a quitté le chat");
        }
      }
      outputStream.println("* Aurevoir " + name + " *");

      /*
       * On place la variable thread courant à null pour permettre la connexion
       * d'un nouveau client au server.
       */
      for (int i = 0; i < maxClients; i++) {
        if (threads[i] == this) {
          threads[i] = null;
        }
      }

      /* On ferme le stream output, input et on ferme la socket */
      inputStream.close();
      outputStream.close();
      clientSocket.close();
    } catch (IOException e) {
   	 	System.err.println(e);
    }
  }
}
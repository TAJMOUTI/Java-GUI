package clientServer;
 
import java.io.*;
import java.net.*;

/*
 * Le serveur de chat qui d�livre des messages publics et priv�s
 */
public class Server {

  private static ServerSocket serverSocket = null; // socket serveur
  private static Socket clientSocket = null; // socket client
  private static final int maxClients = 10; // Le serveur ne peut accepter qu'un nombre de client maximum: ici 10.
  private static final ClientThread[] threads = new ClientThread[maxClients];

  public static void main(String args[]) {
    int port = 8888;
    if (args.length < 1) {
    	 System.out.println("Serveur lanc� sur le port "+ port + "\n");
    } else {
    	port = Integer.valueOf(args[0]).intValue();
    }

    /*
     * Ouvre une socket serveur sur le port sp�cifi�
     */
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println(e);
    }

    /*
     * Cr�e un client socket pour chacune des connections et le passe � un nouveau client.
     */
    while (true) {
      try {
        clientSocket = serverSocket.accept();
        int i = 0;
        for (i = 0; i < maxClients; i++) {
          if (threads[i] == null) {
            (threads[i] = new ClientThread(clientSocket, threads)).start();
            break;
          }
        }
        /* On annule la cr�ation de socket quand le nombre max  de clients est atteint */
        if (i == maxClients) {
          PrintStream os = new PrintStream(clientSocket.getOutputStream());
          os.println("Le serveur est trop occup� pour le moment. R�essayez plus tard.");
          os.close();
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}



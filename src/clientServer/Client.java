package clientServer;

import java.io.*;
import java.net.*;

public class Client implements Runnable {

  private static Socket socket = null; // client socket
  private static PrintStream outputStream = null; // output
  private static DataInputStream dataInputStream = null; // input
  private static BufferedReader inputStream = null;
  private static BufferedReader input = null;
  private static boolean closed = false;
  
  public static void main(String[] args) {

    int port = 8888;
    String host = "localhost";

    if (args.length < 2) {
      System.out.println("Client connecté à " + host + " sur le port " + port + "\n");
    } else {
      host = args[0];
      port = Integer.valueOf(args[1]).intValue();
    }

    /*
     * Ouvre une socket sur l'hôte et le port spécifié.
     * Ouvre les stream I/O.
     */
    try {
		socket = new Socket(host, port);
		input = new BufferedReader(new InputStreamReader(System.in));
		outputStream = new PrintStream(socket.getOutputStream());
		dataInputStream = new DataInputStream(socket.getInputStream());
		inputStream = new BufferedReader(new InputStreamReader(dataInputStream)); 
    } catch (UnknownHostException e) {
      System.err.println("Ne reconnait pas l'hôte " + host);
    } catch (IOException e) {
      System.err.println("Connection impossible à l'hôte " + host);
    }
    
    /* Si tout a été bien initialisé, on écrit des données vers la socket */
    if (socket != null && outputStream != null && inputStream != null) {
      try {
    	  
        /* Crée un thread à lire depuis le serveur */
        new Thread(new Client()).start();
        while (!closed) {
        	outputStream.println(input.readLine().trim());
        }
        
        /* Ferme le stream Output et Input puis ferme la socket */
        outputStream.close();
        inputStream.close();
        socket.close();
        
     } catch (IOException e) {
        System.err.println("IOException:  " + e);
      }
    }
  }

  
  /*
   * Crée un thread à lire depuis le serveur
   */
  public void run() {
    String response;
    /*
     * Continue à lire depuis la socket jusqu'à recevoir "* Aurevoir" depuis le serveur.
     * Une fois le message reçu, on break.
     */
    try {
      while ((response = inputStream.readLine()) != null) {
        System.out.println(response);
        if (response.indexOf("* Aurevoir") != -1)
          break;
      }
      closed = true;
    } catch (IOException e) {
      System.err.println("IOException: " + e);
    }
  }
}
//Parametrer la connexion au serveur bdd
package Bdd;

public class Parametre {
	public static String IPHOST = "127.0.0.1";
	public static String HOST_DB = "jdbc:mysql://" + IPHOST + ":3306/bddchat";
	public static String USERNAME_DB = "root";
	public static String PASSWORD_DB = "";
	public static int PORT = 11111;
	public static String USER;//Utilisateur qu'on rentre nous-meme dans phpmyadmin
}



package Bdd;

import java.net.Socket;
import java.sql.*;

public class BDD {
	
	Connection connection;
	Statement statement;
	String SQL;
	String username;
	String password;
    Socket client;
    String url;
    int Port;
    String Host;
//Constructeur qui initialise les param�tres de connexion
    
    public BDD(String url, String username, String password, String Host, int Port) {

    	this.url = url;
    	this.username = username;
    	this.password = password;
    	this.Host = Host;
    	this.Port = Port;
    	
	    }
//On se connecte a la BDD
    
	 public Connection connexionDatabase() {


	  try {
	            Class.forName("com.mysql.jdbc.Driver");//Demande d'utiliser mysql au format jdbc(java data base connector)
	            connection = DriverManager.getConnection(url, username, password);//L'objet DriverManager va se connecter grace � getConnection
	            
	        	} catch (Exception e) 
	{System.err.println(e);//print a cause d'une eventuelle erreur
	        }
	        return connection;
	    }
	 
//Savoir si un utilisateur s'est deconnect� = fermeture du port de connexion � la bdd

	 public Connection closeconnexion() {

	        try {
	            connection.close();
	        } catch (Exception e) {System.err.println(e);//
	        }
	        return connection;
	    }

	    public ResultSet ex�cutionQuery(String sql) {
	        connexionDatabase();
	        ResultSet resultSet = null;
	        try {
	            statement = connection.createStatement();//l'etat de la connexion(en ligne/deconnect�)
	            resultSet = statement.executeQuery(sql);//attrape le resultat que nous rend la bdd(
	            System.out.println(sql);
	        } catch (SQLException ex) {System.err.println(ex);//
	        }
	        return resultSet;
	    }

	    public ResultSet querySelectAll(String nomTable, String �tat) {

	        connexionDatabase();
	        SQL = "SELECT * FROM " + nomTable + " WHERE " + �tat;
	        return this.ex�cutionQuery(SQL);

	    }

}

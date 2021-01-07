import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.awt.event.ActionEvent;

public class ChatPageCustom implements Runnable{
	//Socket = Permet de communiquer entre les couches de l'ordinateur
	//Thread = Permet de faire plusieurs taches en meme temps, (exemple tourner le boucle infini et lire en meme temps le programme pricipale
	private JFrame frmSimplet;
	private Socket s;
	private JTextArea textArea_Recevoir;
	private String Adresse = "127.0.0.1";
	private int PORT = 5000;
	private OutputStream o;// Stream d'envoi de données au serveur
	private InputStream i; // Stream de reception de données depuis le serveur
	private String Titre;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatPageCustom window = new ChatPageCustom();
					window.frmSimplet.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	// Constructeur qui sert au Thread qui tourne continuellement en attendant un message à recevoir
	public ChatPageCustom(Socket s, JTextArea textArea_Recevoir) {
		super();
		this.s = s;
		this.textArea_Recevoir = textArea_Recevoir;
	}

	/**
	 * Create the application.
	 */
	// Constructeur par defaut qui defini la page en elle-meme
	public ChatPageCustom() { // Pourquoi constructeur sans parametre
		initialize();
	}
	
	// Constructeur qui permet de definir la meme page que le constructeur par defaut sauf que ici, on defini le pseudo de la page (à qui appartient la page Chat...)
	public ChatPageCustom(String pseudo) {
		this.Titre = pseudo; //On précise que c'est le "Titre" qui appartient à cette classe
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSimplet = new JFrame();
		frmSimplet.setTitle(this.Titre);
		frmSimplet.setBounds(100, 100, 880, 540);
		frmSimplet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSimplet.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Joindre");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				s = new Socket();
				SocketAddress me = new InetSocketAddress(Adresse, PORT);
				try {
					s.connect(me);
					o = s.getOutputStream();// Recuperer le Stream d'envoie des données du textArea_Send au serveur
					i = s.getInputStream();// Recuperer le Stream de reception des données du serveur 
					System.out.println("Connecté");
					Thread th = new Thread(new ChatPageCustom(s,textArea_Recevoir)); //Declaration du thread pendant l'ouveture du serveur
					th.start();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		JTextArea textArea_Send = new JTextArea();
		textArea_Send.setBounds(22, 340, 543, 22);
		frmSimplet.getContentPane().add(textArea_Send);
		
		btnNewButton.setBounds(22, 13, 180, 25);
		frmSimplet.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Envoyer");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = textArea_Send.getText();//Stockage du message de textArea_Send dans une variable
				try {
					o.write(message.getBytes());// Envoie le message dans le OutputStream
					o.flush();//Forcer l'envoie des données meme si le tableau de byte est trop petit
					System.out.println("Ca marche");
					textArea_Send.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_1.setBounds(22, 375, 180, 25);
		frmSimplet.getContentPane().add(btnNewButton_1);
		
		textArea_Recevoir = new JTextArea();
		textArea_Recevoir.setBounds(22, 51, 543, 276);
		frmSimplet.getContentPane().add(textArea_Recevoir);
		
		JList list = new JList();
		list.setBounds(594, 51, 228, 304);
		frmSimplet.getContentPane().add(list);
		
		JLabel lblParticipants = new JLabel("Participants:");
		lblParticipants.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblParticipants.setBounds(594, 17, 99, 16);
		frmSimplet.getContentPane().add(lblParticipants);
		
		JLabel lblNewLabel = new JLabel("New Label");
		lblNewLabel.setBounds(12, 446, 78, 16);
		frmSimplet.getContentPane().add(lblNewLabel);
		
		JLabel lblEtat = new JLabel("Etat:");
		lblEtat.setBounds(12, 475, 56, 16);
		frmSimplet.getContentPane().add(lblEtat);
		
	}
	
	//Thread: 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("thread crée");
		try {
			i = s.getInputStream();
			while(true) {// Boucle infinie
				byte[] rep= new byte[200];// Crée un tableau de byte pour stocker les caractères
				int n = i.read(rep); //on lit le message envoyé au "InputStream" qu'on va stocker dans (rep)//  le "n" rend le nombre de byte qu'il à reçu
				//System.out.println("Message : " + new String(rep));
				textArea_Recevoir.append(new String(rep) + "\n");
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public JFrame getFrmSimplet() {
		return frmSimplet;
	}
}

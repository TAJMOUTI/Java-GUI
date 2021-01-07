import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Bdd.BDD;
import Bdd.Parametre;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;

public class Login extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private static Login dialog;
	ResultSet rs;
	BDD db;
	String username;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			dialog = new Login();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Login() {
		//Iitialisation de la bdd
		db = new BDD(new Parametre().HOST_DB, new Parametre().USERNAME_DB,new Parametre().PASSWORD_DB, new Parametre().IPHOST, new Parametre().PORT);
		
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblEntrerVotrePseudo = new JLabel("Entrer votre Pseudo");
		lblEntrerVotrePseudo.setBounds(160, 24, 121, 16);
		contentPanel.add(lblEntrerVotrePseudo);
		
		textField = new JTextField();
		textField.setBounds(106, 53, 233, 22);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblIncorrect = new JLabel("");
		lblIncorrect.setForeground(Color.RED);
		lblIncorrect.setBounds(106, 92, 233, 16);
		contentPanel.add(lblIncorrect);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener(){ //Action de clicker sur le bouton"OK"

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						//Connexion à la bdd
						rs = db.querySelectAll("login", "username='" + textField.getText() + "'");
						
						try {
                            while (rs.next()) {
                                username = rs.getString("username");

                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        }
						if (username == null) {
							lblIncorrect.setText("Nom d'utilisateur incorrect");
						}else {
							System.out.println("Reussi");
							ChatPageCustom window = new ChatPageCustom(username);//Creation de la page ChatPageCustom dans le Login si le pseudo est trouvé
							window.getFrmSimplet().setVisible(true); //Afichage de la page ChatP... 
							dialog.setVisible(false);
						}
						
						
						/*String pseudo = textField.getText() ; //Enregistre dans la variable pseudo ce qu'il y a dans le champ "TextField"
						
						if (pseudo.equals("nizar")) { 
							System.out.println("Cool");
							
							ChatPageCustom window = new ChatPageCustom(pseudo);//Creation de la page ChatPageCustom dans le Login si le pseudo est trouvé
							window.getFrmSimplet().setVisible(true); //Afichage de la page ChatP... 
							dialog.setVisible(false);
						}else {
							System.out.println("Ca marche pas");
						}*/
					}
					
				}) ;
			}
			
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}

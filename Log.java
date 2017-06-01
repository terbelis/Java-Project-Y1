package client;

/**
 * Fonts and colours modified with Windows Builder Eclipse feature
 * 
 * Guest -   
 * Elizabeth: password
 * u: u
 * 
 * Manager - 
 * Nick: pass2
 * a: a
 * 
 * @author Elitsa Popova
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Log extends JFrame {
	
	private static final long serialVersionUID = 1L;// Serialised by JVM
	private JLabel welcomeL = new JLabel("Welcome to Shamrock hotel");
	private JLabel shamrockL = new JLabel("");// Label placeholder for an image
	private JLabel bedImgL = new JLabel(""); // Label placeholder for an image
	private JLabel noaccountL = new JLabel("No Account?");
	private JLabel usernameL = new JLabel("User name:");
	private JLabel passwordL = new JLabel("Password:");
	private JButton registerB = new JButton("Register");
	private JButton loginB = new JButton("Log in");
	private JTextField usernameT = new JTextField(20);
	private JPasswordField passwordT = new JPasswordField(20);
	private JPanel panel = new JPanel();
    // the database connector details  
	private static final String DRIVER = "com.mysql.jdbc.Driver"; // JDBC driver
	private static final String DATABASE = "myhotel"; // Database name
	private static final String HOST = "localhost"; // Database host IP 
	//static final String HOST = "10.1.63.200";// Uni
	private static final String DATABASE_URL = "jdbc:mysql://" + HOST + "/" + DATABASE;
	private Connection connection = null;
	private Statement statement = null;
	private String userID;
	

	//********************** CONSTRUCTOR *****************************
	// Initialising frame components
	public Log() {
		super("Shamrock's hotel bookings");
		// Setting the frame
		setSize(723,475);
		setLocation(45,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBackground(new Color(102, 102, 153));
		panel.setForeground(Color.BLACK);
		panel.setLayout(null);

		// Setting the components
		welcomeL.setBounds(26, 22, 540, 59);
		welcomeL.setForeground(new Color(0, 0, 51));
		welcomeL.setFont(new Font("Lucida Calligraphy", Font.ITALIC, 28));

		shamrockL.setBounds(588, 22, 89, 71);// Source https://pixabay.com/en/shamrock-celtic-plant-clover-149821/
		Image img2 = new ImageIcon(this.getClass().getResource("/image2small.png")).getImage();// Setting the image path
		shamrockL.setIcon(new ImageIcon(img2));

		bedImgL.setBounds(26, 101, 395, 288);// Source https://pixabay.com/en/hotel-room-curtain-green-furniture-1979406/
		Image img1 = new ImageIcon(this.getClass().getResource("/hotel1.png")).getImage();// Setting the image path
		bedImgL.setIcon(new ImageIcon(img1));

		noaccountL.setBounds(454, 126, 76, 31);
		noaccountL.setForeground(new Color(0, 0, 51));
		noaccountL.setFont(new Font("Tahoma", Font.BOLD, 11));

		usernameL.setBounds(454, 209, 80, 14);
		usernameL.setForeground(new Color(0, 0, 51));

		passwordL.setBounds(458, 250, 76, 14);
		passwordL.setForeground(new Color(0, 0, 51));

		usernameT.setBounds(531, 206, 146, 20);
		passwordT.setBounds(531, 247, 146, 20);

		registerB.setBounds(531, 130, 89, 23);
		registerB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				Register register = new Register();
			
			}});

		loginB.setBounds(531, 292, 89, 23);
		loginB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				isItAdmin();// Calls the method to check if is admin
				
				if (isItAdmin() == null){
					JOptionPane.showMessageDialog(null,"Name and password do not match. Please try again.");
				}
				
				else {
				if ((isItAdmin()).equals("No")) {
					CustomerGUI customerGUI = new CustomerGUI(userID);
					customerGUI.setVisible(true);
					dispose();
				}

				else if((isItAdmin()).equals("Yes")) {
					Manager manager = new Manager();
					manager.setVisible(true);
					//manager.isItAdmin(results);
					dispose();

				}
				}
				
			}
		});


		// Adding the components to the panel
		panel.add(welcomeL);
		panel.add(shamrockL);
		panel.add(bedImgL);
		panel.add(noaccountL);
		panel.add(usernameL);
		panel.add(passwordL);
		panel.add(registerB);
		panel.add(loginB);
		panel.add(usernameT);
		panel.add(passwordT);

		getContentPane().add(panel);
	}

    // ****************************** CHECK IF ADMIN *******************************************
	public String isItAdmin() {

		ResultSet resultSet2 = null;
		Statement statement2 = null;
		String columnValue = null;
		String answer="";

		// Querying the database
		try {
			// Opening a connection
			setConnection();

			statement2 = connection.createStatement();

			String name3 = usernameT.getText();
			@SuppressWarnings("deprecation")
			String password3 = passwordT.getText();

			resultSet2 = statement2.executeQuery("SELECT * from usr where Name = '"+name3+"' and Pass = '"+password3+"' ");

			java.sql.ResultSetMetaData rsmd = resultSet2.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			while (resultSet2.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						// Getting the admin with their userID
						columnValue = resultSet2.getString("IsAdmin");
					userID = resultSet2.getString("UserID");

				}
			}
		} catch (SQLException s) {

		}

		finally { // Closing resultSet, statement and connection

			try {                                                          
				resultSet2.close();                                      
				statement2.close();                                      
				connection.close();                                     
			}                                               
			catch (Exception exception) {                                                          
				exception.printStackTrace();                            
			}                                             
		}
		answer = columnValue;
		return answer;

	}

	// ***************************** GET USER DATA **************************************

	public String getData() {
		
		String password = "";
		String name = "root";
		String results = "Yes";
		String sqlQuery = null;
		ResultSetMetaData metadata = null;
		ResultSet resultSet = null;

		try {
			
			resultSet = statement.executeQuery("SELECT Name, Pass FROM usr WHERE Name ='" + name + "' AND Pass ='" + password + "'");
			// create Statement for querying database
			statement = connection.createStatement();
			// query database                                        
			resultSet = statement.executeQuery(sqlQuery);
			// process query results
			metadata = resultSet.getMetaData();
			int numberOfColumns = metadata.getColumnCount();     
			// extract the results
			while (resultSet.next()) {

				for (int i = 1; i <= numberOfColumns; i++) {

					if(password.equals(resultSet.getString("Pass")) && name.equals(resultSet.getString("Name"))) {

						results = resultSet.getString(name);
					} else {
						JOptionPane.showMessageDialog(null, "Login Failed. Please try again.");
					}
				}
			}
		}

		catch (SQLException sqlException) {                                                                  
			sqlException.printStackTrace();
			// Print an error message instead of results
			System.err.println( "ERROR! Something went wrong, please consult stack trace!");
		}                                                     
		finally {                                                             
			try {                                                          
				resultSet.close();                                      
				statement.close();                                      
				connection.close();                                     
			}                                              
			catch (Exception exception) {                                                          
				exception.printStackTrace();                            
			}                                             
		} 

		System.out.println("name and pass are : "+results);// Debugging
		return results;
	} 

	//**************************** CONNECTION ***************************************

	public boolean setConnection() {

		boolean connectionOK = false;
		// Connect to database 
		try {
			// Load the driver class
			Class.forName(DRIVER);
			// Establish connection to database    
			connection =  DriverManager.getConnection(DATABASE_URL, "root", "");
			connectionOK = true;  
		}
		catch (SQLException sqlException) {       
			System.err.println("Driver loaded, but something went wrong elsewhere!");               
			sqlException.printStackTrace();
		}                                                       
		catch (ClassNotFoundException classNotFound) {                        
			System.err.println("Couldn't find driver!");
			classNotFound.printStackTrace();            
		}                           

		return connectionOK;
	}

    //********************************** MAIN ******************************************

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try{

					Log log = new Log();
					log.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
        });
    }
}			

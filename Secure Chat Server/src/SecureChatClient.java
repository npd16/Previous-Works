import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
public class SecureChatClient extends JFrame implements Runnable,ActionListener{
	public static final int PORT = 8765;
	
	BufferedReader myReader;
	JTextArea outputArea;
	JLabel prompt;
	JTextField inputField;
	String myName, serverName;
	Socket connection;
	ObjectOutputStream output;
	ObjectInputStream input;
	BigInteger e,n,k;
	String c;
	SymCipher cipher;
	byte[] key;
	
	public SecureChatClient(){
		try{
			myName = JOptionPane.showInputDialog(this,"Enter your user name: ");
			serverName = JOptionPane.showInputDialog(this,"Enter the server name: ");
			InetAddress addr = InetAddress.getByName(serverName);
			connection = new Socket(addr,PORT);
			
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
			
			e = (BigInteger)input.readObject();
			n = (BigInteger)input.readObject();
			c = (String)input.readObject();
			
			if(c.toLowerCase().equals("add")){
				cipher = new Add128();
				key = new byte[128];			
			}
			else{
				cipher = new Substitute();
				key = new byte[256];
			}
			key = cipher.getKey();
			k = new BigInteger(1,key);
			System.out.println(k);
			
			k = k.modPow(e,n);
			output.writeObject(k);
			output.flush();
	
			byte[] name = new byte[myName.length()];
			name = cipher.encode(myName);
			output.writeObject(name);
			output.flush();
			
			this.setTitle(myName);
			
			Box b = Box.createHorizontalBox();
			outputArea = new JTextArea(8,30);
			outputArea.setEditable(false);
			b.add(new JScrollPane(outputArea));
			
			outputArea.append("Welcome to the Chat Group, " + myName + "\n");
			
			inputField = new JTextField("");
			inputField.addActionListener(this);
			
			prompt = new JLabel("Type your messages below:");
			Container c = getContentPane();
			
			c.add(b,BorderLayout.NORTH);
			c.add(prompt,BorderLayout.CENTER);
			c.add(inputField, BorderLayout.SOUTH);
			
			Thread outputThread = new Thread(this);
			outputThread.start();
			
			addWindowListener(
					new WindowAdapter(){
						public void windowClosing(WindowEvent e){
							String out = "CLIENT CLOSING";
							byte[] close = cipher.encode(out);
							try {
								output.writeObject(close);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
							}
							try {
								output.flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
							}
							
							System.out.println("CLIENT CLOSING");
							System.exit(0);
						}
					}
			);
			
			setSize(500,200);
			setVisible(true);
		}
		catch(Exception e){
			System.out.println("Problem starting client!");
		}
	}
	
	public void run(){
		while(true){
			try{
				byte[] bytes = (byte[]) input.readObject();
				BigInteger messBytes = new BigInteger(bytes);
				String currentMsg = cipher.decode(bytes);
				BigInteger decodeBytes = new BigInteger(currentMsg.getBytes());
				
				outputArea.append(currentMsg+"\n");
				
				System.out.println(messBytes);
				System.out.println(decodeBytes);
				System.out.println(currentMsg);
								
			}
			catch(Exception e){
				System.out.println(e + ", closing client");
				break;
			}
		}
		System.exit(0);
	}
	
	public void actionPerformed(ActionEvent e){
		String currentMsg = e.getActionCommand();
		String overallMsg = myName + ": " + currentMsg;
		inputField.setText("");
		byte[] bytes = new byte[overallMsg.length()];
		bytes = cipher.encode(overallMsg);
		
		BigInteger messBytes = new BigInteger(overallMsg.getBytes());
		BigInteger encodeBytes = new BigInteger(bytes);
		
		System.out.println(overallMsg);
		System.out.println(messBytes);
		System.out.println(encodeBytes);
		
		try {
			output.writeObject(bytes);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}
		try {
			output.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}
	}
	
	public static void main(String[] args){
		SecureChatClient JR = new SecureChatClient();
		JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}

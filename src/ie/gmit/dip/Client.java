package ie.gmit.dip;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	/*
	 * This class connects each Client to the Server and allows the user to send and
	 * receive messages to and from the Server and other Clients
	 */

	public final static int PORT = 13; // Port number for socket connection
	private Socket clientSocket; // Client side socket
	private BufferedReader br; // Reads messages from input stream
	private BufferedWriter bw; // Writes messages to output stream
	private String clientName; // Stores the client name for the chat application

	public Client(Socket clientSocket, String clientName) { // Client constructor for initial setup
		try {
			this.clientSocket = clientSocket; // Sets the socket connection
			this.br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			// Sets the Buffered Reader to read from input stream
			this.bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			// Sets the Buffered Writer to write to output stream
			this.clientName = clientName;
			// Sets the Client name for use in the chat application
		} catch (IOException e) {
			closeAll(clientSocket, br, bw); // If error occurs, close all resources
			System.out.println("I/O Exception.\nApplication has closed");
			System.exit(0); // Terminate application
		}
	}

	public void clientReceiveMessage() { // Method runs on a new Thread to listen for incoming messages
		new Thread(new Runnable() {
			@Override
			public void run() {
				String messageIn;
				while (clientSocket.isConnected()) { // Run while there is a socket connection
					try {
						messageIn = br.readLine(); // Stores message from input stream in variable
						System.out.println(messageIn); // Display message on Client console
					} catch (IOException e) {
						closeAll(clientSocket, br, bw); // If error occurs, close all resources
						System.out.println("Server Disconnected.\nApplication has closed");
						System.exit(0); // Terminate application
					}
				}
			}
		}).start(); // Start the thread
	}

	public void clientSendMessage() {
		try {
			bw.write(clientName); // Sends clientName to ClientHandler to be stored as a variable in that class
			bw.newLine(); // Starts a new line on the console
			bw.flush(); // Flushes the stream

			Scanner scanner = new Scanner(System.in); // Scans input from keyboard
			while (clientSocket.isConnected()) { // Run while there is a socket connection
				String messageOut = scanner.nextLine(); // Sets keyboard input to variable
				bw.write(clientName + ": " + messageOut); // Writes variable to output stream with clientName attached
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			closeAll(clientSocket, br, bw); // If error occurs, close all resources
			System.out.println("I/O Exception.\nApplication has closed");
			System.exit(0); // Terminate application
		}
	}

	public void closeAll(Socket clientSocket, BufferedReader br, BufferedWriter bw) {
		// Closes all resources, checks if null to avoid null pointer exception
		try {
			if (br != null) {
				br.close();
			}
			if (bw != null) {
				bw.close();
			}
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("################     CHAT CLIENT     ################\n");
		System.out.println("Please enter a hostname/IP address to connect to: ");
		Scanner scanner = new Scanner(System.in);
		String hostname_IP = scanner.nextLine(); // Input hostname or IP address to connect to
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(hostname_IP, PORT);
			// Creates socket and connects to specified port on chosen host
		} catch (IOException e) { // Error if socket connection is unsuccessful
			System.out.println("Unable to connect to Server.\nApplication has closed");
			System.exit(0); // Terminate application
		}
		System.out.println("Please enter a username for the chat application: ");
		String clientName = scanner.nextLine();// Input chosen Client name for chat application
		Client client = new Client(clientSocket, clientName); // Creates a new Client
		client.clientReceiveMessage(); // Listens for messages
		client.clientSendMessage(); // Sends messages
	}
}
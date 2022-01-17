package ie.gmit.dip;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	/*
	 * This class sets up a server socket and listens for connection requests. When a
	 * connection is accepted it is passed to the ClientHandler class and an instance
	 * of that class is run on a new thread. The Server also broadcasts to and
	 * receives messages from all connected Clients
	 */

	public final static int PORT = 13; // Port number for socket connection
	private ServerSocket serverSocket; // Server side socket
	private Socket clientSocket; // Client side socket
	private ClientHandler clientHandler; // Stores instance of clientHandler

	public Server(ServerSocket serverSocket) { // Server constructor for initial setup
		this.serverSocket = serverSocket;
	}

	public void startServer() {
		Thread socketConnections = new Thread(new Runnable() {
			// New thread to listen for and setup new connections
			@Override
			public void run() {
				while (!serverSocket.isClosed()) { // While the server socket is open
					try {
						clientSocket = serverSocket.accept();
						// Listen and accept connections and store in variable
					} catch (IOException e) {
						closeServerSocket(); // Close the server socket
						System.out.println("I/O Exception.\nApplication has closed");
						System.exit(0); // Terminate application
					}
					clientHandler = new ClientHandler(clientSocket); // Pass connection to clientHandler
					System.out.println(clientHandler.getClientName() + " has connected to the server!");
					Thread thread = new Thread(clientHandler); // Run instance of clientHandler on new Thread
					thread.start();
				}
			}
		});
		socketConnections.start(); // Start Thread
	}

	public void serverBroadcastMessage() {
		Scanner scanner = new Scanner(System.in); // Scan keyboard input
		while (true) {
			String messageOut = scanner.nextLine(); // Set input to variable
			for (ClientHandler ch : ClientHandler.getClientList()) {
				// Iterate list of clients created in clientHandler class
				try {
					ch.bw.write("SERVER: " + messageOut);
					// Write message to each clients output stream
					ch.bw.newLine();
					ch.bw.flush();
				} catch (IOException e) {
					closeServerSocket(); // Close the server socket
					System.out.println("I/O Exception\n Application has closed");
					System.exit(0); // Terminate application
				}
			}
		}
	}

	public void closeServerSocket() {
		// Closes the server socket
		try {
			if (serverSocket != null) {
				// Checks if not null to avoid null pointer exception
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("################     CHAT SERVER     ################\n");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT); // Create server socket on chosen port
		} catch (IOException e) {
			System.out.println("I/O Exception.\nApplication has closed");
			System.exit(0); // Terminate application
		}
		Server server = new Server(serverSocket); // Pass server socket to Server class
		server.startServer(); // Start the Server
		server.serverBroadcastMessage(); // Send messages from Server to all Clients
	}
}
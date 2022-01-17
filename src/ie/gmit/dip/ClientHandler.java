package ie.gmit.dip;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
	/*
	 * This class handles each client connected to the Server. The clientSocket
	 * connection is added to an array list. When a message is received from the
	 * input stream the list is iterated and the message is sent to all clients in
	 * the list and the server.
	 */

	private static ArrayList<ClientHandler> clientList = new ArrayList<>();
	// Array List to stores each client connected
	private Socket clientSocket; // Client side socket
	public BufferedWriter bw; // Writes messages to output stream
	public BufferedReader br; // Reads messages from input stream
	private String clientName; // Stores the client name for the chat application

	public ClientHandler(Socket clientSocket) { // ClientHandler constructor for initial setup
		try {
			this.clientSocket = clientSocket;
			this.bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.clientName = br.readLine();
			clientList.add(this); // Add this instance to array list
			broadcastMessage("SERVER: " + clientName + " has joined the chat!");
		} catch (IOException e) {
			closeAll(clientSocket, br, bw); // If error occurs, close all resources
			System.out.println("I/O Exception.\nApplication has closed");
			System.exit(0); // Terminate application
		}
	}

	@Override
	public void run() {
		String messageFromClient;

		while (clientSocket.isConnected()) { // While socket is connected
			try {
				messageFromClient = br.readLine(); // Read from input stream and store in variable

				if (messageFromClient.equals(clientName + ": " + "\\q")) {
					// If variable equals \q remove the client that send message from array list
					removeClientHandler();
					// Removes client from list and sends info message to other clients and server
					break;
				}
				broadcastMessage(messageFromClient);
				// Sends message to all connected clients in array list
				System.out.println(messageFromClient);
				// Displays message on Server console
			} catch (IOException e) {
				closeAll(clientSocket, br, bw); // If error occurs, close all resources
				break;
			}
		}
	}

	public void broadcastMessage(String messageToSend) {
		for (ClientHandler ch : clientList) {
			try {
				if (!ch.clientName.equals(clientName)) {
					ch.bw.write(messageToSend);
					ch.bw.newLine();
					ch.bw.flush();
				}
			} catch (IOException e) {
				closeAll(clientSocket, br, bw); // If error occurs, close all resources
				System.out.println("I/O Exception.\nApplication has closed");
				System.exit(0); // Terminate application
			}
		}
	}

	public void removeClientHandler() {
		clientList.remove(this); // Remove this client from array list
		broadcastMessage("SERVER: " + clientName + " has left the chat!");
		// Inform other connected clients that this client has disconnected
		System.out.println(clientName + " has disconnected from the server");
		// Display message on Server console
	}

	public void closeAll(Socket clientSocket, BufferedReader br, BufferedWriter bw) {
		removeClientHandler(); // Remove client from array list
		// Close all resources if not null
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

	public static ArrayList<ClientHandler> getClientList() {
		// Returns the client array list
		return clientList;
	}

	public String getClientName() {
		// Returns the client name
		return clientName;
	}
}
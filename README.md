# Java-Client-Server-Chat-App

The Chat Application consists of three classes. The Server class, The Client class and the ClientHandler class.

The Server class sets up a server socket and listens for connection requests from clients on port 13. When a connection is accepted it is passed to the ClientHandler class and an instance of that class is run on a new thread. This allows for multiple client connections.

The Server class also broadcasts to and receives messages from all connected clients through the input and output streams set up in the ClientHandler class.

The Server class listens for connection requests on its own thread which allows for the class to send messages to clients on the main path of execution without blocking the server socket.

The ClientHandler class handles each client connected to the Server. The client socket connection is added to an array list. When a message is received from the input stream that is set up in this class, the array list is then iterated and the message is sent to all clients on the list through the output stream.

Messages are also printed to the Server console and the Server class also uses the client array list to send messages to all connected clients.

The Client class connects each client to the server and allows the user to send and receive messages to and from the server and other connected clients through input and output streams.
E
ach client has a username to identify it in the chat application. When a message is sent, the ClientHandler class will identify the client by its username and will forward the message on to the server and all other clients apart from the client that sent the message.

The Client class listens for messages in its own thread so that it does not block the main path of execution which is used to send messages.

Any potential errors are handled in try/catch blocks for all these classes. If an error occurs a message is displayed on the console informing the user of the error and the application is closed.

Instructions
The Server Class should be executed first as this will set up a server socket and listen for connection requests on port 13. When a request is accepted that connection is run on a new thread.

When the Client class is executed the user is prompted to input the hostname or IP address they would like to connect to. The port number is already set to 13.

The user is then prompted to enter a username to be used for the chat application. When messages are displayed on the console the username is used to identify the client that sent the message.

Once connected the user can send and receive messages to and from the Server and all other connected Clients through the command line.

If the Server is not running and the Client application is executed and attempts to connect, the console will display an error message and close the Client application.

If the Server is closed while the Client is connected, the client console will display an error message and close the Client application.

If the Client user inputs “\q” the client will be removed from the group chat. The other clients will receive a message informing them that the client has left the chat.
When a new client connects to the server all other clients who are already connected are informed.

The application allows for multiple users to connect to the server and join the chat and disconnect from the server while it is running.

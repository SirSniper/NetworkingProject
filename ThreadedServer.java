import java.net.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ThreadedServer{
    private int port;
    // Set port to host on
    ThreadedServer(int port){
        this.port = port;
    }

    public void serve(){
        ServerSocket serverSocket;
        // Host the server
        try{
            serverSocket = new ServerSocket(this.port);
        } catch(Exception e){
            System.out.println("Error connecting to host port");
            return ;
        }

        List<ConnectionThread> connections = new ArrayList<>(); 
        ConnectionThread newConnection;
        Socket clientSocket;
        // Repeat accepting connections and add them to the connections, start each handling thread
        while(true){
            try{
                clientSocket = serverSocket.accept();
                newConnection = new ConnectionThread(clientSocket);
                newConnection.start();
                connections.add(newConnection);
            } catch(Exception e){
                System.out.println("Error accepting connection");
                break;
            }
        }
        try{
            serverSocket.close();
        } catch(Exception e){
            System.out.println("Error closing socket");
        }
    }
}
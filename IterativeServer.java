import java.net.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;

public class IterativeServer{
    private int port;

    IterativeServer(int port){
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

        Connection newConnection;
        Socket clientSocket;
        Iterator clientHandler;
        
        try{
            clientSocket = serverSocket.accept();
            newConnection = new Connection(clientSocket);
            clientHandler = new Iterator(newConnection);
        } catch(Exception e){
            System.out.println("Error accepting initial connection");
            return;
        }
        
        
        // Repeat accepting connections and add them to the connections
        while(true){
            try{
                clientSocket = serverSocket.accept();
                newConnection = new Connection(clientSocket);
                clientHandler.addClient(newConnection);
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

    private class Iterator extends Thread{
        private List<Connection> clients;

        Iterator(Connection client){
            this.clients = new ArrayList<>();
            this.clients.add(client);
        }

        @Override
        public void run(){
            int command = 0;
            while(true){
                for(Connection client : this.clients){
                    command = client.getCommand();
                    if(command != 7){
                        this.clients.remove(client);
                    }else{
                        client.executeCommand(command);
                    }
                }
            }
        }

        public void addClient(Connection newClient){
            this.clients.add(newClient);
        }

    }
}
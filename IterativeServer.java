import java.net.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;
import java.util.concurrent.CopyOnWriteArrayList;

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
            System.out.println("Accepting new connection");
            clientHandler.start();
        } catch(Exception e){
            System.out.println("Error accepting initial connection");
            try{
                serverSocket.close();
            }catch(Exception f){
                System.out.println("Error closing socket");
            }
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

    public static void main(String[] args){
        IterativeServer cur = new IterativeServer(3333);
        cur.serve();
    }

    private class Iterator extends Thread{
        private List<Connection> clients;
        private List<Connection> clientsToRemove;

        Iterator(Connection client){
            this.clients = new CopyOnWriteArrayList<>();
            this.clientsToRemove = new CopyOnWriteArrayList<>();
            this.clients.add(client);
        }

        @Override
        public void run(){
            int command = 0;
            while(true){
                if(!this.clientsToRemove.isEmpty()){
                    this.clients.removeAll(this.clientsToRemove);
                    this.clientsToRemove = new CopyOnWriteArrayList<>();
                }
                for(Connection client : this.clients){
                    command = client.getCommand();
                    if(command == 7){
                        this.clientsToRemove.add(client);
                        System.out.println("Client Disconnecting");
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
import java.util.ArrayList;

public class ClientHandler{
    private int numClients, port;
    private int[] commandList;
    private String hostname;
    private List<Client> clients;
    private String[] times;

    ClientHandler(int numClients, int[] commandList, String hostname, int port){
        this.numClients = numClients;
        this.commandList = commandList;
        this.hostName = hostname;
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void run(){
        for(int i = 0; i < this.numClients; i++){
            // Create connection
            // Add connection to current list
            this.clients.add(new Client(this.hostname, this.port, this.commandList));
        }
        // Start each emulation thread
        for(int i = 0; i < this.numClients; i++){
            this.clients[i].start();
        }
        // Wait for all of the client threads to complete and rejoin
        for(int i = 0; i < this.numClients; i++){
            this.client[i].join();
        }
        // TODO: Set up method for calculating/outputing all of the execution times
    }
}
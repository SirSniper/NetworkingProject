

public class ClientHandler{
    private int numClients;
    private int[] commandList;
    private Client[] clients;
    private String[] times;

    ClientHandler(int numClients, int[] commandList){
        this.numClients = numClients;
        this.commandList = commandList;
    }

    public void run(){
        for(int i = 0; i < this.numClients; i++){
            // Create connection
            // Add connection to current
            this.clients.add(new Client());
        }
    }
}
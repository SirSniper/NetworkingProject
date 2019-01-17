import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.Thread;

public class ClientHandler extends Thread{
    private int numClients, port;
    private int[] commandList;
    private String hostname;
    private List<Client> clients;
    public List<Double> times = new ArrayList<>();
    public Double avgTime = 0.0;

    ClientHandler(int numClients, int[] commandList, String hostname, int port){
        this.numClients = numClients;
        this.commandList = commandList;
        this.hostname = hostname;
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void run(){
        for(int i = 0; i < this.numClients; i++){
            // Create connection
            // Add connection to current list
            this.clients.add(new Client(this.hostname, this.port, this.commandList, false));
        }
        // Start each emulation thread
        for(int i = 0; i < this.numClients; i++){
            this.clients.get(i).start();
        }
        // Wait for all of the client threads to complete and rejoin
        for(int i = 0; i < this.numClients; i++){
            try{
                this.clients.get(i).join();
            }catch(Exception e){
                System.out.printf("Error waiting for thread %d\n", i);
            }
        }
        int timeCount = 0;
        Double sum = 0.0;
        for(int i = 0; i < this.numClients; i++){
            for(Double time : this.clients.get(i).commandTimes){
                System.out.printf("Time %f\n", time);
                sum += time;
            }
            timeCount += this.clients.get(i).commandTimes.size();
        }
        this.avgTime = sum / new Double(timeCount);
        // TODO: Set up method for calculating/outputing all of the execution times
    }

    public static void main(String[] args){
        ClientHandler testing = new ClientHandler(5, new int[] {1,1,1}, "192.168.101.123", 3333);
        testing.start();
        try{
            testing.join();
        }catch(Exception e){
            System.out.println("Error waiting for thread");
        }
        System.out.printf("Average Execution time is %4.2f\n", testing.avgTime);

    }
}
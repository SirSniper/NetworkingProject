import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client extends Thread{
    private String hostname;
    private int port;
    private Socket conn;
    private PrintWriter sender;
    private BufferedReader receiver;
    public List<Double> commandTimes = new ArrayList<>();
    private int[] emulationCommands;
    private boolean printOutput;

    Client(String hostname, int port, int[] emulationCommands, boolean printOutput){
        this.hostname = hostname;
        this.port = port;
        this.emulationCommands = emulationCommands;
        this.printOutput = printOutput;
        try{
            this.conn = new Socket(this.hostname, this.port);
            this.sender = new PrintWriter(this.conn.getOutputStream(), true);
            this.receiver = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
        }catch(Exception e){
            System.out.println("Error connection to server");
        }
    }

    private static String[] commandOptions = {"Host Date and Time", "Host uptime", "Host memory use", "Host netstat", "Host current users", "Host running processes", "Quit"};


    private static void getMenu(){
        for(int i = 0; i < commandOptions.length; i++){
            System.out.printf("%d. %s\n", i+1, commandOptions[i]);
        }
    }

    private static int getMenuSelection(){
        Scanner userSelectionReader = new Scanner(System.in);
        boolean valid = false;
        String selection;
        int selectedItem = 0;
        while(!valid){
            getMenu();
            selection = userSelectionReader.nextLine();
            try{
                selectedItem = Integer.parseInt(selection);
            } catch (Exception e){
                System.out.println("That wasn't a valid selection, please only enter an integer between 1 and 7");
                continue;
            }
            if(selectedItem < 1 || selectedItem > 7){
                System.out.println("That wasn't a valid selection, please only enter an integer between 1 and 7");
                continue;
            }else{
                valid = true;
            }
        }
        // try{
        //     userSelectionReader.close();
        // }catch(Exception e){
        //     System.out.println("Error closing scanner");
        // }
        return selectedItem;
    }

    public void sendCommand(int command){
        this.sender.println(Integer.toString(command));
        String line = "";
        if(command == 7){
            try{
                this.conn.close();
            }catch(Exception e){
                System.out.println("Error closing connection");
            }
        } else{
            // While the connection has content, print it to the screen
            try{
                line = receiver.readLine();
                if(this.printOutput){
                    System.out.println(line);
                }
                while (receiver.ready()) {
                    line = receiver.readLine();
                    if(this.printOutput){
                        System.out.println(line);
                    }
                }
            } catch(Exception e){
                System.out.println("Error reading received data");
            }
        }
        return ;
    }

    public static void main(String args[]){
        int selectedCommand;
        Client curClient = new Client("192.168.101.123",3333, new int[]{}, true);
        System.out.println("Connected");
        while(true){
            System.out.println("Getting input");
            selectedCommand = getMenuSelection();
            System.out.printf("Got command %d\n", selectedCommand);
            curClient.sendCommand(selectedCommand);
            if(selectedCommand == 7){
                System.out.println("Exiting...");
                break;
            }
        }
    }

    public void emulate(int[] commands){
        for(int command: commands){
            long start = System.nanoTime();  
            System.out.printf("Sending command %d\n", command);
            this.sendCommand(command);
            this.commandTimes.add(Long.valueOf((System.nanoTime() - start) / 1000000).doubleValue());
        }
    }

    public void run(){
        this.emulate(this.emulationCommands);
    }

}
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread{
    private String hostname;
    private int port;
    private Socket conn;
    private PrintWriter sender;
    private BufferedReader receiver;
    public List<Double> commandTimes = new ArrayList<>();
    private int[] emulationCommands;

    Client(String hostname, int port, int[] emulationCommands){
        this.hostname = hostname;
        this.port = port;
        this.emulationCommands = emulationCommands;
        try{
            this.conn = new Socket(this.hostname, this.port);
            this.sender = new PrintWriter(this.conn.getOutputStream(), true);
            this.receiver = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
        }catch(Exception e){
            System.out.println("Error connection to server");
        }
    }

    private static String[] commandOptions = {"test","test","test","test","test","test","test"};


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
        return selectedItem;
    }

    public void sendCommand(int command){
        this.sender.println(Integer.toString(command));
        String line = "";
        if(command == 7){
            this.conn.close();
        } else{
            // While the connection has content, print it to the screen
            while ((line = receiver.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public static void main(String args[]){
        int selectedCommand;
        Client curClient = new Client("",3333, new String[]{});
        while(true){
            selectedCommand = getMenuSelection();
            curClient.sendCommand(selectedCommand);
            if(command == 7){
                break;
            }
        }
    }

    public void emulate(int[] commands){
        for(int command: commands){
            long start = System.nanoTime();  
            this.sendCommand(command);
            this.commandTimes.add((System.nanoTime() - start) / 1000000);
        }
    }

    public void run(){
        this.emulate(this.emulationCommands);
    }

}
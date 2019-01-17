import java.net.*;
import java.io.*;

public class Connection{
    private Socket conn;
    private static String[] commands = {
        "date", "uptime", "free", "netstat", "who", "ps -e", "quit"
    };
    BufferedReader in;
    PrintWriter out;
    
    Connection(Socket conn){
        this.conn = conn;
        try{
            this.in = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            this.out = new PrintWriter(this.conn.getOutputStream(), true);
        } catch(Exception e){
            System.out.println("Error loading input and output for client");
        }
        
    }

    public int getCommand(){
        try {
            System.out.println("Awaiting input");
            String input = in.readLine();
            System.out.println("Got input " + input);
            if (input == null || input.isEmpty()) {
                // If the input it empty (i.e. dead connection),  return 7 indicating closing connection
                return 7;
            }
            int inputValue = Integer.parseInt(input);
            return inputValue;
        } catch (IOException e) {
            System.out.println("Error reading command from socket");
            return 7;
        }
    }

    public void executeCommand(int command){
        long start = System.nanoTime();
        Runtime cmd = Runtime.getRuntime();
        Process p;
        try{
            p = cmd.exec(commands[command-1]);
        }catch(Exception e){
            System.out.println("Error executing command");
            return ;
        }
        
        // Create a reader to handle the terminal output
        BufferedReader cmdReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        try{
            System.out.println("Sending Command");
            while ((line = cmdReader.readLine()) != null) {
                this.out.println(line);
            }
            cmdReader.close();
            try{
                p.waitFor();
            } catch (Exception t){
                System.out.println("Error waiting for exit");
            }
        } catch (Exception e){
            System.out.println("Error closing cmd reader");
        }
        System.out.printf("Execution time %f\n", Long.valueOf((System.nanoTime() - start) / 1000000).doubleValue());
        return ;
    }
}
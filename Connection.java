import java.lang.Thread;
import java.net.*;
import java.io.*;

public class Connection{
    private Socket conn;
    private static String[] commands = {
        "echo 1"
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
            String input = in.readLine();
            if (input == null || input.isEmpty()) {
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
        Runtime cmd = Runtime.getRuntime();
        Process p;
        try{
            p = cmd.exec(commands[command]);
            p.waitFor();
        }catch(Exception e){
            System.out.println("Error executing command");
            return ;
        }
        
        BufferedReader cmdReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        try{
            while ((line = cmdReader.readLine()) != null) {
                this.out.println(line);
            }
            cmdReader.close();
        } catch (Exception e){
            System.out.println("Error closing cmd reader");
        }
        return ;
    }
}
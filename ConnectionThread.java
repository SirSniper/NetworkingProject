import java.lang.Thread;
import java.net.*;
import java.io.*;

public class ConnectionThread extends Thread{
    private Socket conn;
    private static String[] commands = {
        "echo 1"
    };
    
    ConnectionThread(Socket conn){
        super();
        this.conn = conn;
    }

    @Override
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            PrintWriter out = new PrintWriter(this.conn.getOutputStream(), true);

            // Get messages from the client, line by line; return them capitalized
            while (true) {
                String input = in.readLine();
                if (input == null || input.isEmpty()) {
                    break;
                }
                int inputValue = Integer.parseInt(input);
                Runtime cmd = Runtime.getRuntime();
                Process p;
                try{
                    p = cmd.exec(commands[inputValue-1]);
                    p.waitFor();
                } catch(Exception e){
                    System.out.println("Failed to execute command");
                    continue;
                }
                BufferedReader cmdReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";

                while ((line = cmdReader.readLine()) != null) {
                    out.println(input);
                }

                cmdReader.close();
            }
        } catch (IOException e) {
            System.out.println("Error reading command from socket");
        } finally {
            try { this.conn.close(); } catch (IOException e) {}
            System.out.println("Connection closed");
        }
    }
}
import java.lang.Thread;
import java.net.*;
import java.io.*;

public class ConnectionThread extends Thread{
    private Socket conn;
    private static String[] commands = {
        "date", "uptime", "free", "netstat", "who", "ps -e", "quit"
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
            BufferedReader cmdReader;
            Runtime cmd = Runtime.getRuntime();
            Process p;

            // Keep trying to get commands
            while (true) {
                String input = in.readLine();
                if (input == null || input.isEmpty()) {
                    // If the stream is empty (i.e. dead connection) break out and close this connection
                    break;
                }
                int inputValue;
                try{
                    inputValue = Integer.parseInt(input);
                }catch(Exception e){
                    out.println("Error processing last command");
                    continue;
                }
                long start = System.nanoTime();
                ProcessBuilder pb = new ProcessBuilder(commands[inputValue-1]);
                try{
                    p = pb.start();
                    
                } catch(Exception e){
                    System.out.println("Failed to execute command");
                    continue;
                }
                cmdReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";

                // While the terminal has out, send each line back to connection
                while ((line = cmdReader.readLine()) != null) {
                    out.println(line);
                }
                cmdReader.close();

                try{
                    p.waitFor();
                } catch(Exception t){
                    System.out.println("Error waiting for process");
                }
                System.out.printf("Execution time %f\n", Long.valueOf((System.nanoTime() - start) / 1000000).doubleValue());

            }

        } catch (SocketException e){
            System.out.println("Client Disconnected");
        } catch (IOException e) {
            System.out.println("Error reading command from socket");
            e.printStackTrace();
        } finally {
            try { this.conn.close(); } catch (IOException e) {}
            System.out.println("Connection closed");
        }
    }
}
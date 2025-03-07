import se.mau.DA343A.VT25.projekt.Buffer;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable {

    private SecurityTokens tokens;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private boolean connected = false;
    private Buffer<Integer> buffer;

    public Client(Buffer<Integer> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                connectToServer();
                while (connected) {
                    int watt = buffer.get();
                    System.out.println("Sending watt usage: " + watt);
                    outputStream.writeInt(watt);
                    outputStream.flush();

                    String input = inputStream.readUTF();
                    System.out.println("Received from server: " + input);

                    Thread.sleep(1000);
                }
            } catch (SocketException e) {
                System.err.println("SocketException: " + e.getMessage());
                connected = false;
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                connected = false;
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
                connected = false;
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Failed to close socket: " + e.getMessage());
                }
                System.err.println("Connection lost. Retrying...");
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before retrying
                } catch (InterruptedException e) {
                    System.err.println("Retry sleep interrupted: " + e.getMessage());
                }
            }
        }
    }

    private void connectToServer() throws IOException {
        socket = new Socket("localhost", 8080);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());

        tokens = new SecurityTokens("Grupp21");

        String token = tokens.generateToken();
        System.out.println("Generated token: " + token);
        System.out.println("Token verification: " + tokens.verifyToken(token));

        outputStream.writeUTF(token);
        outputStream.flush();

        System.out.println("Authentication successful!");
        connected = true;
    }
}
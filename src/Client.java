import se.mau.DA343A.VT25.projekt.Buffer;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {

    private SecurityTokens tokens;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private boolean connected = false;
    private Buffer buffer;

    public Client(Buffer<Double> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        try {
            socket = new Socket("localhost", 8888);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());

            tokens = new SecurityTokens("Grupp21");

            String token = tokens.generateToken();
            System.out.println(token);
            System.out.println(tokens.verifyToken(token));

            outputStream.writeUTF(token);
            outputStream.flush();

            System.out.println("Authentication successful!");
            connected = true;

            while(connected) {

                double watt = (double) buffer.get();
                outputStream.writeInt((int) watt);
                outputStream.flush();

                String input = inputStream.readUTF();
                System.out.println(input);

                Thread.sleep(1000);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
            //Kommer alltid in här efter första connection till servern
        }
    }
}

package serveur;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private PrintWriter out;

    public ClientHandler(Socket socket, PrintWriter out) {
        this.socket = socket;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client : " + message);
                ServeurChatActif.diffuser("Client : " + message, out);
            }

        } catch (Exception e) {
            System.out.println("Client déconnecté");
        } finally {
            ServeurChatActif.supprimerClient(out);
            try {
                socket.close();
            } catch (Exception e) {}
        }
    }
}
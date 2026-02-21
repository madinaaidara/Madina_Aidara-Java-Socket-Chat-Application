package serveur;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurChatActif {

    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Serveur actif démarré sur le port 6000");

            // Thread pour accepter les clients
            new Thread(() -> {
                try {
                    while (true) {
                        Socket socket = serverSocket.accept();
                        System.out.println("Nouveau client connecté");

                        PrintWriter out = new PrintWriter(
                                socket.getOutputStream(), true);
                        clients.add(out);

                        new ClientHandler(socket, out).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Lecture clavier serveur
            BufferedReader clavier = new BufferedReader(
                    new InputStreamReader(System.in));

            String message;
            while (true) {
                System.out.print("Serveur : ");
                message = clavier.readLine();
                diffuser("Serveur : " + message, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void diffuser(
            String message, PrintWriter expéditeur) {

        for (PrintWriter client : clients) {
            if (client != expéditeur) {
                client.println(message);
            }
        }
    }

    public static synchronized void supprimerClient(PrintWriter client) {
        clients.remove(client);
    }
}

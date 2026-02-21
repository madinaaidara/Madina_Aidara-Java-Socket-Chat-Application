package client;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientChatGUI extends JFrame {

    private JTextPane zoneMessages;
    private JTextField champMessage;
    private JButton boutonEnvoyer;
    private JButton boutonQuitter;
    private JButton boutonSauver;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String pseudo;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    // Styles
    private Style styleMoi;
    private Style styleAutre;
    private Style styleServeur;

    public ClientChatGUI() {

        // ===== PSEUDO =====
        pseudo = JOptionPane.showInputDialog(
                this,
                "Entrez votre pseudo :",
                "Pseudo",
                JOptionPane.PLAIN_MESSAGE
        );

        if (pseudo == null || pseudo.trim().isEmpty()) {
            pseudo = "Utilisateur";
        }

        // ===== FENÊTRE =====
        setTitle("Chat - " + pseudo);
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== ZONE MESSAGES =====
        zoneMessages = new JTextPane();
        zoneMessages.setEditable(false);
        zoneMessages.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(zoneMessages);

        // ===== STYLES =====
        StyledDocument doc = zoneMessages.getStyledDocument();

        styleMoi = doc.addStyle("moi", null);
        StyleConstants.setForeground(styleMoi, new Color(0, 102, 204));
        StyleConstants.setBold(styleMoi, true);

        styleAutre = doc.addStyle("autre", null);
        StyleConstants.setForeground(styleAutre, Color.BLACK);

        styleServeur = doc.addStyle("serveur", null);
        StyleConstants.setForeground(styleServeur, new Color(0, 153, 0));
        StyleConstants.setItalic(styleServeur, true);

        // ===== BAS =====
        champMessage = new JTextField();
        boutonEnvoyer = new JButton("Envoyer");
        boutonQuitter = new JButton("Déconnexion");
        boutonSauver = new JButton("Exporter");

        JPanel bas = new JPanel(new BorderLayout(5, 5));
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        boutons.add(boutonSauver);
        boutons.add(boutonQuitter);

        bas.add(champMessage, BorderLayout.CENTER);
        bas.add(boutonEnvoyer, BorderLayout.EAST);
        bas.add(boutons, BorderLayout.SOUTH);

        add(scroll, BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        connecterServeur();
        gestionActions();

        setVisible(true);
        champMessage.requestFocusInWindow();
    }

    // ===== CONNEXION SERVEUR =====
    private void connecterServeur() {
        try {
            socket = new Socket("localhost", 6000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            ajouterMessage("🟢 Connecté au serveur\n", styleServeur);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        if (msg.startsWith("Serveur")) {
                            ajouterMessage(msg + "\n", styleServeur);
                        } else {
                            ajouterMessage(msg + "\n", styleAutre);
                        }
                    }
                } catch (Exception e) {
                    ajouterMessage("🔴 Connexion perdue\n", styleServeur);
                }
            }).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de se connecter au serveur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    // ===== ACTIONS =====
    private void gestionActions() {

        boutonEnvoyer.addActionListener(e -> envoyer());
        champMessage.addActionListener(e -> envoyer());

        boutonQuitter.addActionListener(e -> quitter());

        boutonSauver.addActionListener(e -> sauvegarder());
    }

    // ===== ENVOYER =====
    private void envoyer() {
        String texte = champMessage.getText().trim();

        if (!texte.isEmpty()) {
            String heure = sdf.format(new Date());
            String msg = "[" + heure + "] " + pseudo + " : " + texte;

            ajouterMessage("Moi : " + msg + "\n", styleMoi);
            out.println(msg);

            champMessage.setText("");
            champMessage.requestFocus();
        }
    }

    // ===== AJOUT MESSAGE + SCROLL =====
    private void ajouterMessage(String msg, Style style) {
        try {
            StyledDocument doc = zoneMessages.getStyledDocument();
            doc.insertString(doc.getLength(), msg, style);
            zoneMessages.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // ===== DECONNEXION =====
    private void quitter() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (Exception e) {}

        System.exit(0);
    }

    // ===== EXPORT DISCUSSION =====
    private void sauvegarder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Sauvegarder la discussion");

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".txt")) {
                fw.write(zoneMessages.getText());
                JOptionPane.showMessageDialog(this,
                        "Discussion sauvegardée avec succès");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la sauvegarde");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientChatGUI::new);
    }
}

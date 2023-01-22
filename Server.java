import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Server extends JFrame {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare Component

    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor..

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is Ready to accept Connection");
            System.out.println("Waiting......");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWriting();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Key Released" + e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("You Have Pressed Enter Button");

                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }

            }

        });

    }

    private void createGUI() {
        // GUI code..

        this.setTitle("Server messenger[END]");
        this.setSize(500, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding For Component

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        messageArea.setEditable(false);
        // frame Layout design

        this.setLayout(new BorderLayout());

        // adding the Components to Frame

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    // start Reading Method

    public void startReading() {
        // thread read krke dega

        Runnable r1 = () -> {

            System.out.println("Reader started....");

            try {

                while (true) {

                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Client terminate the Chat...");
                        JOptionPane.showMessageDialog(this, "Client terminated the Chat...");
                        messageInput.setEnabled(false);

                        socket.close();
                        break;
                    }

                    // System.out.println("Client : "+msg);
                    messageArea.append("Client : " + msg + "\n");
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }

        };
        new Thread(r1).start();

    }

    public void startWriting() {
        // thread -data user se lega or use client tk send krega

        Runnable r2 = () -> {

            System.out.println("Writer started..");

            try {

                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);

                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }

                System.out.println("Connection is closed");

            }

            catch (Exception e) {

                e.printStackTrace();
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {

        System.out.println("This is Server....Going to start server ");

        new Server();

    }

}
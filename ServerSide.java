import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

class ServerGUI extends JFrame implements ActionListener, FocusListener {
    JPanel panel1, panel2, panel3;
    JButton send;
    JTextField text;
    JTextArea ChattingArea;
    JLabel label1;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    ServerGUI(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupGUI();
        startReading();
        startFileReceiver();
    }

    private void setupGUI() {
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SERVER_SIDE");

        label1 = new JLabel("CHAT APPLICATION", SwingConstants.CENTER);
        label1.setFont(new Font("Frutiger", Font.BOLD, 40));
        label1.setForeground(Color.BLACK);

        panel1 = new JPanel(new BorderLayout());
        panel1.setPreferredSize(new Dimension(600, 80));
        panel1.setBackground(new Color(230, 230, 250));
        panel1.add(label1, BorderLayout.CENTER);

        ChattingArea = new JTextArea();
        ChattingArea.setEditable(false);
        ChattingArea.setFont(new Font("Arial", Font.PLAIN, 15));
        ChattingArea.setBackground(Color.WHITE);
        ChattingArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(ChattingArea);
        panel2 = new JPanel(new BorderLayout());
        panel2.add(scrollPane, BorderLayout.CENTER);

        text = new JTextField("Enter Here......");
        text.setPreferredSize(new Dimension(400, 40));
        text.setFont(new Font("Arial", Font.ITALIC, 18));
        text.setForeground(Color.GRAY);
        text.addFocusListener(this);

        send = new JButton("Send");
        send.setPreferredSize(new Dimension(80, 40));
        send.addActionListener(this);

        panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel3.setBackground(new Color(245, 245, 255));
        panel3.add(text);
        panel3.add(send);

        setLayout(new BorderLayout());
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send) {
            String message = text.getText().trim();
            if (!message.isEmpty()) {
                text.setText("");
                ChattingArea.append("Server: " + message + "\n");
                writer.println(message);
            }
        }
    }

    public void focusGained(FocusEvent e) {
        if (text.getText().equals("Enter Here......")) {
            text.setText("");
            text.setForeground(Color.BLACK);
        }
    }

    public void focusLost(FocusEvent e) {
        if (text.getText().isEmpty()) {
            text.setText("Enter Here......");
            text.setForeground(Color.GRAY);
        }
    }

    public void startFileReceiver() {
        Thread fileThread = new Thread(() -> {
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                while (true) {
                    String fileName = dis.readUTF();
                    long fileSize = dis.readLong();

                    File receivedFile = new File("received_" + fileName);
                    FileOutputStream fos = new FileOutputStream(receivedFile);

                    byte[] buffer = new byte[4096];
                    long remaining = fileSize;
                    int read;
                    while (remaining > 0 && (read = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                        fos.write(buffer, 0, read);
                        remaining -= read;
                    }
                    fos.close();
                    ChattingArea.append("File received: " + receivedFile.getName() + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                ChattingArea.append("File receiving stopped.\n");
            }
        });
        fileThread.start();
    }

    public void startReading() {
        Thread readThread = new Thread(() -> {             // Arrow Function is used and Use try catch Block regularly
            try {
                String msg;
                while ((msg = reader.readLine()) != null) {
                    ChattingArea.append("Client: " + msg + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                ChattingArea.append("Connection closed.\n");
            }
        });
        readThread.start();
    }
}

public class ServerSide {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Server is waiting for client...");
            Socket socket = ss.accept();
            System.out.println("Client connected.");
            new ServerGUI(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

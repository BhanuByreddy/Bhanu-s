import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

class ClientGUI extends JFrame implements ActionListener, FocusListener {
    JPanel panel1, panel2, panel3, panel4, panel5;
    JButton send,sendfile;
    JTextField text;
    JTextArea ChattingArea;
    JLabel label1;
    JFileChooser file;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
	InputStream input;
	OutputStream output;

    ClientGUI() {
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 255, 255));
        setTitle("CLIENT_SIDE");

        label1 = new JLabel();
        label1.setText("CHAT APPLICATION");
        label1.setFont(new Font("Frutiger", Font.BOLD, 45));
        label1.setForeground(new Color(255, 255, 255));
        file = new JFileChooser();
        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(600, 100));
        panel1.setBackground(Color.BLACK);
        panel1.setLayout(new FlowLayout());
        panel1.add(label1);

        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(600, 370));
        panel2.setLayout(new BorderLayout());

        ChattingArea = new JTextArea();
        ChattingArea.setBackground(new Color(255, 182, 193));
        ChattingArea.setBorder(BorderFactory.createLineBorder(Color.PINK, 5));
        ChattingArea.setEditable(false);
        ChattingArea.setFont(new Font("Arial", Font.BOLD, 15));
        ChattingArea.setLineWrap(true);
        ChattingArea.setWrapStyleWord(true);

        JScrollPane scrollpane = new JScrollPane(ChattingArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setBorder(BorderFactory.createLineBorder(Color.PINK, 6));
        panel2.add(scrollpane, BorderLayout.CENTER);

        panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(600, 100));
        panel3.setBackground(new Color(0, 0, 0));
        panel3.setLayout(new BorderLayout());

        text = new JTextField();
		text.setText("Enter Here......");
		text.setPreferredSize(new Dimension(250, 40)); 
		text.setForeground(Color.GRAY);
		text.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		text.setFont(new Font("Arial", Font.BOLD, 18));
		text.addFocusListener(this);

		panel4 = new JPanel();
		panel4.setPreferredSize(new Dimension(300, 80)); 
		panel4.setBackground(new Color(54, 69, 79));
		panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15)); 
		panel4.add(text);
		panel4.setBorder(BorderFactory.createEtchedBorder());

		sendfile=new JButton("Send File");
		sendfile.setPreferredSize(new Dimension(70,50));
		sendfile.setBackground(new Color(245, 245, 245));
		sendfile.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
		sendfile.addActionListener(this);
		
		
        panel5 = new JPanel();
        panel5.setPreferredSize(new Dimension(150, 100));
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER,5, 20));
        panel5.setBackground(new Color(54, 69, 79));
		
        panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(600, 80));
		panel3.setBackground(new Color(54, 69, 79));
		panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15)); 

		text = new JTextField("Enter Here......");
		text.setPreferredSize(new Dimension(300, 40)); 
		text.setForeground(Color.GRAY);
		text.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		text.setFont(new Font("Arial", Font.BOLD, 18));
		text.addFocusListener(this);

		send = new JButton("Send");
		send.setPreferredSize(new Dimension(90, 40));
		send.setBackground(new Color(245, 245, 245));
		send.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		send.addActionListener(this);

		sendfile = new JButton("Send File");
		sendfile.setPreferredSize(new Dimension(100, 40));
		sendfile.setBackground(new Color(245, 245, 245));
		sendfile.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		sendfile.addActionListener(this);

		
		panel3.add(text);
		panel3.add(send);
		panel3.add(sendfile);

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
        
        setVisible(true);
		connectToServer();
        startReading();
    }
    public void connectToServer() {
        try {
            socket = new Socket("localhost", 1234); // connect to localhost
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            ChattingArea.append("Connected to server.\n");
        } catch (Exception e) {
            ChattingArea.append("Connection failed: " + e.getMessage() + "\n");
        }
    }
	public void startReading() {
        Thread readThread = new Thread() {
            public void run() {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        ChattingArea.append("Server: " + message + "\n");
                    }
                } catch (IOException e) {
                    ChattingArea.append("Disconnected.\n");
                }
            }
        };
        readThread.start();
    }
	public void sendtoServer(File Selectedfile)
	{
        try
		{
		   	 byte[] buffer = new byte[4096];
        int bytesRead;

      
        FileInputStream fis = new FileInputStream(Selectedfile);

        
        OutputStream out = socket.getOutputStream();

       
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeUTF(Selectedfile.getName());
        dataOut.writeLong(Selectedfile.length());

        while ((bytesRead = fis.read(buffer)) != -1) {
            dataOut.write(buffer, 0, bytesRead);
        }

        fis.close();
        ChattingArea.append("File sent: " + Selectedfile.getName() + "\n");

    } catch (IOException e) {
        ChattingArea.append("Error sending file: " + e.getMessage() + "\n");
    }
	}
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send) {
            String str = text.getText();
			 writer.println(str);
            text.setText("");
            ChattingArea.append("Client: " + str + "\n");
        }
		if( e.getSource() == sendfile)
		{
		    int value = file.showOpenDialog(this); 
			if(value == JFileChooser.APPROVE_OPTION)
			{ 
		       File Selectedfile = file.getSelectedFile();
			   sendtoServer(Selectedfile);
			}
		}
    }

    public void focusGained(FocusEvent e) {
        if (e.getSource() == text) {
            if (text.getText().equals("Enter Here......")) {
                text.setText("");
                text.setForeground(Color.BLACK);
            }
        }
    }

    public void focusLost(FocusEvent e) {
        if (e.getSource() == text) {
            if (text.getText().isEmpty()) {
                text.setText("Enter Here......");
                text.setForeground(Color.GRAY);
            }
        }
    }
}

public class ClientSide {
    public static void main(String[] args) {
        new ClientGUI();
    }
}

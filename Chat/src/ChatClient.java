import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private Frame frame;
    private TextField textField;
    private TextArea textArea;
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    String temp = "";

    public ChatClient() {
        frame = new Frame("ChatApp");
        textArea = new TextArea();
        textField = new TextField();
        try {
            clientSocket = new Socket("127.0.0.1", 9999);
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTextComponentAndAction() {
        this.frame.setLayout(new BorderLayout());
        this.frame.add(textArea, BorderLayout.NORTH);
        this.frame.add(textField, BorderLayout.SOUTH);
        this.textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataOutputStream.writeUTF(textField.getText().trim());
                    dataOutputStream.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                temp += (textField.getText().trim() + "\r\n");
                //textArea.setText(textField.getText().trim());
                textField.setText("");
            }
        });
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });
    }

    private void showClientFrame() {
        addTextComponentAndAction();
        this.frame.pack();
        this.frame.setVisible(true);
    }


    public static void main(String[] args) {
        ChatClient myChatClient = new ChatClient();
        myChatClient.showClientFrame();
        myChatClient.receiveMessage();

    }

    public void receiveMessage() {
        new Thread(new ReceiveMessage()).start();
    }

    class ReceiveMessage implements Runnable {

        @Override
        public void run() {
            String temp;
            String cache = "";
            try {
                while ((temp = dataInputStream.readUTF()) != null) {
                    cache += (temp + "\r\n");
                    textArea.setText(cache);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

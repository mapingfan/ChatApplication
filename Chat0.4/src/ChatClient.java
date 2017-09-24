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

    Frame frame;
    TextArea textArea;
    TextField textField;
    String content = "";
    Socket clientSocket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    public ChatClient() {
        frame = new Frame("ChatApp");
        textArea = new TextArea();
        textField = new TextField();
        try {
            clientSocket = new Socket("127.0.0.1",9999);
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClient() {
        this.frame.setLayout(new BorderLayout());
        this.frame.add(textArea, BorderLayout.NORTH);
        this.frame.add(textField, BorderLayout.SOUTH);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });
        this.textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                content += textField.getText().trim()+"\r\n";
                sendMessage(textField.getText().trim());
                //textArea.setText(content);
                textField.setText("");


            }
        });
        this.frame.pack();
    }

    public void launchFrame() {
        this.setClient();
        this.frame.setVisible(true);
        //this.receiveMessage();
        Thread thread = new Thread(new ReceiveThread());
        thread.start();
    }

    public void sendMessage(String string) {
        try {
            dataOutputStream.writeUTF(string);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() {
        String tempLine;
        String str = "";
        try {
            while ((tempLine=dataInputStream.readUTF())!=null) {
                str += (tempLine+"\r\n");
                textArea.setText(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ReceiveThread implements Runnable {
        @Override
        public void run() {
            receiveMessage();
        }
    }

    public static void main(String[] args) {
        ChatClient myChatClient = new ChatClient();
        myChatClient.launchFrame();

    }
}

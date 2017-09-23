import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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


    public ChatClient() {
        frame = new Frame("ChatApp");
        textArea = new TextArea();
        textField = new TextField();
        try {
            clientSocket = new Socket("127.0.0.1",9999);
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
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
                textArea.setText(content);
                textField.setText("");


            }
        });
        this.frame.pack();
    }

    public void launchFrame() {
        this.setClient();
        this.frame.setVisible(true);
    }

    public void sendMessage(String string) {
        try {
            dataOutputStream.writeUTF(string);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient myChatClient = new ChatClient();
        myChatClient.launchFrame();
    }
}


/*
客户端：界面，组件，网络连接；
 */
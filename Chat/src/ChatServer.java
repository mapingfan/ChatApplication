import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {

    private ServerSocket serverSocket;
    private static int cnt = 0;
    private ArrayList<DataOutputStream> dataOutputStreamArrayList = new ArrayList<>();

    public ChatServer() {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServer() {
        while (true) {
            ConnectionManage connectionManage = new ConnectionManage();
            dataOutputStreamArrayList.add(connectionManage.dataOutputStream);
            cnt++;
            Thread thread = new Thread(connectionManage);
            thread.start();
        }
    }

    class ConnectionManage implements Runnable {

        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        public ConnectionManage() {
            try {
                this.socket = serverSocket.accept();
                dataInputStream = new DataInputStream(this.socket.getInputStream());
                dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("A client has connected");
            receiveAndForwardMessage();
        }

        private void receiveAndForwardMessage() {
            String temp;
            try {
                while ((temp = dataInputStream.readUTF()) != null) {
                    System.out.println(temp);
                    forwardMessage(temp);
                }
            } catch (IOException e) {

            } finally {
                int tempCount = cnt;
                cnt--;
                if (tempCount - 1 == cnt) {
                    dataOutputStreamArrayList.remove(this.dataOutputStream);
                }
                if (cnt == 0) {
                    System.exit(-1);
                }
                try {
                    dataInputStream.close();
                    this.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        private void forwardMessage(String string) {
            Iterator<DataOutputStream> iterator = dataOutputStreamArrayList.iterator();
            while (iterator.hasNext()) {
                DataOutputStream temp = iterator.next();
                try {
                    temp.writeUTF(string);
                    temp.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ChatServer myChatServer = new ChatServer();
        myChatServer.runServer();
    }
}

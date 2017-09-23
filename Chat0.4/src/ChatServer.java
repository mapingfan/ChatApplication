import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    ServerSocket serverSocket;
    Socket acceptSocket;
    static int cnt = 0;

    public static void main(String[] args) {
        ChatServer myServer = new ChatServer();
        myServer.runServer();
    }

    public ChatServer() {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServer() {
        try {
            while (true) {
                acceptSocket = serverSocket.accept();
                ConnectionManage connectionManage = new ConnectionManage(acceptSocket);
                cnt++;
                new Thread(connectionManage).start();
            }

        } catch (IOException e) {
            System.exit(-1);
            //e.printStackTrace();
        }
    }


    class ConnectionManage implements Runnable {

        Socket socket;
        DataInputStream dataInputStream;
        String receiveContent;

        public ConnectionManage(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(this.socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("A client has connected");
            try {
                while ((receiveContent = dataInputStream.readUTF()) != null) {
                    if (receiveContent.equals("exit")) {
                        System.exit(-1);
                    }
                    System.out.println(receiveContent);
                }
            } catch (IOException e) {

            } finally {
                try {
                    cnt--;
                    if(cnt == 0) {
                        System.exit(-1);
                    }
                    dataInputStream.close();
                    socket.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}


/*
客户端：网络连接组件
 */
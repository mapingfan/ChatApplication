import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {

    ServerSocket serverSocket;
    Socket acceptSocket;
    static int cnt = 0;
    ArrayList<Socket> socketArrayList = new ArrayList<>();
    String forwardContent = "";

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
                socketArrayList.add(acceptSocket);
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
                    //forwardContent += receiveContent;
                    System.out.println(receiveContent);
                    forwardMessage(receiveContent);
                }
            } catch (IOException e) {

            } finally {
                try {
                    int temp =cnt;
                    cnt--;
                    if(temp-1 == cnt) {
                        socketArrayList.remove(socket);
                    }
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

    public void forwardMessage(String string) {
        //获得每个线程的socket.getOutputStream。

        Iterator<Socket> socketIterator = socketArrayList.iterator();
        while (socketIterator.hasNext()) {
            Socket temp = socketIterator.next();
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(temp.getOutputStream());
                dataOutputStream.writeUTF(string);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

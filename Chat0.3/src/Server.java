import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket serverSocket;
    Socket acceptSocket;
    DataInputStream dataInputStream;

    public static void main(String[] args) {
        Server myServer = new Server();
        myServer.runServer();


    }

    public void runServer() {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            e.printStackTrace();
        }
        while (true) {
            try {

                acceptSocket = serverSocket.accept();
                dataInputStream = new DataInputStream(acceptSocket.getInputStream());
                receiveInformation();
            } catch (IOException e) {
                try {
                    acceptSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                try {
                    acceptSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            //System.out.println("A client connected");
        }
    }

    public void receiveInformation() {
        String temp;
        try {
            while ((temp = dataInputStream.readUTF()) != null) {
                if (temp.equals("exit")) {
                    break;
                }
                System.out.println(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



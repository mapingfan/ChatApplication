import java.awt.*;

public class Client {
    public static void main(String[] args) {
        new MyClientFrame("ChatAPP");
    }
}

class MyClientFrame extends Frame {

    public MyClientFrame(String title) throws HeadlessException {
        super(title);
        this.setSize(400,400);
        this.setVisible(true);
    }

}


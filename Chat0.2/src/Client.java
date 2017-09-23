import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Client {

    public static void main(String[] args) {
        new MyClientFrame("ChatAPP").addComponent();
    }
}

class MyClientFrame extends Frame {

    String content = "";
    TextField textField;
    TextArea textArea;

    public MyClientFrame(String title) throws HeadlessException {
        super(title);
        this.setSize(400,400);
        this.setVisible(true);
        textField = new TextField(10);
        textArea = new TextArea();
    }

    public void addComponent() {


        textField.addActionListener(new TextFieldActionMonitor());
        this.setLayout(new BorderLayout());
        this.add(textArea,BorderLayout.NORTH);
        this.add(textField,BorderLayout.SOUTH);
        pack(); //消除白框问题。
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });
    }

    private class TextFieldActionMonitor implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            content += textField.getText().trim()+"\r\n";
            textArea.setText(content);
            textField.setText("");
        }
    }

}


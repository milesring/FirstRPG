import java.awt.EventQueue;
import javax.swing.*;

public class Arena extends JFrame {

    private Arena() {

        initUI();
    }

    private void initUI() {
        int x = 640;
        int y = 480;
        add(new Board(x, y));

        setSize(x, y);
        setResizable(false);
        setTitle("Arena");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                Arena ex = new Arena();
                ex.setVisible(true);
            }
        });
    }
}
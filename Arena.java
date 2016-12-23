import java.awt.EventQueue;
import javax.swing.JFrame;

public class Arena extends JFrame {

    public Arena() {

        initUI();
    }

    private void initUI() {
        int x = 1920;
        int y = 1080;
        add(new Board(x, y));

        setSize(x, y);
        setResizable(false);
        setTitle("Arena");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
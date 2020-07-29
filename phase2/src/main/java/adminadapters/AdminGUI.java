package adminadapters;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminGUI implements ActionListener {
    private final JFrame frame;
    private final int width;
    private final int height;
    private final JPanel panel;
    private JButton button;

    public AdminGUI(int width, int height) {
        this.width = width;
        this.height = height;
        this.frame = new JFrame();
        this.button = new JButton();
        this.panel = new JPanel();
    }

    public void adminInitialScreen() {
        frame.setSize(width, height);
        frame.setTitle("Admin User");   // or we could use JLabel and add to the panel?
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     // AdminUser closes the window
        frame.add(panel);
    }

    public void askAdminToAddNewAdmin() {


    }

    @Override
    public void actionPerformed(ActionEvent e) {



    }
}

package adminadapters;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AdminGUI implements ActionListener {
    private final JFrame frame;
    private final int width;
    private final int height;
    private final JPanel panel;
    private JButton button;
    private AdminPresenter adminPresenter;
    private  AdminController adminController;

    public AdminGUI(int width, int height, AdminPresenter adminPresenter, AdminController adminController) {
        this.width = width;
        this.height = height;
        this.frame = new JFrame();
        this.button = new JButton();
        this.panel = new JPanel();
        this.adminPresenter = adminPresenter;
        this.adminController = adminController;
    }

    public void adminInitialScreen() {
        frame.setSize(width, height);
        frame.setTitle("Admin User");   // or we could use JLabel and add to the panel?
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     // AdminUser closes the window
        frame.add(panel);
    }

    public JTextField askAdminForName() {
        JLabel askForName = new JLabel(adminPresenter.accountEnterName());
        panel.add(askForName);
        JTextField name = new JTextField();
        return name;
    }

    public JTextField askAdminForUserName() {
        JLabel askForUserName = new JLabel(adminPresenter.accountEnterUsername());
        panel.add(askForUserName);
        JTextField username = new JTextField();
        return username;
    }

    public JTextField askAdminForPassword() {
        JLabel askForPassword = new JLabel(adminPresenter.accountEnterPassword());
        panel.add(askForPassword);
        JTextField password = new JTextField();
        return password;

    }

    public void addAdminListener() throws IOException {
        adminController.askAdminToAddNewAdmin(askAdminForName(), askAdminForUserName(), askAdminForPassword());
    }



    //public void askAdminToAddNewAdmin() {
      //  JLabel askForName = new JLabel(adminPresenter.accountEnterName());
      //  JLabel askForUserName = new JLabel(adminPresenter.accountEnterUsername());
      //  JLabel askForPassword = new JLabel(adminPresenter.accountEnterPassword());
      //  panel.add(askForName);
      //  panel.add(askForUserName);
      //  panel.add(askForPassword);
      //  JTextField name = new JTextField();
      //  JTextField username = new JTextField();
      //  JTextField password = new JTextField();
   // }

    @Override
    public void actionPerformed(ActionEvent e) {



    }
}

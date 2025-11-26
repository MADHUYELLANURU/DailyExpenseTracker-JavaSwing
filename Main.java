import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // initialize database
        DatabaseInit.init();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}

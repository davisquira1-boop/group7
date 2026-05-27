import javax.swing.SwingUtilities;

public class BaselineMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BaselineTaskManagerGUI().createAndShowGUI();
        });
    }
}

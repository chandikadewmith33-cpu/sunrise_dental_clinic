package sunrise.dental;

import view.Login;

import javax.swing.*;

/**
 * Application entry point. Launches the Login screen.
 */
public class SunriseDental {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}

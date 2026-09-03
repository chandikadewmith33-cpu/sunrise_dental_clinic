package sunrise.dental;

import view.Login;
import service.LocalPDFServer;

import javax.swing.*;

/**
 * Application entry point.
 * Starts the local PDF server and launches the Login screen.
 */
public class SunriseDental {

    public static void main(String[] args) {

        // Start local PDF server
        LocalPDFServer.startServer();

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            System.err.println(
                    "Could not set system look and feel: "
                    + e.getMessage()
            );
        }

        SwingUtilities.invokeLater(() ->
                new Login().setVisible(true)
        );
    }
}
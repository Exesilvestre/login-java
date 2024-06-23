package challenge.demo.frontend;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class FrontendApplication {

    public static void main(String[] args) {
        // Configurar el tema oscuro FlatDarculaLaf
        try {
            FlatDarculaLaf.setup();
        } catch (Exception ex) {
            ex.printStackTrace();
            // Manejar la excepción si ocurre algún problema al configurar el tema oscuro
        }

        SwingUtilities.invokeLater(() -> {

            login loginFrame = new login();
            loginFrame.setVisible(true);
        });
    }
}
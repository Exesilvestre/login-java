import challenge.demo.Services.usersDTO.ReadUserDTO;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Logged extends JFrame{
    private JPanel Logged;
    private JButton cerrarSesiónButton;
    private JLabel NomYApell;
    private JLabel bienvenido;


    public Logged(ReadUserDTO userDTO) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        add(Logged);
        setTitle("Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        NomYApell.setText(userDTO.getName() + " " + userDTO.getLastName());

        cerrarSesiónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                new login();
            }
        });
    }
}

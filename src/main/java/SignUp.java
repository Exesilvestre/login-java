import challenge.demo.Services.ErrorMessage;
import challenge.demo.Services.usersDTO.CreateUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends JFrame{
    private JPanel Registrar;
    private JTextField emailInput;
    private JPasswordField passwordInput;
    private JButton confirmarRegistroButton;
    private JPanel signup;
    private JTextField apellidoInput;
    private JTextField nombreInput;

    public SignUp() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        add(signup);
        setTitle("SignUp");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        confirmarRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailInput.getText().trim();
                String contrasena = new String(passwordInput.getPassword()).trim();
                String nombre = nombreInput.getText().trim();
                String apellido = apellidoInput.getText().trim();

                if (email.isEmpty() || contrasena.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUp.this, "Por favor complete todos los campos.");
                    return;
                }

                CreateUserDTO userDTO = new CreateUserDTO(email, contrasena, nombre, apellido);

                try {
                    HttpURLConnection con = setupConnection("http://localhost:8080/api/users/signUp", "POST", "application/json");


                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonInputString = objectMapper.writeValueAsString(userDTO);

                    con.setDoOutput(true);
                    con.getOutputStream().write(jsonInputString.getBytes());

                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(SignUp.this, "Registro exitoso");
                        dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new login().setVisible(true);
                            }
                        });
                    }else {
                        handleSignUpFailure(con);
                    }

                    con.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignUp.this, "Error de conexión: " + ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignUp.this, "Error inesperado: " + ex.getMessage());
                }
            }
        });
    }

    private HttpURLConnection setupConnection(String urlString, String method, String contentType) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", contentType);
        con.setDoOutput(true);
        return con;
    }

    private void handleSignUpFailure(HttpURLConnection con) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage();
        String errorMessageParsed = errorMessage.readErrorResponse(con);
        JOptionPane.showMessageDialog(this, "Error al registrarse: " + errorMessageParsed);
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new SignUp();
    }

}

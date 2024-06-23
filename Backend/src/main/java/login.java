import javax.swing.*;

import challenge.demo.Services.ErrorMessage;
import challenge.demo.Services.usersDTO.LoginUserDTO;
import challenge.demo.Services.usersDTO.ReadUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatDarculaLaf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton ingresarButton;
    private  JPanel Login;
    private JButton olvideContraseñaButton;
    private JButton registrarseButton;

    public login() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        add(Login);
        setTitle("Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText().trim();
                String password = new String(passwordField1.getPassword()).trim();
                LoginUserDTO loginUserDTO = new LoginUserDTO(email, password);

                try {
                    HttpURLConnection con = setupConnection("http://localhost:8080/api/users/login", "POST", "application/json");

                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonInputString = objectMapper.writeValueAsString(loginUserDTO);
                    con.getOutputStream().write(jsonInputString.getBytes());

                    int responseCode = con.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        handleLoginSuccess(con, objectMapper);
                    } else {
                        handleLoginFailure(con);
                    }

                    con.disconnect();
                } catch (IOException ex) {
                    handleError("Error de conexión: " + ex.getMessage());
                } catch (Exception ex) {
                    handleError("Error inesperado: " + ex.getMessage());
                }
            }
        });

        registrarseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUp signUpWindow = new SignUp();
                signUpWindow.setVisible(true);

                dispose();
            }
        });

        olvideContraseñaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText().trim();

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(login.this, "Por favor ingrese su correo electrónico.");
                    return;
                }

                try {
                    HttpURLConnection con = setupConnection("http://localhost:8080/api/users/changePassword/?email=" + email, "POST", "application/json");


                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(login.this, "Nueva contraseña enviada por email.");
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        JOptionPane.showMessageDialog(login.this, "Usuario no encontrado.");
                    } else {
                        handleForgotPasswordError(con);
                    }

                    con.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(login.this, "Error de conexión: " + ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(login.this, "Error inesperado: " + ex.getMessage());
                }
            }
        });
    }

    private void handleForgotPasswordError(HttpURLConnection con) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage();
        String errorMessageParsed = errorMessage.readErrorResponse(con);
        JOptionPane.showMessageDialog(this, "Error: " + errorMessageParsed);
    }

    private HttpURLConnection setupConnection(String urlString, String method, String contentType) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", contentType);
        con.setDoOutput(true);
        return con;
    }

    private void handleLoginSuccess(HttpURLConnection con, ObjectMapper objectMapper) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            ReadUserDTO userDTO = objectMapper.readValue(response.toString(), ReadUserDTO.class);
            Logged loggedWindow = new Logged(userDTO);
            loggedWindow.setVisible(true);
            dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
            handleError("Error al procesar la respuesta del servidor: " + ex.getMessage());
        }
    }

    private void handleLoginFailure(HttpURLConnection con) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage();
        String errorMessageParsed = errorMessage.readErrorResponse(con);
        JOptionPane.showMessageDialog(this, "Error en el inicio de sesión: " + errorMessageParsed);
    }

    private void handleError(String message) {
        JOptionPane.showMessageDialog(this, "Error: " + message);
    }


    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new login();
    }


}



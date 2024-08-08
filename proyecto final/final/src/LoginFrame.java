import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // Fondo azul claro
        getContentPane().setBackground(new Color(173, 216, 230));

        // Configuración del layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Título principal
        JLabel lblTitulo = new JLabel("AutoPartsXpress");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("Username:");
        add(userLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (userDAO.authenticate(username, password)) {
                    Document user = userDAO.findUserByUsername(username);
                    String role = user.getString("role");
                    dispose();
                    if (role.equals("admin")) {
                        new AdminFrame().setVisible(true);
                    } else if (role.equals("cajero")) {
                        new CashierFrame(username).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed!");
                }
            }
        });

        gbc.gridy = 4;
        JButton exitButton = new JButton("Salir");
        add(exitButton, gbc);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Conectar a la base de datos
        String uri = "mongodb+srv://jp7mavort:12345@final.012d6.mongodb.net/AutoPartsXpress?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("AutoPartsXpress");
        userDAO = new UserDAO(database);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}

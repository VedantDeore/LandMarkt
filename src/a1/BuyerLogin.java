package a1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BuyerLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private int currentSessionID = -1; // To track the current session

    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc"; // Replace with your database password

    public BuyerLogin() {
        setTitle("Buyer Login");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(140, 191, 214));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(new Color(255, 255, 255, 200));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(titleLabel);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel subTitleLabel = new JLabel("Please enter your name and phone number to login.");
        subTitleLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(subTitleLabel);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        usernameField = new JTextField("Name");
        usernameField.setFont(new Font("Roboto", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        usernameField.setMaximumSize(new Dimension(300, 40));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(usernameField);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        passwordField = new JPasswordField("Number");
        passwordField.setFont(new Font("Roboto", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(passwordField);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Roboto", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(50, 143, 138));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validate the login using the SQL database
                if (isValidLogin(username, password)) {
                    // Create a new session
                    createSession(username);

                    // Close the login window
                    dispose();

                    // Open the BuyerScreen
                    SwingUtilities.invokeLater(() -> {
                        new BuyerScreen().setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect username or password");
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Roboto", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(255, 99, 71));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update session logout time
                if (currentSessionID != -1) {
                    updateSessionLogoutTime(currentSessionID);
                }

                // Navigate back to the MainPage
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new RealEstateApp().setVisible(true); // Adjust this if you have a different page to navigate to
                });
            }
        });

        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel registerLabel = new JLabel("Not registered? ");
        registerLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        loginPanel.add(registerLabel);

        JLabel registerLink = new JLabel("Create an account");
        registerLink.setFont(new Font("Roboto", Font.PLAIN, 12));
        registerLink.setForeground(new Color(76, 175, 80));
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Implement a link action here
            	 new BuyerRegistration().setVisible(true);
                 setVisible(false); // Hide the login frame
            }
        });
        loginPanel.add(registerLink);

        mainPanel.add(loginPanel, gridBagConstraints);

        add(mainPanel);
        setVisible(true);
    }

    private boolean isValidLogin(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT * FROM buyertable WHERE Name = ? AND Number = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            boolean valid = resultSet.next(); // If there's a matching row in the database, the login is valid.

            conn.close();
            return valid;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createSession(String username) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Find the BuyerID for the logged-in user
            String query = "SELECT BuyerID FROM buyertable WHERE Name = ? AND Number = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, passwordField.getText());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int buyerID = resultSet.getInt("BuyerID");

                // Insert a new session record
                query = "INSERT INTO UserSession (BuyerID, LoginTime) VALUES (?, NOW())";
                PreparedStatement insertStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setInt(1, buyerID);
                insertStatement.executeUpdate();

                // Retrieve the generated session ID
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    currentSessionID = generatedKeys.getInt(1);
                }
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSessionLogoutTime(int sessionID) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "UPDATE UserSession SET LogoutTime = NOW() WHERE SessionID = ? AND LogoutTime IS NULL";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, sessionID);
            statement.executeUpdate();

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BuyerLogin();
        });
    }
}

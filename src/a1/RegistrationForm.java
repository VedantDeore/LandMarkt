package a1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationForm {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";

    private JFrame frame;
    private JTextField nameField;
    private JTextField numberField;
    private JTextField emailField;
    private JRadioButton buyerRadioButton;
    private JRadioButton sellerRadioButton;
    private ButtonGroup radioButtonGroup;
    private JButton registerButton;
    private JLabel outputLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistrationForm().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Registration Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame to fill the entire screen
        frame.setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel numberLabel = new JLabel("Number:");
        numberField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        buyerRadioButton = new JRadioButton("Register as Buyer");
        sellerRadioButton = new JRadioButton("Register as Seller");
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(buyerRadioButton);
        radioButtonGroup.add(sellerRadioButton);

        registerButton = new JButton("Register");
        outputLabel = new JLabel();

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(numberLabel);
        frame.add(numberField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(buyerRadioButton);
        frame.add(sellerRadioButton);
        frame.add(registerButton);
        frame.add(outputLabel);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String number = numberField.getText();
                String email = emailField.getText();
                String userType = buyerRadioButton.isSelected() ? "Buyer" : "Seller";

                String tableName = userType.equals("Buyer") ? "buyertable" : "sellertable";
                String insertQuery = String.format("INSERT INTO %s (Name, Number, Email) VALUES (?, ?, ?)", tableName);
                executeQuery(insertQuery, name, number, email);
            }
        });

        frame.setVisible(true);
    }
    private void redirectToRealEstateApp() {
        // Open the RealEstateApp.java or redirect to a new JFrame
        RealEstateApp.main(new String[]{});
        frame.dispose(); // Close the current registration frame
    }
    private void executeQuery(String query, String... params) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = conn.prepareStatement(query)) {
                int index = 1;
                for (String param : params) {
                    statement.setString(index, param);
                    index++;
                }
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    outputLabel.setText("Registration Successful.");
                } else {
                    outputLabel.setText("Registration failed.");
                }
                redirectToRealEstateApp();
            } catch (SQLException e) {
                outputLabel.setText("Error: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

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

public class BuyerRegistration {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";

    private JFrame frame;
    private JTextField nameField;
    private JTextField numberField;
    private JTextField emailField;
    private JLabel outputLabel;

    public BuyerRegistration() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Buyer Registration Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Replace with your actual image path
                Image backgroundImage = new ImageIcon("images/registration_bg.jpg").getImage();
                g.drawImage(backgroundImage, 0, 0, this);
            }
        };
        mainPanel.setLayout(null);

        JPanel formPanel = new JPanel();
        formPanel.setBounds(500, 150, 500, 400);
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(255, 255, 255, 150)); // Updated background color

        JLabel titleLabel = new JLabel("Buyer Registration");
        titleLabel.setBounds(100, 30, 300, 50);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK); // Updated text color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 100, 150, 30);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        nameLabel.setForeground(Color.BLACK); // Updated text color
        formPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 100, 250, 30);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        formPanel.add(nameField);

        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setBounds(50, 150, 150, 30);
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        numberLabel.setForeground(Color.BLACK); // Updated text color
        formPanel.add(numberLabel);

        numberField = new JTextField();
        numberField.setBounds(200, 150, 250, 30);
        numberField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        formPanel.add(numberField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 200, 150, 30);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        emailLabel.setForeground(Color.BLACK); // Updated text color
        formPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(200, 200, 250, 30);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        formPanel.add(emailField);

        JButton registerButton = new JButton("Register Buyer");
        registerButton.setBounds(150, 300, 200, 40);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerButton.setBackground(new Color(50, 143, 138));
        registerButton.setForeground(Color.WHITE);
        formPanel.add(registerButton);

        outputLabel = new JLabel();
        outputLabel.setBounds(50, 350, 400, 30);
        outputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        outputLabel.setForeground(Color.BLACK); // Updated text color
        formPanel.add(outputLabel);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String number = numberField.getText();
                String email = emailField.getText();

                if (!name.isEmpty() && !number.isEmpty() && !email.isEmpty()) {
                    checkAndRegisterBuyer(name, number, email);
                } else {
                    outputLabel.setText("Please fill all fields.");
                }
            }
        });

        mainPanel.add(formPanel);
        frame.setContentPane(mainPanel);
    }

    private void checkAndRegisterBuyer(String name, String number, String email) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String checkQuery = "SELECT * FROM buyertable WHERE Email = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, email);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        outputLabel.setText("Buyer already registered with this email.");
                    } else {
                        String insertQuery = "INSERT INTO buyertable (Name, Number, Email) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, name);
                            insertStmt.setString(2, number);
                            insertStmt.setString(3, email);
                            int rowsAffected = insertStmt.executeUpdate();
                            if (rowsAffected > 0) {
                                outputLabel.setText("Buyer registered successfully.");
                                // Dispose current frame and show BuyerLogin frame
                                frame.dispose();
                                new BuyerLogin().setVisible(true);
                            } else {
                                outputLabel.setText("Registration failed.");
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                outputLabel.setText("Error: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean visible) {
        if (frame != null) {
            frame.setVisible(visible);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BuyerRegistration registration = new BuyerRegistration();
            registration.setVisible(true);
        });
    }
}

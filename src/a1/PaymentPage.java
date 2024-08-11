package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PaymentPage extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";
    
    private JLabel imageLabel;
    private JTextArea detailsArea;
    private JTextField buyerDetailsField;
    private JTextField sellerDetailsField;
    private JButton payButton;

    public PaymentPage(int propertyId) {
        initComponents(propertyId);
        loadPropertyDetails(propertyId);
    }

    private void initComponents(int propertyId) {
        setTitle("Payment Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Set to full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(e -> dispose());
        topPanel.add(backButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // Image Section
        imageLabel = new JLabel();
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        imageLabel.setPreferredSize(new Dimension(400, 300));
        
        // Property Details Section
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 18));
        detailsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.add(new JLabel("Property Details:", SwingConstants.CENTER), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        // Buyer and Seller Details Section
        buyerDetailsField = new JTextField("Buyer Details");
        buyerDetailsField.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        buyerDetailsField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        buyerDetailsField.setEditable(false);
        buyerDetailsField.setPreferredSize(new Dimension(600, 50)); // Increased size

        sellerDetailsField = new JTextField("Seller Details");
        sellerDetailsField.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        sellerDetailsField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        sellerDetailsField.setEditable(false);
        sellerDetailsField.setPreferredSize(new Dimension(600, 50)); // Increased size

        // Payment Button
        payButton = new JButton("Make Payment");
        payButton.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        payButton.setPreferredSize(new Dimension(300, 60)); // Increased size
        payButton.addActionListener(e -> makePayment(propertyId));
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomPanel.add(buyerDetailsField);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bottomPanel.add(sellerDetailsField);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bottomPanel.add(payButton);
        
        contentPanel.add(imageLabel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadPropertyDetails(int propertyId) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM property WHERE PropertyID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, propertyId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String imagePath = "images/" + result.getString("PropertyName").toLowerCase().replace(" ", "_") + ".jpg";
                imageLabel.setIcon(new ImageIcon(imagePath));
                detailsArea.setText("Name: " + result.getString("PropertyName") +
                        "\nAddress: " + result.getString("Address") +
                        "\nCity: " + result.getString("City") +
                        "\nState: " + result.getString("State") +
                        "\nPIN Code: " + result.getString("PINCode") +
                        "\nProperty Type: " + result.getString("PropertyType") +
                        "\nNumber of Units: " + result.getString("NumUnits") +
                        "\nTotal Sq Footage: " + result.getString("TotalSqFootage") +
                        "\nYear Built: " + result.getString("YearBuilt") +
                        "\nPurchase Date: " + result.getString("PurchaseDate") +
                        "\nPurchase Price: " + result.getString("PurchasePrice") +
                        "\nCurrent Market Value: " + result.getString("CurrentMarketValue"));

                // Load buyer and seller details
                int buyerId = getBuyerIdFromSession();
                if (buyerId != -1) {
                    String buyerName = getBuyerName(buyerId);
                    buyerDetailsField.setText("Buyer: " + buyerName);
                }

                int sellerId = getSellerIdFromProperty(propertyId);
                if (sellerId != -1) {
                    String sellerName = getSellerName(sellerId);
                    sellerDetailsField.setText("Seller: " + sellerName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getBuyerIdFromSession() {
        Connection connection = null;
        int buyerId = -1;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT BuyerID FROM usersession ORDER BY LoginTime DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                buyerId = result.getInt("BuyerID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return buyerId;
    }

    private String getBuyerName(int buyerId) {
        Connection connection = null;
        String buyerName = "Unknown";
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT Name FROM buyertable WHERE BuyerID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, buyerId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                buyerName = result.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return buyerName;
    }

    private int getSellerIdFromProperty(int propertyId) {
        Connection connection = null;
        int sellerId = -1;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT SellerID FROM property WHERE PropertyID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, propertyId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                sellerId = result.getInt("SellerID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sellerId;
    }

    private String getSellerName(int sellerId) {
        Connection connection = null;
        String sellerName = "Unknown";
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT Name FROM sellertable WHERE SellerID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, sellerId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                sellerName = result.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sellerName;
    }

    private void makePayment(int propertyId) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "INSERT INTO payment (BuyerName, BuyerID, SellerName, SellerID, PropertyID, Amount) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            int buyerId = getBuyerIdFromSession();
            String buyerName = getBuyerName(buyerId);
            int sellerId = getSellerIdFromProperty(propertyId);
            String sellerName = getSellerName(sellerId);
            double amount = getPropertyPrice(propertyId);

            statement.setString(1, buyerName);
            statement.setInt(2, buyerId);
            statement.setString(3, sellerName);
            statement.setInt(4, sellerId);
            statement.setInt(5, propertyId);
            statement.setDouble(6, amount);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Payment successful!", "Payment", JOptionPane.INFORMATION_MESSAGE);
                new PurchaseStatus().setVisible(true);
//                dispose(); // Close the payment page
                dispose(); // Close the payment page
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while processing payment.", "Payment Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private double getPropertyPrice(int propertyId) {
        Connection connection = null;
        double price = 0.0;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT CurrentMarketValue FROM property WHERE PropertyID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, propertyId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                price = result.getDouble("CurrentMarketValue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return price;
    }

    public static void main(String[] args) {
        // For testing purposes, create an instance with a dummy property ID
        SwingUtilities.invokeLater(() -> new PaymentPage(1).setVisible(true));
    }
}

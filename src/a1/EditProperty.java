package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditProperty extends JPanel {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";
    private static final String IMAGE_FOLDER_PATH = "E:\\Eclipse p\\Assignment\\images\\";

    private JComboBox<String> stateComboBox;
    private JComboBox<String> cityComboBox;
    private JLabel imageLabel;
    private File selectedImageFile;

    public EditProperty() {
        initComponents();
    }

    private void initComponents() {
        setLayout(null);

        JLabel jLabel1 = new JLabel("Add or Edit your Property", SwingConstants.CENTER);
        jLabel1.setFont(new Font("Segoe UI Emoji", Font.BOLD, 48));

        JLabel jLabel2 = new JLabel("Enter Property Name:");
        JLabel jLabel3 = new JLabel("Enter Number of Units");
        JLabel jLabel4 = new JLabel("Enter Address:");
        JLabel jLabel5 = new JLabel("Enter City:");
        JLabel jLabel6 = new JLabel("Enter State:");
        JLabel jLabel7 = new JLabel("Enter Pincode:");
        JLabel jLabel8 = new JLabel("Enter Year Built:");
        JLabel jLabel9 = new JLabel("Enter Property Type:");
        JLabel jLabel10 = new JLabel("Enter Total Sq. Ft.");
        JLabel jLabel11 = new JLabel("Enter Current Market Value:");
        JLabel jLabel12 = new JLabel("Enter Purchase Date:");
        JLabel jLabel13 = new JLabel("Enter Purchase Price:");

        JTextField jTextField1 = new JTextField("Property Name");
        JTextField jTextField3 = new JTextField("Current Market Value");
        JTextField jTextField4 = new JTextField("(YYYY-MM-DD)");
        JTextField jTextField6 = new JTextField("Purchase Price");
        JTextField jTextField7 = new JTextField("Pincode");
        JTextArea jTextArea1 = new JTextArea("Address");
        JScrollPane jScrollPane1 = new JScrollPane(jTextArea1);

        JSpinner jSpinner1 = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JSpinner jSpinner2 = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        JSpinner jSpinner3 = new JSpinner(new SpinnerNumberModel(0.0, 0.0, null, 1.0));
        JSpinner jSpinner4 = new JSpinner(new SpinnerNumberModel(2023, 0, 2024, 1));

        JComboBox<String> jComboBox1 = new JComboBox<>(new String[]{"Commercial", "Apartment", "House"});

        JButton jButton1 = new JButton("ADD");
        JButton jButton2 = new JButton("Delete");
        JButton jButton3 = new JButton("Update");
        JButton jButton4 = new JButton("Back");
        JButton jButton5 = new JButton("Upload Image");

        stateComboBox = new JComboBox<>();
        cityComboBox = new JComboBox<>();
        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        imageLabel.setBounds(1250, 150, 300, 200); // Set dimensions to 300x200 and move right

        // Setting Fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 18);

        for (JLabel label : new JLabel[]{jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9, jLabel10, jLabel11, jLabel12, jLabel13}) {
            label.setFont(labelFont);
        }
        for (JTextField textField : new JTextField[]{jTextField1, jTextField3, jTextField4, jTextField6, jTextField7}) {
            textField.setFont(fieldFont);
        }
        jTextArea1.setFont(fieldFont);
        jComboBox1.setFont(fieldFont);
        stateComboBox.setFont(fieldFont);
        cityComboBox.setFont(fieldFont);

        // Setting Layout
        int xLabel = 50;
        int xField = 300;
        int y = 150;
        int yIncrement = 60;

        jLabel1.setBounds(0, 20, 1366, 60);
        add(jLabel1);

        jLabel2.setBounds(xLabel, y, 250, 40);
        jTextField1.setBounds(xField, y, 250, 40);
        y += yIncrement;

        jLabel4.setBounds(xLabel, y, 250, 40);
        jScrollPane1.setBounds(xField, y, 250, 80);
        y += yIncrement + 40;

        jLabel5.setBounds(xLabel, y, 250, 40);
        cityComboBox.setBounds(xField, y, 250, 40);
        y += yIncrement;

        jLabel6.setBounds(xLabel, y, 250, 40);
        stateComboBox.setBounds(xField, y, 250, 40);
        y += yIncrement;

        jLabel7.setBounds(xLabel, y, 250, 40);
        jTextField7.setBounds(xField, y, 250, 40);
        y += yIncrement;

        jLabel3.setBounds(xLabel, y, 250, 40);
        jSpinner2.setBounds(xField, y, 250, 40);
        y += yIncrement;

        jLabel8.setBounds(xLabel, y, 250, 40);
        jSpinner4.setBounds(xField, y, 250, 40);
        y += yIncrement;

        y = 150;

        jLabel9.setBounds(600, y, 250, 40);
        jComboBox1.setBounds(850, y, 250, 40);
        y += yIncrement;

        jLabel10.setBounds(600, y, 250, 40);
        jSpinner3.setBounds(850, y, 250, 40);
        y += yIncrement;

        jLabel12.setBounds(600, y, 250, 40);
        jTextField4.setBounds(850, y, 250, 40);
        y += yIncrement;

        jLabel13.setBounds(600, y, 250, 40);
        jTextField6.setBounds(850, y, 250, 40);
        y += yIncrement;

        jLabel11.setBounds(600, y, 250, 40);
        jTextField3.setBounds(850, y, 250, 40);

        jButton1.setBounds(100, 600, 150, 50);
        jButton2.setBounds(300, 600, 150, 50);
        jButton3.setBounds(500, 600, 150, 50);
        jButton4.setBounds(700, 600, 150, 50);
        jButton5.setBounds(900, 600, 150, 50);

        imageLabel.setBounds(1200, 150, 300, 200);

        add(jLabel2);
        add(jTextField1);
        add(jLabel4);
        add(jScrollPane1);
        add(jLabel5);
        add(cityComboBox);
        add(jLabel6);
        add(stateComboBox);
        add(jLabel7);
        add(jTextField7);
        add(jLabel3);
        add(jSpinner2);
        add(jLabel8);
        add(jSpinner4);
        add(jLabel9);
        add(jComboBox1);
        add(jLabel10);
        add(jSpinner3);
        add(jLabel12);
        add(jTextField4);
        add(jLabel13);
        add(jTextField6);
        add(jLabel11);
        add(jTextField3);

        add(jButton1);
        add(jButton2);
        add(jButton3);
        add(jButton4);
        add(jButton5);
        add(imageLabel);

        // Populate state combo box
        populateStates();

        // Add action listeners
        stateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCityComboBox((String) stateComboBox.getSelectedItem());
            }
        });

        jButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedImageFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage img = ImageIO.read(selectedImageFile);
                        ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH));
                        imageLabel.setIcon(imageIcon);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error loading image. " + ex.getMessage());
                    }
                }
            }
        });

        
            
            
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Extracting data from UI components
                String propertyName = jTextField1.getText().trim();
                String address = jTextArea1.getText().trim();
                String city = (String) cityComboBox.getSelectedItem();
                String state = (String) stateComboBox.getSelectedItem();
                String pincode = jTextField7.getText().trim();
                String propertyType = (String) jComboBox1.getSelectedItem();
                Integer numUnits = (Integer) jSpinner2.getValue();
                Double totalSqFootage = (Double) jSpinner3.getValue();
                Integer yearBuilt = (Integer) jSpinner4.getValue();
                String purchaseDate = jTextField4.getText().trim();
                String purchasePriceText = jTextField6.getText().trim();
                String currentMarketValueText = jTextField3.getText().trim();

                // Validation checks
                if (propertyName.isEmpty() || address.isEmpty() || city == null || state == null ||
                    pincode.isEmpty() || propertyType == null || purchaseDate.isEmpty() ||
                    purchasePriceText.isEmpty() || currentMarketValueText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!pincode.matches("\\d{6}")) {
                    JOptionPane.showMessageDialog(null, "Pincode must be a 6-digit number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Double purchasePrice;
                Double currentMarketValue;

                try {
                    purchasePrice = Double.parseDouble(purchasePriceText);
                    currentMarketValue = Double.parseDouble(currentMarketValueText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Purchase Price and Current Market Value must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (purchasePrice <= 0 || currentMarketValue <= 0) {
                    JOptionPane.showMessageDialog(null, "Purchase Price and Current Market Value must be positive numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedImageFile == null) {
                    JOptionPane.showMessageDialog(null, "Please upload an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if property with the same name and address already exists
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM property WHERE PropertyName = ? AND Address = ?")) {
                    checkStmt.setString(1, propertyName);
                    checkStmt.setString(2, address);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(null, "Property with this name and address already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Save image if selected
                    File destinationFile = new File(IMAGE_FOLDER_PATH + propertyName + ".jpg");
                    ImageIO.write(ImageIO.read(selectedImageFile), "jpg", destinationFile);

                    // Insert data into database
                    String insertQuery = "INSERT INTO property (PropertyName, Address, City, State, PINCode, PropertyType, NumUnits, TotalSqFootage, YearBuilt, PurchaseDate, PurchasePrice, CurrentMarketValue) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                        pstmt.setString(1, propertyName);
                        pstmt.setString(2, address);
                        pstmt.setString(3, city);
                        pstmt.setString(4, state);
                        pstmt.setString(5, pincode);
                        pstmt.setString(6, propertyType);
                        pstmt.setInt(7, numUnits);
                        pstmt.setDouble(8, totalSqFootage);
                        pstmt.setInt(9, yearBuilt);
                        pstmt.setString(10, purchaseDate);
                        pstmt.setDouble(11, purchasePrice);
                        pstmt.setDouble(12, currentMarketValue);
                        pstmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Property added successfully!");
                    }
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding property. Please try again. " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        

        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Delete button action logic
            }
        });

        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update button action logic
            }
        });

        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Back button action logic
            	 SellerScreen sellerScreen = new SellerScreen();
                 sellerScreen.setVisible(true);

                 // Dispose of the current frame containing the EditProperty panel
                 Window window = SwingUtilities.getWindowAncestor(EditProperty.this);
                 if (window != null) {
                     window.dispose();
                 }
            }
        });
    }

    private void populateStates() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT DISTINCT State FROM property");
             ResultSet rs = pstmt.executeQuery()) {
            stateComboBox.addItem("Select State");
            while (rs.next()) {
                stateComboBox.addItem(rs.getString("State"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading states. " + ex.getMessage());
        }
    }

    private void updateCityComboBox(String state) {
        cityComboBox.removeAllItems();
        cityComboBox.addItem("Select City");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT DISTINCT City FROM property WHERE State = ?")) {
            pstmt.setString(1, state);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cityComboBox.addItem(rs.getString("City"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading cities. " + ex.getMessage());
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
            frame.setContentPane(new EditProperty());
            frame.setVisible(true);
        });
    }
}

package a1;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

public class LegalDocumentation extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";
    private static final String IMAGE_PATH = "E:\\Eclipse p\\Assignment\\images\\";

    private JComboBox<String> propertyComboBox;
    private JButton generatePDFButton;
    private JButton backButton; // New back button
    private JLabel propertyIDLabel, propertyNameLabel, addressLabel, cityLabel, stateLabel, pinCodeLabel;
    private JLabel propertyTypeLabel, numUnitsLabel, sqFootageLabel, yearBuiltLabel, purchaseDateLabel;
    private JLabel purchasePriceLabel, marketValueLabel, imageLabel;

    private JTextField propertyIDField, propertyNameField, addressField, cityField, stateField;
    private JTextField pinCodeField, propertyTypeField, numUnitsField, sqFootageField, yearBuiltField;
    private JTextField purchaseDateField, purchasePriceField, marketValueField;

    private JLabel imageDisplay;

    public LegalDocumentation() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Legal Documentation");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanelSetup();
        formSetup();
        eventListenersSetup();
    }

    private void mainPanelSetup() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Legal Document");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

     // Add footer panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(70, 130, 180));

        // Add back button to the left
        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(220, 20, 60));
        footerPanel.add(backButton, BorderLayout.WEST);

        // Add generate PDF button to the right
        generatePDFButton = new JButton("Generate PDF");
        generatePDFButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        generatePDFButton.setForeground(Color.WHITE);
        generatePDFButton.setBackground(new Color(60, 179, 113));
        footerPanel.add(generatePDFButton, BorderLayout.EAST);

        // Add some spacing between buttons
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(footerPanel, BorderLayout.SOUTH);


        setContentPane(mainPanel);
    }

    private void formSetup() {
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        String[] labels = {"Property ID:", "Property Name:", "Address:", "City:", "State:", "PIN Code:",
                           "Property Type:", "Number of Units:", "Total Sq Footage:", "Year Built:",
                           "Purchase Date:", "Purchase Price:", "Current Market Value:"};

        JLabel[] labelArray = new JLabel[labels.length];
        JTextField[] fieldArray = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            labelArray[i] = new JLabel(labels[i]);
            labelArray[i].setFont(new Font("Segoe UI", Font.BOLD, 16));
            gbc.gridy = i;
            formPanel.add(labelArray[i], gbc);

            fieldArray[i] = new JTextField();
            fieldArray[i].setEditable(false);
            fieldArray[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
            gbc.gridx = 1;
            formPanel.add(fieldArray[i], gbc);
            gbc.gridx = 0;
        }

        // Assign fields to instance variables
        propertyIDField = fieldArray[0];
        propertyNameField = fieldArray[1];
        addressField = fieldArray[2];
        cityField = fieldArray[3];
        stateField = fieldArray[4];
        pinCodeField = fieldArray[5];
        propertyTypeField = fieldArray[6];
        numUnitsField = fieldArray[7];
        sqFootageField = fieldArray[8];
        yearBuiltField = fieldArray[9];
        purchaseDateField = fieldArray[10];
        purchasePriceField = fieldArray[11];
        marketValueField = fieldArray[12];

        // Image display
        imageDisplay = new JLabel();
        imageDisplay.setPreferredSize(new Dimension(300, 200));
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        formPanel.add(imageDisplay, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void eventListenersSetup() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        propertyComboBox = new JComboBox<>();
        propertyComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.add(propertyComboBox, BorderLayout.NORTH);

        populatePropertyComboBox();

        propertyComboBox.addActionListener(e -> {
            String selectedProperty = (String) propertyComboBox.getSelectedItem();
            displayPropertyDetails(selectedProperty);
        });

        getContentPane().add(controlPanel, BorderLayout.WEST);

        generatePDFButton.addActionListener(e -> generatePDF());
        
        // Back button event listener
        backButton.addActionListener(e -> {
            // Define the action for the back button, e.g., close the current window
        	new SellerScreen().setVisible(true);
            dispose();
        });
    }

    private void displayPropertyDetails(String selectedProperty) {
        if (selectedProperty == null || selectedProperty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No property selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Replace spaces with underscores in the property name for image filename
        String imageFileName = selectedProperty.replace(" ", "_") + ".jpg";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM property WHERE PropertyName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, selectedProperty);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        propertyIDField.setText(resultSet.getString("PropertyID"));
                        propertyNameField.setText(resultSet.getString("PropertyName"));
                        addressField.setText(resultSet.getString("Address"));
                        cityField.setText(resultSet.getString("City"));
                        stateField.setText(resultSet.getString("State"));
                        pinCodeField.setText(resultSet.getString("PINCode"));
                        propertyTypeField.setText(resultSet.getString("PropertyType"));
                        numUnitsField.setText(String.valueOf(resultSet.getInt("NumUnits")));
                        sqFootageField.setText(String.valueOf(resultSet.getDouble("TotalSqFootage")));
                        yearBuiltField.setText(String.valueOf(resultSet.getInt("YearBuilt")));
                        purchaseDateField.setText(resultSet.getDate("PurchaseDate").toString());
                        purchasePriceField.setText(String.valueOf(resultSet.getDouble("PurchasePrice")));
                        marketValueField.setText(String.valueOf(resultSet.getDouble("CurrentMarketValue")));

                        // Load and display image
                        File imgFile = new File(IMAGE_PATH + imageFileName);
                        if (imgFile.exists()) {
                            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
                            imageDisplay.setIcon(icon);
                        } else {
                            imageDisplay.setIcon(null);
                            JOptionPane.showMessageDialog(this, "Image file not found: " + imgFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No data found for the selected property.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving property details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populatePropertyComboBox() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT PropertyName FROM property";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    propertyComboBox.addItem(resultSet.getString("PropertyName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error populating property combo box: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePDF() {
        String selectedProperty = (String) propertyComboBox.getSelectedItem();
        if (selectedProperty == null || selectedProperty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No property selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Replace spaces with underscores in the property name for image filename
        String imageFileName = selectedProperty.replace(" ", "_") + ".jpg";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM property WHERE PropertyName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, selectedProperty);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(selectedProperty + ".pdf"));
                        document.open();

                        // Add property details table
                        PdfPTable table = new PdfPTable(2);

                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        for (int i = 1; i <= columnCount; i++) {
                            table.addCell(new PdfPCell(new Phrase(metaData.getColumnName(i))));
                            table.addCell(new PdfPCell(new Phrase(resultSet.getString(i))));
                        }

                        document.add(table);

                        // Add property image
                        File imgFile = new File(IMAGE_PATH + imageFileName);
                        if (imgFile.exists()) {
                            Image img = Image.getInstance(imgFile.getAbsolutePath());
                            img.scaleToFit(300, 200);
                            document.add(img);
                        } else {
                            JOptionPane.showMessageDialog(this, "Image file not found: " + imgFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        document.close();
                        JOptionPane.showMessageDialog(this, "PDF generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No data found for the selected property.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LegalDocumentation legalDocumentation = new LegalDocumentation();
            legalDocumentation.setVisible(true);
        });
    }
}

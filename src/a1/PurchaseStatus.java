package a1;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class PurchaseStatus extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";

    private JTextField paymentIDField, buyerNameField, buyerIDField, sellerNameField, sellerIDField, propertyIDField, amountField;
    private JButton generatePDFButton, backButton;

    public PurchaseStatus() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Latest Payment Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanelSetup();
        formSetup();
        displayLatestPaymentDetails();
        eventListenersSetup();
    }

    private void mainPanelSetup() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(110, 101, 101));
        JLabel titleLabel = new JLabel("Latest Purchase Status");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Add footer panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(110, 101, 101));

        // Add back button to the left
        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(250, 95, 95));
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

        String[] labels = {"Payment ID:", "Buyer Name:", "Buyer ID:", "Seller Name:", "Seller ID:", "Property ID:", "Amount:"};

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
        paymentIDField = fieldArray[0];
        buyerNameField = fieldArray[1];
        buyerIDField = fieldArray[2];
        sellerNameField = fieldArray[3];
        sellerIDField = fieldArray[4];
        propertyIDField = fieldArray[5];
        amountField = fieldArray[6];

        JScrollPane scrollPane = new JScrollPane(formPanel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void displayLatestPaymentDetails() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM payment ORDER BY PaymentID DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    paymentIDField.setText(resultSet.getString("PaymentID"));
                    buyerNameField.setText(resultSet.getString("BuyerName"));
                    buyerIDField.setText(resultSet.getString("BuyerID"));
                    sellerNameField.setText(resultSet.getString("SellerName"));
                    sellerIDField.setText(resultSet.getString("SellerID"));
                    propertyIDField.setText(resultSet.getString("PropertyID"));
                    amountField.setText(resultSet.getString("Amount"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving payment details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eventListenersSetup() {
        generatePDFButton.addActionListener(e -> generatePDF());

        // Back button event listener
        backButton.addActionListener(e -> {
            // Define the action for the back button, e.g., close the current window
        	 dispose();
            
           
        });
    }

    private void generatePDF() {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Latest_Purchase_Status.pdf"));
            document.open();

            // Add header
            addHeader(document);

            // Add title
            Paragraph title = new Paragraph("Purchase Status Invoice", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Font.BOLD, BaseColor.WHITE));
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            PdfPCell titleCell = new PdfPCell(title);
            titleCell.setBackgroundColor(BaseColor.DARK_GRAY);
            titleCell.setPadding(10);
            titleCell.setBorder(Rectangle.NO_BORDER);
            
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100);
            titleTable.addCell(titleCell);
            document.add(titleTable);
            document.add(Chunk.NEWLINE);

            // Retrieve property name for image
            String propertyName = getPropertyName(propertyIDField.getText());
            Image propertyImage = Image.getInstance("E:/Eclipse p/Assignment/images/" + propertyName + ".jpg");
            propertyImage.scaleToFit(150, 150); // Adjust size as needed
            propertyImage.setAlignment(Image.ALIGN_CENTER);
            document.add(propertyImage);
            document.add(Chunk.NEWLINE);

            // Add payment details table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Adding table cells
            table.addCell(createCell("Payment ID", BaseColor.LIGHT_GRAY));
            table.addCell(createCell(paymentIDField.getText(), BaseColor.WHITE));
            table.addCell(createCell("Buyer Name", BaseColor.LIGHT_GRAY));
            table.addCell(createCell(buyerNameField.getText(), BaseColor.WHITE));
            table.addCell(createCell("Buyer ID", BaseColor.LIGHT_GRAY));
            table.addCell(createCell(buyerIDField.getText(), BaseColor.WHITE));
            table.addCell(createCell("Seller Name", BaseColor.LIGHT_GRAY));
            table.addCell(createCell(sellerNameField.getText(), BaseColor.WHITE));
            table.addCell(createCell("Seller ID", BaseColor.LIGHT_GRAY));
            table.addCell(createCell(sellerIDField.getText(), BaseColor.WHITE));
            table.addCell(createCell("Property ID", BaseColor.LIGHT_GRAY));
            table.addCell(createCell(propertyIDField.getText(), BaseColor.WHITE));
            table.addCell(createCell("Amount", BaseColor.LIGHT_GRAY));
            table.addCell(createCell(amountField.getText(), BaseColor.WHITE));

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Add signature image
            Image signatureImage = Image.getInstance("E:/Eclipse p/Assignment/images/signature.jpg");
            signatureImage.scaleToFit(2500, 150); // Adjust size as needed
            signatureImage.setAlignment(Image.ALIGN_CENTER);
            signatureImage.setSpacingBefore(20f);
            document.add(signatureImage);

            // Add footer
            addFooter(document);

            // Close document
            document.close();
            JOptionPane.showMessageDialog(this, "PDF generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private PdfPCell createCell(String text, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        cell.setPadding(8);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    private void addHeader(Document document) throws DocumentException, IOException {
        // Add header
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(10f);

        PdfPCell headerCell = new PdfPCell(new Phrase("Real Estate Company", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("666, Upper Indiranagar, Bibwewadi, Pune- 411 037", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(headerCell);

        document.add(headerTable);
    }

    private void addFooter(Document document) throws DocumentException {
        // Add footer
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        footerTable.setSpacingBefore(10f);

        PdfPCell footerCell = new PdfPCell(new Phrase("Thank you for your business!", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerCell.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(footerCell);

        footerCell = new PdfPCell(new Phrase("For any queries, please contact us at info@vedantdeore.com", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerCell.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(footerCell);

        document.add(footerTable);
    }


    private String getPropertyName(String propertyID) {
        String propertyName = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT PropertyName FROM property WHERE PropertyID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, propertyID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        propertyName = resultSet.getString("PropertyName").replace(" ", "_").toLowerCase();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propertyName;
    }

    private PdfPCell createCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        cell.setPadding(8);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setBackgroundColor(BaseColor.WHITE);
        return cell;
    }

//    private void addHeader(Document document) throws DocumentException, IOException {
//        // Add header
//        PdfPTable headerTable = new PdfPTable(1);
//        headerTable.setWidthPercentage(100);
//        headerTable.setSpacingAfter(10f);
//
//        PdfPCell headerCell = new PdfPCell(new Phrase("Real Estate Company", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
//        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        headerCell.setBorder(Rectangle.NO_BORDER);
//        headerTable.addCell(headerCell);
//
//        headerCell = new PdfPCell(new Phrase("Address Line 1, Address Line 2, City, State, ZIP", FontFactory.getFont(FontFactory.HELVETICA, 12)));
//        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        headerCell.setBorder(Rectangle.NO_BORDER);
//        headerTable.addCell(headerCell);
//
//        document.add(headerTable);
//    }
//
//    private void addFooter(Document document) throws DocumentException {
//        // Add footer
//        PdfPTable footerTable = new PdfPTable(1);
//        footerTable.setWidthPercentage(100);
//        footerTable.setSpacingBefore(10f);
//
//        PdfPCell footerCell = new PdfPCell(new Phrase("Thank you for your business!", FontFactory.getFont(FontFactory.TIMES_ITALIC, 12)));
//        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        footerCell.setBorder(Rectangle.NO_BORDER);
//        footerTable.addCell(footerCell);
//
//        footerCell = new PdfPCell(new Phrase("For any queries, please contact us at info@realestatecompany.com", FontFactory.getFont(FontFactory.HELVETICA, 12)));
//        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        footerCell.setBorder(Rectangle.NO_BORDER);
//        footerTable.addCell(footerCell);
//
//        document.add(footerTable);
//    }

//    private String getPropertyName(String propertyID) {
//        String propertyName = "";
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            String query = "SELECT PropertyName FROM property WHERE PropertyID = ?";
//            try (PreparedStatement statement = connection.prepareStatement(query)) {
//                statement.setString(1, propertyID);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    if (resultSet.next()) {
//                        propertyName = resultSet.getString("PropertyName").replace(" ", "_").toLowerCase();
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return propertyName;
//    }
//
//    private PdfPCell createCell(String text) {
//        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 12)));
//        cell.setPadding(8);
//        cell.setBorderColor(BaseColor.GRAY);
//        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        return cell;
//    }
//
//
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PurchaseStatus purchaseStatus = new PurchaseStatus();
            purchaseStatus.setVisible(true);
        });
    }
}

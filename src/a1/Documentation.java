//import com.itextpdf.text.Document;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Documentation extends JFrame {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "9022296054@abc";
//
//    private List<Payment> payments;
//    private List<Property> properties;
//
//    public Documentation() {
//        setTitle("Real Estate App");
//        setSize(800, 600);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        JButton fetchDataButton = new JButton("Fetch Data");
//        JButton generatePdfButton = new JButton("Generate PDF");
//
//        fetchDataButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                fetchDataFromDatabase();
//            }
//        });
//
//        generatePdfButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                generatePDF();
//            }
//        });
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(fetchDataButton);
//        buttonPanel.add(generatePdfButton);
//
//        add(buttonPanel, BorderLayout.SOUTH);
//
//        // Other GUI components for displaying data
//
//        setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new Documentation());
//    }
//
//    private void fetchDataFromDatabase() {
//        payments = new ArrayList<>();
//        properties = new ArrayList<>();
//
//        try {
//            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//
//            // Fetch data from the Payment table
//            String paymentQuery = "SELECT * FROM payment";
//            PreparedStatement paymentStatement = connection.prepareStatement(paymentQuery);
//            ResultSet paymentResult = paymentStatement.executeQuery();
//
//            while (paymentResult.next()) {
//                Payment payment = new Payment(
//                        paymentResult.getInt("PaymentID"),
//                        paymentResult.getString("BuyerName"),
//                        paymentResult.getInt("BuyerID"),
//                        paymentResult.getString("SellerName"),
//                        paymentResult.getInt("SellerID"),
//                        paymentResult.getInt("PropertyID"),
//                        paymentResult.getDouble("Amount")
//                );
//                payments.add(payment);
//            }
//
//            paymentResult.close();
//            paymentStatement.close();
//
//            // Fetch data from the Property table (based on PropertyID)
//            String propertyQuery = "SELECT * FROM property WHERE PropertyID = ?";
//            PreparedStatement propertyStatement = connection.prepareStatement(propertyQuery);
//
//            for (Payment payment : payments) {
//                propertyStatement.setInt(1, payment.getPropertyID());
//                ResultSet propertyResult = propertyStatement.executeQuery();
//
//                if (propertyResult.next()) {
//                    Property property = new Property(
//                        // Populate property attributes
//                    );
//                    properties.add(property);
//                }
//
//                propertyResult.close();
//            }
//
//            propertyStatement.close();
//            connection.close();
//
//            // Display data in your GUI components
//            // Example: Update JLabels or JTables with data
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void generatePDF() {
//        // PDF generation logic using iText
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));
//            document.open();
//
//            for (Payment payment : payments) {
//                // Add payment data to the PDF
//                document.add(new Paragraph("PaymentID: " + payment.getPaymentID()));
//                document.add(new Paragraph("BuyerName: " + payment.getBuyerName()));
//                // Add other payment details
//
//                // Retrieve the associated property
//                Property associatedProperty = findProperty(payment.getPropertyID());
//
//                if (associatedProperty != null) {
//                    // Add property data to the PDF
//                    document.add(new Paragraph("PropertyID: " + associatedProperty.getPropertyID()));
//                    document.add(new Paragraph("PropertyName: " + associatedProperty.getPropertyName()));
//                    // Add other property details
//                }
//
//                document.add(new Paragraph("\n")); // Separate payments
//            }
//
//            document.close();
//            JOptionPane.showMessageDialog(this, "PDF generated successfully.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "PDF generation failed.");
//        }
//    }
//
//    private Property findProperty(int propertyID) {
//        for (Property property : properties) {
//            if (property.getPropertyID() == propertyID) {
//                return property;
//            }
//        }
//        return null;
//    }
//}
//
//class Payment {
//    // Payment attributes
//}
//
//class Property {
//    // Property attributes
//}

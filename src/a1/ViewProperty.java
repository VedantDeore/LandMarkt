package a1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewProperty extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> propertyTypeComboBox;
    private JButton searchButton;
    private JButton purchaseButton;
    private JTable propertyTable;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";

    public ViewProperty() {
        setTitle("Property Search");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());

        // Create a combo box for property types
        String[] propertyTypes = {"Select Property", "Apartment", "Commercial", "Property 3"};
        propertyTypeComboBox = new JComboBox<>(propertyTypes);

        // Create a button for selecting the property
        searchButton = new JButton("Search");

        // Create a "Purchase" button
//        purchaseButton = new JButton("Purchase");
//        purchaseButton.setEnabled(false);

        // Add the combo box and search button to the top panel
        JPanel topPanel = new JPanel();
        topPanel.add(propertyTypeComboBox);
        topPanel.add(searchButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create a table to list properties
        String[] columnNames = {
            "Property ID", "Property Name", "Address", "City", "State", "PIN Code",
            "Property Type", "Num Units"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        propertyTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(propertyTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(purchaseButton);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProperty = (String) propertyTypeComboBox.getSelectedItem();

                if (!"Select Property".equals(selectedProperty)) {
                    // Add code to retrieve and display the selected property details
                    // You can add a new row to the table or update the property details text fields.
                    updatePropertyDetails(selectedProperty);
                    purchaseButton.setEnabled(true);
                }
            }
        });

        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add code to handle the purchase of the selected property
                // This could involve opening a purchase dialog or performing other actions.
                // For now, we'll just display a message.
                JOptionPane.showMessageDialog(ViewProperty.this, "Property purchased!");
                dispose(); // Close the BuyerSearch window

                // Open the PaymentPage window
               // new PaymentPage();
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private void updatePropertyDetails(String selectedProperty) {
        // Fetch property details from the database and update the table
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Property WHERE PropertyType = '" + selectedProperty + "'";
            ResultSet resultSet = statement.executeQuery(query);

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                int propertyId = resultSet.getInt("PropertyID");
                String propertyName = resultSet.getString("PropertyName");
                String address = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String state = resultSet.getString("State");
                String pincode = resultSet.getString("PINCode");
                String propertyType = resultSet.getString("PropertyType");
                int numUnits = resultSet.getInt("NumUnits");

                tableModel.addRow(new Object[] {propertyId, propertyName, address, city, state, pincode, propertyType, numUnits});
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewProperty();
        });
    }
}

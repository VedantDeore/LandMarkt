package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompareProperties extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private JPanel navbarPanel;
    private JButton compareButton;
    private JButton backButton;
    private JTextField searchTextField;
    private JDialog compareDialog;
    private JPanel propertyCardsPanel;
    private JCheckBox propertyCheckbox;
    private List<Integer> selectedPropertyIds = new ArrayList<>();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";

    public CompareProperties() {
        initComponents();
        loadPropertyCards("");  // Load all property cards on initialization
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Navbar
        navbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbarPanel.setBackground(new Color(0, 0, 0, 150));
        navbarPanel.setPreferredSize(new Dimension(getWidth(), 100));

        searchTextField = new JTextField(20);

        compareButton = new JButton("Compare");
        compareButton.setBackground(new Color(204, 153, 255));
        compareButton.addActionListener(e -> compareProperties());

        backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.addActionListener(e -> goBack());

        navbarPanel.add(searchTextField);
        navbarPanel.add(compareButton);
        navbarPanel.add(backButton);

        mainPanel.add(navbarPanel, BorderLayout.NORTH);

        // Property Cards Panel
        propertyCardsPanel = new JPanel();
        propertyCardsPanel.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(propertyCardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Compare Dialog
        compareDialog = new JDialog(this, "Compare Properties", true);
        compareDialog.setLayout(new GridLayout(1, 2));
        compareDialog.setSize(800, 600);
        compareDialog.setLocationRelativeTo(this);  // Center the dialog

        // Create panels for property comparison
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Add light borders and padding to panels
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Add padding to the panels
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        compareDialog.add(leftPanel);
        compareDialog.add(rightPanel);

        // Load property cards
        loadPropertyCards("");
    }

    private void loadPropertyCards(String query) {
        propertyCardsPanel.removeAll();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM property " + query;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                JPanel propertyCard = createPropertyCard(result);
                propertyCardsPanel.add(propertyCard);
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
        propertyCardsPanel.revalidate();
        propertyCardsPanel.repaint();
    }

    private JPanel createPropertyCard(ResultSet result) throws SQLException {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cardPanel.setPreferredSize(new Dimension(400, 200)); // Set preferred size for the card

        // Panel for image and details
        JPanel imageDetailsPanel = new JPanel();
        imageDetailsPanel.setLayout(new BorderLayout());

        // Image
        JLabel imageLabel = new JLabel(new ImageIcon("images/" + result.getString("PropertyName").toLowerCase().replace(" ", "_") + ".jpg"));
        imageLabel.setPreferredSize(new Dimension(300, 150)); // Set image size
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageDetailsPanel.add(imageLabel, BorderLayout.WEST);

        // Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Badge
        JLabel badgeLabel = new JLabel(result.getString("PropertyType"));
        badgeLabel.setOpaque(true);
        badgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        badgeLabel.setPreferredSize(new Dimension(100, 20)); // Adjust size as needed

        String propertyType = result.getString("PropertyType");
        if ("Apartment".equals(propertyType)) {
            badgeLabel.setBackground(Color.BLUE);
            badgeLabel.setForeground(Color.WHITE);
        } else if ("House".equals(propertyType)) {
            badgeLabel.setBackground(Color.GREEN);
            badgeLabel.setForeground(Color.WHITE);
        } else if ("Commercial".equals(propertyType)) {
            badgeLabel.setBackground(Color.RED);
            badgeLabel.setForeground(Color.WHITE);
        } else if ("Land".equals(propertyType)) {
            badgeLabel.setBackground(Color.YELLOW);
            badgeLabel.setForeground(Color.BLACK);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(badgeLabel, gbc);

        // Property details
        JLabel nameLabel = new JLabel("Name: " + result.getString("PropertyName"));
        gbc.gridy++;
        detailsPanel.add(nameLabel, gbc);

        JLabel costLabel = new JLabel("Cost: " + result.getString("PurchasePrice"));
        gbc.gridy++;
        detailsPanel.add(costLabel, gbc);

        JLabel valueLabel = new JLabel("Value: " + result.getString("CurrentMarketValue"));
        gbc.gridy++;
        detailsPanel.add(valueLabel, gbc);

        JLabel yearBuiltLabel = new JLabel("Year Built: " + result.getString("YearBuilt"));
        gbc.gridy++;
        detailsPanel.add(yearBuiltLabel, gbc);

        // Checkbox for selection
        JCheckBox selectCheckBox = new JCheckBox("Select");
        selectCheckBox.setActionCommand(String.valueOf(result.getInt("PropertyID")));
        selectCheckBox.addActionListener(e -> {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            int propertyId = Integer.parseInt(checkBox.getActionCommand());
            if (checkBox.isSelected()) {
                selectedPropertyIds.add(propertyId);
            } else {
                selectedPropertyIds.remove(Integer.valueOf(propertyId));
            }
        });

        gbc.gridy++;
        detailsPanel.add(selectCheckBox, gbc);

        imageDetailsPanel.add(detailsPanel, BorderLayout.CENTER);
        cardPanel.add(imageDetailsPanel, BorderLayout.CENTER);

        return cardPanel;
    }

    private void compareProperties() {
        if (selectedPropertyIds.size() != 2) {
            JOptionPane.showMessageDialog(this, "Please select exactly 2 properties for comparison.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int propertyId1 = selectedPropertyIds.get(0);
        int propertyId2 = selectedPropertyIds.get(1);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM property WHERE PropertyID = ? OR PropertyID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, propertyId1);
            statement.setInt(2, propertyId2);
            ResultSet result = statement.executeQuery();

            // Clear previous content
            compareDialog.getContentPane().removeAll();

            JPanel comparisonPanel = new JPanel(new GridLayout(1, 2));
            
            while (result.next()) {
                JPanel propertyPanel = new JPanel(new BorderLayout());
                propertyPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

                // Panel for image and details
                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;

                // Image
                JLabel imageLabel = new JLabel(new ImageIcon("images/" + result.getString("PropertyName").toLowerCase().replace(" ", "_") + ".jpg"));
                imageLabel.setPreferredSize(new Dimension(300, 150)); // Set image size
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                propertyPanel.add(imageLabel, BorderLayout.NORTH);

                // Property details
                addPropertyDetail(detailsPanel, gbc, "Property ID", result.getString("PropertyID"));
                addPropertyDetail(detailsPanel, gbc, "Name", result.getString("PropertyName"));
                addPropertyDetail(detailsPanel, gbc, "Address", result.getString("Address"));
                addPropertyDetail(detailsPanel, gbc, "City", result.getString("City"));
                addPropertyDetail(detailsPanel, gbc, "State", result.getString("State"));
                addPropertyDetail(detailsPanel, gbc, "PIN Code", result.getString("PINCode"));
                addPropertyDetail(detailsPanel, gbc, "Property Type", result.getString("PropertyType"));
                addPropertyDetail(detailsPanel, gbc, "Number of Units", result.getString("NumUnits"));
                addPropertyDetail(detailsPanel, gbc, "Total Sq Footage", result.getString("TotalSqFootage"));
                addPropertyDetail(detailsPanel, gbc, "Year Built", result.getString("YearBuilt"));
                addPropertyDetail(detailsPanel, gbc, "Purchase Date", result.getString("PurchaseDate"));
                addPropertyDetail(detailsPanel, gbc, "Purchase Price", result.getString("PurchasePrice"));
                addPropertyDetail(detailsPanel, gbc, "Current Market Value", result.getString("CurrentMarketValue"));
                addPropertyDetail(detailsPanel, gbc, "Seller ID", result.getString("SellerID"));

                propertyPanel.add(detailsPanel, BorderLayout.CENTER);
                comparisonPanel.add(propertyPanel);
            }

            // Add padding to the comparison panel
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
            contentPanel.add(comparisonPanel, BorderLayout.CENTER);

            compareDialog.setContentPane(contentPanel);
            compareDialog.revalidate(); // Revalidate the dialog to apply changes
            compareDialog.repaint(); // Repaint the dialog to reflect new content
            compareDialog.setVisible(true);

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


    private void addPropertyDetail(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridy++;
        JLabel propertyLabel = new JLabel(label + ":");
        JLabel propertyValue = new JLabel(value);
        panel.add(propertyLabel, gbc);
        gbc.gridx = 1;
        panel.add(propertyValue, gbc);
        gbc.gridx = 0;
    }

    private void goBack() {
        new BuyerScreen().setVisible(true);
        this.dispose();
        // You might want to create and show the previous window or main application window here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	
        	  
            new CompareProperties().setVisible(true);
        });
    }
}

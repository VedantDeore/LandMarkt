package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuyerSearch extends JFrame {
    private JPanel mainPanel;
    private JPanel navbarPanel;
    private JButton searchButton;
    private JButton backButton;
    private JButton filterButton;
    private JButton sortButton;
    private JTextField searchTextField;
    private JDialog filterDialog;
    private JDialog sortDialog;
    private JPanel propertyCardsPanel;
    private JCheckBox residentialCheckBox;
    private JCheckBox industrialCheckBox;
    private JCheckBox commercialCheckBox;
    private JCheckBox landCheckBox;
    private JCheckBox sortByCostCheckBox;
    private JCheckBox sortByNameCheckBox;
    private JCheckBox sortByPriceCheckBox;
    private JCheckBox sortByYearBuiltCheckBox;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";
	private static final boolean True = false;

    public BuyerSearch() {
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

        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(255, 204, 0));
        searchButton.addActionListener(e -> searchProperties());

        backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.addActionListener(e -> goBack());

        filterButton = new JButton("Filter");
        filterButton.setBackground(new Color(204, 153, 255));
        filterButton.addActionListener(e -> showFilterDialog());

        sortButton = new JButton("Sort");
        sortButton.setBackground(new Color(204, 153, 255));
        sortButton.addActionListener(e -> showSortDialog());

        searchTextField = new JTextField(20);

        navbarPanel.add(searchTextField);
        navbarPanel.add(searchButton);
        navbarPanel.add(filterButton);
        navbarPanel.add(sortButton);
        navbarPanel.add(backButton);

        mainPanel.add(navbarPanel, BorderLayout.NORTH);

        // Property Cards Panel
        propertyCardsPanel = new JPanel();
        propertyCardsPanel.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(propertyCardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Filter Dialog
        filterDialog = new JDialog(this, "Filter", true);
        filterDialog.setLayout(new GridLayout(0, 1));
        filterDialog.setSize(300, 200);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(0, 1));
        residentialCheckBox = new JCheckBox("Apartment");
        industrialCheckBox = new JCheckBox("House");
        commercialCheckBox = new JCheckBox("Commercial");
        landCheckBox = new JCheckBox("Land");
        JButton filterCancelButton = new JButton("Cancel");
        JButton filterApplyButton = new JButton("Apply");

        filterPanel.add(residentialCheckBox);
        filterPanel.add(industrialCheckBox);
        filterPanel.add(commercialCheckBox);
        filterPanel.add(landCheckBox);
        filterPanel.add(filterCancelButton);
        filterPanel.add(filterApplyButton);

        filterDialog.add(filterPanel);
        filterCancelButton.addActionListener(e -> filterDialog.dispose());
        filterApplyButton.addActionListener(e -> {
            loadPropertyCards(getFilterQuery());
            filterDialog.dispose();
        });

        // Sort Dialog
        sortDialog = new JDialog(this, "Sort", true);
        sortDialog.setLayout(new GridLayout(0, 1));
        sortDialog.setSize(300, 200);

        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new GridLayout(0, 1));
        sortByCostCheckBox = new JCheckBox("Cost");
        sortByNameCheckBox = new JCheckBox("Name");
        sortByPriceCheckBox = new JCheckBox("Price");
        sortByYearBuiltCheckBox = new JCheckBox("Year Built");
        JButton sortCancelButton = new JButton("Cancel");
        JButton sortApplyButton = new JButton("Apply");

        sortPanel.add(sortByCostCheckBox);
        sortPanel.add(sortByNameCheckBox);
        sortPanel.add(sortByPriceCheckBox);
        sortPanel.add(sortByYearBuiltCheckBox);
        sortPanel.add(sortCancelButton);
        sortPanel.add(sortApplyButton);

        sortDialog.add(sortPanel);
        sortCancelButton.addActionListener(e -> sortDialog.dispose());
        sortApplyButton.addActionListener(e -> {
            loadPropertyCards(getSortQuery());
            sortDialog.dispose();
        });
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

    private String getFilterQuery() {
        StringBuilder query = new StringBuilder("WHERE 1=1 ");
        List<String> filters = new ArrayList<>();
        
        if (residentialCheckBox.isSelected()) {
            filters.add("PropertyType='Apartment'");
        }
        if (industrialCheckBox.isSelected()) {
            filters.add("PropertyType='House'");
        }
        if (commercialCheckBox.isSelected()) {
            filters.add("PropertyType='Commercial'");
        }
        if (landCheckBox.isSelected()) {
            filters.add("PropertyType='Land'");
        }
        
        // Combine filters with OR
        if (!filters.isEmpty()) {
            query.append("AND (");
            query.append(String.join(" OR ", filters));
            query.append(") ");
        }
        
        return query.toString();
    }

    private String getSortQuery() {
        if (sortByCostCheckBox.isSelected()) {
            return "ORDER BY PurchasePrice ASC ";
        }
        if (sortByNameCheckBox.isSelected()) {
            return "ORDER BY PropertyName ASC ";
        }
        if (sortByPriceCheckBox.isSelected()) {
            return "ORDER BY CurrentMarketValue ASC ";
        }
        if (sortByYearBuiltCheckBox.isSelected()) {
            return "ORDER BY YearBuilt ASC "; // Sorting by year built in ascending order
        }
        return "";
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

        // Check if the property is sold
        boolean isSold = isPropertySold(result.getInt("PropertyID"));

        // Button or label based on sold status
        if (isSold) {
            JLabel soldLabel = new JLabel("Sold");
            soldLabel.setForeground(Color.RED);
            gbc.gridy++;
            detailsPanel.add(soldLabel, gbc);
        } else {
            JButton viewDetailsButton = new JButton("View Details");
            viewDetailsButton.setActionCommand(String.valueOf(result.getInt("PropertyID")));
            viewDetailsButton.addActionListener(e -> {
                int propertyId = Integer.parseInt(((JButton) e.getSource()).getActionCommand());
                openPaymentPage(propertyId);
            });
            gbc.gridy++;
            detailsPanel.add(viewDetailsButton, gbc);
        }

        imageDetailsPanel.add(detailsPanel, BorderLayout.CENTER);
        cardPanel.add(imageDetailsPanel, BorderLayout.CENTER);

        return cardPanel;
    }


    private boolean isPropertySold(int propertyId) {
        boolean isSold = false;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT COUNT(*) FROM payment WHERE PropertyID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, propertyId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isSold = resultSet.getInt(1) > 0;
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
        return isSold;
    }

    private void searchProperties() {
        String searchText = searchTextField.getText();
        String query = "WHERE PropertyName LIKE '%" + searchText + "%'";
        loadPropertyCards(query);
    }

    private void goBack() {
        // Implement the action for the "Back" button
        // You might want to close this window and return to the previous screen
    	new BuyerScreen().setVisible(true);
        dispose();
    }

    private void showFilterDialog() {
        filterDialog.setVisible(true);
    }

    private void showSortDialog() {
        sortDialog.setVisible(true);
    }

    private void openPaymentPage(int propertyId) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM property WHERE PropertyID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, propertyId);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String propertyName = result.getString("PropertyName");
                String propertyImage = "images/" + propertyName.toLowerCase().replace(" ", "_") + ".jpg";
                String propertyDetails = "<html>Name: " + propertyName + "<br>" +
                                          "Cost: " + result.getString("PurchasePrice") + "<br>" +
                                          "Value: " + result.getString("CurrentMarketValue") + "<br>" +
                                          "Year Built: " + result.getString("YearBuilt") + "</html>";

                // Create and display the PaymentPage
                PaymentPage paymentPage = new PaymentPage(propertyId);
                paymentPage.setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BuyerSearch().setVisible(true));
    }
}

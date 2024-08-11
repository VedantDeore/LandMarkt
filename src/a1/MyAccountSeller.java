package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyAccountSeller extends JPanel {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9022296054@abc";

    public MyAccountSeller() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Create and style the title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Seller Account");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 64));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.setBackground(new Color(60, 63, 65));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setPreferredSize(new Dimension(0, 150));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Profile photo
        JLabel profilePhotoLabel = new JLabel();
        profilePhotoLabel.setPreferredSize(new Dimension(160, 160));
        profilePhotoLabel.setOpaque(true);
        profilePhotoLabel.setBackground(Color.GRAY);
        profilePhotoLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        profilePhotoLabel.setIcon(getCircularIcon(new ImageIcon("path/to/profile/photo.jpg"), 160, 160));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(profilePhotoLabel, gbc);

        // Labels and fields
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel idLabel = new JLabel("Select SellerID:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        contentPanel.add(idLabel, gbc);

        JTextField idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPanel.add(idField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        contentPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPanel.add(nameField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        contentPanel.add(numberLabel, gbc);

        JTextField numberField = new JTextField(20);
        numberField.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPanel.add(numberField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        contentPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPanel.add(emailField, gbc);

        // Update button and status label
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;

        JButton updateButton = new JButton("Update");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        updateButton.setBackground(new Color(50, 143, 138));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setPreferredSize(new Dimension(150, 40));
        contentPanel.add(updateButton, gbc);

        JLabel updateStatus = new JLabel(" ");
        updateStatus.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        updateStatus.setForeground(Color.RED);
        gbc.gridy = 5;
        contentPanel.add(updateStatus, gbc);

        add(contentPanel, BorderLayout.CENTER);

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the values from UI components
                int sellerID = 0; // Initialize to a default value
                try {
                    sellerID = Integer.parseInt(idField.getText());
                } catch (NumberFormatException ex) {
                    updateStatus.setText("Invalid SellerID"); // Show an error message
                    return;
                }
                String name = nameField.getText();
                String number = numberField.getText();
                String email = emailField.getText();

                // Query to update data in the sellertable
                String updateQuery = "UPDATE sellertable SET Name=?, Number=?, Email=? WHERE SellerID=?";

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, number);
                    pstmt.setString(3, email);
                    pstmt.setInt(4, sellerID);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        updateStatus.setText("Data updated successfully");
                        conn.commit();
                    } else {
                        updateStatus.setText("Failed to update data");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private Icon getCircularIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setClip(new Ellipse2D.Double(0, 0, width, height));
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();

        return new ImageIcon(bufferedImage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Seller Account");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setContentPane(new MyAccountSeller());

            // Enter full-screen mode
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(frame);
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fallback
            }

            frame.setVisible(true);
        });
    }
}

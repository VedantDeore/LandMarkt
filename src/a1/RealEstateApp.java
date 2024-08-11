package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RealEstateApp extends JFrame {

    public RealEstateApp() {
        setTitle("Real Estate Management App");
        setSize(1366, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Create a main panel with a background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon("images/BG.png").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);

        
        Color customColor = new Color(255, 255, 255);
        Color buyerfontColor = new Color(19, 18, 151);
        Color sellerfontColor = new Color(173, 11, 11);// Red (255, 0, 0)
        // Create the "Buyer" panel
        JPanel buyerPanel = new JPanel();
        buyerPanel.setLayout(null);
        buyerPanel.setBackground(new Color(255, 255, 255)); // Blue color for buyer
        buyerPanel.setBounds(260, 129, 460, 483);

        JLabel jLabel2 = new JLabel("Buyer");
        jLabel2.setBounds(10, 11, 438, 66);
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setFont(new Font("Eras Bold ITC", Font.PLAIN, 48));
        jLabel2.setForeground(buyerfontColor);
        buyerPanel.add(jLabel2);

        ImageIcon buyerImage = new ImageIcon("images/buyer_login.jpg");
        JLabel jLabel1 = new JLabel(buyerImage);
        jLabel1.setBounds(10, 77, 438, 237);
        buyerPanel.add(jLabel1);

        JLabel jLabel7 = new JLabel("Buy a Property");
        jLabel7.setBounds(20, 321, 450, 35);
        jLabel7.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        buyerPanel.add(jLabel7);

        JButton jButton1 = new JButton("Select");
        jButton1.setBounds(20, 366, 415, 50);
        jButton1.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 48));
        jButton1.setBackground(new Color(102, 102, 255));
        jButton1.setForeground(Color.WHITE);
        buyerPanel.add(jButton1);
        mainPanel.add(buyerPanel);
        
        
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuyerLogin();
                SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
            }
        });

        // Create the "Seller" panel
        JPanel sellerPanel = new JPanel();
        sellerPanel.setLayout(null);
        sellerPanel.setBackground(new Color(255, 255, 255)); // Red color for seller
        sellerPanel.setBounds(872, 129, 454, 486);

        JLabel jLabel3 = new JLabel("Seller");
        jLabel3.setBounds(10, 11, 434, 66);
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setFont(new Font("Eras Bold ITC", Font.PLAIN, 48));
        jLabel3.setForeground(sellerfontColor);
        sellerPanel.add(jLabel3);

        ImageIcon sellerImage = new ImageIcon("images/seller_login.jpg");
        JLabel jLabel4 = new JLabel(sellerImage);
        jLabel4.setBounds(10, 77, 434, 239);
        sellerPanel.add(jLabel4);

        JLabel jLabel8 = new JLabel("Sell a Property");
        jLabel8.setBounds(20, 321, 442, 35);
        jLabel8.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel8.setHorizontalAlignment(SwingConstants.CENTER);
        sellerPanel.add(jLabel8);

        JButton jButton2 = new JButton("Select");
        jButton2.setBounds(20, 366, 415, 50);
        jButton2.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 48));
        jButton2.setBackground(new Color(255, 102, 102));
        jButton2.setForeground(Color.WHITE);
        sellerPanel.add(jButton2);

        mainPanel.add(sellerPanel);
        
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SellerLogin();
                SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
            }
        });


        // Add jLabel5 and jLabel6
        JLabel jLabel5 = new JLabel("Real Estate App");
        jLabel5.setBounds(260, 22, 1066, 101);
        jLabel5.setFont(new Font("Perpetua Titling MT", Font.BOLD, 48));
        jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel5.setForeground(customColor);
        mainPanel.add(jLabel5);

        JLabel jLabel6 = new JLabel("We would like to express our sincere gratitude for choosing our Swing App as your go-to platform for buying and selling. Your trust and support mean the world to us. Thank you for giving us a chance.");
        jLabel6.setBounds(260, 627, 1066, 113);
        jLabel6.setForeground(customColor);
        mainPanel.add(jLabel6);

        // Set the main panel as the content pane
        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RealEstateApp().setVisible(true);
        });
    }
}

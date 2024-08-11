package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Thankyou extends JPanel {

    private BufferedImage backgroundImage;

    public Thankyou() {
        try {
            backgroundImage = ImageIO.read(new File("images/23116c2e-c722-43f6-8153-ba4a983a5a11.png")); // Replace with the path to your image
        } catch (IOException e) {
            e.printStackTrace();
        }
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        mainPanel.setLayout(null);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, 20, 200, 40);
        logoutButton.setBackground(new Color(255, 102, 102));
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 24));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(1300, 510, 230, 80);
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(0, 0, 0, 128));
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel);
        add(mainPanel, BorderLayout.CENTER);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RealEstateApp realEstateApp = new RealEstateApp();
                realEstateApp.setVisible(true); // Make the RealEstateApp frame visible
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thank You Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            frame.setContentPane(new Thankyou());
            frame.setVisible(true);
        });
    }
}

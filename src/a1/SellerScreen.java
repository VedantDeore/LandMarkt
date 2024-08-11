 package a1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellerScreen extends JFrame {
	private JPanel mainPanel; 
    private JPanel jPanel1;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JPanel jPanel2;
    private JButton jButton3;
    private JLabel jLabel3;
    private JLabel jLabel6;
    private JPanel jPanel3;
    private JButton jButton4;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JPanel jPanel9;
    private JButton jButton9;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JPanel jPanel10;
    private JButton jButton10;
    private JLabel jLabel13;
    private JLabel jLabel14;

    public SellerScreen() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create the main panel and set its layout to null
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw the background image
                Image backgroundImage = new ImageIcon("images/landscape-sunset-sea-city-cityscape-night-843171-wallhere.com2.jpg").getImage();
                g.drawImage(backgroundImage, 0, 0, this);
            }
        };
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        jPanel1 = new JPanel();
        jPanel1.setBounds(1300, 10, 230, 180);
        jPanel1.setLayout(null);
        jPanel1.setBackground(new Color(0, 0, 0,230)); 
        
        jButton1 = new JButton("Logout");
        jButton1.setBounds(15, 120, 200, 40);
        jButton1.setBackground(new Color(255, 102, 102));
        jButton1.setFont(new Font("Segoe UI", Font.PLAIN, 24));
       jPanel1.add(jButton1);

        jButton2 = new JButton("My Account");
        jButton2.setBounds(15, 15, 200, 90);
        jButton2.setBackground(new Color(153, 255, 153));
        jButton2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
       jPanel1.add(jButton2);

        jLabel1 = new JLabel("Seller Dashboard");
        jLabel1.setBounds(300, 20, 600, 100);
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 48));
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(jLabel1);

        
        
//        JButton viewPropertiesButton = createStyledButton("View Properties", Color.BLUE, Color.WHITE);
//        JButton changePropertyButton = createStyledButton("Change Property", Color.BLUE, Color.WHITE);
//        JButton documentManagementButton = createStyledButton("Document Management", Color.BLUE, Color.WHITE);
//        JButton purchasedStatusButton = createStyledButton("Purchased Status", Color.BLUE, Color.WHITE);
//        JButton logoutButton = createStyledButton("Logout", Color.RED, Color.WHITE);
        
        
        add(jPanel1);

        jPanel2 = new JPanel();
        jPanel2.setBounds(50, 230, 330, 550);
        jPanel2.setLayout(null);
        jPanel2.setBackground(new Color(0, 0, 0,230)); 
        
        jButton3 = new JButton("Edit Properties");
        jButton3.setBounds(15, 30, 300, 40);
        jButton3.setBackground(new Color(255, 204, 0));
        jButton3.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        jPanel2.add(jButton3);

        jLabel3 = new JLabel("Edit your Property");
        jLabel3.setBounds(15, 80, 300, 40);
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setForeground(Color.WHITE);
        jPanel2.add(jLabel3);

        jLabel6 = new JLabel();
        jLabel6.setBounds(15, 130, 300, 400);
        // Add an image to jLabel6 if needed
         ImageIcon image6 = new ImageIcon("images/image1_0 (81).jpg");
         jLabel6.setIcon(image6);
        jPanel2.add(jLabel6);

        add(jPanel2);

        jPanel3 = new JPanel();
        jPanel3.setBounds(420, 230, 330, 550);
        jPanel3.setLayout(null);
        jPanel3.setBackground(new Color(0, 0, 0,230)); 
        
        jButton4 = new JButton("Legal Documentation");
        jButton4.setBounds(15, 30, 300, 40);
        jButton4.setBackground(new Color(255, 102, 102));
        jButton4.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        jPanel3.add(jButton4);

        jLabel7 = new JLabel("Verify the Legal Document");
        jLabel7.setForeground(Color.WHITE);
        jLabel7.setBounds(15, 80, 300, 40);
        jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel3.add(jLabel7);

        jLabel8 = new JLabel();
        jLabel8.setBounds(15, 130, 300, 400);
        // Add an image to jLabel8 if needed
         ImageIcon image8 = new ImageIcon("images/image1_0 (6).jpg");
         jLabel8.setIcon(image8);
        jPanel3.add(jLabel8);

        add(jPanel3);

        jPanel9 = new JPanel();
        jPanel9.setBounds(790, 230, 330, 550);
        jPanel9.setLayout(null);
        jPanel9.setBackground(new Color(0, 0, 0,230)); 
        
        jButton9 = new JButton("Purchased Status");
        jButton9.setBounds(15, 30, 300, 40);
        jButton9.setBackground(new Color(255, 102, 153));
        jButton9.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        jPanel9.add(jButton9);

        jLabel11 = new JLabel("See what properties are purchased");
        jLabel11.setForeground(Color.WHITE);
        jLabel11.setBounds(15, 80, 300, 40);
        jLabel11.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel9.add(jLabel11);

        jLabel12 = new JLabel();
        jLabel12.setBounds(15, 130, 300, 400);
        // Add an image to jLabel12 if needed
         ImageIcon image12 = new ImageIcon("images/image1_0 (71).jpg");
         jLabel12.setIcon(image12);
        jPanel9.add(jLabel12);

        add(jPanel9);

        jPanel10 = new JPanel();
        jPanel10.setBounds(1160, 230, 330, 550);
        jPanel10.setLayout(null);
        jPanel10.setBackground(new Color(0, 0, 0,230)); 
        
        jButton10 = new JButton("Comming Soon..");
        jButton10.setBounds(15, 30, 300, 40);
        jButton10.setBackground(new Color(204, 153, 255));
        jButton10.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        jPanel10.add(jButton10);

        jLabel13 = new JLabel("This section is comming soon");
        jLabel13.setForeground(Color.WHITE);
        jLabel13.setBounds(15, 80, 300, 40);
        jLabel13.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel10.add(jLabel13);

        jLabel14 = new JLabel();
        jLabel14.setBounds(15, 130, 300, 400);
        // Add an image to jLabel14 if needed
         ImageIcon image14 = new ImageIcon("images/image1_0 (9).jpg");
         jLabel14.setIcon(image14);
        jPanel10.add(jLabel14);

        add(jPanel10);

        pack();
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RealEstateApp realEstateApp = new RealEstateApp();
                realEstateApp.setVisible(true); // Make the RealEstateApp frame visible
            }
        });


        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new MyAccountSeller instance
            	 MyAccountSeller myAccountSeller = new MyAccountSeller();
                 JFrame myAccountFrame = new JFrame("My Account Seller");
                 myAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 myAccountFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set appropriate size
                 myAccountFrame.setContentPane(myAccountSeller);
                 myAccountFrame.setVisible(true);
            }
        });



        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditProperty editProperty = new EditProperty();
                setContentPane(editProperty); // Set content to EditProperty panel
                revalidate(); // Refresh the frame
            }
        });







        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LegalDocumentation legalDocumentation = new LegalDocumentation();
                legalDocumentation.setVisible(true); // Make the LegalDocumentation frame visible
                dispose();
            }
        });


        jButton9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new PurchaseStatus().setVisible(true);;
                // Add code to handle the "Property Reviews" button click event
                // You can open another window, change content, or perform other actions here
            }
        });

        jButton10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//            	  JFrame frame = new JFrame("Property Reviews");
//                  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                  frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                  new PropertyReviews().setVisible(true);
                 

//                  SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
            }
        });
    }
    

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new SellerScreen().setVisible(true);
        });
    }
}

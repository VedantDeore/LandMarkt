package a1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyReviews extends JFrame {
    private JPanel mainPanel;
    private JTextArea commentTextArea;
    private JButton submitCommentButton;
    private JButton backButton;

    public PropertyReviews() {
     

        initComponents();
        loadDummyComments();
        
        // Show popup message on load
        JOptionPane.showMessageDialog(
            this,
            "This page is under progress. Some features may not work.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setTitle("Property Reviews");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Header Panel with Back Button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(100, 190, 180)); // Steel Blue
        backButton = createStyledButton("Back");
        backButton.addActionListener(e -> {
            // Implement back button action here
            System.out.println("Back button pressed");
            dispose();
//            new BuyerScreen().setVisible(true);
        });
        headerPanel.add(backButton, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Main Panel for comments
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245)); // Light Gray
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel for new comment
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(new Color(245, 245, 245)); // Light Gray
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        commentTextArea = new JTextArea(3, 40);
        commentTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        submitCommentButton = createStyledButton("Share");
        inputPanel.add(new JScrollPane(commentTextArea), BorderLayout.CENTER);
        inputPanel.add(submitCommentButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadDummyComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("Lisa D.", "consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.", "11 min ago", "From Mobile", "lisa_d.jpg"));
        comments.add(new Comment("John Doe", "Lorem ipsum dolor sit amet.", "11 min ago", "From Mobile", "john_doe.jpg"));
        comments.add(new Comment("Maria Leanz", "Duis autem vel eum iriure dolor in hendrerit in vulputate ?", "2 min ago", "From Web", "maria_leanz.jpg"));
     
        for (Comment comment : comments) {
            addCommentToUI(comment);
        }
    }

    private void addCommentToUI(Comment comment) {
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
        ));
        commentPanel.setBackground(Color.WHITE);

        // Load profile image
        JLabel profileLabel = new JLabel();
        profileLabel.setIcon(getScaledProfileImage(comment.profileImagePath, 50, 50));

        // Create User and Time labels
        JLabel userLabel = new JLabel(comment.username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel timeLabel = new JLabel("<html><i>" + comment.time + " - " + comment.source + "</i></html>");
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 14));

        // Comment text area
        JTextArea commentArea = new JTextArea(comment.text);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setEditable(false);
        commentArea.setFont(new Font("Arial", Font.PLAIN, 16));
        commentArea.setBackground(new Color(245, 245, 245)); // Light Gray
        commentArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Header panel for profile image and user info
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(profileLabel, BorderLayout.WEST);
        
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.add(userLabel);
        userInfoPanel.add(timeLabel);
        headerPanel.add(userInfoPanel, BorderLayout.CENTER);

        commentPanel.add(headerPanel, BorderLayout.NORTH);
        commentPanel.add(commentArea, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton likeButton = createStyledButton("Like");
        JButton dislikeButton = createStyledButton("Dislike");
        JButton commentButton = createStyledButton("Comment");

        buttonPanel.add(likeButton);
        buttonPanel.add(dislikeButton);
        buttonPanel.add(commentButton);

        commentPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(commentPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Add space between comments
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        return button;
    }

    private Icon getScaledProfileImage(String profileImageName, int width, int height) {
        // Construct full path to the image file
        String path = "E:\\Eclipse p\\Assignment\\images\\" + profileImageName;

        // Load the image from file
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    private static class Comment {
        String username;
        String text;
        String time;
        String source;
        String profileImagePath;

        Comment(String username, String text, String time, String source, String profileImagePath) {
            this.username = username;
            this.text = text;
            this.time = time;
            this.source = source;
            this.profileImagePath = profileImagePath;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PropertyReviews frame = new PropertyReviews();
            frame.setVisible(true);
            
        });
    }
}



//import com.teamdev.jxbrowser.chromium.Browser;
//import com.teamdev.jxbrowser.chromium.BrowserFactory;
//import javax.swing.*;
//import java.awt.*;
//
//public class ComingSoon {
//    public static void main(String[] args) {
//        Browser browser = BrowserFactory.create();
//
//        JFrame frame = new JFrame("Google Maps in Java Swing");
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.add(browser.getView().getComponent(), BorderLayout.CENTER);
//        frame.setSize(700, 500);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//
//        browser.loadURL("http://maps.google.com");
//    }
//}

//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.web.WebView;
//import javafx.stage.Stage;
//
//public class GoogleMapsJavaFXExample extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        WebView webView = new WebView();
//        webView.getEngine().load("https://www.google.com/maps");
//
//        Scene scene = new Scene(webView, 800, 600);
//        primaryStage.setTitle("Google Maps in JavaFX");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

//
//import org.cef.CefApp;
//import org.cef.CefClient;
//import org.cef.CefSettings;
//import org.cef.browser.CefBrowser;
//import org.cef.handler.CefLoadHandler;
//import org.cef.handler.CefLoadHandlerAdapter;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class GoogleMapsJCEFExample {
//
//    public static void main(String[] args) {
//        CefApp.addAppHandler(new CefApp.CefAppHandlerAdapter() {
//            @Override
//            public void stateHasChanged(org.cef.CefApp.CefAppState state) {
//                if (state == CefApp.CefAppState.TERMINATED) {
//                    System.exit(0);
//                }
//            }
//        });
//
//        CefSettings settings = new CefSettings();
//        settings.windowless_rendering_enabled = false;
//        CefApp.getInstance().initialize(args, settings, false);
//
//        CefClient client = CefApp.getInstance().createClient();
//        CefBrowser browser = client.createBrowser("https://www.google.com/maps", false, false);
//
//        JFrame frame = new JFrame("Google Maps in JCEF");
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.setLayout(new BorderLayout());
//        frame.add(client.getUIComponent(), BorderLayout.CENTER);
//        frame.setVisible(true);
//
//        CefApp.getInstance().addCefLoadHandler(new CefLoadHandlerAdapter() {
//            @Override
//            public void onLoadEnd(CefBrowser browser, int frameIdentifer, int httpStatusCode) {
//                if (httpStatusCode == 200) {
//                    System.out.println("Page loaded successfully.");
//                }
//            }
//        });
//    }
//}
//

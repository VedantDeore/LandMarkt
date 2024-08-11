package a1;

import java.sql.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    public static void executeQuery(String query) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root",
                    "Darshan123");
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (Exception e1) {
            System.out.println(e1);
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Java Swing Assignment",null);
        ImageIcon backgroundImage = new ImageIcon("D:/ECLIP/JAVACP0/src/Pack1/demo2.png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        JLabel l = new JLabel("Student Information");
        l.setFont(new java.awt.Font("Segoe UI", 1, 20)); 
        backgroundLabel.setBounds(0, 0, 500, 400);
        backgroundLabel.add(l);
        backgroundLabel.setLayout(null);
        f.setContentPane(backgroundLabel);
        f.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        JLabel l1 = new JLabel("Roll No :");
        JLabel l2 = new JLabel("Name :");
        final JLabel li = new JLabel();
        final JTextField t1 = new JTextField();
        final JTextField t2 = new JTextField();
        JButton b1 = new JButton("ADD");
        JButton b2 = new JButton("DELETE");
        JButton b3 = new JButton("UPDATE");

        f.add(l);
        f.add(l1);
        f.add(l2);
        f.add(t1);
        f.add(t2);
        f.add(b1);
        f.add(b2);
        f.add(b3);
        f.add(li);

        l.setBounds(150, 10, 200, 30);
        l1.setBounds(20, 70, 200, 30);
        l2.setBounds(20, 130, 200, 30);
        t1.setBounds(80, 73, 300, 30);
        t2.setBounds(80, 133, 300, 30);
        b1.setBounds(80, 200, 100, 30);
        b2.setBounds(250, 200, 100, 30);
        b3.setBounds(165, 250, 100, 30);
        li.setBounds(150, 300, 200, 30);

        f.setLayout(null);
        f.setSize(500, 400);
        f.setVisible(true);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int roll = Integer.parseInt(t1.getText());
                String name = t2.getText();
                String insert = "insert into student_info(roll_no,name)values('" + roll + "','" + name + "')";
                executeQuery(insert);
                li.setText("Data Inserted In Table.");
            }

        });

        b2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                int roll = Integer.parseInt(t1.getText());
                String delete = "delete from student_info where roll_no = '" + roll + "'";
                executeQuery(delete);
                li.setText("Roll No: " + roll + " Deleted.");
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int roll = Integer.parseInt(t1.getText());
                String name = t2.getText();
                String update = "update student_info set name ='" + name + "' where roll_no = '" + roll + "'";
                executeQuery(update);
                li.setText("Roll No: " + roll + " Updated");
            }
        });

    }
}
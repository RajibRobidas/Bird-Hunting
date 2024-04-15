package p;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private Container c;
    private JLabel userLevel;
    private Font f;
    private JButton button;
    public Main(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,500);
        setLocationRelativeTo(null);
        setTitle("Start Game--");
        //frame.setResizable(false);

        welcome();
    }
    public void welcome() {
        c = this.getContentPane();
        c.setLayout(null);
        c.setBackground(Color.lightGray);

        f = new Font("Arial",Font.BOLD,60);

        userLevel = new JLabel();
        userLevel.setText("WELCOME");
        userLevel.setFont(f);
        userLevel.setBounds(210,60,400,150);
        c.add(userLevel);

        f = new Font("Arial",Font.BOLD,20);

        userLevel = new JLabel();
        userLevel.setText("Let enjoy the Game");
        userLevel.setFont(f);
        userLevel.setForeground(Color.BLUE);
        userLevel.setBounds(275,160,200,100);
        c.add(userLevel);

        button = new JButton();
        button.setText("▶");
        //button.setFont(f);
        button.setBounds(340,260,60,30);
        c.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                New NF = new New();
            }
        });
    }
    public static void main(String[] args) {
        Main frame = new Main();
    }
}

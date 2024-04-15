package p;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class New extends JFrame {
    private Container c;
    private JLabel userLevel;
    private Font f;
    private JButton button;
    public New(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,500);
        setLocationRelativeTo(null);
        setTitle("Play Game--");
        //frame.setResizable(false);

        welcome();
    }
    public void welcome() {
        c = this.getContentPane();
        c.setLayout(null);
        c.setBackground(Color.LIGHT_GRAY);

        f = new Font("Arial",Font.BOLD,17);

        button = new JButton();
        button.setText("Popping Shape");
        button.setFont(f);
        button.setBounds(280,140,250,50);
        button.setForeground(Color.black);
        button.setOpaque(true);
        button.setBackground(Color.white);
        c.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartGame frame = new StartGame();
            }
        });

        button = new JButton();
        button.setText("Bird Hunting");
        button.setFont(f);
        button.setBounds(280,240,250,50);
        button.setForeground(Color.black);
        button.setOpaque(true);
        button.setBackground(Color.white);
        c.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartGame2 frame = new StartGame2();
            }
        });

        button = new JButton();
        button.setText("Back");
        button.setFont(f);
        button.setBounds(650,380,90,30);
        button.setForeground(Color.black);
        button.setOpaque(true);
        button.setBackground(Color.green);
        c.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main frame = new Main();
            }
        });

    }
    public static void main(String[] args) {
        New frame = new New();
    }
}

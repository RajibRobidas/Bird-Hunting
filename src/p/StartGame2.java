package p;

import p1.FallingBlocksGame;
import p2.FallingBlocksGame2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame2 extends JFrame {
    private Container c;
    private JLabel userLevel;
    private Font f;
    private JButton button;
    public StartGame2(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,500);
        setLocationRelativeTo(null);
        setTitle("Run Game--");
        //frame.setResizable(false);

        welcome();
    }
    public void welcome() {
        c = this.getContentPane();
        c.setLayout(null);
        c.setBackground(Color.GRAY);

        f = new Font("Arial",Font.BOLD,17);

        button = new JButton();
        button.setText("Start Game");
        button.setFont(f);
        button.setBounds(320,80,150,50);
        button.setForeground(Color.black);
        button.setOpaque(true);
        button.setBackground(Color.white);
        c.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FallingBlocksGame2 game = new FallingBlocksGame2();
            }
        });

        button = new JButton();
        button.setText("Quit game");
        //button.setFont(f);
        button.setBounds(330,200,140,30);
        button.setForeground(Color.black);
        button.setOpaque(true);
        button.setBackground(Color.white);
        c.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                New frame = new New();
            }
        });
    }
    public static void main(String[] args) {
        StartGame2 frame = new StartGame2();
    }
}

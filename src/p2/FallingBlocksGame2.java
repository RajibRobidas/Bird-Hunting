package p2;

import p1.*;

import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import p.StartGame2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FallingBlocksGame2 extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final int BLOCK_SIZE = 25;
    private static final int DESK_RADIUS = 25;
    private static final int MAX_LIVES = 5;

    public Font f;

    private Thread fallingBlockThread;
    private int score;
    private int lives;
    private boolean isGameOver;
    private Desk desk;
    private ArrayList<Block> blocks;
    private ArrayList<Block2>blocks2;
    private Ball ball;
    private boolean restartGame;


    private Color[] availablecolor = { Color.green , Color.white, Color.ORANGE, Color.BLUE, Color.BLACK, Color.GRAY, Color.PINK, Color.YELLOW };
    private Color color;

    public static AudioPlayer audio = new AudioPlayer();


    public FallingBlocksGame2() {
        setTitle("Hunting Birds");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setFocusable(true);

        addKeyListener(new DeskControl());

        desk = new Desk();
        blocks = new ArrayList<>();
        blocks2 = new ArrayList<>();
        score = 0;
        lives = MAX_LIVES;
        isGameOver = false;
        restartGame = false;

        startFallingBlocks();

        setVisible(true);
    }

    private void startFallingBlocks() {
        fallingBlockThread = new Thread(() -> {                                                           //he
            while (!isGameOver) {

                if (Math.random() < 0.009 && ball == null) {
                    int x = (int) (Math.random() * (WIDTH - BLOCK_SIZE));
                    int y = 0;
                    Block newBlock = new Block(x, y, BLOCK_SIZE, BLOCK_SIZE);

                    blocks.add(newBlock);
                }
//

                if (Math.random() < 0.05 && ball == null) {
                    int y = (int) (Math.random() * (WIDTH/2 - BLOCK_SIZE));
                    int x = 0;
                    Block2 newBlock = new Block2(x, y, BLOCK_SIZE, BLOCK_SIZE);

                    blocks2.add(newBlock);
                }
//
                if (ball != null) {
                    ball.move();
                    if (ball.getY() <= 0) {
                        ball = null;
                    } else {
                        for (int i = 0; i < blocks2.size(); i++) {
                            Block2 block = blocks2.get(i);
                            if (block.isVisible() && ball.intersects(block)) {
                                block.setVisible(false);
                                score+=5;
                                playcongrats();
                                //playnbeeeps();
                                ball = null;
                                break;
                            }
                        }
                    }
                }

                for (int i = 0; i < blocks.size(); i++) {
                    Block block = blocks.get(i);
                    if (block.isVisible()) {
                        block.move();
                        if (desk.intersects(block)) {
                            playnbeeeps();
                            lives--;

                            if (lives == 0) {
                                playnbeeeps();
                                isGameOver = true;
                                break;
                            }
                        }
                        if (block.getY() >= HEIGHT) {
                            block.setVisible(false);
                        }
                    } else {
                        blocks.remove(i);
                    }
                }
//
                for (int i = 0; i < blocks2.size(); i++) {
                    Block2 block = blocks2.get(i);
                    if (block.isVisible()) {
                        block.move();

                        if (block.getX() >= WIDTH) {
                            block.setVisible(false);
                        }
                    } else {
                        blocks2.remove(i);
                    }
                }
//
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                repaint();
            }
        });

        fallingBlockThread.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        desk.draw(g);
        if (ball != null) {
            ball.draw(g);
        }
        for (Block block : blocks) {
            block.draw(g);
        }
//
        for (Block2 block : blocks2) {
            block.draw(g);
        }
//
        f = new Font("Arial",Font.BOLD,17);
        g.setFont(f);
        g.setColor(Color.black);
        g.drawString("Score: " + score, WIDTH - 100, 50);
        //g.setColor(Color.green);
        g.drawString("Lives: " + lives, 10, 50);
        g.drawString(".", 10, 70);

        if (isGameOver) {
            //playwins();
            f = new Font("Arial",Font.BOLD,22);
            g.setFont(f);
            g.setColor(Color.green);
            g.drawString("Game Over!", WIDTH / 2 - 50, HEIGHT / 3);
            g.drawString("Total Score: " + score, WIDTH / 2 - 63, HEIGHT / 3 + 50);

            g.setColor(Color.cyan);
            g.drawString("Press Enter to Restart Game", WIDTH / 2 - 125, HEIGHT / 3 + 110);

            g.setColor(Color.red);
            g.drawString("Press Space to Exit Game", WIDTH / 2 - 115, HEIGHT / 3 + 160);
            
            g.setColor(Color.red);
            g.drawString("Press BackSpace to back Game", WIDTH / 2 - 145, HEIGHT / 3 + 210);
        }
    }


    public static void playcongrats(){
        audio.playcongrat();
    }
    public static void playnbeeeps(){
        audio.playnbeep();
    }


    private class DeskControl extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (isGameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
                FallingBlocksGame2 fa = new FallingBlocksGame2();
            } else if (e.getKeyCode() == KeyEvent.VK_UP ) {
                desk.throwBall();
            } else if(e.getKeyCode() == KeyEvent.VK_SPACE && isGameOver ) {
                System.exit(0);
            }else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && isGameOver ) {
                StartGame2 frame = new StartGame2();
            }else {
                desk.move(e);
            }
        }
    }

    private class Desk {
        private int x;
        private int y;

        public Desk() {
            x = WIDTH / 2 - DESK_RADIUS;
            y = HEIGHT - 2 * DESK_RADIUS;
        }

        public void move(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && x > 0) {
                x -= 35;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && x < WIDTH - 2 * DESK_RADIUS) {
                x += 35;
            }
        }

        public void throwBall() {
            ball = new Ball(x + DESK_RADIUS / 2, y - DESK_RADIUS);
        }

        public void draw(Graphics g) {
            g.setColor(Color.green);
            g.fillOval(x, y-20, 2*(2 * DESK_RADIUS)/3, (2 * DESK_RADIUS+10));
            g.setColor(Color.yellow);
            g.fillRect(0,485,600,10);
        }

        public boolean intersects(Block block) {
            int blockX = block.getX() + block.getWidth() / 2;
            int blockY = block.getY() + block.getHeight() / 2;

            int deskX = x - 8 + DESK_RADIUS;
            int deskY = y + DESK_RADIUS;

            double distance = Math.sqrt(Math.pow(blockX - deskX, 2) + Math.pow(blockY - deskY, 2));

            if (distance <= DESK_RADIUS) {
                block.setVisible(false);
                ball = null;
                return true;
            }

            return false;
        }
    }

    private class Block {
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean visible;

        public Block(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width/2;
            this.height = height;
            visible = true;
        }

        public void move() {
            y += 8;
        }

        public void draw(Graphics g) {
            if (visible) {
                Random r = new Random();
                g.setColor(Color.red);

                g.fillOval(x, y, width, height+15);

            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }
    }
    //
    private class Block2 {private int x;
        private int y;
        private int width;
        private int height;
        private boolean visible;
        private BufferedImage birdImage; // Image for the bird


        public Block2(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width+10;
            this.height = height+10;
            visible = true;
            loadBirdImage(); // Load the bird image when a Block2 object is created
        }

        private void loadBirdImage() {
            try {
                birdImage = ImageIO.read(new File("src/main/resources/bird_img.png")); // Load bird image from file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void move() {
            x += 6;
        }

        public void draw(Graphics g) {
            if (visible && birdImage != null) {
                g.drawImage(birdImage, x, y, width, height, null); // Draw bird image
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }
    }
    //
    private class Ball {
        private int x;
        private int y;

        public Ball(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            y -= 20;
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval(x, y, 15, 15);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean intersects(Block2 block) {
            int blockX = block.getX();
            int blockY = block.getY();
            int blockWidth = block.getWidth();
            int blockHeight = block.getHeight();

            if (x >= blockX-10 && x <= blockX + blockWidth && y >= blockY && y <= blockY + blockHeight) {
                return true;
            }

            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FallingBlocksGame2::new);
    }
}

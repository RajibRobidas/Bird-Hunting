package p1;

import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import p.StartGame;
import p.StartGame2;

public class FallingBlocksGame extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final int BLOCK_SIZE = 30;
    private static final int DESK_RADIUS = 30;
    private static final int MAX_LIVES = 5;

    public Font f;

    private Thread fallingBlockThread;
    private int score;
    private int lives;
    private boolean isGameOver;
    private Desk desk;
    private ArrayList<Block> blocks;
    private Ball ball;
    private boolean restartGame;
//
    private Color currentColor;
    private Timer colorChangeTimer;
    private int colorChangeInterval = 1000; // Change color every 1 second

    private Color[] availablecolor = { Color.green , Color.red, Color.ORANGE, Color.BLUE, Color.BLACK, Color.PINK, Color.YELLOW };
    private Color color;

    public static AudioPlayer audio = new AudioPlayer();

    private ArrayList<Color> blockColors;

    public FallingBlocksGame() {
        setTitle("Pop the shape");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setFocusable(true);

        addKeyListener(new DeskControl());


        blockColors = new ArrayList<>();

        desk = new Desk();
        blocks = new ArrayList<>();
        score = 0;
        lives = MAX_LIVES;
        isGameOver = false;
        restartGame = false;

        startFallingBlocks();

        setVisible(true);

        //
        currentColor = getRandomColor();
        startColorChangeTimer();
    }

    private void startColorChangeTimer() {
        colorChangeTimer = new Timer(colorChangeInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = getRandomColor();
            }
        });
        colorChangeTimer.start();
    }

    private Color getRandomColor() {
        Random random = new Random();
        return availablecolor[random.nextInt(availablecolor.length)];
    }

    private void startFallingBlocks() {
        fallingBlockThread = new Thread(() -> {                                                           //he
            while (!isGameOver) {

                if (Math.random() < 0.1 && ball == null) {

                    int x = (int) (Math.random() * (WIDTH - BLOCK_SIZE));
                    int y = 0;
                    int width = BLOCK_SIZE;
                    int height = BLOCK_SIZE;
                    BlockShape shape = getRandomShape();
                    Block newBlock = new Block(x, y, width, height, shape);

                    // Use the current color for the new block
                    newBlock.setColor(currentColor);

                    blocks.add(newBlock);
                    blockColors.add(color);  // Store the color for the block
                }

                if (ball != null) {
                    ball.move();
                    if (ball.getY() <= 0) {
                        ball = null;
                    } else {
                        for (int i = 0; i < blocks.size(); i++) {
                            Block block = blocks.get(i);
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
                            //playcongrats();
                            lives--;

                            if (lives == 0) {
                                //playcongrats();
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
        //blocks.draw(g);
        f = new Font("Arial",Font.BOLD,17);
        g.setFont(f);
        g.setColor(Color.black);
        g.drawString("Score: " + score, WIDTH - 100, 50);
        //g.setColor(Color.green);
        g.drawString("Lives: " + lives, 10, 50);
        g.drawString(".", 10, 70);
        if (isGameOver) {
            f = new Font("Arial",Font.BOLD,22);
            g.setFont(f);
            g.setColor(Color.green);
            g.drawString("Game Over!", WIDTH / 2 - 50, HEIGHT / 3);
            g.drawString("Total Score: " + score, WIDTH / 2 - 63, HEIGHT / 3 + 50);
            //g.drawString("Finish game", WIDTH / 2 - 50, HEIGHT / 3 + 40);
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
                p1.FallingBlocksGame fa = new p1.FallingBlocksGame();
            } else if (e.getKeyCode() == KeyEvent.VK_UP ) {
                desk.throwBall();
            } else if(e.getKeyCode() == KeyEvent.VK_SPACE && isGameOver ) {
                System.exit(0);
            }else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && isGameOver ) {
                StartGame frame = new StartGame();
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
            g.fillOval(x, y-20, 2 * DESK_RADIUS, 2 * DESK_RADIUS+10);
            g.setColor(Color.yellow);
            g.fillRect(0,485,600,10);
        }

        public boolean intersects(Block block) {
            int blockX = block.getX() + block.getWidth() / 2;
            int blockY = block.getY() + block.getHeight() / 2;

            int deskX = x + DESK_RADIUS;
            int deskY = y + DESK_RADIUS;

            double distance = Math.sqrt(Math.pow(blockX - deskX, 2) + Math.pow(blockY - deskY, 2));

            if (distance <= DESK_RADIUS*1.5) {
                block.setVisible(false);
                ball = null;
                return true;
            }

            return false;
        }
    }

    public enum BlockShape {
        RECTANGLE, OVAL
    }
    private BlockShape getRandomShape() {
        Random random = new Random();
        return BlockShape.values()[random.nextInt(BlockShape.values().length)];
    }

    private class Block {
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean visible;
        public Color color;

        private BlockShape shape;

        public Block(int x, int y, int width, int height, BlockShape shape) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.shape = shape;
            visible = true;
        }

        public void draw(Graphics g) {
            if (visible) {
                g.setColor(color);
                switch (shape) {
                    case RECTANGLE:
                        g.fillRect(x, y, width, height);
                        break;
                    case OVAL:
                        g.fillOval(x, y, width, height);
                        break;
                }
            }
        }

/*

        public Block(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            visible = true;
        }

        public void draw(Graphics g) {
            if (visible) {
                g.setColor(color);

                int p = 100 * new Random().nextInt();
                if (p < 50) g.fillOval(x, y, width, height);
                else g.fillRect(x, y, width, height);
            }
        }
*/

        public void move() {
    y += 5;
}

        public void setColor(Color color) {
            this.color = color;
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

    private class Ball {
        private int x;
        private int y;

        public Ball(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            y -= 15;
        }

        public void draw(Graphics g) {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, 15, 15);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean intersects(Block block) {
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
        SwingUtilities.invokeLater(p1.FallingBlocksGame::new);
    }
}

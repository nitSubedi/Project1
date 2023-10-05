import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final int UNIT_SIZE = 10;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;//Speed
    private final ArrayList<Integer> snakeX = new ArrayList<>();
    private final ArrayList<Integer> snakeY = new ArrayList<>();
    private int foodX;
    private int foodY;
    private int score;
    private char direction = 'R';
    private boolean isRunning = false;
    private final Random random = new Random();
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressedHandler(e);
            }
        });
        startGame();
    }

    public void startGame() {
        snakeX.clear();
        snakeY.clear();
        snakeX.add(WIDTH / 2);
        snakeY.add(HEIGHT / 2);
        placeFood();
        score = 0;
        direction = 'R';
        isRunning = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void placeFood() {
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
		int headX = snakeX.get(0);
		int headY = snakeY.get(0);

		switch (direction) {
			case 'U':
				headY -= UNIT_SIZE;
            break;
			case 'D':
				headY += UNIT_SIZE;
            break;
			case 'L':
				headX -= UNIT_SIZE;
            break;
			case 'R':
				headX += UNIT_SIZE;
            break;
    }

    if (headX == foodX && headY == foodY) {
        snakeX.add(0, headX);
        snakeY.add(0, headY);
        placeFood();
        score++;
    } else {
        // Remove the tail only if no food is eaten
        snakeX.remove(snakeX.size() - 1);
        snakeY.remove(snakeY.size() - 1);
        snakeX.add(0, headX);
        snakeY.add(0, headY);
    }
}
    public void checkCollision() {
        // Check collision with food
        if (snakeX.get(0) == foodX && snakeY.get(0) == foodY) {
            snakeX.add(foodX);
            snakeY.add(foodY);
            score++;
            placeFood();
        }

        // Check collision with walls
        if (snakeX.get(0) >= WIDTH || snakeX.get(0) < 0 || snakeY.get(0) >= HEIGHT || snakeY.get(0) < 0) {
            isRunning = false;
        }

        // Check collision with itself
        for (int i = 1; i < snakeX.size(); i++) {
            if (snakeX.get(i) == snakeX.get(0) && snakeY.get(i) == snakeY.get(0)) {
                isRunning = false;
            }
        }

        if (!isRunning) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (isRunning) {
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snakeX.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.YELLOW);
                }
                g.fillRect(snakeX.get(i), snakeY.get(i), UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            // Game over
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", WIDTH / 2 - 90, HEIGHT / 2 - 15);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, WIDTH / 2 - 30, HEIGHT / 2 + 15);
        }
    }

    public void keyPressedHandler(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && direction != 'R') {
            direction = 'L';
        }
        if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && direction != 'L') {
            direction = 'R';
        }
        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && direction != 'D') {
            direction = 'U';
        }
        if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && direction != 'U') {
            direction = 'D';
        }
        if (key == KeyEvent.VK_SPACE && !isRunning) {
            startGame();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

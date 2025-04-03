import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 70;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int foodEaten = 0;
    int applesEaten = 0;
    int counter;
    int foodX;
    int foodY;
    int appleX;
    int appleY;
    char direction = 'R'; //U:Up, D:Down, R:Right, L:Left
    boolean running = false;
    Timer timer;
    Random random;
    int gameScore;
    GameButton gameButton;



    GamePanel() {
        random = new Random();
        gameButton = new GameButton(this);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.setLayout(null);
        this.add(gameButton);

        startGame();
    }
    public void startGame() {
        running=true;
        newApple();
        newFood();
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void restartGame() {
        bodyParts = 6;
        foodEaten = 0;
        applesEaten = 0;
        counter = 0;
        gameScore = 0;
        direction = 'R';
        running = true;

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;  // reset position
            y[i] = 0;
        }
        gameButton.setVisible(false);
        timer = new Timer(DELAY, this);
        timer.start();
        newApple();
        newFood();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if(running) {

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.yellow);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);


            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + gameScore, (SCREEN_WIDTH - metrics.stringWidth("Score: " + gameScore))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void newFood() {
        foodX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move() {
        for (int i = bodyParts; i >0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction) {
            case'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == appleX) && y[0] == appleY) {
            appleX = appleY = -25;
            applesEaten++;

        }
        if(counter%5 == 0) {
            newApple();
            counter++;
        }
    }
    public void checkFood() {
        if((x[0] == foodX) && y[0] == foodY) {
            bodyParts++;
            foodEaten++;
            counter = foodEaten;
            newFood();
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for(int i = bodyParts; i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
        }
        //checks if head touches border
/*        if(x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }*/
        if(!running) {
            timer.stop();
        }
    }
    public void checkWallJump() {
        if(x[0]<0) {
                x[0] = SCREEN_WIDTH;  // set x position

        }
        else if(x[0]> SCREEN_WIDTH) {
                x[0] = 0;  // set x position

        }
        else if(y[0]<0) {
                y[0] = SCREEN_HEIGHT;  // set y position

        }
        else if(y[0]> SCREEN_HEIGHT) {
                y[0] = 0;  // set y position
        }
    }
    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + gameScore, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + gameScore))/2, g.getFont().getSize());
        gameButton.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkFood();
            checkCollisions();
            checkWallJump();
            gameScore = (applesEaten*5) + foodEaten;
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameButton extends JButton implements ActionListener {

    JButton restartButton;
    GamePanel gamePanel;

    GameButton(GamePanel panel) {
        this.gamePanel = panel;
        restartButton = new JButton();
        this.setText("Restart");
        this.setForeground(Color.blue);
        this.setBackground(Color.GRAY);
        this.setBounds(250, 400, 100, 50);
        this.addActionListener(this);
        this.setVisible(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
            System.out.println("Button clicked"); //debug
            gamePanel.restartGame();
    }
}

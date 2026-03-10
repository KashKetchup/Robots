package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GameWindow extends JInternalFrame implements SaveLoadWindowInfo
{
    private final GameVisualizer gameVisualizer;

    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        gameVisualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public  LastWindowState saveCurrentState() {
     return new LastWindowState(getX(),getY(),getHeight(),getWidth(), isIcon());
    }

    @Override
    public void loadLastState(LastWindowState lastWindowState) {

    }
}

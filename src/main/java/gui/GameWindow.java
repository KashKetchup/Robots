package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class GameWindow extends JInternalFrame implements PreservedWindow
{
    private final GameVisualizer gameVisualizer;
    private final StateConverter stateConverter = new StateConverter();
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
    public LastWindowState saveCurrentState() {
        return stateConverter.saveState(this);
    }

    @Override
    public void loadLastState(LastWindowState lastWindowState) {
        stateConverter.loadState(this, lastWindowState);
    }
}

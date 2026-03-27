package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class GameWindow extends JInternalFrame implements PreservedWindow
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
     return new LastWindowState(getName(),getX(),getY(),getHeight(),getWidth(), isIcon());
    }

    @Override
    public void loadLastState(LastWindowState lastWindowState) {
        this.setSize( lastWindowState.width() >= 0 ? lastWindowState.width() : this.getWidth(),
                lastWindowState.height() >= 0 ? lastWindowState.height() : this.getHeight());
        this.setLocation(lastWindowState.x() >= 0 ? lastWindowState.x() : this.getX(),
                lastWindowState.y() >= 0 ? lastWindowState.y() : this.getY());
        try {
            this.setIcon(lastWindowState.isWindowMinimized());
        } catch (PropertyVetoException e) {}
    }
}

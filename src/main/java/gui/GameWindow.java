package gui;

import localizator.Localizator;
import mvc.RobotController;
import mvc.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class GameWindow extends JInternalFrame implements PreservedWindow
{
    /**
     * Локализатор
     */
    private final Localizator localizator = Localizator.getInstance();

    private final GameVisualizer gameVisualizer;

    private final RobotController robotController;

    /**
     * Конвертер для состояния окна
     */
    private final StateConverter stateConverter = new StateConverter();

    public GameWindow(RobotModel robotModel)
    {
        super("", true, true, true, true);
        setTitle("game.field");
        robotController = new RobotController(robotModel);
        gameVisualizer = new GameVisualizer(robotController);
        robotModel.addPropertyChangeListener(gameVisualizer);
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

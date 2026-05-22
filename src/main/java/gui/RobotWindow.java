package gui;

import localizator.Localizator;
import log.LogChangeListener;
import log.LogWindowSource;
import mvc.RobotData;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Класс окна с информацией о роботе
 */
public class RobotWindow extends JInternalFrame implements PreservedWindow, PropertyChangeListener {
    /**
     * Экземпляр локализатора
     */
    private final static Localizator localizator = Localizator.getInstance();

    /**
     * Текст с информацией
     */
    private TextArea infoContent;
    /**
     * Конвертер для состояния окна
     */
    private final StateConverter stateConverter = new StateConverter();

    /**
     * Конструктор класса
     */
    public RobotWindow() {
        super("", true, true, true, true);
        setTitle(localizator.getString("robot.log.actual"));
        setName("robotWindow");
        this.infoContent = new TextArea("");
        this.infoContent.setSize(250, 150);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(infoContent, BorderLayout.CENTER);
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


    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if(e.getNewValue() instanceof RobotData newData){
            StringBuilder content = new StringBuilder();
            RobotData old = (RobotData)e.getOldValue();
            content.append(localizator.getString("robot.x.coordinate"))
                    .append(" ").append(newData.robotX()).append("\n");
            content.append(localizator.getString("robot.y.coordinate"))
                    .append(" ").append(newData.robotY()).append("\n");
            content.append(localizator.getString("robot.target"))
                    .append(" ").append(newData.robotDir()).append("\n");
            content.append(localizator.getString("robot.angle"))
                    .append(" ").append(Math.atan2(newData.targY()-newData.robotY(), newData.targX()-newData.robotX()));
            infoContent.setText(content.toString());
            infoContent.invalidate();
        }
    }
}

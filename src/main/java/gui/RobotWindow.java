package gui;

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
        super("Актуальная информация о роботе", true, true, true, true);
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
            content.append("Координата [Х]: ").append(newData.robotX()).append("\n");
            content.append("Координата [Y]: ").append(newData.robotY()).append("\n");
            content.append("Направление робота: ").append(newData.robotDir()).append("\n");
            content.append("Угол до цели: ").append(Math.atan2(newData.targY()-newData.robotY(),
                    newData.targX()-newData.robotX()));
            infoContent.setText(content.toString());
            infoContent.invalidate();
        }
    }
}

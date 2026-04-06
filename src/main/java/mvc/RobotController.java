package mvc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс контроллера
 */
public class RobotController {
    /**
     * Модель
     */
    private final RobotModel model;

    /**
     * Конструктор
     */
    public RobotController(RobotModel robotModel){
        model = robotModel;
     }

    /**
     * Установить цель
     */
     public void setTarget(int x, int y){
        model.changeTarget(x,y);
     }
}

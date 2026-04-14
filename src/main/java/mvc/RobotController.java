package mvc;

import log.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс контроллера
 */
public class RobotController {
    /**
     * Таймер
     */
    private final Timer timer = initTimer();
    /**
     * Модель
     */
    private final RobotModel model;
    /**
     * Инициализируем таймер
     */
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    /**
     * Конструктор
     */
    public RobotController(RobotModel robotModel){
        model = robotModel;
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                model.onModelUpdate();
            }
        }, 0, 10);

    }

    /**
     * Установить цель
     */
     public void setTarget(int x, int y){

         model.changeTarget(x,y);
         Logger.sendInfo("Новая цель: x = " + Integer.toString(x) + "; y =" + Integer.toString(y));
     }
}

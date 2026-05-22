package mvc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Модель
 */
public class RobotModel {

    /**
     * Вспомогательный класс для упрощённой регистрации и оповещения слушателей
     */
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Координата робота Х
     */
    private volatile double robotPositionX = 100;

    /**
     * Координата робота У
     */
    private volatile double robotPositionY = 100;

    /**
     * Направление робота
     */
    private volatile double robotDirection = 0;

    /**
     * Координата цели Х
     */
    private volatile int targetPositionX = 150;
    /**
     * Координата цели У
     */
    private volatile int targetPositionY = 100;
    /**
     * Максимальная скорость
     */
    private static final double MAX_VELOCITY = 0.1;

    /**
     * Максимальная угловая скорость
     */
    private static final double MAX_ANGULAR_VELOCITY = 0.001;


    /**
     * Конструктор
     */
    public RobotModel()
    {
        RobotData old = new RobotData(robotDirection,robotPositionX,robotPositionY,targetPositionX,targetPositionY);
        sendData(old);

    }

    /**
     * Добавить слушателя
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
        RobotData oldValue = new RobotData(robotDirection,robotPositionX,
                robotPositionY,targetPositionX,targetPositionY);
        sendData(oldValue);
    }

    /**
     * Удаление слушателя
     */
    public void removePropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    /**
     * Оповестить пользователей
     */
    private void sendData(RobotData oldValue) {
        RobotData newValue = new RobotData(robotDirection,robotPositionX,
                robotPositionY,targetPositionX,targetPositionY);
        propertyChangeSupport.firePropertyChange("RobotData",oldValue,newValue);
    }

    /**
     * Обновить состояние модели
     */
    protected void onModelUpdate() {
        if (distance(targetPositionX, targetPositionY, robotPositionX, robotPositionY) > 1) {
            double velocity = MAX_VELOCITY;
            double angleToTarget = angleTo(robotPositionX, robotPositionY,
                    targetPositionX, targetPositionY);

            double angularVelocity = (isTurnToRight(angleToTarget) ? -1 : 1) * MAX_ANGULAR_VELOCITY;
            moveRobot(velocity, angularVelocity, 20);
        }
    }

    /**
     * Вычислить угол
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * Движение модели
     */
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        RobotData stupidData = new RobotData(robotDirection,robotPositionX,robotPositionY,
                targetPositionX,targetPositionY);
        velocity = applyLimits(velocity, 0, MAX_VELOCITY);
        angularVelocity = applyLimits(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);
        double newX = robotPositionX + velocity / angularVelocity *
                (Math.sin(robotDirection  + angularVelocity * duration) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        }
        double newY = robotPositionY - velocity / angularVelocity *
                (Math.cos(robotDirection  + angularVelocity * duration) -
                        Math.cos(robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        }
        robotPositionX = newX;
        robotPositionY = newY;
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);
        robotDirection = newDirection;

        sendData(stupidData);
    }

    /**
     * Проверка на поворот направо
     */
    public boolean isTurnToRight(double angle){
        if (robotDirection >= 0 && robotDirection < Math.PI) {
            return angle < robotDirection || angle > Math.PI + robotDirection;
        } else {
            return angle < robotDirection && angle > robotDirection - Math.PI;
        }
    }

    /**
     * Находится ли значение в допустимых пределах
     */
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    /**
     * Перевод в радианы
     */
    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    /**
     * Вычисление дистанции
     */
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Изменить координаты цели
     */
    public void changeTarget(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
    }
}

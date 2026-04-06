package mvc;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;

public class RobotModel {
    private final Timer timer = initTimer();

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile double robotPositionX = 100;
    private volatile double robotPositionY = 100;
    private volatile double robotDirection = 0;

    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;

    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

    public RobotModel()
    {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdate();
            }
        }, 0, 10);

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
        RobotData oldValue = new RobotData(robotDirection,robotPositionX,
                robotPositionY,targetPositionX,targetPositionY);
        sendData(oldValue);
    }

    private void sendData(RobotData oldValue) {
        RobotData newValue = new RobotData(robotDirection,robotPositionX,
                robotPositionY,targetPositionX,targetPositionY);
        propertyChangeSupport.firePropertyChange("RobotData",oldValue,newValue);
    }

    private void onModelUpdate() {
        if (distance(targetPositionX, targetPositionY, robotPositionX, robotPositionY) > 1) {
            double velocity = MAX_VELOCITY;
            double angleToTarget = angleTo(robotPositionX, robotPositionY,
                    targetPositionX, targetPositionY);

            double angularVelocity = (isTurnToRight(angleToTarget) ? -1 : 1)
                    * MAX_ANGULAR_VELOCITY;
            moveRobot(velocity, angularVelocity, 20);
        }
    }
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
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
    }
    public boolean isTurnToRight(double angle){
        if (robotDirection >= 0 && robotDirection < Math.PI) {
            return angle < robotDirection || angle > Math.PI + robotDirection;
        } else {
            return angle < robotDirection && angle > robotDirection - Math.PI;
        }
    }
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
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

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public void changeTarget(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
    }
}

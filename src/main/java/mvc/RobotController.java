package mvc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class RobotController {
    private final RobotModel model;
    public RobotController(RobotModel robotModel){
        model = robotModel;
     }
     public void setTarget(int x, int y){
        model.changeTarget(x,y);
     }
}

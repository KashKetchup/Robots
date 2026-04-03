package gui;

import javax.swing.*;
import java.beans.PropertyVetoException;

public class StateConverter {
    public LastWindowState saveState(PreservedWindow window){
        if(window instanceof JInternalFrame w){
            return new LastWindowState(w.getName(),
                    w.getX(), w.getY(), w.getHeight(), w.getWidth(), w.isIcon());
        }
        return new LastWindowState("",-1,-1,-1,-1,false);
    }
    public void loadState(PreservedWindow window, LastWindowState lastWindowState){
        if(window instanceof JInternalFrame w){
            w.setSize( lastWindowState.width() >= 0 ? lastWindowState.width() : w.getWidth(),
                    lastWindowState.height() >= 0 ? lastWindowState.height() : w.getHeight());
            w.setLocation(lastWindowState.x() >= 0 ? lastWindowState.x() : w.getX(),
                    lastWindowState.y() >= 0 ? lastWindowState.y() : w.getY());
            try {
                w.setIcon(lastWindowState.isWindowMinimized());
            } catch (PropertyVetoException e) {}
        }
    }
}

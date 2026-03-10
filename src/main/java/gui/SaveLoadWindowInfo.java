package gui;

import java.util.Map;

public interface SaveLoadWindowInfo {
    LastWindowState saveCurrentState();
    void loadLastState(LastWindowState lastWindowState);
}

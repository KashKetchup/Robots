package gui;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileWriter {
    /**
     * Записать состояния в файл
     */
    public void writeWindowStates(Map<String, LastWindowState> windowStates,
                                  List<String> windowsKeys, String fileName){


        File file;
        if (fileName.isEmpty()) {
            file = new File(System.getProperty("user.home"), "nedoshopa/state.cfg");
        } else {
            file = new File(fileName);
        }
    }
}

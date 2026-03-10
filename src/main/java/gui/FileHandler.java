package gui;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileHandler {


    /**
     * Записать состояния в файл
     */
    public void writeWindowStates(Map<String, LastWindowState> windowStates,
                                  List<String> keys){


        String dir = System.getProperty("user.home") + " nedoshopa/state.cfg";
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(dir));
            for(String key: keys){
                LastWindowState curWindowState = windowStates.getOrDefault(key,
                        new LastWindowState(0,0,0,0,0,false));
                writer.write(convertWindowState(curWindowState));
            }
            writer.flush();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    private String convertWindowState(LastWindowState w){
        return new String(w.x()+" "+w.y()+" "+w.width()+" "+w.height()+" "+w.isWindowMinimized());

    }
}


package gui;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.UIManager.put;

public class FileHandler {
    /**
     * Паттерн для чисел
     */
    private final Pattern numPattern = Pattern.compile("-?\\d+");

    /**
     * Записать состояния в файл
     */
    public void writeWindowStates(Map<String, LastWindowState> windowStates) {
        String dir = System.getProperty("user.home") + "/nedoshopa/state.cfg";
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dir));
            for (String key : windowStates.keySet()) {
                LastWindowState curWindowState = windowStates.getOrDefault(key,
                        new LastWindowState(0, 0, 0, 0, false));
                writer.write(convertWindowState(curWindowState) + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Считать из файла предыдущие состояния
     */
    public Map<String, LastWindowState> readWindowStates(List<String> keys) throws IOException {
        String dir = System.getProperty("user.home") + "/nedoshopa/state.cfg";
        Map<String, LastWindowState> result = new HashMap<>();
        File file = new File(dir);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dir));
            if (file.exists()) {
                for (String key : keys) {
                    String in = bufferedReader.readLine();
                    if (in != null) {
                        String[] tmp = in.split("\\s");
                        result.put(key, new LastWindowState(
                                toInt(tmp[0]), toInt(tmp[1]), toInt(tmp[2]), toInt(tmp[3]),
                                tmp[4] == "1" ? true : false));
                    } else {
                        result.put(key, new LastWindowState(-1, -1, -1, -1, false));
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException(
                    "Couldn't read state from file: " + e.getMessage());
        }
        return result;
    }

    /**
     * Конвертировать в Int
     */
    private int toInt(String num) {
        return Integer.parseInt(num);
    }

    /**
     * Конвертировать из LastWindowState в String
     */
    private String convertWindowState(LastWindowState w) {
        return new String(w.x() + " " + w.y() + " " + w.height() + " " + w.width() + " " + (w.isWindowMinimized() ? 1 : 0));
    }
}


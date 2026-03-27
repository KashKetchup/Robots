package gui;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для записи в файл и чтения из него
 */
public class FileHandler {
    /**
     * Директория для конфигурационного файла
     */
    private final String dir = System.getProperty("user.home") + "/nedoshopa";
    /**
     * Паттерн для чисел
     */
    private final Pattern windowPattern = Pattern.compile(
            "(\\D+) (-?\\d+) (-?\\d+) (-?\\d+) (-?\\d+) (-?\\d+)");

    /**
     * Записать состояния в файл
     */
    public void writeWindowStates(Collection<PreservedWindow> windows) {
        File file = new File(dir);
        if (!file.exists()) {
            try {
                file.mkdirs();
                Files.createFile(Path.of(dir+"/state.cfg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dir+"/state.cfg"));
            for (PreservedWindow state : windows) {
                writer.write(convertWindowState(state.saveCurrentState()) + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Считать из файла предыдущие состояния
     */
    public List<LastWindowState> readWindowStates() throws IOException {
        String fileDir = dir+"/state.cfg";
        List<LastWindowState> result = new ArrayList<>();
        File file = new File(fileDir);
        if(file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileDir));
                if (file.exists()) {
                        String in = bufferedReader.readLine();
                        while (in != null) {
                            Matcher m = windowPattern.matcher(in);
                            if(m.find()){
                                result.add(new LastWindowState(m.group(1),toInt(m.group(2)),
                                        toInt(m.group(3)),toInt(m.group(4)),toInt(m.group(5)),
                                        m.group(6).equals("1") ? true : false ));
                            }
                            in = bufferedReader.readLine();
                        }
                }
            } catch (IOException e) {
                throw new IOException(
                        "Couldn't read state from file: " + e.getMessage());
            }
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
        return new String(w.windowName()+" "+w.x() + " " + w.y() + " " + w.height() + " "
                + w.width() + " " + (w.isWindowMinimized() ? 1 : 0));
    }
}


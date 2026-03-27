package gui;
/**
 * Класс для хранения состояния окна
 */
public record LastWindowState (String windowName,int x,int y, int height, int width,boolean isWindowMinimized){ };

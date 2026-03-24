package gui;

/**
 * Интерфейс для сохраняемых окон
 */
public interface PreservedWindow {
    /**
     * Сохранить текущее состояние окна
     */
    LastWindowState saveCurrentState();

    /**
     * Подгрузить состояние окна
     */
    void loadLastState(LastWindowState lastWindowState);
}

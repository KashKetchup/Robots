package localizator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;

public class Localizator {

    /**
     * Экземпляр локализатора
     */
    private final static Localizator instance = new Localizator();

    /**
     * Имя локализации по умолчанию
     */
    private  final String DEFAULT_LOCALE = "";

    /**
     * Текущая локализация
     */
    private Locale currentLocale;

    /**
     * Набор ресурсов
     */
    private ResourceBundle resourcesBunlde;

    /**
     * Оповещатель подписчиков
     */
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private Localizator() {
        resourcesBunlde = ResourceBundle.getBundle(DEFAULT_LOCALE);
        currentLocale = Locale.of("");
    }

    /**
     * Получить экземпляр локализатора
     */
    public static Localizator getInstance() {
        return instance;
    }

    /**
     * Добавить слушателя свойства
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Удалить слушателя свойства
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Поменять локаль
     */
    public void changeLocale(Locale locale) {
        Locale oldLocale = currentLocale;
        currentLocale = locale;
        resourcesBunlde = ResourceBundle.getBundle(DEFAULT_LOCALE, currentLocale);
        sendData(oldLocale);
    }

    /**
     * Оповестить всех о смнене локализации
     */
    private void sendData(Locale oldLocale) {
        changeSupport.firePropertyChange("language",
                oldLocale, currentLocale);
    }
    public String getString(String key){
        return null;
    }
}

package localizator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Класс локализатор
 */
public class Localizator {

    /**
     * Экземпляр локализатора
     */
    private final static Localizator instance = new Localizator();

    /**
     * Имя локализации по умолчанию
     */
    private  final String DEFAULT_LOCALE = "text";

    /**
     * Текущая локализация
     */
    private Locale currentLocale;

    /**
     * Набор ресурсов
     */
    private ResourceBundle resourcesBundle;

    /**
     * Оповещатель подписчиков
     */
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private Localizator() {
        resourcesBundle = ResourceBundle.getBundle(DEFAULT_LOCALE);
        currentLocale = null;
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
        resourcesBundle = ResourceBundle.getBundle(DEFAULT_LOCALE, currentLocale);
        sendData(oldLocale);
    }

    /**
     * Оповестить всех о смене локализации
     */
    private void sendData(Locale oldLocale) {
        changeSupport.firePropertyChange("LocaleChange",
                oldLocale, currentLocale);
    }

    /**
     * Получение строчки по ключу в соотвествии с текущей локализацией
     */
    public String getString(String key){
        return resourcesBundle.getString(key);
    }
}

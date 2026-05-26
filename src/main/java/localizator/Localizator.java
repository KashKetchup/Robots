package localizator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Класс локализатор
 */
public class Localizator {

    /**
     * Кэш для хранения шаблонов
     */
    private final Map<Locale, Map<String, MessageFormat>> globalCache = new HashMap<>();

    /**
     * Экземпляр локализатора
     */
    private final static Localizator instance = new Localizator();

    /**
     * Имя локализации по умолчанию
     */
    private final String DEFAULT_LOCALE = "text";

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

    /**
     * Приватный конструктор
     */
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
     * Получение имени текущей Локализации
     */
    public String getLocaleName(){
        if(currentLocale != null){
        return currentLocale.toString();
        }
        return "default";
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
        Map<String, MessageFormat> localeCache = globalCache.get(currentLocale);
        if (localeCache == null) {
            localeCache = new HashMap<String, MessageFormat>();
            globalCache.put(currentLocale, localeCache);
        }
        MessageFormat messageFormat = localeCache.get(key);
        if (messageFormat == null) {
            messageFormat = new MessageFormat(resourcesBundle.getString(key));
            localeCache.put(key, messageFormat);
        }
        return messageFormat.format(null);
    }
}

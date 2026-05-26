package gui;

import localizator.Localizator;
import log.Logger;
import mvc.RobotController;
import mvc.RobotData;
import mvc.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainApplicationFrame extends JFrame implements PreservedWindow, PropertyChangeListener
{
    /**
     * Экземпляр локализатора
     */
    private final static Localizator localizator = Localizator.getInstance();

    /**
    * Словарь для Сохраняемых окон
    */
    private final Map<String,PreservedWindow> preservedWindows =  new HashMap<>();

    /**
     * Класс для чтения/записи состояний
     */
    private final FileHandler fileHandler;

    /**
     * Главная панель
     */
    private final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * Конструктор класса
     */
    public MainApplicationFrame(FileHandler newFileHandler ) throws IOException {
    	fileHandler = newFileHandler;
        setScreenSize();
        LogWindow logWindow = createLogWindow();
        localizator.addPropertyChangeListener(this);
        RobotWindow robotWindow = new RobotWindow();
        RobotModel robotModel = new RobotModel();
        robotModel.addPropertyChangeListener(robotWindow);
        addWindow(robotWindow);
        addWindow(logWindow);
        logWindow.setName("logWindow");
        GameWindow gameWindow = new GameWindow(robotModel);
        gameWindow.setSize(400,  400);
        gameWindow.setName("gameWindow");
        addWindow(gameWindow);
        this.setName("mainFrame");
        setRusButtons();
        readWindows();
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        readStates();
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
                confrimDialog();
        	}
        });
    }

    /**
     * Записываем все окна из MainFrame
     */
    private void readWindows(){
        JInternalFrame[] allFrames = desktopPane.getAllFrames();
        for(JInternalFrame frame : allFrames){
            if(frame instanceof PreservedWindow newFrame) {
                preservedWindows.put(frame.getName(), newFrame);
            }
        }
        preservedWindows.put(this.getName(), this);
    }
    /**
     * Считать состояния из файла
     */
    private void readStates(){
        try{
            List<LastWindowState> windowStates = fileHandler.readWindowStates();
            int i = 0;
            for(LastWindowState state : windowStates ){
                if(preservedWindows.containsKey(state.windowName())){
                    preservedWindows.get(state.windowName()).loadLastState(state);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Записать состояния в файл
     */
    private void recordStates(){

        fileHandler.writeWindowStates(preservedWindows.values(),localizator.getLocaleName());
    }
    /**
     * Устанавливаем новый текст для кнопок YES/NO
     */
    private void setRusButtons(){
        UIManager.put("OptionPane.yesButtonText",localizator.getString("button.confrim"));
        UIManager.put("OptionPane.noButtonText",localizator.getString("button.reject"));
    }

    /**
     * Создаём для пользователя окошко выхода
     */
    private void confrimDialog(){
        int result = JOptionPane.showConfirmDialog(
                MainApplicationFrame.this,
                 localizator.getString("dialog.confrim.exit"),
                localizator.getString("dialog.exit"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            recordStates();
            shutDownFunc();
        }
    }
    /**
     * Завершаем работу приложения
     */
    private void shutDownFunc() {
		this.dispose();
		System.exit(0);
    }
    /**
     * Устанавливаем размеры окна
     */
    private void setScreenSize() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);
        setContentPane(desktopPane);
    }
    /**
     * Функция создания нового окна для Логов
     */
    private LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(localizator.getString("log.working"));

        return logWindow;
    }
    /**
     * Функция добавления нового фрейма к desktopPane
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    /**
     * Создаём меню
     */
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createLocaleMenu());
        menuBar.add(exitMenuCreator());
        return menuBar;
    }
    /**
     * Создаём LookAndFeelMenu 
     */
    private JMenu createLookAndFeelMenu() {
        
        JMenu lookAndFeelMenu = new JMenu(localizator.getString("menu.view.mode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(localizator.getString("menu.manage.view"));
        
        JMenuItem systemLookAndFeel = new JMenuItem(localizator.getString("menu.sys.scheme"), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem(localizator.getString(
                "menu.unviversal.scheme"), KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);
        return lookAndFeelMenu;
    }
    /**
     * Создаём TestMenu  
     */
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu( localizator.getString("menu.tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                localizator.getString("menu.tests.comands"));
        
        JMenuItem addLogMessageItem = new JMenuItem(localizator.getString("menu.message.in.log"), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug(localizator.getString("log.new.line"));
        });
        testMenu.add(addLogMessageItem);
        return testMenu;
    }
    /**
     * Создаём меню для смены локализации
     */
    private JMenu createLocaleMenu() {
        JMenu localeMenu = new JMenu( localizator.getString("menu.locales"));
        localeMenu.setMnemonic(KeyEvent.VK_G);
        localeMenu.getAccessibleContext().setAccessibleDescription(
                localizator.getString("menu.locales.info"));

        JMenuItem addSecLocaleItem = new JMenuItem(localizator.getString("menu.locales.en"), KeyEvent.VK_E);
        addSecLocaleItem.addActionListener((event) -> {
            Locale locale = Locale.of("en","US");
            localizator.changeLocale(locale);
        });

        JMenuItem addLocaleItem = new JMenuItem(localizator.getString("menu.locales.ru"), KeyEvent.VK_R);
        addLocaleItem.addActionListener((event) -> {
            Locale locale = Locale.of("ru","RU");
            localizator.changeLocale(locale);
        });
        localeMenu.add(addLocaleItem);
        localeMenu.add(addSecLocaleItem);
        return localeMenu;
    }
    /**
     * Создаём exitMenu 
     */
    private JMenu exitMenuCreator() {
    	JMenu exitMenu = new JMenu(localizator.getString("menu.terminate.session"));
    	exitMenu.getAccessibleContext().setAccessibleDescription(localizator.getString("menu.gout"));
    	JMenuItem exitItem = new JMenuItem(localizator.getString("menu.exit"), KeyEvent.VK_E);
    	exitItem.addActionListener((event) -> {
    	    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
    	        new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    	});
    	exitMenu.add(exitItem);
    	return exitMenu;
    }
    /**
     * Устанавливаем внешний вид приложения 
     */
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    @Override
    public LastWindowState saveCurrentState() {
        return new LastWindowState(this.getName(),this.getX(),this.getY(),this.getHeight(),this.getWidth(),
                (this.getExtendedState() & JFrame.ICONIFIED) == JFrame.ICONIFIED);
    }

    @Override
    public void loadLastState(LastWindowState lastWindowState) {
        this.setSize( lastWindowState.width() >= 0 ? lastWindowState.width() : this.getWidth(),
                lastWindowState.height() >= 0 ? lastWindowState.height() : this.getHeight());
        this.setLocation(lastWindowState.x() >= 0 ? lastWindowState.x() : this.getX(),
                lastWindowState.y() >= 0 ? lastWindowState.y() : this.getY());
        if (lastWindowState.isWindowMinimized()) {
            this.setExtendedState(JFrame.ICONIFIED);
        } else {
            this.setExtendedState(JFrame.NORMAL);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("LocaleChange")){
            recordStates();
            this.getContentPane().removeAll();
            setScreenSize();
            LogWindow logWindow = createLogWindow();
            RobotWindow robotWindow = new RobotWindow();
            RobotModel robotModel = new RobotModel();
            robotModel.addPropertyChangeListener(robotWindow);
            addWindow(robotWindow);
            addWindow(logWindow);
            logWindow.setName("logWindow");
            GameWindow gameWindow = new GameWindow(robotModel);
            gameWindow.setSize(400,  400);
            gameWindow.setName("gameWindow");
            addWindow(gameWindow);
            this.setName("mainFrame");
            setRusButtons();
            readWindows();
            setJMenuBar(generateMenuBar());
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            readStates();
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    confrimDialog();
                }
            });
        }
    }
}

package gui;

import log.Logger;
import mvc.RobotController;
import mvc.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainApplicationFrame extends JFrame implements PreservedWindow
{   /**
    * Словарь для Сохраняемых окон
    */
    private final Map<String,PreservedWindow> preservedWindows =  new HashMap<>();
    /**
     * Класс для чтения/записи состояний
     */
    private final FileHandler fileHandler = new FileHandler();
    /**
     * Главная панель
     */
    private final JDesktopPane desktopPane = new JDesktopPane();
    /**
     * Конструктор класса
     */
    public MainApplicationFrame() throws IOException {
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
        fileHandler.writeWindowStates(preservedWindows.values());
    }
    /**
     * Устанавливаем новый текст для кнопок YES/NO
     */
    private void setRusButtons(){
        UIManager.put("OptionPane.yesButtonText","Да");
        UIManager.put("OptionPane.noButtonText","Нет");
    }

    /**
     * Создаём для пользователя окошко выхода
     */
    private void confrimDialog(){
        int result = JOptionPane.showConfirmDialog(
                MainApplicationFrame.this,
                "Вы точно хотите выйти?",
                "Сообщение о выходе",
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
        Logger.debug("Протокол работает");

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
        menuBar.add(exitMenuCreator());
        return menuBar;
    }
    /**
     * Создаём LookAndFeelMenu 
     */
    private JMenu createLookAndFeelMenu() {
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
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
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);
        return testMenu;
    }
    /**
     * Создаём exitMenu 
     */
    private JMenu exitMenuCreator() {
    	JMenu exitMenu = new JMenu("Завершение Сессии");
    	exitMenu.getAccessibleContext().setAccessibleDescription(
                "Выйти");
    	JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_E);
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
}

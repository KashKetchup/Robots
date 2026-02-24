package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    /**
     * Конструктор класса
     */
    public MainApplicationFrame() {
    	setScreenSize();
    	generateWindows();
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		UIManager.put("OptionPane.yesButtonText","Да");
        		UIManager.put("OptionPane.noButtonText","Нет");
                int result = JOptionPane.showConfirmDialog(
                		MainApplicationFrame.this, 
                        "Вы точно хотите выйти?",
                        "Сообщение о выходе",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
        		if (result == JOptionPane.YES_OPTION) {
        			shutDownFunc();
        		}
        	}
        });
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
     * Создаём окна
     */
    private void generateWindows() {
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);
    }
    /**
     * Функция создания нового окна для Логов
     */
    protected LogWindow createLogWindow()
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
}

package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class LogWindow extends JInternalFrame implements LogChangeListener, PreservedWindow {
    private LogWindowSource logSource;
    private TextArea logContent;

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);
        this.logContent = new TextArea("");
        this.logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public LastWindowState saveCurrentState() {
        return new LastWindowState(getX(), getY(), getHeight(), getWidth(), isIcon());
    }

    @Override
    public void loadLastState(LastWindowState lastWindowState) {
        this.setSize(lastWindowState.width() >= 0 ? lastWindowState.width() : this.getWidth(),
                lastWindowState.height() >= 0 ? lastWindowState.height() : this.getHeight());
        this.setLocation(lastWindowState.x() >= 0 ? lastWindowState.x() : this.getX(),
                lastWindowState.y() >= 0 ? lastWindowState.y() : this.getY());
        try {
            this.setIcon(lastWindowState.isWindowMinimized());
        } catch (PropertyVetoException e) {
        }
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}

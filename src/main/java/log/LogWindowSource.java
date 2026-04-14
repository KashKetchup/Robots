package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{
    private int queueLength;
    
    private QueueLog messages;
    private final ArrayList<LogChangeListener> listeners;
    private volatile LogChangeListener[] activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        queueLength = iQueueLength;
        messages = new QueueLog(iQueueLength);
        listeners = new ArrayList<>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(listeners)
        {
            listeners.add(listener);
            activeListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(listeners)
        {
            listeners.remove(listener);
            activeListeners = null;
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        messages.addNewNode(entry);
        LogChangeListener [] activeListeners = this.activeListeners;
        if (activeListeners == null)
        {
            synchronized (listeners)
            {
                if (this.activeListeners == null)
                {
                    activeListeners = listeners.toArray(new LogChangeListener [0]);
                    this.activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }
    
    public int size()
    {
        return messages.getSize();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= messages.getSize())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.getSize());
        return messages.getSubList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return messages;
    }
}

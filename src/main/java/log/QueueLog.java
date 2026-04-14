package log;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Класс для очереди сообщений
 */
public class QueueLog implements Iterable<LogEntry> {

    /**
     * Ссылка на первый узел
     */
    private volatile Node head;

    /**
     * Ссылка на последний узел
     */
    private volatile Node tail;

    /**
     * Текущий размер очереди
     */
    private volatile int currentLength;

    /**
     * Максимальный размер очереди
     */
    private final int maxLength;

    /**
     * Конструктор класса
     */
    public QueueLog(int maxLength) {
        this.head = null;
        this.tail = null;
        this.maxLength = (maxLength > 0 ? maxLength : 0);
        this.currentLength = 0;
    }

    /**
     * Получить текущую длину очереди
     */
    public synchronized int getSize() {
        return this.currentLength;
    }

    /**
     * Добавить запись в очередь
     */
    public synchronized void addNewNode(LogEntry entry) {
        Node newNode = new Node(entry);
        currentLength++;
        if (head == null) {
            this.tail = newNode;
            this.head = newNode;
        } else {
            if (currentLength > maxLength) {
                removeOldNode();
            }
            this.tail.setNextElem(newNode);
            this.tail = newNode;
        }
    }

    /**
     * Вытеснение старого узла
     */
    private synchronized void removeOldNode() {
        if(this.currentLength > 0){
            this.currentLength--;
            if( currentLength == 0){
                this.head = null;
                this.tail = null;
            }else{
                Node oldNode = this.head;
                this.head = oldNode.getNextElem();
                oldNode.setNextElem(null);
            }
        }
    }

    /**
     * Получить записи из указанного диапазона
     */
    public synchronized List<LogEntry> getSubList(int start, int finish) {
        if( start >= 0 && finish >= 0 && start <= finish){
            int entriesCount = finish - start;
            LogEntry[] entries = new LogEntry[entriesCount];
            Node currentNode = head;
            for (int i = 0; i < start; ++i) {
                currentNode = currentNode.getNextElem();
            }
            for (int i = 0; i < entriesCount; ++i) {
                entries[i] = currentNode.getData();
                currentNode = currentNode.getNextElem();
            }
            return List.of(entries);
        }
        return List.of();
    }

    @Override
    public Iterator<LogEntry> iterator() {
        return new Iterator<LogEntry> () {
            /**
             * Ссылка на хвост
             */
            private volatile Node currentNode = head;

            @Override
            public synchronized boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public synchronized LogEntry next() {
                if (!hasNext()) {
                    currentNode = tail;
                }
                if (hasNext()) {
                    LogEntry currentEntry = currentNode.getData();
                    currentNode = currentNode.getNextElem();
                    return currentEntry;
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Изменения запрещены");
            }
        };
    }
    /**
     * Класс узлов очереди
     */
    private class Node {
        /**
         * Ссылка на следующий узел
         */
        private Node nextElem;
        /**
         * Экземпляр класса LogEntry
         */
        private final LogEntry data;

        /**
         * Конструктор класса
         */
        private Node(LogEntry data) {
            this.data = data;
            this.nextElem = null;
        }

        /**
         * Установить следующий элемент
         */
        public synchronized void setNextElem(Node newNextElem){
            this.nextElem = newNextElem;
        }

        /**
         * Получить ссылку на следующий узел
         */
        public Node getNextElem(){
            return this.nextElem;
        }

        /**
         * Получить LogEntry
         */
        public LogEntry getData(){
            return this.data;
        }

    }


}

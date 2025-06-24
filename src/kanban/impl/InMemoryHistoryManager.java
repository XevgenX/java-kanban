package kanban.impl;

import kanban.HistoryManager;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList<Task> history;

    @Override
    public void add(Task task) {
        if (history == null) {
            history = new LinkedList<>();
        }
        if (history.size() == HISTORY_LIMIT) {
            history.pollLast();
        }
        history.offerFirst(cloneTask(task));
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    private Task cloneTask(Task task) {
        if (task instanceof SubTask) {
            return new SubTask((SubTask) task);
        } else if (task instanceof Epic) {
            return new Epic((Epic) task);
        } else {
            return new Task(task);
        }
    }
}

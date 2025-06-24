package kanban;

import kanban.model.Task;

import java.util.List;

public interface HistoryManager {
    int HISTORY_LIMIT = 10;
    void add(Task task);
    List<Task> getHistory();
}

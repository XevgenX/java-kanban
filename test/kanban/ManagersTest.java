package kanban;

import kanban.impl.InMemoryHistoryManager;
import kanban.impl.InMemoryTaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    @DisplayName("должен возвращать правильную реализацию TaskManager по умолчанию")
    public void shouldReturnCorrectDefaultTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertInstanceOf(InMemoryTaskManager.class, manager);
    }

    @Test
    @DisplayName("должен возвращать правильную реализацию HistoryManager по умолчанию")
    public void shouldReturnCorrectDefaultHistoryManager() {
        HistoryManager manager = Managers.getDefaultHistory();
        assertInstanceOf(InMemoryHistoryManager.class, manager);
    }
}
package kanban.impl;

import kanban.HistoryManager;
import kanban.Managers;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager manager;

    @BeforeEach
    public void setUp() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    @DisplayName("После сохранения задачи, ее можно получить")
    public void shouldReturnAddTask() {
        Task task = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        manager.add(task);
        Task savedInHistoryTask = manager.getHistory().get(0);
        assertEquals("Внести правки по комментариям", savedInHistoryTask.getTitle());
        assertEquals("Правки проверяем по чату", savedInHistoryTask.getDescription());
    }

    @Test
    @DisplayName("После сохранения эпика, его можно получить")
    public void shouldReturnAddEpic() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        manager.add(epic);
        Task savedInHistoryEpic = manager.getHistory().get(0);
        assertEquals("Сделать финальное задание", savedInHistoryEpic.getTitle());
        assertEquals("Постараться ничего не забыть", savedInHistoryEpic.getDescription());
    }

    @Test
    @DisplayName("После сохранения эпика, его можно получить")
    public void shouldReturnAddSubTask() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        manager.add(subTask);
        Task savedInHistoryEpic = manager.getHistory().get(0);
        assertEquals("Сделать коммит", savedInHistoryEpic.getTitle());
        assertEquals("Не забыть про push", savedInHistoryEpic.getDescription());
    }

    @Test
    @DisplayName("После сохранения задачи, можно получить ее старое состояние")
    public void shouldReturnHistoryStateOfAddTask() {
        Task task = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        manager.add(task);
        task.setId(200L);
        task.setTitle("ЧТо то новое");
        task.setDescription("Новое описание");
        Task savedInHistoryTask = manager.getHistory().get(0);
        assertEquals("Внести правки по комментариям", savedInHistoryTask.getTitle());
        assertEquals("Правки проверяем по чату", savedInHistoryTask.getDescription());
    }

    @Test
    @DisplayName("После сохранения эпика, можно получить ее старое состояние")
    public void shouldReturnHistoryStateOfAddEpic() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        manager.add(epic);
        epic.setId(200L);
        epic.setTitle("ЧТо то новое");
        epic.setDescription("Новое описание");
        Task savedInHistoryEpic = manager.getHistory().get(0);
        assertEquals("Сделать финальное задание", savedInHistoryEpic.getTitle());
        assertEquals("Постараться ничего не забыть", savedInHistoryEpic.getDescription());
    }

    @Test
    @DisplayName("После сохранения подзадачи, можно получить ее старое состояние")
    public void shouldReturnHistoryStateOfAddSubTask() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        manager.add(subTask);
        subTask.setId(200L);
        subTask.setTitle("ЧТо то новое");
        subTask.setDescription("Новое описание");
        Task savedInHistoryEpic = manager.getHistory().get(0);
        assertEquals("Сделать коммит", savedInHistoryEpic.getTitle());
        assertEquals("Не забыть про push", savedInHistoryEpic.getDescription());
    }

    @Test
    @DisplayName("Недавно сохраненные задачи должны быть первыми в списке истории")
    public void shouldReturnSavedTasksInCorrectOrder() {
        Task task = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        manager.add(task);
        Task task2 = new Task("Внести правки по комментариям 1", "Правки проверяем по чату 1");
        manager.add(task2);
        Task savedInHistoryTask = manager.getHistory().get(0);
        assertEquals("Внести правки по комментариям 1", savedInHistoryTask.getTitle());
        assertEquals("Правки проверяем по чату 1", savedInHistoryTask.getDescription());
    }

    @Test
    @DisplayName("Недавно сохраненные задачи должны быть первыми в списке истории")
    public void shouldRemoveOlderstElementWhen11ElementAdded() {
        manager.add(new Task("Внести правки по комментариям", "Правки проверяем по чату"));
        manager.add(new Task("Внести правки по комментариям 1", "Правки проверяем по чату 1"));
        manager.add(new Task("Внести правки по комментариям 2", "Правки проверяем по чату 2"));
        manager.add(new Task("Внести правки по комментариям 3", "Правки проверяем по чату 3"));
        manager.add(new Task("Внести правки по комментариям 4", "Правки проверяем по чату 4"));
        manager.add(new Task("Внести правки по комментариям 5", "Правки проверяем по чату 5"));
        manager.add(new Task("Внести правки по комментариям 6", "Правки проверяем по чату 6"));
        manager.add(new Task("Внести правки по комментариям 7", "Правки проверяем по чату 7"));
        manager.add(new Task("Внести правки по комментариям 8", "Правки проверяем по чату 8"));
        manager.add(new Task("Внести правки по комментариям 9", "Правки проверяем по чату 9"));
        manager.add(new Task("Внести правки по комментариям 10", "Правки проверяем по чату 10"));
        assertEquals(10, manager.getHistory().size());
        Task savedInHistoryTask = manager.getHistory().get(0);
        assertEquals("Внести правки по комментариям 10", savedInHistoryTask.getTitle());
        assertEquals("Правки проверяем по чату 10", savedInHistoryTask.getDescription());
        savedInHistoryTask = manager.getHistory().get(9);
        assertEquals("Внести правки по комментариям 1", savedInHistoryTask.getTitle());
        assertEquals("Правки проверяем по чату 1", savedInHistoryTask.getDescription());
    }
}
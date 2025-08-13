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
    @DisplayName("Должен заменять существующие элементы по id")
    public void shouldReplaceDuplicatedTask() {
        Task task = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        task.setId(1L);
        manager.add(task);
        Task task2 = new Task("Внести правки по комментариям 1", "Правки проверяем по чату 1");
        task.setId(1L);
        manager.add(task2);
        Task savedInHistoryTask = manager.getHistory().get(0);
        assertEquals("Внести правки по комментариям 1", savedInHistoryTask.getTitle());
        assertEquals("Правки проверяем по чату 1", savedInHistoryTask.getDescription());
    }

    @Test
    @DisplayName("Должен заменять существующие элементы по id")
    public void shouldPlaceReplacedDuplicatedTaskInCorrectOrder() {
        Task task = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        task.setId(1L);
        manager.add(task);
        Task task2 = new Task("Внести правки по комментариям 1", "Правки проверяем по чату 1");
        task2.setId(2L);
        manager.add(task2);
        Task task3 = new Task("Внести правки по комментариям 2", "Правки проверяем по чату 2");
        task3.setId(1L);
        manager.add(task3);
        Task savedInHistoryTask = manager.getHistory().get(0);
        assertEquals("Внести правки по комментариям 2", savedInHistoryTask.getTitle());
        assertEquals("Правки проверяем по чату 2", savedInHistoryTask.getDescription());
    }
}
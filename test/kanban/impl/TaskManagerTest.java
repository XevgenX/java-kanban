package kanban.impl;

import kanban.TaskManager;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    abstract T getManager();

    @Test
    @DisplayName("При сохранении Task возвращается следующий id")
    public void shouldSaveTaskWithGeneratingId() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        Long id = getManager().createSimpleTask(simpleTask);
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("При сохранении Epic возвращается следующий id")
    public void shouldSaveEpicWithGeneratingId() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long id = getManager().createEpic(epic);
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("При сохранении Epic-а, подзадачи тоже сохраняются и получают id")
    public void shouldSaveEpicWithSubTasksWithGeneratingId() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        epic.addSubTask(subTask2);
        Long epicId = getManager().createEpic(epic);
        Epic savedEpic = getManager().getEpicById(epicId).get();
        assertEquals(2L, savedEpic.getSubTasks().get(0).getId());
        assertEquals(3L, savedEpic.getSubTasks().get(1).getId());
    }

    @Test
    @DisplayName("При сохранении SubTask возвращается следующий id")
    public void shouldSaveSubTaskWithGeneratingId() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long epicId = getManager().createEpic(epic);
        Epic savedEpic = getManager().getEpicById(epicId).get();
        SubTask subTask = new SubTask(savedEpic, "Сделать коммит", "Не забыть про push");
        Long id = getManager().createSubTask(subTask);
        assertEquals(2L, id);
    }

    @Test
    @DisplayName("Не сохраняется SubTask для несуществующего epic-а")
    public void shouldNotSaveSubTaskWithNonExistingEpic() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        Long id = getManager().createSubTask(subTask);
        assertNull(id);
    }

    @Test
    @DisplayName("При сохранении Task с выставленным вручную id, id меняется на правильный")
    public void shouldOverrideManuallyAddedIdWhenCreateTask() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setId(10L);
        Long id = getManager().createSimpleTask(simpleTask);
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("При сохранении Epic с выставленным вручную id, id меняется на правильный")
    public void shouldOverrideManuallyAddedIdWhenCreateEpic() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        epic.setId(10L);
        Long id = getManager().createEpic(epic);
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("При сохранении SubTask с выставленным вручную id, id меняется на правильный")
    public void shouldOverrideManuallyAddedIdWhenCreateSubTask() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long epicId = getManager().createEpic(epic);
        Epic savedEpic = getManager().getEpicById(epicId).get();
        SubTask subTask = new SubTask(savedEpic, "Сделать коммит", "Не забыть про push");
        subTask.setId(10L);
        Long id = getManager().createSubTask(subTask);
        assertEquals(2L, id);
    }

    @Test
    @DisplayName("Сохраненный Task можно получен по id")
    public void shouldReturnSavedTaskById() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        Long id = getManager().createSimpleTask(simpleTask);
        Task savedTask = getManager().getTaskById(id).get();
        assertEquals(1L, savedTask.getId());
        assertEquals(simpleTask.getTitle(), savedTask.getTitle());
        assertEquals(simpleTask.getDescription(), savedTask.getDescription());
        assertEquals(TaskStatus.NEW, savedTask.getStatus());
    }

    @Test
    @DisplayName("Сохраненный Epic можно получен по id")
    public void shouldReturnSavedEpicById() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long id = getManager().createEpic(epic);
        Epic savedEpic = getManager().getEpicById(id).get();
        assertEquals(1L, savedEpic.getId());
        assertEquals(epic.getTitle(), savedEpic.getTitle());
        assertEquals(epic.getDescription(), savedEpic.getDescription());
        assertEquals(TaskStatus.NEW, savedEpic.getStatus());
    }

    @Test
    @DisplayName("Сохраненный SubTask можно получен по id")
    public void shouldReturnSavedSubTaskById() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long epicId = getManager().createEpic(epic);
        Epic savedEpic = getManager().getEpicById(epicId).get();
        SubTask subTask = new SubTask(savedEpic, "Сделать коммит", "Не забыть про push");
        Long id = getManager().createSubTask(subTask);
        SubTask savedSubTask = getManager().getSubTaskById(id).get();
        assertEquals(2L, savedSubTask.getId());
        assertEquals(subTask.getTitle(), savedSubTask.getTitle());
        assertEquals(subTask.getDescription(), savedSubTask.getDescription());
        assertEquals(TaskStatus.NEW, savedSubTask.getStatus());
    }

    @Test
    @DisplayName("Должен сохранятся новый объект Task для неизменяемости")
    public void shouldSaveNewInstanceOfTaskForImmutability() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        Long id = getManager().createSimpleTask(simpleTask);
        simpleTask.setTitle("Что то новое");
        simpleTask.setDescription("Обновленное описание");
        simpleTask.setId(200L);
        simpleTask.tryToMmoveToDone();
        Task savedTask = getManager().getTaskById(id).get();
        assertEquals(1L, savedTask.getId());
        assertEquals("Внести правки по комментариям", savedTask.getTitle());
        assertEquals("Правки проверяем по чату", savedTask.getDescription());
        assertEquals(TaskStatus.NEW, savedTask.getStatus());
    }

    @Test
    @DisplayName("Должен сохранятся новый объект Epic для неизменяемости")
    public void shouldSaveNewInstanceOfEpicForImmutability() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long id = getManager().createEpic(epic);
        epic.setTitle("Что то новое");
        epic.setDescription("Обновленное описание");
        epic.setId(200L);
        epic.tryToMmoveToDone();
        Epic savedEpic = getManager().getEpicById(id).get();
        assertEquals(1L, savedEpic.getId());
        assertEquals("Сделать финальное задание", savedEpic.getTitle());
        assertEquals("Постараться ничего не забыть", savedEpic.getDescription());
        assertEquals(TaskStatus.NEW, savedEpic.getStatus());
    }

    @Test
    @DisplayName("Должен сохранятся новый объект SubTask для неизменяемости")
    public void shouldSaveNewInstanceOfSubTaskForImmutability() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long epicId = getManager().createEpic(epic);
        Epic savedEpic = getManager().getEpicById(epicId).get();
        SubTask subTask = new SubTask(savedEpic, "Сделать коммит", "Не забыть про push");
        Long id = getManager().createSubTask(subTask);
        subTask.setTitle("Что то новое");
        subTask.setDescription("Обновленное описание");
        subTask.setId(200L);
        subTask.tryToMmoveToDone();
        subTask.setEpic(null);
        SubTask savedSubTask = getManager().getSubTaskById(id).get();
        assertEquals(2L, savedSubTask.getId());
        assertEquals("Сделать коммит", savedSubTask.getTitle());
        assertEquals("Не забыть про push", savedSubTask.getDescription());
        assertEquals(1L, savedSubTask.getEpic().getId());
        assertEquals(TaskStatus.NEW, savedSubTask.getStatus());
    }

    @Test
    @DisplayName("Должен возвращать приоритизированный список задач")
    public void shouldGetPrioritizedTasksCorrectly() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(1997, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(1995, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(2000, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        List<Task> prioritizedTasks = getManager().getPrioritizedTasks();
        assertEquals(2L, prioritizedTasks.get(0).getId());
        assertEquals(1L, prioritizedTasks.get(1).getId());
        assertEquals(3L, prioritizedTasks.get(2).getId());
    }

    @Test
    @DisplayName("Задачи без даты начала не должны попадать в приоритизированный список задач")
    public void shouldIgnoreTasksWithoutStartDateInGetPrioritizedTasksCorrectly() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(1997, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        getManager().createSimpleTask(simpleTask);
        simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(2000, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        List<Task> prioritizedTasks = getManager().getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size());
    }

    @Test
    @DisplayName("Должен возвращать приоритизированный список задач после обновления задачи")
    public void shouldGetPrioritizedTasksCorrectlyAfterUpdate() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(1997, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(1995, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(2000, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        simpleTask = getManager().getTaskById(1L).get();
        simpleTask.setStartTime(LocalDateTime.of(1987, 11, 8, 14, 30));
        getManager().updateTask(simpleTask);
        List<Task> prioritizedTasks = getManager().getPrioritizedTasks();
        assertEquals(1L, prioritizedTasks.get(0).getId());
        assertEquals(2L, prioritizedTasks.get(1).getId());
        assertEquals(3L, prioritizedTasks.get(2).getId());
    }

    @Test
    @DisplayName("Должен выбрасывать исключение при пересечении интервалов")
    public void shouldThrowExceptionWithInvalidIntervals() {
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask.setStartTime(LocalDateTime.of(1997, 11, 8, 14, 30));
        simpleTask.setDuration(Duration.ofMinutes(50));
        getManager().createSimpleTask(simpleTask);
        final Task simpleTask2 = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        simpleTask2.setStartTime(LocalDateTime.of(1997, 11, 8, 15, 00));
        simpleTask2.setDuration(Duration.ofMinutes(50));
        assertThrows(IllegalArgumentException.class, () -> getManager().createSimpleTask(simpleTask2));
    }
}

package kanban.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    public void setUp() {
        epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        epic.addSubTask(subTask1);
        subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        epic.addSubTask(subTask2);
    }

    @Test
    @DisplayName("epic-и должны быть равны, если равны их id")
    public void shouldBeEqualsWhenIdEquals() {
        Epic epic2 = new Epic("Сделать финальное задание2", "Постараться ничего не забыть2");
        epic.setId(1L);
        epic2.setId(1L);
        assertEquals(epic, epic2);
    }

    @Test
    @DisplayName("epic-и должны быть неравны, если неравны их id")
    public void shouldBeUnEqualsWhenIdUnEquals() {
        Epic epic2 = new Epic("Сделать финальное задание2", "Постараться ничего не забыть2");
        epic.setId(1L);
        epic2.setId(2L);
        assertNotEquals(epic, epic2);
    }

    @Test
    @DisplayName("epic-и должны быть неравны, если у одного из них id = null")
    public void shouldBeUnEqualsWhenOneIdIsNull() {
        Epic epic2 = new Epic("Сделать финальное задание2", "Постараться ничего не забыть2");
        epic.setId(1L);
        assertNotEquals(epic, epic2);
    }

    @Test
    @DisplayName("В epic можно добавить subTask")
    public void shouldAddSubTaskToEpic() {
        epic.clearSubTasks();
        subTask1.setId(1L);
        epic.addSubTask(subTask1);
        assertNotNull(epic.getSubTasks());
        assertEquals(1, epic.getSubTasks().size());
        assertEquals(subTask1, epic.getSubTasks().get(0));
    }

    @Test
    @DisplayName("Когда нет подзадач, после вызова adjustStatus(), статус epic-а NEW")
    public void shouldHaveStatusNewWhenNoSubTasks() {
        epic.adjustStatus();
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Когда подзадачи со статусом NEW, после вызова adjustStatus(), статус epic-а NEW")
    public void shouldHaveStatusNewWhenAllSubTasksNew() {
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        epic.adjustStatus();
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Когда подзадачи со статусом NEW и DONE, после вызова adjustStatus(), статус epic-а IN_PROGRESS")
    public void shouldHaveStatusInProgressWhenNewAndDoneSubTasks() {
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask2.tryToMmoveToDone();
        epic.adjustStatus();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Когда подзадачи со статусом NEW и IN_PROGRESS, после вызова adjustStatus(), статус epic-а IN_PROGRESS")
    public void shouldHaveStatusInProgressWhenNewAndInProgressSubTasks() {
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask2.tryToMoveToInProgress();
        epic.adjustStatus();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Когда подзадачи со статусом DONE и IN_PROGRESS, после вызова adjustStatus(), статус epic-а IN_PROGRESS")
    public void shouldHaveStatusInProgressWhenDoneAndInProgressSubTasks() {
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask1.tryToMmoveToDone();
        subTask2.tryToMoveToInProgress();
        epic.adjustStatus();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Когда подзадачи со статусом IN_PROGRESS, после вызова adjustStatus(), статус epic-а IN_PROGRESS")
    public void shouldHaveStatusInProgressWhenInProgressSubTasks() {
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask1.tryToMoveToInProgress();
        subTask2.tryToMoveToInProgress();
        epic.adjustStatus();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Когда подзадачи со статусом IN_PROGRESS, после вызова adjustStatus(), статус epic-а IN_PROGRESS")
    public void shouldHaveStatusDoneWhenDoneSubTasks() {
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask1.tryToMmoveToDone();
        subTask2.tryToMmoveToDone();
        epic.adjustStatus();
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }
}
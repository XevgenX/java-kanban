package kanban.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;

    @BeforeEach
    public void setUp() {
        task = new Task("Внести правки по комментариям", "Правки проверяем по чату");
    }

    @Test
    @DisplayName("task-и должны быть равны, если равны их id")
    public void shouldBeEqualsWhenIdEquals() {
        Task task2 = new Task("Внести правки по комментариям2", "Правки проверяем по чату2");
        task.setId(1L);
        task2.setId(1L);
        assertEquals(task, task2);
    }

    @Test
    @DisplayName("task-и должны быть неравны, если неравны их id")
    public void shouldBeUnEqualsWhenIdUnEquals() {
        Task task2 = new Task("Внести правки по комментариям2", "Правки проверяем по чату2");
        task.setId(1L);
        task2.setId(2L);
        assertNotEquals(task, task2);
    }

    @Test
    @DisplayName("task-и должны быть неравны, если у одного из них id = null")
    public void shouldBeUnEqualsWhenOneIdIsNull() {
        Task task2 = new Task("Внести правки по комментариям2", "Правки проверяем по чату2");
        task.setId(1L);
        assertNotEquals(task, task2);
    }
}
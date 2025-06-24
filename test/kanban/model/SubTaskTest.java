package kanban.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    public void setUp() {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        subTask = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
    }

    @Test
    @DisplayName("subTask-и должны быть равны, если равны их id")
    public void shouldBeEqualsWhenIdEquals() {
        SubTask subTask2 = new SubTask(epic, "Написать код2", "Внимательно прочитать задание2");
        subTask.setId(1L);
        subTask2.setId(1L);
        assertEquals(subTask, subTask2);
    }

    @Test
    @DisplayName("subTask-и должны быть неравны, если неравны их id")
    public void shouldBeUnEqualsWhenIdUnEquals() {
        SubTask subTask2 = new SubTask(epic, "Написать код2", "Внимательно прочитать задание2");
        subTask.setId(1L);
        subTask2.setId(2L);
        assertNotEquals(subTask, subTask2);
    }

    @Test
    @DisplayName("subTask-и должны быть неравны, если у одного из них id = null")
    public void shouldBeUnEqualsWhenOneIdIsNull() {
        SubTask subTask2 = new SubTask(epic, "Написать код2", "Внимательно прочитать задание2");
        subTask.setId(1L);
        assertNotEquals(subTask, subTask2);
    }
}

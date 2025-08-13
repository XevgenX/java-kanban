package kanban.converter;

import kanban.model.Task;
import kanban.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskConverterTest {
    private TaskConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new TaskConverter();
    }

    @Test
    @DisplayName("должен правильно парсить задачу")
    public void shouldParseCorrectly() {
        Task task = converter.fromCsvLine(new String[]{"1",
                "TASK",
                "Task1",
                "IN_PROGRESS",
                "Description task1"});
        assertEquals(1L, task.getId());
        assertEquals("Task1", task.getTitle());
        assertEquals("Description task1", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    @DisplayName("должен правильно формировать строку")
    public void shouldConvertToStringCorrectly() {
        Task task = new Task(1L, "Task1", "Description task1", TaskStatus.IN_PROGRESS);
        String line = converter.toCsvLine(task);
        assertEquals("1,TASK,Task1,IN_PROGRESS,Description task1,", line);
    }
}

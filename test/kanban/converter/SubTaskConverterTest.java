package kanban.converter;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskConverterTest {
    private SubTaskConverter converter;
    private Epic epic;

    @BeforeEach
    public void setUp() {
        converter = new SubTaskConverter();
        epic = new Epic(2L, "Epic2", "Description epic2", TaskStatus.DONE);
    }

    @Test
    @DisplayName("должен правильно парсить задачу")
    public void shouldParseCorrectly() {
        SubTask subTask = converter.fromCsvLine(new String[]{"3",
                "EPIC",
                "Sub Task2",
                "DONE",
                "Description sub task3"}, epic);
        assertEquals(3L, subTask.getId());
        assertEquals("Sub Task2", subTask.getTitle());
        assertEquals("Description sub task3", subTask.getDescription());
        assertEquals(TaskStatus.DONE, subTask.getStatus());
    }

    @Test
    @DisplayName("должен правильно формировать строку")
    public void shouldConvertToStringCorrectly() {
        SubTask subTask = new SubTask(3L, "Sub Task2", "Description sub task3", TaskStatus.DONE, epic);
        String line = converter.toCsvLine(subTask);
        assertEquals("3,SUBTASK,Sub Task2,DONE,Description sub task3,2", line);
    }
}

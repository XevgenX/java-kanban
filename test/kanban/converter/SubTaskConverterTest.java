package kanban.converter;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskConverterTest {
    private SubTaskConverter converter;
    private Epic epic;

    @BeforeEach
    public void setUp() {
        converter = new SubTaskConverter();
        epic = new Epic(2L, "Epic2", "Description epic2", TaskStatus.DONE, Optional.empty(), Optional.empty());
    }

    @Test
    @DisplayName("должен правильно парсить задачу")
    public void shouldParseCorrectly() {
        SubTask subTask = converter.fromCsvLine(new String[]{"3",
                "EPIC",
                "Sub Task2",
                "DONE",
                "Description sub task3",
                "",
                ""}, epic);
        assertEquals(3L, subTask.getId());
        assertEquals("Sub Task2", subTask.getTitle());
        assertEquals("Description sub task3", subTask.getDescription());
        assertEquals(TaskStatus.DONE, subTask.getStatus());
        assertEquals(Optional.empty(), subTask.getStartTime());
        assertEquals(Optional.empty(), subTask.getEndTime());
        assertEquals(Optional.empty(), subTask.getDuration());
    }

    @Test
    @DisplayName("должен правильно парсить задачу, включая дату старта и длительность")
    public void shouldParseCorrectlyIncludingTimings() {
        SubTask subTask = converter.fromCsvLine(new String[]{"3",
                "EPIC",
                "Sub Task2",
                "DONE",
                "Description sub task3",
                "14:30 08.11.1997",
                "50"}, epic);
        assertEquals(3L, subTask.getId());
        assertEquals("Sub Task2", subTask.getTitle());
        assertEquals("Description sub task3", subTask.getDescription());
        assertEquals(TaskStatus.DONE, subTask.getStatus());
        assertEquals(LocalDateTime.of(1997, 11, 8, 14, 30), subTask.getStartTime().get());
        assertEquals(Duration.ofMinutes(50), subTask.getDuration().get());
        assertEquals(LocalDateTime.of(1997, 11, 8, 15, 20), subTask.getEndTime().get());
    }

    @Test
    @DisplayName("должен правильно формировать строку")
    public void shouldConvertToStringCorrectly() {
        SubTask subTask = new SubTask(3L, "Sub Task2", "Description sub task3", TaskStatus.DONE, epic, Optional.empty(), Optional.empty());
        String line = converter.toCsvLine(subTask);
        assertEquals("3,SUBTASK,Sub Task2,DONE,Description sub task3, , ,2", line);
    }

    @Test
    @DisplayName("должен правильно формировать строку, включая дату старта и длительность")
    public void shouldConvertToStringCorrectlyIncludingTimings() {
        SubTask subTask = new SubTask(3L, "Sub Task2", "Description sub task3",
                TaskStatus.DONE, epic,
                Optional.of(LocalDateTime.of(1997, 11, 8, 14, 30)),
                Optional.of(Duration.ofMinutes(50)));
        String line = converter.toCsvLine(subTask);
        assertEquals("3,SUBTASK,Sub Task2,DONE,Description sub task3,14:30 08.11.1997,50,2", line);
    }
}

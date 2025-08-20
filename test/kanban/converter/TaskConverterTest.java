package kanban.converter;

import kanban.model.Task;
import kanban.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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
                "Description task1",
                "",
                ""});
        assertEquals(1L, task.getId());
        assertEquals("Task1", task.getTitle());
        assertEquals("Description task1", task.getDescription());
        assertEquals("Description task1", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(Optional.empty(), task.getStartTime());
        assertEquals(Optional.empty(), task.getEndTime());
        assertEquals(Optional.empty(), task.getDuration());
    }

    @Test
    @DisplayName("должен правильно парсить задачу, включая дату старта и длительность")
    public void shouldParseCorrectlyIncludingTimings() {
        Task task = converter.fromCsvLine(new String[]{"1",
                "TASK",
                "Task1",
                "IN_PROGRESS",
                "Description task1",
                "14:30 08.11.1997",
                "50"});
        assertEquals(1L, task.getId());
        assertEquals("Task1", task.getTitle());
        assertEquals("Description task1", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(LocalDateTime.of(1997, 11, 8, 14, 30), task.getStartTime().get());
        assertEquals(Duration.ofMinutes(50), task.getDuration().get());
        assertEquals(LocalDateTime.of(1997, 11, 8, 15, 20), task.getEndTime().get());
    }


    @Test
    @DisplayName("должен правильно формировать строку")
    public void shouldConvertToStringCorrectly() {
        Task task = new Task(1L, "Task1", "Description task1", TaskStatus.IN_PROGRESS, Optional.empty(), Optional.empty());
        String line = converter.toCsvLine(task);
        assertEquals("1,TASK,Task1,IN_PROGRESS,Description task1, , ,", line);
    }

    @Test
    @DisplayName("должен правильно формировать строку, включая дату старта и длительность")
    public void shouldConvertToStringCorrectlyIncludingTiming() {
        Task task = new Task(1L, "Task1", "Description task1", TaskStatus.IN_PROGRESS,
                Optional.of(LocalDateTime.of(1997, 11, 8, 14, 30)),
                Optional.of(Duration.ofMinutes(50)));
        String line = converter.toCsvLine(task);
        assertEquals("1,TASK,Task1,IN_PROGRESS,Description task1,14:30 08.11.1997,50,", line);
    }
}

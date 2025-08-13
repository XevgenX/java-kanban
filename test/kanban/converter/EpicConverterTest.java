package kanban.converter;

import kanban.model.Epic;
import kanban.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicConverterTest {
    private EpicConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new EpicConverter();
    }

    @Test
    @DisplayName("должен правильно парсить задачу")
    public void shouldParseCorrectly() {
        Epic epic = converter.fromCsvLine(new String[]{"2",
                "EPIC",
                "Epic2",
                "DONE",
                "Description epic2"});
        assertEquals(2L, epic.getId());
        assertEquals("Epic2", epic.getTitle());
        assertEquals("Description epic2", epic.getDescription());
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("должен правильно формировать строку")
    public void shouldConvertToStringCorrectly() {
        Epic epic = new Epic(2L, "Epic2", "Description epic2", TaskStatus.DONE);
        String line = converter.toCsvLine(epic);
        assertEquals("2,EPIC,Epic2,DONE,Description epic2,", line);
    }
}

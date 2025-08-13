package kanban.converter;

import kanban.model.Epic;
import kanban.model.TaskStatus;
import kanban.model.TaskType;

public class EpicConverter extends TaskConverter {
    public String toCsvLine(Epic epic) {
        return prepareCommonPart(epic, TaskType.EPIC);
    }

    public Epic fromCsvLine(String[] cells) {
        Epic epic = new Epic(Long.parseLong(cells[0]), cells[2], cells[4], TaskStatus.valueOf(cells[3]));
        return epic;
    }
}

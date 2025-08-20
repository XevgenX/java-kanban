package kanban.converter;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.TaskStatus;
import kanban.model.TaskType;

public class SubTaskConverter extends TaskConverter {
    public String toCsvLine(SubTask subTask) {
        StringBuilder builder = new StringBuilder(prepareCommonPart(subTask, TaskType.SUBTASK));
        builder.append(subTask.getEpic().getId());
        return builder.toString();
    }

    public SubTask fromCsvLine(String[] cells, Epic epic) {
        TaskStatus status = TaskStatus.valueOf(cells[3]);
        SubTask subTask = new SubTask(Long.parseLong(cells[0]),
                cells[2],
                cells[4],
                status,
                epic,
                extractStartTime(cells),
                extractDuration(cells));
        return subTask;
    }
}

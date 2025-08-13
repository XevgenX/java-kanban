package kanban.converter;

import kanban.model.Task;
import kanban.model.TaskStatus;
import kanban.model.TaskType;

import static kanban.impl.FileBackedTaskManager.CSV_SEPARATOR;

public class TaskConverter {
    public String toCsvLine(Task task) {
        return prepareCommonPart(task, TaskType.TASK);
    }

    public Task fromCsvLine(String[] cells) {
        Task task = new Task(Long.parseLong(cells[0]), cells[2], cells[4], TaskStatus.valueOf(cells[3]));
        return task;
    }

    protected String prepareCommonPart(Task task, TaskType type) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId());
        builder.append(CSV_SEPARATOR);
        builder.append(type);
        builder.append(CSV_SEPARATOR);
        builder.append(task.getTitle());
        builder.append(CSV_SEPARATOR);
        builder.append(task.getStatus());
        builder.append(CSV_SEPARATOR);
        builder.append(task.getDescription());
        builder.append(CSV_SEPARATOR);
        return builder.toString();
    }
}

package kanban.converter;

import kanban.model.Task;
import kanban.model.TaskStatus;
import kanban.model.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static kanban.impl.FileBackedTaskManager.CSV_SEPARATOR;

public class TaskConverter {
    protected final DateTimeFormatter formatter;

    public TaskConverter() {
        this.formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");;
    }

    public String toCsvLine(Task task) {
        return prepareCommonPart(task, TaskType.TASK);
    }

    public Task fromCsvLine(String[] cells) {
        Task task = new Task(Long.parseLong(cells[0]),
                cells[2],
                cells[4],
                TaskStatus.valueOf(cells[3]),
                extractStartTime(cells),
                extractDuration(cells));
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
        builder.append(task.getStartTime().isPresent() ? formatter.format(task.getStartTime().get()) : " ");
        builder.append(CSV_SEPARATOR);
        builder.append(task.getDuration().isPresent() ? task.getDuration().get().toMinutes() : " ");
        builder.append(CSV_SEPARATOR);
        return builder.toString();
    }

    protected Optional<LocalDateTime> extractStartTime(String[] cells) {
        return (cells[5].isBlank())
                ? Optional.empty()
                : Optional.of(LocalDateTime.parse(cells[5], formatter));
    }

    protected Optional<Duration> extractDuration(String[] cells) {
        return (cells[6].isBlank())
                ? Optional.empty()
                : Optional.of(Duration.ofMinutes(Integer.parseInt(cells[6])));
    }
}

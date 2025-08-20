package kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Большая задача, которая делится на подзадачи
 */
public class Epic extends Task {
    /** какие подзадачи входят в epic */
    private List<SubTask> subTasks;

    public Epic(String title, String description) {
        super(title, description);
        subTasks = new ArrayList<>();
    }

    public Epic(Long id, String title, String description, TaskStatus status,
                Optional<LocalDateTime> startTime, Optional<Duration> duration) {
        super(id, title, description, status, Optional.empty(), Optional.empty());
        subTasks = new ArrayList<>();
    }

    public Epic(Epic epic) {
        super(epic.getTitle(), epic.getDescription());
        subTasks = epic.getSubTasks().stream()
                .map(st -> new SubTask(st))
                .collect(Collectors.toList());
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    public void clearSubTasks() {
        subTasks.clear();
    }

    public void adjustStatus() {
        adjustStatusForEmptySubTaskList();
        tryToMoveToInProgress();
        tryToMmoveToDone();
    }

    @Override
    public void tryToMoveToInProgress() {
        long subTasksCountWithStatusNew = subTasks.stream()
                .map(SubTask::getStatus)
                .filter(status -> status == TaskStatus.NEW).count();
        long subTasksCountWithStatusInProgress = subTasks.stream()
                .map(SubTask::getStatus)
                .filter(status -> status == TaskStatus.IN_PROGRESS).count();
        long subTasksCountWithStatusDone = subTasks.stream()
                .map(SubTask::getStatus)
                .filter(status -> status == TaskStatus.DONE).count();
        if (subTasksCountWithStatusInProgress > 0) {
            status = TaskStatus.IN_PROGRESS;
        }
        if (subTasksCountWithStatusNew > 0 && subTasksCountWithStatusDone > 0) {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    /** Завершение всех подзадач эпика считается завершением эпика */
    @Override
    public void tryToMmoveToDone() {
        if (subTasks.isEmpty()) {
            return;
        }
        if (subTasks.stream()
                .anyMatch(subTask -> subTask.getStatus() != TaskStatus.DONE)) {
            return;
        }
        status = TaskStatus.DONE;
    }

    @Override
    public void setDuration(Duration duration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void adjustTiming() {
        if (subTasks != null) {
            subTasks.stream()
                    .map(SubTask::getStartTime)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .min(LocalDateTime::compareTo)
                    .ifPresent(minSubTaskStartTime -> startTime = Optional.of(minSubTaskStartTime));
            subTasks.stream()
                    .map(SubTask::getEndTime)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .max(LocalDateTime::compareTo)
                    .ifPresent(minSubTaskEndTime -> endTime = Optional.of(minSubTaskEndTime));
            Duration calculatedDuration = subTasks.stream()
                    .map(SubTask::getDuration)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(Duration.ZERO, Duration::plus);
            duration = (calculatedDuration.getSeconds() > 0) ? Optional.of(calculatedDuration) : Optional.empty();
        }
    }

    private void adjustStatusForEmptySubTaskList() {
        if (subTasks.isEmpty()) {
            status = TaskStatus.NEW;
        } else {
            boolean everySubTaskIsNew = subTasks.stream()
                    .map(SubTask::getStatus)
                    .allMatch(status -> status == TaskStatus.NEW);
            if (everySubTaskIsNew) {
                status = TaskStatus.NEW;
            }
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                '}';
    }
}

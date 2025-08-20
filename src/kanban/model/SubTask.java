package kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Подзадача
 */
public class SubTask extends Task {
    /** в рамках какого эпика подзадача выполняется */
    private Epic epic;

    public SubTask(Epic epic, String title, String description) {
        super(title, description);
        this.epic = epic;
    }

    public SubTask(Long id, String title, String description, TaskStatus status, Epic epic,
                   Optional<LocalDateTime> startTime, Optional<Duration> duration) {
        super(id, title, description, status, startTime, duration);
        this.epic = epic;
    }

    public SubTask(SubTask subTask) {
        super(subTask.getTitle(), subTask.getDescription());
        this.startTime = subTask.getStartTime();
        this.duration = subTask.getDuration();
        this.epic = subTask.getEpic();
        adjustTiming();
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public void tryToMoveToInProgress() {
        status = TaskStatus.IN_PROGRESS;
        epic.tryToMoveToInProgress();
    }

    /** Завершение всех подзадач эпика считается завершением эпика */
    @Override
    public void tryToMmoveToDone() {
        status = TaskStatus.DONE;
        epic.tryToMmoveToDone();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
        epic.adjustTiming();
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
        epic.adjustTiming();
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                '}';
    }
}

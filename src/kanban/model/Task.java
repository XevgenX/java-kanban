package kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Простейший кирпичик трекера — задача (англ. task)
 */
public class Task {
    /** Уникальный идентификационный номер задачи, по которому её можно будет найти */
    protected Long id;
    /** Название, кратко описывающее суть задачи (например, «Переезд») */
    protected String title;
    /** Описание, в котором раскрываются детали */
    protected String description;
    /** Статус, отображающий её прогресс */
    protected TaskStatus status;
    /** Продолжительность задачи, оценка того, сколько времени она займёт в минутах */
    protected Optional<Duration> duration = Optional.empty();
    /** дата и время, когда предполагается приступить к выполнению задачи */
    protected Optional<LocalDateTime> startTime = Optional.empty();
    /** дата и время завершения задачи, которые рассчитываются исходя из startTime и duration */
    protected Optional<LocalDateTime> endTime = Optional.empty();

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(Long id, String title, String description, TaskStatus status,
                Optional<LocalDateTime> startTime, Optional<Duration> duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        adjustTiming();
    }

    public Task(Task task) {
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.startTime = task.getStartTime();
        this.duration = task.getDuration();
        status = task.getStatus();
        adjustTiming();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void tryToMoveToInProgress() {
        status = TaskStatus.IN_PROGRESS;
    }

    public void tryToMmoveToDone() {
        status = TaskStatus.DONE;
    }

    public Optional<Duration> getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = Optional.ofNullable(duration);
        adjustTiming();
    }

    public Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = Optional.ofNullable(startTime);
        adjustTiming();
    }

    public Optional<LocalDateTime> getEndTime() {
        return endTime;
    }

    protected void adjustTiming() {
        if (startTime.isPresent() && duration.isPresent()) {
            endTime = Optional.of(startTime.get().plus(duration.get()));
        }
    }

    /** две задачи с одинаковым id должны выглядеть для менеджера как одна и та же */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                '}';
    }
}

package kanban.model;

import java.util.Objects;

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

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(Long id, String title, String description, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(Task task) {
        this.title = task.getTitle();
        this.description = task.getDescription();
        status = task.getStatus();
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

package kanban.model;

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

    public SubTask(SubTask subTask) {
        super(subTask.getTitle(), subTask.getDescription());
        this.epic = subTask.getEpic();
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
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                '}';
    }
}

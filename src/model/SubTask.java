package model;

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

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public void moveToInProgress() {
        status = TaskStatus.IN_PROGRESS;
        epic.moveToInProgress();
    }

    /** Завершение всех подзадач эпика считается завершением эпика */
    @Override
    public void tryToMmoveToDone() {
        status = TaskStatus.DONE;
        epic.tryToMmoveToDone();
    }
}

package model;

import java.util.ArrayList;

/**
 * Большая задача, которая делится на подзадачи
 */
public class Epic extends Task {
    /** какие подзадачи входят в epic */
    private ArrayList<SubTask> subTasks;

    public Epic(String title, String description) {
        super(title, description);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
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

    /** Завершение всех подзадач эпика считается завершением эпика */
    @Override
    public void tryToMmoveToDone() {
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != TaskStatus.DONE) {
                return;
            }
        }
        status = TaskStatus.DONE;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subTasks=" + subTasks +
                '}';
    }
}

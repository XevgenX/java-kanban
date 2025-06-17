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

    public void adjustStatus() {
        adjustStatusForEmptySubTaskList();
        tryToMoveToInProgress();
        tryToMmoveToDone();
    }

    @Override
    public void tryToMoveToInProgress() {
        int subTasksCountWithStatusNew = 0;
        int subTasksCountWithStatusInProgress = 0;
        int subTasksCountWithStatusDone = 0;
        for (SubTask subTask : subTasks) {
            switch (subTask.getStatus()) {
                case NEW:
                    subTasksCountWithStatusNew++;
                    break;
                case IN_PROGRESS:
                    subTasksCountWithStatusInProgress++;
                    break;
                case DONE:
                    subTasksCountWithStatusDone++;
                    break;
            }
        }
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
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != TaskStatus.DONE) {
                return;
            }
        }
        status = TaskStatus.DONE;
    }

    private void adjustStatusForEmptySubTaskList() {
        if (subTasks.isEmpty()) {
            status = TaskStatus.NEW;
        } else {
            boolean everySubTaskIsNew = false;
            for (SubTask subTask : subTasks) {
                if (subTask.getStatus() == TaskStatus.NEW) {
                    everySubTaskIsNew = true;
                } else {
                    everySubTaskIsNew = false;
                    break;
                }
            }
            if (everySubTaskIsNew) {
                status = TaskStatus.NEW;
            }
        }
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

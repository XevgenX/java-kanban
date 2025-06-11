import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    /** Хранение задач всех типов */
    private HashMap<Long, Task> savedTasks;
    private Long currentMaxId;

    public TaskManager() {
        savedTasks = new HashMap<>();
        currentMaxId = 0L;
    }

    /** Получение списка всех задач независимо от типа */
    public ArrayList<Task> getAllElements() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : savedTasks.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    /** Получение списка всех простых задач */
    public ArrayList<Task> getAllSimpleTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : savedTasks.values()) {
            if (!(task instanceof Epic) && !(task instanceof SubTask)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    /** Получение списка всех эпиков */
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Task task : savedTasks.values()) {
            if (task instanceof Epic epic) {
                epics.add(epic);
            }
        }
        return epics;
    }

    /** Получение списка всех подзадач независимо от эпика */
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Task task : savedTasks.values()) {
            if (task instanceof SubTask subTask) {
                subTasks.add(subTask);
            }
        }
        return subTasks;
    }

    /** Получение списка всех подзадач эпика */
    public ArrayList<SubTask> getAllSubTasks(Long epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        if (savedTasks.containsKey(epicId)) {
            Task task = savedTasks.get(epicId);
            if (task instanceof Epic epic) {
                for (SubTask subTask : epic.getSubTasks()) {
                    subTasks.add(subTask);
                }
            }
        }
        return subTasks;
    }

    public Task createSimpleTask(Task task) {
        if (task != null) {
            task.setId(currentMaxId++);
            savedTasks.put(task.getId(), task);
        }
        return task;
    }

    public Epic createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(currentMaxId++);
            for (SubTask subTask : epic.getSubTasks()) {
                subTask.setId(currentMaxId++);
                if (subTask.getStatus() != TaskStatus.NEW) {
                    epic.moveToInProgress();
                }
            }
            epic.tryToMmoveToDone();
            savedTasks.put(epic.getId(), epic);
        }
        return epic;
    }

    public void update(Task task) {
        if (task != null
                && task.getId() != null
                && savedTasks.containsKey(task.getId())) {
            savedTasks.put(task.getId(), task);
        }
    }

    public void moveTaskToInProgress(Task task) {
        if (task != null) {
            task.moveToInProgress();
        }
    }

    public void moveTaskToDone(Task task) {
        if (task != null) {
            // Логика для разных типов реализована в самих классах
            task.tryToMmoveToDone();
        }
    }

    public void deleteTask(Long id) {
        if (savedTasks.containsKey(id)) {
            Task task = savedTasks.get(id);
            if (task instanceof Epic epic) {
                deleteEpic(epic);
            } else if (task instanceof SubTask subTask) {
                deleteSubTask(subTask);
            } else {
                savedTasks.remove(id);
            }
        }
    }

    /** Удаление всех задач */
    public void clearTasks() {
        savedTasks.clear();
    }

    private void deleteEpic(Epic epic) {
        for (SubTask subTask : epic.getSubTasks()) {
            savedTasks.remove(subTask.getId());
        }
        epic.clearSubTasks();
        savedTasks.remove(epic.getId());
    }

    private void deleteSubTask(SubTask subTask) {
        subTask.getEpic().removeSubTask(subTask);
        savedTasks.remove(subTask.getId());
    }
}

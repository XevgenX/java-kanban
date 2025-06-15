import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    /** Хранение задач всех типов */
    private final HashMap<Long, Task> savedTasks;
    private final HashMap<Long, Epic> savedEpics;
    private final HashMap<Long, SubTask> savedSubTasks;
    private Long currentMaxId;

    public TaskManager() {
        savedTasks = new HashMap<>();
        savedEpics = new HashMap<>();
        savedSubTasks = new HashMap<>();
        currentMaxId = 0L;
    }

    /** Получение списка всех простых задач */
    public ArrayList<Task> getAllSimpleTasks() {
        return new ArrayList<>(savedTasks.values());
    }

    /** Получение списка всех эпиков */
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(savedEpics.values());
    }

    /** Получение списка всех подзадач независимо от эпика */
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(savedSubTasks.values());
    }

    /** Получение списка всех подзадач эпика */
    public ArrayList<SubTask> getSubTasksByEpic(Long epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        if (savedEpics.containsKey(epicId)) {
            Epic epic = savedEpics.get(epicId);
            subTasks.addAll(epic.getSubTasks());
        }
        return subTasks;
    }

    public Task getTaskById(Long id) {
        return savedTasks.get(id);
    }

    public Task getEpicById(Long id) {
        return savedEpics.get(id);
    }

    public Task getSubTaskById(Long id) {
        return savedSubTasks.get(id);
    }

    /**
     * Метод сохранения простой задачи
     * @param task Предзаполненная задача
     * @return Сгенерированный id сохраненного элемента или null, если что то пошло не так
     */
    public Long createSimpleTask(Task task) {
        Long generatedId = null;
        if (task != null) {
            task.setId(nextId());
            savedTasks.put(task.getId(), task);
            generatedId = task.getId();
        }
        return generatedId;
    }

    public Long createEpic(Epic epic) {
        Long generatedId = null;
        if (epic != null) {
            epic.setId(nextId());
            for (SubTask subTask : epic.getSubTasks()) {
                subTask.setId(nextId());
                savedSubTasks.put(subTask.getId(), subTask);
            }
            epic.adjustStatus();
            savedEpics.put(epic.getId(), epic);
            generatedId = epic.getId();
        }
        return generatedId;
    }

    public Long createSubTask(SubTask subTask) {
        Long generatedId = null;
        if (subTask != null
                && subTask.getEpic() != null
                && subTask.getEpic().getId() != null
                && savedEpics.containsKey(subTask.getEpic().getId())) {
            Epic epic = savedEpics.get(subTask.getEpic().getId());
            subTask.setId(nextId());
            subTask.setEpic(epic);
            epic.addSubTask(subTask);
            savedSubTasks.put(subTask.getId(), subTask);
            generatedId = subTask.getId();
        }
        return generatedId;
    }

    public void updateTask(Task task) {
        if (task != null
                && task.getId() != null
                && savedTasks.containsKey(task.getId())) {
            savedTasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null
                && epic.getId() != null
                && savedEpics.containsKey(epic.getId())) {
            savedEpics.put(epic.getId(), epic);
            epic.adjustStatus();
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTask != null
                && subTask.getId() != null
                && savedSubTasks.containsKey(subTask.getId())) {
            savedSubTasks.put(subTask.getId(), subTask);
            subTask.getEpic().adjustStatus();
        }
    }

    public void deleteTask(Long id) {
        if (id != null && savedTasks.containsKey(id)) {
            Task task = savedTasks.get(id);
            savedTasks.remove(id);
        }
    }

    public void deleteEpic(Long id) {
        if (id != null && savedEpics.containsKey(id)) {
            Epic epic = savedEpics.get(id);
            for (SubTask subTask : epic.getSubTasks()) {
                savedSubTasks.remove(subTask.getId());
            }
            epic.clearSubTasks();
            savedEpics.remove(epic.getId());
        }
    }

    public void deleteSubTask(Long id) {
        if (id != null && savedSubTasks.containsKey(id)) {
            SubTask subTask = savedSubTasks.get(id);
            subTask.getEpic().removeSubTask(subTask);
            subTask.getEpic().adjustStatus();
            savedSubTasks.remove(subTask.getId());
        }
    }

    /** Удаление всех задач */
    public void clearTasks() {
        savedTasks.clear();
    }

    public void clearEpic() {
        for (Long id : savedEpics.keySet()) {
            deleteEpic(id);
        }
    }

    public void clearSubTasks() {
        for (Long id : savedSubTasks.keySet()) {
            deleteSubTask(id);
        }
    }

    private Long nextId() {
        return ++currentMaxId;
    }

}

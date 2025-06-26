package kanban.impl;

import kanban.HistoryManager;
import kanban.Managers;
import kanban.TaskManager;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    /** Хранение задач всех типов */
    private final HashMap<Long, Task> savedTasks;
    private final HashMap<Long, Epic> savedEpics;
    private final HashMap<Long, SubTask> savedSubTasks;
    private Long currentMaxId;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        savedTasks = new HashMap<>();
        savedEpics = new HashMap<>();
        savedSubTasks = new HashMap<>();
        currentMaxId = 0L;
        this.historyManager = Managers.getDefaultHistory();
    }

    /** Второй конструктор на случай, если появится не только default_ная реализация HistoryManager */
    public InMemoryTaskManager(HistoryManager historyManager) {
        savedTasks = new HashMap<>();
        savedEpics = new HashMap<>();
        savedSubTasks = new HashMap<>();
        currentMaxId = 0L;
        this.historyManager = historyManager;
    }

    @Override
    public ArrayList<Task> getAllSimpleTasks() {
        return new ArrayList<>(savedTasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(savedEpics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(savedSubTasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasksByEpic(Long epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        if (savedEpics.containsKey(epicId)) {
            Epic epic = savedEpics.get(epicId);
            subTasks.addAll(epic.getSubTasks());
        }
        return subTasks;
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = savedTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Long id) {
        Epic epic = savedEpics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(Long id) {
        SubTask subTask = savedSubTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Long createSimpleTask(Task task) {
        Long generatedId = null;
        if (task != null) {
            Task taskToBeSaved = new Task(task);
            taskToBeSaved.setId(nextId());
            savedTasks.put(taskToBeSaved.getId(), taskToBeSaved);
            generatedId = taskToBeSaved.getId();
        }
        return generatedId;
    }

    @Override
    public Long createEpic(Epic epic) {
        Long generatedId = null;
        if (epic != null) {
            Epic epicToBeSaved = new Epic(epic);
            epicToBeSaved.setId(nextId());
            for (SubTask subTask : epicToBeSaved.getSubTasks()) {
                subTask.setId(nextId());
                savedSubTasks.put(subTask.getId(), subTask);
            }
            epicToBeSaved.adjustStatus();
            savedEpics.put(epicToBeSaved.getId(), epicToBeSaved);
            generatedId = epicToBeSaved.getId();
        }
        return generatedId;
    }

    @Override
    public Long createSubTask(SubTask subTask) {
        Long generatedId = null;
        if (subTask != null
                && subTask.getEpic() != null
                && subTask.getEpic().getId() != null
                && savedEpics.containsKey(subTask.getEpic().getId())) {
            Epic epic = savedEpics.get(subTask.getEpic().getId());
            SubTask subTaskToBeSaved = new SubTask(subTask);
            subTaskToBeSaved.setId(nextId());
            subTaskToBeSaved.setEpic(epic);
            epic.addSubTask(subTaskToBeSaved);
            savedSubTasks.put(subTaskToBeSaved.getId(), subTaskToBeSaved);
            savedEpics.put(epic.getId(), epic);
            generatedId = subTaskToBeSaved.getId();
        }
        return generatedId;
    }

    @Override
    public void updateTask(Task task) {
        if (task != null
                && task.getId() != null
                && savedTasks.containsKey(task.getId())) {
            savedTasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null
                && epic.getId() != null
                && savedEpics.containsKey(epic.getId())) {
            savedEpics.put(epic.getId(), epic);
            epic.adjustStatus();
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask != null
                && subTask.getId() != null
                && savedSubTasks.containsKey(subTask.getId())) {
            savedSubTasks.put(subTask.getId(), subTask);
            subTask.getEpic().adjustStatus();
        }
    }

    @Override
    public void deleteTask(Long id) {
        if (id != null && savedTasks.containsKey(id)) {
            Task task = savedTasks.get(id);
            savedTasks.remove(id);
        }
    }

    @Override
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

    @Override
    public void deleteSubTask(Long id) {
        if (id != null && savedSubTasks.containsKey(id)) {
            SubTask subTask = savedSubTasks.get(id);
            subTask.getEpic().removeSubTask(subTask);
            subTask.getEpic().adjustStatus();
            savedSubTasks.remove(subTask.getId());
        }
    }

    @Override
    public void clearTasks() {
        savedTasks.clear();
    }

    @Override
    public void clearEpic() {
        for (Long id : savedEpics.keySet()) {
            deleteEpic(id);
        }
    }

    @Override
    public void clearSubTasks() {
        for (Long id : savedSubTasks.keySet()) {
            deleteSubTask(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Long nextId() {
        return ++currentMaxId;
    }
}

package kanban.impl;

import kanban.HistoryManager;
import kanban.Managers;
import kanban.TaskManager;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    /** Хранение задач всех типов */
    private final HashMap<Long, Task> savedTasks;
    private final HashMap<Long, Epic> savedEpics;
    private final HashMap<Long, SubTask> savedSubTasks;
    private final Set<Task> prioritizedTasks;
    private Long currentMaxId;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        savedTasks = new HashMap<>();
        savedEpics = new HashMap<>();
        savedSubTasks = new HashMap<>();
        currentMaxId = 0L;
        this.historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(t -> t.getStartTime().get()));
    }

    /** Второй конструктор на случай, если появится не только default_ная реализация HistoryManager */
    public InMemoryTaskManager(HistoryManager historyManager) {
        this();
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getAllSimpleTasks() {
        return new ArrayList<>(savedTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(savedEpics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(savedSubTasks.values());
    }

    @Override
    public List<SubTask> getSubTasksByEpic(Long epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        if (savedEpics.containsKey(epicId)) {
            Epic epic = savedEpics.get(epicId);
            subTasks.addAll(epic.getSubTasks());
        }
        return subTasks;
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        Optional<Task> task = Optional.ofNullable(savedTasks.get(id));
        task.ifPresent(historyManager::add);
        return task;
    }

    @Override
    public Optional<Epic> getEpicById(Long id) {
        Optional<Epic> epic = Optional.ofNullable(savedEpics.get(id));
        epic.ifPresent(historyManager::add);
        return epic;
    }

    @Override
    public Optional<SubTask> getSubTaskById(Long id) {
        Optional<SubTask> subTask = Optional.ofNullable(savedSubTasks.get(id));
        subTask.ifPresent(historyManager::add);
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
            saveToPrioritizedSet(taskToBeSaved);
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
            epicToBeSaved.adjustTiming();
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
            saveToPrioritizedSet(subTaskToBeSaved);
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
            saveToPrioritizedSet(task);
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
            saveToPrioritizedSet(subTask);
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
            epic.getSubTasks().forEach(subTask -> savedSubTasks.remove(subTask.getId()));
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
        savedEpics.keySet().forEach(this::deleteEpic);
    }

    @Override
    public void clearSubTasks() {
        savedSubTasks.keySet().forEach(this::deleteSubTask);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Long nextId() {
        return ++currentMaxId;
    }

    private void saveToPrioritizedSet(Task task) {
        if (task.getStartTime().isPresent()) {
            if (prioritizedTasks.contains(task)) {
                prioritizedTasks.remove(task);
            }
            if (validateDateIntervals(task)) {
                prioritizedTasks.add(task);
            } else {
                throw new IllegalArgumentException("Задача имеет пересечение интервалов с другими задачами");
            }
        }
    }

    private boolean validateDateIntervals(Task task) {
        return prioritizedTasks.stream()
                .noneMatch(t -> {
                    System.out.println(t.getStartTime().get() + " " + t.getEndTime().get());
                    System.out.println(task.getStartTime().get() +" " + task.getEndTime().get());
                    return (t.getStartTime().get().isBefore(task.getEndTime().get()) // t
                            && task.getStartTime().get().isBefore(t.getEndTime().get()))
                            || (task.getStartTime().get().isBefore(t.getEndTime().get())
                            && t.getStartTime().get().isBefore(task.getEndTime().get()));
                });
    }
}

package kanban;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    List<Task> getPrioritizedTasks();

    List<Task> getAllSimpleTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    List<SubTask> getSubTasksByEpic(Long epicId);

    Optional<Task> getTaskById(Long id);

    Optional<Epic> getEpicById(Long id);

    Optional<SubTask> getSubTaskById(Long id);

    Long createSimpleTask(Task task);

    Long createEpic(Epic epic);

    Long createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTask(Long id);

    void deleteEpic(Long id);

    void deleteSubTask(Long id);

    void clearTasks();

    void clearEpic();

    void clearSubTasks();

    List<Task> getHistory();
}
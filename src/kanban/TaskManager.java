package kanban;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllSimpleTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<SubTask> getSubTasksByEpic(Long epicId);

    Task getTaskById(Long id);

    Epic getEpicById(Long id);

    SubTask getSubTaskById(Long id);

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

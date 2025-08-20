package kanban.impl;

import kanban.converter.EpicConverter;
import kanban.converter.SubTaskConverter;
import kanban.converter.TaskConverter;
import kanban.exception.ManagerLoadException;
import kanban.exception.ManagerSaveException;
import kanban.model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public static final String FILE_HEADER = "id,type,name,status,description,start_date,duration,epic";
    public static final String CSV_SEPARATOR = ",";
    private final TaskConverter taskConverter = new TaskConverter();
    private final EpicConverter epicConverter = new EpicConverter();
    private final SubTaskConverter subTaskConverter = new SubTaskConverter();
    private final File fileStorage;

    public FileBackedTaskManager(File file) {
        this.fileStorage = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.loadFromStorage(file);
        return fileBackedTaskManager;
    }

    private void loadFromStorage(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                if (line.equals(FILE_HEADER)) {
                    continue;
                }
                String[] cells = line.split(CSV_SEPARATOR);
                TaskType type = TaskType.valueOf(cells[1]);
                switch (type) {
                    case TASK:
                        createSimpleTask(taskConverter.fromCsvLine(cells));
                        break;
                    case EPIC:
                        createEpic(epicConverter.fromCsvLine(cells));
                        break;
                    case SUBTASK:
                        Optional<Epic> epic = getEpicById(Long.parseLong(cells[7]));
                        if (epic.isPresent()) {
                            Long id = createSubTask(subTaskConverter.fromCsvLine(cells, epic.get()));
                            Optional<SubTask> savedSubTask = getSubTaskById(id);
                            if (savedSubTask.isPresent()) {
                                epic.get().adjustTiming();
                                TaskStatus status = TaskStatus.valueOf(cells[3]);
                                switch (status) {
                                    case IN_PROGRESS:
                                        savedSubTask.get().tryToMoveToInProgress();
                                        updateSubTask(savedSubTask.get());
                                        break;
                                    case DONE:
                                        savedSubTask.get().tryToMoveToInProgress();
                                        updateSubTask(savedSubTask.get());
                                        savedSubTask.get().tryToMmoveToDone();
                                        updateSubTask(savedSubTask.get());
                                        break;
                                }
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки задач из файла", e);
        }
    }

    @Override
    public Long createSimpleTask(Task task) {
        Long id = super.createSimpleTask(task);
        save();
        return id;
    }

    @Override
    public Long createEpic(Epic epic) {
        Long id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public Long createSubTask(SubTask subTask) {
        Long id = super.createSubTask(subTask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(Long id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(Long id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(Long id) {
        super.deleteSubTask(id);
        save();
    }

    public void clearTasks() {
        super.clearTasks();
        save();
    }

    public void clearEpic() {
        super.clearEpic();
        save();
    }

    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(fileStorage);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(FILE_HEADER);
            saveTasks(printWriter);
            saveEpics(printWriter);
            saveSubTasks(printWriter);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач в файл", e);
        }
    }

    private void saveTasks(PrintWriter printWriter) {
        for (Task task : getAllSimpleTasks()) {
            printWriter.println(taskConverter.toCsvLine(task));
        }
    }

    private void saveEpics(PrintWriter printWriter) {
        for (Epic epic : getAllEpics()) {
            printWriter.println(epicConverter.toCsvLine(epic));
        }
    }

    private void saveSubTasks(PrintWriter printWriter) {
        for (SubTask subTask : getAllSubTasks()) {
            printWriter.println(subTaskConverter.toCsvLine(subTask));
        }
    }
}

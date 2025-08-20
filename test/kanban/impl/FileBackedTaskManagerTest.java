package kanban.impl;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

import static kanban.impl.FileBackedTaskManager.FILE_HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest {
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        testFile = initTestFile();
    }

    @Test
    @DisplayName("должен правильно создать manager и загрузить данные задачи из файла")
    public void shouldInitAndLoadTasksCorrectly() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        assertNotNull(manager);
        ArrayList<Task> tasks = manager.getAllSimpleTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        assertEquals(1L, task.getId());
        assertEquals("Task1", task.getTitle());
        assertEquals("Description task1", task.getDescription());
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    @DisplayName("должен правильно создать manager и загрузить данные эпика из файла")
    public void shouldInitAndLoadEpicCorrectly() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        assertNotNull(manager);
        ArrayList<Epic> epics = manager.getAllEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
        Epic epic = epics.get(0);
        assertEquals(2L, epic.getId());
        assertEquals("Epic2", epic.getTitle());
        assertEquals("Description epic2", epic.getDescription());
        assertEquals(TaskStatus.DONE, epic.getStatus());
        assertNotNull(epic.getSubTasks());
        assertEquals(1, epic.getSubTasks().size());
        assertEquals(3, epic.getSubTasks().get(0).getId());
    }

    @Test
    @DisplayName("должен правильно создать manager и загрузить данные подзадачи из файла")
    public void shouldInitAndLoadSubTaskCorrectly() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        assertNotNull(manager);
        ArrayList<SubTask> subTasks = manager.getAllSubTasks();
        assertNotNull(subTasks);
        assertEquals(1, subTasks.size());
        SubTask subTask = subTasks.get(0);
        assertEquals(3L, subTask.getId());
        assertEquals("Sub Task2", subTask.getTitle());
        assertEquals("Description sub task3", subTask.getDescription());
        assertEquals(TaskStatus.DONE, subTask.getStatus());
        assertNotNull(subTask.getEpic());
        assertEquals(2, subTask.getEpic().getId());
    }

    @Test
    @DisplayName("должен правильно сохранять новые задачи")
    public void shouldSaveTaskCorrectly() throws IOException {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        Task simpleTask = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        manager.createSimpleTask(simpleTask);
        String body = Files.readString(testFile.toPath()).replaceAll("\\R", "\n");
        String expected = """
                id,type,name,status,description,epic
                1,TASK,Task1,NEW,Description task1,
                4,TASK,Внести правки по комментариям,NEW,Правки проверяем по чату,
                2,EPIC,Epic2,DONE,Description epic2,
                3,SUBTASK,Sub Task2,DONE,Description sub task3,2
                """.replaceAll("\\R", "\n");
        assertEquals(expected, body);
    }

    @Test
    @DisplayName("должен правильно сохранять новые эпики")
    public void shouldSaveEpicCorrectly() throws IOException {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        manager.createEpic(epic);
        String body = Files.readString(testFile.toPath()).replaceAll("\\R", "\n");
        String expected = """
                id,type,name,status,description,epic
                1,TASK,Task1,NEW,Description task1,
                2,EPIC,Epic2,DONE,Description epic2,
                4,EPIC,Сделать финальное задание,NEW,Постараться ничего не забыть,
                3,SUBTASK,Sub Task2,DONE,Description sub task3,2
                """.replaceAll("\\R", "\n");
        assertEquals(expected, body);
    }

    @Test
    @DisplayName("должен правильно сохранять новые подзадачи")
    public void shouldSaveSubTaskCorrectly() throws IOException {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        Long epicId = manager.createEpic(epic);
        Epic savedEpic = manager.getEpicById(epicId);
        SubTask subTask = new SubTask(savedEpic, "Сделать коммит", "Не забыть про push");
        manager.createSubTask(subTask);
        String body = Files.readString(testFile.toPath()).replaceAll("\\R", "\n");
        String expected = """
                id,type,name,status,description,epic
                1,TASK,Task1,NEW,Description task1,
                2,EPIC,Epic2,DONE,Description epic2,
                4,EPIC,Сделать финальное задание,NEW,Постараться ничего не забыть,
                3,SUBTASK,Sub Task2,DONE,Description sub task3,2
                5,SUBTASK,Сделать коммит,NEW,Не забыть про push,4
                """.replaceAll("\\R", "\n");
        assertEquals(expected, body);
    }

    private File initTestFile() throws IOException {
        File initFile = File.createTempFile("test", "csv");
        try (FileWriter fileWriter = new FileWriter(initFile);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(FILE_HEADER);
            printWriter.println("1,TASK,Task1,NEW,Description task1,");
            printWriter.println("2,EPIC,Epic2,DONE,Description epic2,");
            printWriter.println("3,SUBTASK,Sub Task2,DONE,Description sub task3,2");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return initFile;
    }
}

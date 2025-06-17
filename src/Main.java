import model.Epic;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        testEpicStatusWithAllNewSubTasks(manager);
        testEpicStatusWithNoSubTasks(manager);
        testEpicStatusWithNewAndDoneSubTasks(manager);
        testEpicStatusWithNewAndInProgressSubTasks(manager);
        testEpicStatusWithInProgressAndDoneSubTasks(manager);
        testEpicStatusWithAllInProgressSubTasks(manager);
        testEpicStatusWithAllDoneSubTasks(manager);

    }

    private static void testMainLogic(TaskManager manager) {
        manager.createSimpleTask(new Task("Проверить deadline на ЯП", "Когда конец спринта?"));
        Task simpleTask2 = new Task("Внести правки по комментариям", "Правки проверяем по чату");
        manager.createSimpleTask(simpleTask2);
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        System.out.println("====");
        System.out.println(manager.getAllSimpleTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
        System.out.println("====");
        subTask1.tryToMoveToInProgress();
        System.out.println(manager.getAllEpics());
        System.out.println("====");
        subTask1.tryToMmoveToDone();
        subTask2.tryToMmoveToDone();
        System.out.println(manager.getAllEpics());
        System.out.println("====");
        manager.deleteTask(simpleTask2.getId());
        manager.deleteEpic(epic.getId());
        System.out.println(manager.getAllSimpleTasks());
        System.out.println(manager.getAllEpics());
    }

    private static void testEpicStatusWithAllNewSubTasks(TaskManager manager) {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        epic.adjustStatus();
        System.out.println("Статус эпика " + epic.getStatus() + " со статусам подзадач: " + subTask1.getStatus() + ", " + subTask2.getStatus());
    }

    private static void testEpicStatusWithNoSubTasks(TaskManager manager) {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        manager.createEpic(epic);
        epic.adjustStatus();
        System.out.println("Статус эпика " + epic.getStatus() + " без подзадач");
    }

    private static void testEpicStatusWithNewAndDoneSubTasks(TaskManager manager) {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        subTask2.tryToMmoveToDone();
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        epic.adjustStatus();
        System.out.println("Статус эпика " + epic.getStatus() + " со статусам подзадач: " + subTask1.getStatus() + ", " + subTask2.getStatus());
    }

    private static void testEpicStatusWithNewAndInProgressSubTasks(TaskManager manager) {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        subTask2.tryToMoveToInProgress();
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        epic.adjustStatus();
        System.out.println("Статус эпика " + epic.getStatus() + " со статусам подзадач: " + subTask1.getStatus() + ", " + subTask2.getStatus());
    }

    private static void testEpicStatusWithInProgressAndDoneSubTasks(TaskManager manager) {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        subTask1.tryToMmoveToDone();
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        subTask2.tryToMoveToInProgress();
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        epic.adjustStatus();
        System.out.println("Статус эпика " + epic.getStatus() + " со статусам подзадач: " + subTask1.getStatus() + ", " + subTask2.getStatus());
    }

    private static void testEpicStatusWithAllInProgressSubTasks(TaskManager manager) {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        subTask1.tryToMoveToInProgress();
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        subTask2.tryToMoveToInProgress();
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        epic.adjustStatus();
        System.out.println("Статус эпика " + epic.getStatus() + " со статусам подзадач: " + subTask1.getStatus() + ", " + subTask2.getStatus());
    }

    private static void testEpicStatusWithAllDoneSubTasks(TaskManager manager) {
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        subTask1.tryToMmoveToDone();
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        subTask2.tryToMmoveToDone();
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        epic.adjustStatus();
        System.out.println("Статус эпика " + epic.getStatus() + " со статусам подзадач: " + subTask1.getStatus() + ", " + subTask2.getStatus());
    }
}

import model.Epic;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.createSimpleTask(new Task("Проверить deadline на ЯП", "Когда конец спринта?"));
        Epic epic = new Epic("Сделать финальное задание", "Постараться ничего не забыть");
        SubTask subTask1 = new SubTask(epic, "Написать код", "Внимательно прочитать задание");
        epic.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic, "Сделать коммит", "Не забыть про push");
        epic.addSubTask(subTask2);
        manager.createEpic(epic);
        System.out.println(manager.getAllSimpleTasks());
        System.out.println(manager.getAllEpics());
        subTask1.tryToMoveToInProgress();
        System.out.println(manager.getAllEpics());
        subTask1.tryToMmoveToDone();
        subTask2.tryToMmoveToDone();
        System.out.println(manager.getAllEpics());
        manager.deleteTask(epic.getId());
    }
}

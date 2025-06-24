package kanban;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        testMainLogic(manager);
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
        manager.getEpicById(3L);
        manager.getSubTaskById(5L);
        manager.getSubTaskById(4L);
        manager.getSubTaskById(5L);
        System.out.println(manager.getHistory());
    }

}

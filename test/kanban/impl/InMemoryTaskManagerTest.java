package kanban.impl;

import kanban.Managers;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private InMemoryTaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = (InMemoryTaskManager) Managers.getDefault();
    }


    @Override
    InMemoryTaskManager getManager() {
        return manager;
    }
}
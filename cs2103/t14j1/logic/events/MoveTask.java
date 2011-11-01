package cs2103.t14j1.logic.events;

import cs2103.t14j1.storage.Task;
import cs2103.t14j1.storage.TaskLists;

/**
 * a move task event
 * 
 * @author Zhuochun
 * 
 */
public class MoveTask extends Event {

    int index;
    Task task;
    String oldListName;
    String newListName;

    public void register(Object... objs) {
        if (objs[0] instanceof Integer) {
            index = (Integer) objs[0];
            task = null;
        } else if (objs[0] instanceof Task) {
            task = (Task) objs[0];
            index = -1;
        }

        newListName = (String) objs[1];
        oldListName = task.getList();
    }

    public boolean execute() {
        String feedback = null;
        boolean success = false;

        try {
            if (index != -1) {
                task = eventHandler.getTask(index);
            }

            TaskLists lists = eventHandler.getLists();

            lists.moveTask(newListName, task);

            eventHandler.setModified();
            eventHandler.refreshAll();

            success  = true;
            feedback = String.format(eventHandler.getMsg("msg.MOVE"), task.getName(), newListName);
        } catch (IllegalArgumentException e) {
            success  = false;
            feedback = e.getMessage();
        } catch (NullPointerException e) {
            success  = false;
            feedback = e.getMessage();
        } catch (IndexOutOfBoundsException e) {
            success  = false;
            feedback = e.getMessage();
        }

        eventHandler.setStatus(feedback);
        return success;
    }

    public boolean hasUndo() {
        return true;
    }

    public Event undo() {
        Event undo = new MoveTask();
        undo.setEventLisnter(eventHandler);

        undo.register(task, oldListName);
        
        boolean success = undo.execute();
        
        if (success) {
            return undo;
        } else {
            return null;
        }
    }

}

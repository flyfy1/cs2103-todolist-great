package logic.events;

import storage.TaskList;

/**
 * a Add List event
 * 
 * @author Zhuochun
 * 
 */
public class AddList extends Event {

    String listname;
    TaskList list;

    public void register(Object... objs) {
        if (objs[0] instanceof String) {
            listname = (String) objs[0];
            list = null;
        } else if (objs[0] instanceof TaskList) {
            list = (TaskList) objs[0];
            listname = list.getName();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean execute() {
        String feedback;
        boolean success = false;

        try {
            if (list == null) {
                list = new TaskList(listname);
            }

            success = eventHandler.getLists().addList(list);

            if (success) {
                eventHandler.setModified();
                eventHandler.displayList(listname);
                eventHandler.switchToList(listname);

                feedback = String.format(eventHandler.getMsg("msg.ADD_SUCCESS"), "List", listname);
            } else {
                feedback = String.format(eventHandler.getMsg("msg.LIST_EXIST"), listname);
            }
        } catch (NullPointerException e) {
            feedback = e.getMessage();
            success  = false;
        }

        eventHandler.setStatus(feedback);
        return success;
    }

    public boolean hasUndo() {
        return true;
    }

    public Event undo() {
        Event undo = new DeleteList();
        undo.setEventLisnter(eventHandler);

        undo.register(listname);
        
        boolean success = undo.execute();
        
        if (success) {
            return undo;
        } else {
            return null;
        }
    }
}

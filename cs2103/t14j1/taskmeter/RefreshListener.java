package cs2103.t14j1.taskmeter;

/**
 * allow reminderDialog to display tray reminder
 * 
 * @author Zhuochun
 *
 */
public interface RefreshListener {
    
    /**
     * refresh the tasks
     */
    public void refresh();
    
    /**
     * pop system tray reminder
     * 
     * @param title
     * @param msg
     */
    public void trayRemind(String title, String msg);

}

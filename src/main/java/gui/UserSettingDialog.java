package gui;

import gui.reminder.Reminder;

import java.util.Date;

import logic.DateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import storage.user.User;


/**
 * User setting dialog, it refers to storage.user.User class for settings
 * 
 * @author Zhuochun
 *
 */
public class UserSettingDialog extends Dialog {

    protected Shell shell;
    private boolean result;
    

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public UserSettingDialog(Shell parent) {
        super(parent, SWT.NONE);
        result = false;
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public boolean open() {
        createContents();
        center();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }
    

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
        shell.setSize(455, 350);
        shell.setText("User Settings");

        sortMethod();
        
        Label label0 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        label0.setBounds(5, 75, 435, 2);
        
        autoSave();;
        
        Label label1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setBounds(5, 118, 435, 2);
        
        autoComplete();
        
        Label label2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setBounds(5, 155, 435, 2);
        
        defaultReminder();
        
        Label label3 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        label3.setBounds(5, 195, 435, 2);
        
        displayMethod();
    }

    private void defaultReminder() {
        final String[] reminderOptions = {"Start Time", "End Time", "Deadline"};
        final Reminder[] reminders = {Reminder.START, Reminder.END, Reminder.DEADLINE};
        
        final Label lblDefaultReminder = new Label(shell, SWT.NONE);
        lblDefaultReminder.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblDefaultReminder.setBounds(10, 165, 195, 20);
        lblDefaultReminder.setText("Default Reminder Parameter");
        
        final Combo cbReminder = new Combo(shell, SWT.READ_ONLY);
        cbReminder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setDefaultReminder(reminders[cbReminder.getSelectionIndex()]);
            }
        });
        cbReminder.setItems(reminderOptions);
        cbReminder.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbReminder.setBounds(215, 162, 180, 28);
        cbReminder.select(User.getDefaultReminder().ordinal());
    }

    private void displayMethod() {
        final Button btnDisplayDurationIn = new Button(shell, SWT.CHECK);
        btnDisplayDurationIn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setUseAbbreviate(btnDisplayDurationIn.getSelection());
                result = true;
            }
        });
        btnDisplayDurationIn.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        btnDisplayDurationIn.setBounds(10, 205, 225, 20);
        btnDisplayDurationIn.setText("Display Duration in Abbreviate");
        btnDisplayDurationIn.setSelection(User.isUseAbbreviate());
        
        Label lbldDay = new Label(shell, SWT.NONE);
        lbldDay.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
        lbldDay.setBounds(240, 208, 194, 20);
        lbldDay.setText("(D - Day, H - Hour, M - Minute)");
        
        final Button btnDisplayDeadlineLike = new Button(shell, SWT.CHECK);
        btnDisplayDeadlineLike.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setDurationAlike(btnDisplayDeadlineLike.getSelection());
                result = true;
            }
        });
        btnDisplayDeadlineLike.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        btnDisplayDeadlineLike.setBounds(10, 230, 225, 20);
        btnDisplayDeadlineLike.setText("Display Deadline Like Duration");
        btnDisplayDeadlineLike.setSelection(User.isDurationAlike());
        
        Label lblegHour = new Label(shell, SWT.NONE);
        lblegHour.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
        lblegHour.setBounds(240, 233, 98, 15);
        lblegHour.setText("(eg. 1 Hour Ago)");
        
        Label lblChooseDateAnd = new Label(shell, SWT.NONE);
        lblChooseDateAnd.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblChooseDateAnd.setBounds(10, 260, 404, 20);
        lblChooseDateAnd.setText("Choose Date and Time Display:");
        
        final Date now = DateFormat.getNow();
        
        final Combo cbDate = new Combo(shell, SWT.READ_ONLY);
        cbDate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setUserDateFormat(cbDate.getSelectionIndex());
                result = true;
            }
        });
        String[] dates = new String[User.dateFormats.length];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = DateFormat.dateToStr(now, User.dateFormats[i]);
        }
        cbDate.setItems(dates);
        cbDate.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbDate.setBounds(10, 285, 185, 28);
        cbDate.select(User.getUserDateFormat());
        
        final Combo cbGap = new Combo(shell, SWT.READ_ONLY);
        cbGap.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setGapBetween(cbGap.getSelectionIndex());
                result = true;
            }
        });
        cbGap.setItems(User.gapBetween);
        cbGap.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbGap.setBounds(205, 285, 45, 28);
        cbGap.select(User.getGapBetween());
        
        final Combo cbTime = new Combo(shell, SWT.READ_ONLY);
        cbTime.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setUserTimeFormat(cbTime.getSelectionIndex());
                result = true;
            }
        });
        String[] times = new String[User.timeFormats.length];
        for (int i = 0; i < times.length; i++) {
            times[i] = DateFormat.dateToStr(now, User.timeFormats[i]);
        }
        cbTime.setItems(times);
        cbTime.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbTime.setBounds(260, 285, 181, 28);
        cbTime.select(User.getUserTimeFormat());
    }
    
    private final String[] autoSaveOptions = {"2", "5 (Default)", "10", "15", "20"};
    private final int[]    autoSaveTime    = {120000, 300000, 600000, 900000, 1200000};

    private void autoSave() {
        Label lblAutoSaveTasks = new Label(shell, SWT.NONE);
        lblAutoSaveTasks.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblAutoSaveTasks.setBounds(10, 85, 185, 20);
        lblAutoSaveTasks.setText("Auto Save TaskMeter Every");
        
        final Combo cbAutoSave = new Combo(shell, SWT.READ_ONLY);
        cbAutoSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setAutoSaveTime(autoSaveTime[cbAutoSave.getSelectionIndex()]);
            }
        });
        cbAutoSave.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbAutoSave.setItems(autoSaveOptions);
        cbAutoSave.setBounds(201, 82, 110, 28);
        cbAutoSave.select(getAutoSaveSelection());
        
        Label lblMinutes = new Label(shell, SWT.NONE);
        lblMinutes.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblMinutes.setBounds(325, 85, 60, 20);
        lblMinutes.setText("Minutes");
    }

    private void autoComplete() {
        final Button btnPerformAutoComplete = new Button(shell, SWT.CHECK);
        btnPerformAutoComplete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setPerformAutoComplete(btnPerformAutoComplete.getSelection());
            }
        });
        btnPerformAutoComplete.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        btnPerformAutoComplete.setBounds(10, 126, 431, 20);
        btnPerformAutoComplete.setSelection(User.performAutoComplete());
        btnPerformAutoComplete.setText("Perform Auto Complete in SmartBar");
    }
    
    private int getAutoSaveSelection() {
        for (int i = 0; i < autoSaveTime.length; i++) {
            if (autoSaveTime[i] == User.getAutoSaveTime())
                return i;
        }
        return 0;
    }

    private void sortMethod() {
        final String[] SortMethods = { "Priority", "Start Date", "Deadline", "Duration", "Status" };
        
        Label lblSort = new Label(shell, SWT.NONE);
        lblSort.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblSort.setBounds(10, 10, 205, 20);
        lblSort.setText("Default Tasks Sorting Method:");
        
        Label lblDefaultDeadlineStart = new Label(shell, SWT.NONE);
        lblDefaultDeadlineStart.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
        lblDefaultDeadlineStart.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
        lblDefaultDeadlineStart.setBounds(215, 11, 220, 20);
        lblDefaultDeadlineStart.setText("(Default: Deadline, Start Date, Priority)");

        Label lblFirst = new Label(shell, SWT.NONE);
        lblFirst.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblFirst.setBounds(10, 44, 32, 20);
        lblFirst.setText("First");

        final Combo cbFirst = new Combo(shell, SWT.READ_ONLY);
        cbFirst.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setSortingMethod(0, cbFirst.getSelectionIndex() + 2);
            }
        });
        cbFirst.setItems(SortMethods);
        cbFirst.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbFirst.setBounds(48, 40, 96, 20);
        cbFirst.select(User.getSortingMethod(0) - 2);

        Label lblSecond = new Label(shell, SWT.NONE);
        lblSecond.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblSecond.setBounds(150, 44, 55, 20);
        lblSecond.setText("Second");

        final Combo cbSecond = new Combo(shell, SWT.READ_ONLY);
        cbSecond.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setSortingMethod(1, cbSecond.getSelectionIndex() + 2);
            }
        });
        cbSecond.setItems(SortMethods);
        cbSecond.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbSecond.setBounds(205, 40, 96, 20);
        cbSecond.select(User.getSortingMethod(1) - 2);

        Label lblLast = new Label(shell, SWT.NONE);
        lblLast.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        lblLast.setBounds(307, 44, 32, 20);
        lblLast.setText("Last");

        final Combo cbLast = new Combo(shell, SWT.READ_ONLY);
        cbLast.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                User.setSortingMethod(2, cbLast.getSelectionIndex() + 2);
            }
        });
        cbLast.setItems(SortMethods);
        cbLast.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
        cbLast.setBounds(345, 40, 96, 20);
        cbLast.select(User.getSortingMethod(2) - 2);
    }
    
    private void center() {
        Rectangle parent = getParent().getBounds();
        Rectangle rect = shell.getBounds();
        int x = parent.x + (parent.width - rect.width) / 2;
        int y = parent.y + (parent.height - rect.height) / 2;
        shell.setLocation(x, y);
    }
}

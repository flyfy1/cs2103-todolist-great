package storage.user;

import gui.reminder.Reminder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import storage.TaskList;


/**
 * a default User setting
 * 
 * @author Zhuochun
 * 
 */
public class User {
    
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    private static String  folder      = "user/";
    private static String  name        = "default";
    private static String  setting     = "setting.txt";
    private static boolean isFirstTime = false;

    private static final String[] saveStrings = {
        "AutoComplete", "SortingMethod", "AutoSaveTime", "DefaultRemindType",
        "useAbbreviate", "useDurationLike", "UserTimeFormat", "UserDateFormat"
        };

    // Settings
    private static boolean  performAutoComplete = false; // default close auto completion
    private static int[]    sortingMethod       = { TaskList.SORT_DEADLINE,
                                                    TaskList.SORT_DATE,
                                                    TaskList.SORT_PRIORITY };
    private static int      autoSaveTime        = 300000; // default 5 mins
    private static Reminder defaultReminder     = Reminder.START; // default remind type
    private static boolean  useAbbreviate       = false; // use abbreviate word for day,hour,minute
    private static boolean  useDurationAlike    = false; // duration like display method
    private static int      userTimeFormat      = 0; // default 0
    private static int      userDateFormat      = 0; // default 0
    private static int      gapBetweenDateTime  = 0; // default 0

    // Other informations
    public static final int defaultSortingNum   = 3;
    
    // Date and Time format
    public static final String[] timeFormats = { "HH:mm", "h:mm a", "hh:mm a" };
    public static final String[] gapBetween  = { ", ", " " };
    public static final String[] dateFormats = { "MMM dd", "EEE, MMM d", "MMMM dd", "yyyy, MMM d", "dd-MM-yyyy",
            "yyyy-MM-dd", "dd/MM/yyyy" };
    
    // Exception Strings
    private static final String EXCEPTION_EMPTY_NAME = "User name cannot be null or empty";

    public static String getName() {
        return name;
    }

    public static void setName(String newName) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException(EXCEPTION_EMPTY_NAME);
        }

        name = newName;
    }

    public static boolean isDurationAlike() {
        return useDurationAlike;
    }

    public static void setDurationAlike(boolean set) {
        useDurationAlike = set;
    }

    public static boolean isUseAbbreviate() {
        return useAbbreviate;
    }

    public static void setUseAbbreviate(boolean use) {
        useAbbreviate = use;
    }

    public static Reminder getDefaultReminder() {
        return defaultReminder;
    }

    public static void setDefaultReminder(Reminder i) {
        defaultReminder = i;
    }

    public static int getSortingMethod(int i) {
        return sortingMethod[i];
    }

    public static void setSortingMethod(int i, int newMethod) {
        sortingMethod[i] = newMethod;
    }

    public static int getAutoSaveTime() {
        return autoSaveTime;
    }

    public static void setAutoSaveTime(int length) {
        autoSaveTime = length;
    }

    public static boolean performAutoComplete() {
        return performAutoComplete;
    }

    public static void setPerformAutoComplete(boolean perform) {
        performAutoComplete = perform;
    }

    public static String getUserPath() {
        return folder + name + "/";
    }
    
    public static String getTimeFormat() {
        return timeFormats[userTimeFormat];
    }

    public static int getUserTimeFormat() {
        return userTimeFormat;
    }

    public static void setUserTimeFormat(int i) {
        userTimeFormat = i;
    }
    
    public static String getDateFormat() {
        return dateFormats[userDateFormat];
    }

    public static int getUserDateFormat() {
        return userDateFormat;
    }

    public static void setUserDateFormat(int i) {
        userDateFormat = i;
    }
    
    public static String getGapBetweenDateTime() {
        return gapBetween[gapBetweenDateTime];
    }

    public static int getGapBetween() {
        return gapBetweenDateTime;
    }

    public static void setGapBetween(int i) {
        gapBetweenDateTime = i;
    }
    
    public static String getDateTimeFormat() {
        StringBuilder result = new StringBuilder();
        
        result.append(getDateFormat());
        result.append(gapBetween[gapBetweenDateTime]);
        result.append(getTimeFormat());
        
        return result.toString();
    }

    public static boolean isFirstTimeUser() {
        return isFirstTime;
    }

    public static void setFirstTimeUser() {
        isFirstTime = true;
    }

    public static void initial() {
        String filename = getUserPath() + setting;

        try {
            File file = new File(filename);

            // read in file
            if (file.isFile()) {
                BufferedReader r = new BufferedReader(new FileReader(file));
                // load user setting
                String line = r.readLine(); // First Line is User Name

                String[] terms;

                // load performAutoComplete
                while ((line = r.readLine()) != null) {
                    terms = line.split(" = ");
                    
                    //System.out.println(terms[0] + " -> " + terms[1]);

                    if (terms[0].equals(saveStrings[0])) {
                        performAutoComplete = terms[1].equals("true") ? true : false;
                    } else if (terms[0].equals(saveStrings[1])) {
                        String[] items = terms[1].split(",");
                        for (int i = 0; i < 3; i++) {
                            sortingMethod[i] = Integer.parseInt(items[i]);
                        }
                    } else if (terms[0].equals(saveStrings[2])) {
                        autoSaveTime = Integer.parseInt(terms[1]);
                    } else if (terms[0].equals(saveStrings[3])) {
                        defaultReminder = Reminder.valueOf(terms[1]);
                    } else if (terms[0].equals(saveStrings[4])) {
                        useAbbreviate = terms[1].equals("true") ? true : false;
                    } else if (terms[0].equals(saveStrings[5])) {
                        useDurationAlike = terms[1].equals("true") ? true : false; 
                    } else if (terms[0].equals(saveStrings[6])) {
                        userTimeFormat = Integer.parseInt(terms[1]);
                    } else if (terms[0].equals(saveStrings[7])) {
                        userDateFormat = Integer.parseInt(terms[1]);
                    }
                }
            }
            
            LOGGER.log(Level.FINE, "Load User Setting Success");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Load User Setting", e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Load User Setting", e);
        }

    }

    public static void save() {
        String filename = getUserPath() + setting;
        
        try {
            // open file
            File file = new File(filename);
            FileWriter w = new FileWriter(file);

            // write line by line
            w.write("[User Settings]\n");
            w.write(saveStrings[0] + " = " + performAutoComplete + "\n");
            w.write(saveStrings[1] + " = ");
            for (int i = 0; i < 2; i++) {
                w.write(sortingMethod[i] + ",");
            }
            w.write(sortingMethod[2] + "\n");
            w.write(saveStrings[2] + " = " + autoSaveTime + "\n");
            w.write(saveStrings[3] + " = " + defaultReminder + "\n");
            w.write(saveStrings[4] + " = " + useAbbreviate + "\n");
            w.write(saveStrings[5] + " = " + useDurationAlike + "\n");
            w.write(saveStrings[6] + " = " + userTimeFormat + "\n");
            w.write(saveStrings[7] + " = " + userDateFormat + "\n");

            w.close();
            
            LOGGER.log(Level.FINE, "Save User Setting");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Save User Setting", e);
        }
    }
}

package cs2103.t14j1.logic.smartbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegexMatcher {
	
	public static void main(String args[]){
		// for testing only
		String str = "10:30:20 ,12/Oct/2011";
		System.out.println("matched Str: " + regexMatchedStr(regDateTimeOverallFormat, str, true));
	}
	
	protected static final boolean IGNORE_CASE = true;
	protected static final boolean CARE_CASE = false;
	
	protected String matchedStr;
	protected String originalStr;
	
	
	/* define the regex */
	protected static final String regexWordSpacer = "([^\\d\\w#!@]|(^)|($))";
	protected static final String regexSpacer = "((\\s)|(^)|($))";
	protected static final String regexNonSpaceWordSpacer = "([^\\d\\w\\ ]|(^)|($))";
	protected static final String regexDateSpacer = "[,-/. ]+";
	
		// time related
	// regular expression for matching the time
	protected static final String regTimePointAmPm = // match the 5am/pm, 5 am/pm, or 5:00 am/pm. 
			"(1[012]|\\d)(:[0-5]\\d){0,2}((\\ )?[ap]m)";
	protected static final String regTimePoint24H = 
			"([01]?\\d|2[0-3])(:[0-5]\\d){1,2}";
	protected static final String regTimeFormat = // can be either 24 hour, or am/pm
			"((at" + regexWordSpacer + ")?" + "(" + regTimePointAmPm + "|" + regTimePoint24H +")" +  ")";
	
		// regular expression for matching the date
	protected static final String regMonthText =
			"(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|" + 
			"August|Aug|September|Sept|October|Oct|November|Nov|December|Dec)";
	protected static final String regWeekText =
			"(Monday|Mon|Tuesday|Tue|Wednesday|Wed|Thursday|Thur|Friday|Fri|Saturday|Sat|Sunday|Sun)";
	protected static final String regDayOrder = "((this)?|next)";

			// date format
	protected static final String regDateFormat_dd_$mm$_$yy$yy$$ = 
			"(([12][0-9]|3[01]|(0)?[1-9])" + regexDateSpacer + "(0[1-9]|1[012])(" + 
			regexDateSpacer + "(19|20)?\\d\\d)?)";
	protected static final String regDateFormat_dd_$M$_$yy$yy$$ = // When month is text, year should be a full string if exist
			"(([12][0-9]|3[01]|(0)?[1-9])(st|nd|rd|th)?" + regexDateSpacer + "(\\ )*" +
			regMonthText + "(" + regexDateSpacer + "(19|20)\\d\\d)?)";
	protected static final String regDateFormat_M_dd_$yyyy$ = "(" +
		regMonthText + regexDateSpacer + "([12][0-9]|3[01]|(0)?[1-9])(st|nd|rd|th)?(" + 
		regexDateSpacer + "(19|20)\\d\\d)?)";
	protected static final String regDateFormat_dd_$mm$M$_$yy$yy$$ = 
			"((on\\ )?(" + regDateFormat_dd_$M$_$yy$yy$$+ "|" + regDateFormat_dd_$mm$_$yy$yy$$ + "))";
	
	protected static final String regDateFormat_order_weekD = 
			"((on\\ )?(" + regDayOrder + regexWordSpacer + ")?" + regWeekText + ")";
	
	protected static final String regDateFormat_today_tomorrow = 
			"(today|tomorrow|tmr)";
	
		// the date format for mm/dd/yy; leave it here for the possible use in the future
	protected static final String regDateFormat_mm_dd_yy =
			"(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])([- /.](19|20)\\d\\d)?";
	protected static final String regTimeUnit = 
			"((hour(s)?|h)|(minute(s)?|min)|(second(s)?|sec)|(day(s)?))";
	protected static final String regTimeUnitForSearch = 
			"((this|next)\\ (" + regTimeUnit + "|week|month|year|" + regWeekText + "|" + regMonthText + "))";
	
	protected static final String regDateOverallFormat =
			"(" + regDateFormat_dd_$mm$M$_$yy$yy$$ + "|" + regDateFormat_order_weekD + 
			"|" + regDateFormat_today_tomorrow + ")";
	
	protected static final String regDateTimeOverallFormat = "(" +
//				regTimeFormat +
//			regDateOverallFormat + regWordSpacer + regTimeFormat + 
			"(" + regDateOverallFormat+ "(" + regexWordSpacer + "*" + regTimeFormat + ")?)|" + 	// date (time)?
			"(" + regTimeFormat + "("+ regexWordSpacer + "*" + regDateOverallFormat + ")?)" +  // time (date)?
			")";
	
	
	protected static final String regDurationFormat = "(for(\\ [\\d]+\\ " + regTimeUnit + ")+)";
	protected static final String regReminderAfterTimeFormat = "(in(\\ [\\d]+\\ " + regTimeUnit + ")+)";
	protected static final String regPlaceFormat = 
		"((@[\\w\\d]+)|(@\\([^\\)]+\\)))";	// format: @ + word; or: @ + (words)
	protected static final String regPriorityFormat = "(![123])";
	protected static final String regexList = "((#[\\w]+)|(#\\([^\\)]+\\)))";
	
	protected static final String regexSearchCommand = "^/(" + regexSpacer +")*";
	protected static final String regexAddCommand = "^add(" + regexSpacer +")*";
	protected static final String regexSwitchListCommand = "^" + regexList + "(" + regexSpacer +")*$";
	protected static final String regexDeleteTaskCommand = "^(delete|del)\\ [\\d]+$";	// TODO: would need to change later
	protected static final String regexDeleteListCommand = "^(delete|del)\\ "+ regexList + "$";
	protected static final String regexDisplayTaskCommand = "^(display|dis)\\ [\\d]+";
	protected static final String regexDisplayListCommand = "^(display|dis)\\ " + regexList;
	protected static final String regMoveTaskToListCmd = "^(move|mv)\\ [\\d]+\\ (" + regexList+ "|#)$";
	protected static final String regMarkAsCompleteCmd = "(^(done)\\ [\\d]+$)|(^[\\d]+\\ (done)$)";	// Syntax 1: [num] + done;  Syntax 2: done + [num]
	protected static final String regEditTaskCmd = "^(edit)\\ [\\d]+$";	// simply signal an edit
	protected static final String regSetPriorityCmd = "^[\\d]\\ " + regPriorityFormat + "$";
	protected static final String regAddListCmd = "^(add)(\\ )+" + regexList+ "$";
	protected static final String regRenameListCmd = "^(rename)\\ " + regexList+ "\\ " + regexList+ "$";
	protected static final String regEditListCmd = "^(edit)\\ " + regexList + "$";
	protected static final String regReminderGeneralCmd = "^(remind)\\ [\\d]+\\ (start|end|deadline|" + regDateTimeOverallFormat +")";
	protected static final String regReminderNumOnly = "^(remind)\\ [\\d]+$";
	protected static final String regRemoveReminder = "^(remind)\\ [\\d]+\\ cancel$";
	
	public RegexMatcher(String strToMatch) {
		this.originalStr  =strToMatch;
	}
	
	protected static String regexMatchedStr(String regex, String matchStr,
			boolean ignoreCase) {
		Pattern regPattern = Pattern.compile(
				regex,
				ignoreCase?Pattern.CASE_INSENSITIVE:0);
		Matcher regMatcher = regPattern.matcher(matchStr);
		if(regMatcher.find()){
			return regMatcher.group();
		} else{
			return null;
		}
	}
	
	protected boolean regexMatchedStrAndSaveMatchedStr(String reg,String matchStr,boolean ignoreCase){
		this.matchedStr = regexMatchedStr(reg,matchStr,ignoreCase);
		return this.matchedStr!=null;
	}
	
	
	/**
	 * @param msg
	 * This method is specifically for error tracking
	 */
	protected static void outputErr(String msg){
		try{
			// throw new exception for print stack
			throw new Exception(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected static String regexAddLeadingTailingSpaceMatcher(String regex) {
		return regexSpacer + regex + regexSpacer;
	}
	
	protected static String removeLeadingAndTailingNonDigitLetterDefinedSymbol(String inStr){
		if(inStr == null){
			return inStr;
		}
		
		// the leading word spacer
		while(inStr.length() > 0 && inStr.substring(0, 1).matches(regexWordSpacer)){
			inStr = inStr.substring(1);
		}
		
		// the tailing word spacer
		while(inStr.length() > 0 && inStr.substring(inStr.length()-1).matches(regexWordSpacer)){
			inStr = inStr.substring(0, inStr.length()-1);
		}
		
		return inStr;
	}
}

package goku.action;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import hirondelle.date4j.DateTime;

import java.util.Date;
import java.util.Iterator;

public class SearchAction extends Action {

	public String title;
	public String deadline;
	public String from;
	public String to;
	public Date dline;
	public DateRange period;
	public boolean testFree;
	public DateTime dateQuery;

	public SearchAction(GOKU goku) {
		super(goku);

		// initialise params
		deadline = null;
		from = null;
		to = null;
		dline = null;
		period = null;
		testFree = false;
		dateQuery = null;
	}

	private static final String MSG_SUCCESS = "Found tasks!";
	private static final String MSG_FAIL = "No relevant tasks.";
	private static final String IS_FREE = "Specified datetime is available.";
	private static final String NOT_FREE = "Specified datetime is not available.";
	public static final String ERR_INSUFFICIENT_ARGS = "Can't search! Try \"search title\"";
	public static final String ERR_NO_VALID_DATE_FOUND = "Can't search! Try entering a valid date after \"free\"";
	private static final String ERR_DEADLINE_PERIOD_CONFLICT = "Can't search! Conflicting deadline and period.";

	public Result searchTitle() {
		Task task = new Task();
		task.setTitle(title);
		TaskList foundTasks = list.findTaskByTitle(task);
		return new Result(true, MSG_SUCCESS, null, foundTasks);
	}

	public Result searchByDeadline() {
		Task task = new Task();
		
		assert(dline!=null);
		task.setDeadline(dline);
		TaskList foundTasks = list.findTaskByDeadline(task);
		if (foundTasks.size() != 0) {
			return new Result(true, MSG_SUCCESS, null, foundTasks);
		} else {
			return new Result(false, null, MSG_FAIL, null);
		}
	}

	public Result searchByPeriod() {
		Task task = new Task();
		
		assert(period!=null);
		task.setPeriod(period);
		TaskList foundTasks = list.findTaskByPeriod(task);
		if (foundTasks.size() != 0) {
			return new Result(true, MSG_SUCCESS, null, foundTasks);
		} else {
			return new Result(false, null, MSG_FAIL, null);
		}
	}
	
	public Result checkFreeTime() {
		
		assert(dateQuery!=null);
		
		if(list.isFree(dateQuery) == true) {
			return new Result(true, IS_FREE, null, null);
		} else {
			return new Result(false, null, NOT_FREE, null);
		}
	}

	@Override
	public Result doIt() throws MakeActionException {

		Result result = null;

		if (dateQuery != null) {
			result = checkFreeTime();
		} else if (dline != null && period != null) {
			result = searchByDeadlineAndPeriod();
		} else if (dline != null) {
			result = searchByDeadline();
		} else if (period != null) {
			result = searchByPeriod();
		} else {
			result = searchTitle();
		}

		return result;
	}

	/*
	 * Searches by both deadline and period and merges result
	 */
	private Result searchByDeadlineAndPeriod() throws MakeActionException {

		// check for conflicting deadline and period
		if (!dline.before(period.getEndDate()) || !dline.after(period.getStartDate())) {
			throw new MakeActionException(ERR_DEADLINE_PERIOD_CONFLICT);
		}

		Result mergedResults = null;

		Result byDeadline = searchByDeadline();
		Result byPeriod = searchByPeriod();

		if (byDeadline.isSuccess() && byPeriod.isSuccess()) {
			// merge matching tasks in both results
			Iterator<Task> dlIterator = byDeadline.getTasks().iterator();
			Iterator<Task> prIterator = byPeriod.getTasks().iterator();
			TaskList mergedFoundTasks = new TaskList();

			while (dlIterator.hasNext()) {
				Task dlTask = dlIterator.next();

				while (prIterator.hasNext()) {
					Task prTask = prIterator.next();

					if (!dlTask.equals(prTask)) {
						mergedFoundTasks.addTask(prTask);
					}
				}

				mergedFoundTasks.addTask(dlTask);
				prIterator = byPeriod.getTasks().iterator();
			}

			mergedResults = new Result(true, MSG_SUCCESS, null,	mergedFoundTasks);
		} else if (byDeadline.isSuccess()) {
			mergedResults = byDeadline;
		} else {
			mergedResults = byPeriod;
		}

		return mergedResults;
	}

	public String getDeadline() {
		return deadline;
	}

	public String getTitle() {
		return title;
	}
}

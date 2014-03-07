package goku.action;

import java.util.Date;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

public class SearchAction extends Action {

	public String title;
	public String deadline;
	public String from;
	public String to;
	public Date dline;
	public DateRange period;

	public SearchAction(GOKU goku) {
		super(goku);
		
		// initialise params to null
		deadline = null;
		from = null;
		to = null;
		dline = null;
		period = null;
	}

	private static final String MSG_SUCCESS = "Found tasks!";

	public Result searchTitle() {
		Task task = new Task();
		task.setTitle(title);
		TaskList foundTasks = list.findTaskByTitle(task);
		return new Result(true, MSG_SUCCESS, null, foundTasks);
	}
	
	public Result searchByDeadline() {
		Task task = new Task();
		task.setDeadline(dline);
		TaskList foundTasks = list.findTaskByDeadline(task);
		return new Result(true, MSG_SUCCESS, null, foundTasks);
	}

	@Override
	public Result doIt() {
		return searchTitle();
	}

	public String getDeadline() {
		return deadline;
	}

	public String getTitle() {
		return title;
	}
}

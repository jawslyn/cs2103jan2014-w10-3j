package goku;

public class Result {
  private boolean isSuccess;
  private String successMsg;
  private String errorMsg;
  private Task[] tasks;

  public Result(boolean isSuccess, String successMsg, String errorMsg,
      Task[] tasks) {
    this.isSuccess = isSuccess;
    this.setSuccessMsg(successMsg);
    this.setErrorMsg(errorMsg);
    this.setTasks(tasks);
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public String getSuccessMsg() {
    return successMsg;
  }

  public void setSuccessMsg(String successMsg) {
    this.successMsg = successMsg;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Task[] getTasks() {
    return tasks;
  }

  public void setTasks(Task[] tasks) {
    this.tasks = tasks;
  }
}

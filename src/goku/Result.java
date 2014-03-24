package goku;

import java.util.List;


public class Result {
  private static final String MSG_DEFAULT_SUCCESS = "Success!";
  private static final String MSG_DEFAULT_FAILURE = "Failed.";
  private boolean isSuccess;
  private String successMsg;
  private String errorMsg;
  public List<Task> listOfTask;

  public Result(boolean isSuccess, String successMsg, String errorMsg,
      List<Task> list) {
    this.isSuccess = isSuccess;
    setSuccessMsg(successMsg);
    setErrorMsg(errorMsg);
    setTasks(list);
  }

  public void setTasks(List<Task> list) {
    this.listOfTask = list;
  }

  public static Result makeSuccessResult() {
    return new Result(true, MSG_DEFAULT_SUCCESS, null, null);
  }

  public static Result makeFailureResult() {
    return new Result(false, null, MSG_DEFAULT_FAILURE, null);
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public String getSuccessMsg() {
    return successMsg;
  }

  public List<Task> getTasks() {
    return listOfTask;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public void setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public void setSuccessMsg(String successMsg) {
    this.successMsg = successMsg;
  }
}

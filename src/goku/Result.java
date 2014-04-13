//@author A0099585Y
package goku;

import java.util.List;

public class Result {
  private static final String MSG_DEFAULT_SUCCESS = "Success!";
  private static final String MSG_DEFAULT_FAILURE = "Failed.";
  private boolean isSuccess;
  private String successMsg;
  private String clashMsg;
  private String errorMsg;
  private List<String> listMsg;
  public List<Task> listOfTask;

  public Result(boolean isSuccess, String successMsg, String errorMsg,
      List<Task> list) {
    this.isSuccess = isSuccess;
    setSuccessMsg(successMsg);
    setErrorMsg(errorMsg);
    setTasks(list);
  }

  public Result(boolean isSuccess, String successMsg, String clashMsg,
      String errorMsg, List<Task> list) {
    this.isSuccess = isSuccess;
    setSuccessMsg(successMsg);
    setClashMsg(clashMsg);
    setErrorMsg(errorMsg);
    setTasks(list);
  }

  public Result(boolean isSuccess, String successMsg, String errorMsg,
      List<String> listMsg, List<Task> list) {
    this.isSuccess = isSuccess;
    setSuccessMsg(successMsg);
    setErrorMsg(errorMsg);
    setMsgList(listMsg);
    setTasks(list);
  }

  public void setTasks(List<Task> list) {
    this.listOfTask = list;
  }

  public void setMsgList(List<String> list) {
    this.listMsg = list;
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

  public String getClashMsg() {
    return clashMsg;
  }

  public List<String> getListMsg() {
    return listMsg;
  }

  // @author A0096444X
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

  public void setClashMsg(String clashMsg) {
    this.clashMsg = clashMsg;
  }

  public void setSuccessMsg(String successMsg) {
    this.successMsg = successMsg;
  }
}

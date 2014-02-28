package goku;

public class ActionFactory {
  public static Action buildAction(Command command) {
    Action action;
    switch (command.getType()) {
      case ADD :
        action = new Add();
        break;
      case DELETE :
        action = new Delete();
        break;
      case DISPLAY :
        action = new Display();
        break;
      case SEARCH :
        action = new Search();
        break;
      case EDIT :
        action = new Edit();
        break;
      default :
        action = null;
        throw new Error("Unkonwn command type");
    }
    action.setCommand(command);
    action.construct();
    return action;
  }
}

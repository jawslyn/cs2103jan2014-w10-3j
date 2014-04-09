package goku.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ScrollController extends Controller {
  ScrollPane scrollPane;

  public ScrollController(ScrollPane scrollPane) {
    this.scrollPane = scrollPane;
  }

  @Override
  boolean isHandling(KeyEvent key) {
    return key.getCode() == KeyCode.PAGE_DOWN
        || key.getCode() == KeyCode.PAGE_UP;
  }

  @Override
  void handle(KeyEvent key) {
    System.out.println(this.getClass().toString());
    if (key.getCode() == KeyCode.PAGE_DOWN) {
      scrollPane.setVvalue(scrollPane.getVvalue() + 0.3);
    } else {
      scrollPane.setVvalue(scrollPane.getVvalue() - 0.3);
    }
  }

}

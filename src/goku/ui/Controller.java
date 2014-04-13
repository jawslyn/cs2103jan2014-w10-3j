//@author A0099903R
package goku.ui;

import javafx.scene.input.KeyEvent;

/**
 * A Controller reacts to some particular keys and does an action based on the
 * key that was pressed.
 * 
 * @author ZhiAn
 * 
 */
public abstract class Controller {
  abstract boolean isHandling(KeyEvent key);

  abstract void handle(KeyEvent key);
}

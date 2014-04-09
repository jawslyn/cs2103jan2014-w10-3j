package goku.ui;

import goku.GOKU;
import goku.Result;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXGUI is the JavaFX Application sub-class that is started and shows a window
 * to the user. FXGUI has a number of empty method implementations of
 * UserInterface because of the big differences in the way a CLI and GUI in
 * JavaFX does things.
 * 
 * @author ZhiAn
 * 
 */
public class FXGUI extends Application implements UserInterface {
  private static GOKU goku;
  private static Stage stage;
  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);

  public FXGUI() {
  }

  public FXGUI(GOKU goku) {
    FXGUI.goku = goku;
  }

  public static GOKU getGokuInstance() {
    return goku;
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      FXGUI.stage = primaryStage;
      stage.initStyle(StageStyle.UNDECORATED);
      stage.getIcons().add(
          new Image(FXGUI.class.getResource("icon.png").toExternalForm()));
      Font.loadFont(
          FXGUI.class.getResource("Inconsolata.otf").toExternalForm(), 20);
      AnchorPane page = (AnchorPane) FXMLLoader.load(FXGUI.class
          .getResource("Main.fxml"));
      Scene scene = new Scene(page);
      scene.getStylesheets().add(
          getClass().getResource("app.css").toExternalForm());
      stage.setScene(scene);
      stage.setTitle("GOKU");
      stage.show();
    } catch (IOException e) {
      LOGGER
          .log(Level.SEVERE, "Unable to start GOKU Graphical User Interface.");
      System.err.println("GOKU has failed to start.");
      e.printStackTrace();
    }
  }

  public static Stage getStage() {
    return stage;
  }

  @Override
  public String getUserInput() {
    return null;
  }

  @Override
  public void feedBack(Result result) {
  }

  @Override
  public void run() {
    launch(FXGUI.class);
  }
}

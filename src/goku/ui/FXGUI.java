package goku.ui;

import goku.GOKU;
import goku.Result;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FXGUI extends Application implements UserInterface {
  private static GOKU goku;

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
    if (goku == null) {
      System.out.println("NULL");
    }
    try {
      // primaryStage.initStyle(StageStyle.UNDECORATED);
      Font.loadFont(
          FXGUI.class.getResource("Inconsolata.otf").toExternalForm(), 20);
      AnchorPane page = (AnchorPane) FXMLLoader.load(FXGUI.class
          .getResource("Main.fxml"));
      Scene scene = new Scene(page);
      scene.getStylesheets().add(
          getClass().getResource("app.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("GOKU");
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
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

package goku.ui;

import goku.GOKU;
import goku.Result;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXGUI extends Application implements UserInterface {
  private static GOKU goku;
  private static Stage stage;

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
      primaryStage.initStyle(StageStyle.UNDECORATED);
      Font.loadFont(
          FXGUI.class.getResource("Inconsolata.otf").toExternalForm(), 20);
      AnchorPane page = (AnchorPane) FXMLLoader.load(FXGUI.class
          .getResource("Main.fxml"));
      FXGUI.stage = primaryStage;
      primaryStage.getIcons().add(
          new Image(FXGUI.class.getResource("icon.png").toExternalForm()));

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

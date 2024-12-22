package net.cloudshiku.ioa0032gh.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginView {
    FXMLLoader fmlLoader = new FXMLLoader();
    public Scene LoginView() throws IOException {
        fmlLoader.setLocation(getClass().getResource("login-view.fxml"));
        Scene loginScene = new Scene(fmlLoader.load());
        loginScene.getStylesheets().getClass().getResource("login-style.css");
        return loginScene;
    }
}

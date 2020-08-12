package loginadapters;

import adminadapters.AdminGUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import useradapters.UserMenuGUI;

import java.util.ArrayList;
import java.util.List;

public class LoginGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final LogInController controller;
    private final LogInPresenter presenter;
    private final int width;
    private final int height;
    private final TradeModel model;
    private RunnableGUI nextGUI;
    private GridPane grid;

    public LoginGUI(Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        this.controller = new LogInController(model);
        this.presenter = new LogInPresenter();
        this.width = width;
        this.height = height;
        this.model = model;
    }

    public void usernameTaken(String username) {
        presenter.usernameTaken(username);
        Text message = new Text(presenter.next());
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void invalidAccount(){
        presenter.invalidAccount();
        Text message = new Text(presenter.next());
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 5, 2, 1);
        }
    }

    @Override
    public void initialScreen() {
        presenter.initialScreen();
        stage.setTitle(presenter.next());

        Text title = new Text(presenter.next());
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

//        Button userLoginBtn = new Button("Log in as a trading user");
//        Button adminLoginBtn = new Button("Log in as an admin");
//        Button registerBtn = new Button("Create a new account");
//        Button demoBtn = new Button("Program demo");
        presenter.startMenu();
        List<Button> buttons = new ArrayList<>();
        while (presenter.hasNext()) {
            buttons.add(new Button(presenter.next()));
        }

//        userLoginBtn.setOnAction(actionEvent -> logIn(false));
//        adminLoginBtn.setOnAction(actionEvent -> logIn(true));
//        registerBtn.setOnAction(actionEvent -> newAccount());
//        demoBtn.setOnAction(actionEvent -> {
//            controller.demo();
//        });
        buttons.get(0).setOnAction(actionEvent -> {
            logIn(false);
        });
        buttons.get(1).setOnAction(actionEvent -> {
            logIn(true);
        });
        buttons.get(2).setOnAction(actionEvent -> {
            newAccount();
        });
        buttons.get(3).setOnAction(actionEvent -> {
            controller.demo();
        });

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

//        grid.add(title, 0, 0, 2, 1);
//        grid.add(userLoginBtn, 0, 1, 2, 1);
//        grid.add(adminLoginBtn, 0, 2, 2, 1);
//        grid.add(registerBtn, 0, 3, 2, 1);
//        grid.add(demoBtn, 0, 4, 2, 1);
        grid.add(title, 0, 0, 2, 1);
        for (int i = 0; i < buttons.size(); i++) {
            grid.add(buttons.get(i), 0, i+1, 2, 1);
        }

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void logIn(boolean isAdmin){
        presenter.logIn(isAdmin);
        Text title = new Text(presenter.next());

        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        Label usernameLabel = new Label(presenter.next());
        TextField usernameField = new TextField();
        Label passwordLabel = new Label(presenter.next());
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button(presenter.next());
        HBox hBoxLoginButton = new HBox(10);
        hBoxLoginButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLoginButton.getChildren().add(loginButton);

        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(hBoxLoginButton, 1, 4);

        loginButton.setOnAction(actionEvent -> {
            logInHandler(usernameField.getText(), passwordField.getText(), isAdmin);
        });

        usernameField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                logInHandler(usernameField.getText(), passwordField.getText(), isAdmin);
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                logInHandler(usernameField.getText(), passwordField.getText(), isAdmin);
            }
        });

        backButton();

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    private void newAccount(){
        presenter.createAccount();
        Text title = new Text(presenter.next());
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

//        GridPane grid = new GridPane();
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        Label nameLabel = new Label(presenter.next());
        TextField nameField = new TextField();
        Label usernameLabel = new Label(presenter.next());
        TextField usernameField = new TextField();
        Label passwordLabel = new Label(presenter.next());
        PasswordField passwordField = new PasswordField();
        Label cityLabel = new Label(presenter.next());
        TextField cityField = new TextField();

        Button registerButton = new Button(presenter.next());
        HBox hBoxRegisterButton = new HBox(10);
        hBoxRegisterButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxRegisterButton.getChildren().add(registerButton);

        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(usernameLabel, 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(cityLabel, 0, 4);
        grid.add(cityField, 1, 4);
        grid.add(hBoxRegisterButton, 1, 5);

        registerButton.setOnAction(actionEvent -> {
            if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || cityField.getText().isEmpty()) {
                tryAgain();
            } else if(controller.newTradingUser(nameField.getText(), usernameField.getText(), passwordField.getText(), cityField.getText())){
                nextGUI = new UserMenuGUI(stage, width, height, model, usernameField.getText());
                nextGUI.initialScreen();
            } else {
                usernameTaken(nameField.getText());
            }
        });

        backButton();

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    public void tryAgain() {
        presenter.tryAgain();
        Text message = new Text(presenter.next());
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 10, 1, 1);
        }
    }

    private void backButton() {
        presenter.backButton();
        Button backButton = new Button(presenter.next());
        grid.add(backButton, 0, 6);
        backButton.setOnAction(actionEvent -> {
            initialScreen();
        });
    }

    private void logInHandler(String username, String password, boolean isAdmin) {
        if(controller.logIn(isAdmin, username, password)){
            model.setCurrentUser(username);
            if (isAdmin){
                nextGUI = new AdminGUI(stage, width, height, model);
            } else {
                nextGUI = new UserMenuGUI(stage, width, height, model, username);
            }
            nextGUI.initialScreen();
        } else {
            invalidAccount();
        }
    }
}

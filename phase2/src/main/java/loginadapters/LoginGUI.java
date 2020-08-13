package loginadapters;

import adminadapters.AdminGUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import trademisc.UserMainGUI;
import useradapters.DemoGUI;

public class LoginGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final LogInController controller;
    private final int width;
    private final int height;
    private final TradeModel model;
    private RunnableGUI nextGUI;
    private GridPane grid;

    public LoginGUI(Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        this.controller = new LogInController(model);
        this.width = width;
        this.height = height;
        this.model = model;
    }

    public void usernameTaken(String username) {
        Text message = new Text("Username " + username + " is already taken.");
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void invalidAccount(){
        Text message = new Text("Incorrect username or password.");
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 5, 2, 1);
        }
    }

    @Override
    public void initialScreen() {
        showScreen();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeScreen() {
        stage.setTitle("Trading System - Login");

        Text title = new Text("Welcome");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button userLoginBtn = new Button("Log in as a trading user");
        Button adminLoginBtn = new Button("Log in as an admin");
        Button registerBtn = new Button("Create a new account");
        Button demoBtn = new Button("Program demo");

        userLoginBtn.setOnAction(actionEvent -> logIn(false));
        adminLoginBtn.setOnAction(actionEvent -> logIn(true));
        registerBtn.setOnAction(actionEvent -> newAccount());
        demoBtn.setOnAction(actionEvent -> demoUserLogIn());

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        grid.add(userLoginBtn, 0, 1, 2, 1);
        grid.add(adminLoginBtn, 0, 2, 2, 1);
        grid.add(registerBtn, 0, 3, 2, 1);
        grid.add(demoBtn, 0, 4, 2, 1);
    }

    private void logIn(boolean isAdmin){
        Text title;
        if (isAdmin) {
            title = new Text("Admin login");
        } else {
            title = new Text("Trader login");
        }

        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Log In");
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
        Text title = new Text("Create a new account");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label cityLabel = new Label("City:");
        TextField cityField = new TextField();

        Button registerButton = new Button("Register");
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
                nextGUI = new UserMainGUI(800, 800, model, usernameField.getText());
                nextGUI.initialScreen();
            } else {
                usernameTaken(nameField.getText());
            }
        });

        backButton();

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    private void demoUserLogIn() {
        Text title = new Text("Demo Welcome");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        Label usernameLabel = new Label("Username: ");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Log In");
        HBox hBoxLoginButton = new HBox(10);
        hBoxLoginButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLoginButton.getChildren().add(loginButton);

        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(hBoxLoginButton, 1, 4);

        loginButton.setOnAction(actionEvent -> {
            if (usernameField.getText().equals("u1") && passwordField.getText().equals("Password123")) {
                nextGUI = new DemoGUI(usernameField.getText(), stage, width, height, model);
                nextGUI.initialScreen();
            } else {
                invalidAccount();
            }
        });

        backButton();

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    public void tryAgain() {
        Text message = new Text("Please try again");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 10, 1, 1);
        }
    }

    private void backButton() {
        Button backButton = new Button("Back");
        grid.add(backButton, 0, 6);
        backButton.setOnAction(actionEvent -> {
            initialScreen();
        });
    }

    private void logInHandler(String username, String password, boolean isAdmin) {
        if(controller.logIn(isAdmin, username, password)){
            model.setCurrentUser(username);
            if (isAdmin){
                nextGUI = new AdminGUI(stage, 800, 800, model);
            } else {
                nextGUI = new UserMainGUI(800, 800, model, username);
            }
            nextGUI.showScreen();
        } else {
            invalidAccount();
        }
    }
}

package loginadapters;

import adminadapters.AdminMainGUI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import trademisc.UserMainGUI;
import useradapters.DemoMainGUI;

import java.lang.reflect.Type;
import java.util.Map;

public class LoginGUI implements RunnableGUI {
    private final Stage stage;
    private final LogInController controller;
    private final int width;
    private final int height;
    private final TradeModel tradeModel;
    private RunnableGUI nextGUI;
    private GridPane grid;
    private final VBox root;

    public LoginGUI(Stage stage, int width, int height, TradeModel tradeModel) {
        this.stage = stage;
        this.controller = new LogInController(tradeModel);
        this.width = width;
        this.height = height;
        this.tradeModel = tradeModel;
        this.root = new VBox();
    }

    @Override
    public void initialScreen() {

    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeScreen() {
        stage.setTitle("Trading System - Login");
        changeScreen("login");
    }

    private GridPane logIn(){
        Text title = new Text("Welcome");
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

        Hyperlink newAccountLink = new Hyperlink("Don't have an account?");
        newAccountLink.setBorder(Border.EMPTY);
        newAccountLink.setPadding(new Insets(0, 0, 0, 0));
        newAccountLink.setOnAction(actionEvent -> changeScreen("signup"));
        HBox hBoxNewAccount = new HBox(10);
        hBoxNewAccount.setAlignment(Pos.BOTTOM_LEFT);
        hBoxNewAccount.getChildren().add(newAccountLink);

        Hyperlink demoLink = new Hyperlink("Look around without signing up");
        demoLink.setBorder(Border.EMPTY);
        demoLink.setPadding(new Insets(0, 0, 0, 0));
        demoLink.setOnAction(actionEvent -> {
            demoUserLogIn();
            demoLink.setVisited(false);
        });
        HBox hBoxDemo = new HBox(10);
        hBoxDemo.setAlignment(Pos.BOTTOM_CENTER);
        hBoxDemo.getChildren().add(demoLink);

        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(hBoxLoginButton, 1, 4);
        grid.add(hBoxNewAccount, 0, 4);
        grid.add(hBoxDemo, 0, 7, 2, 1);

        loginButton.setOnAction(actionEvent -> {
            logInHandler(usernameField.getText(), passwordField.getText());
        });

        usernameField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                logInHandler(usernameField.getText(), passwordField.getText());
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                logInHandler(usernameField.getText(), passwordField.getText());
            }
        });

        return grid;
    }


    private void changeScreen(String screen) {
        GridPane newGrid = new GridPane();
        if (screen.equals("login")) {
            newGrid = logIn();
        }
        else if (screen.equals("signup")) {
            newGrid = newAccount();
        }
        root.getChildren().setAll(newGrid);

    }

    private GridPane newAccount(){
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

        Hyperlink logInLink = new Hyperlink("Already have an account?");
        logInLink.setBorder(Border.EMPTY);
        logInLink.setPadding(new Insets(0, 0, 0, 0));
        logInLink.setOnAction(actionEvent -> changeScreen("login"));
        HBox hBoxLogIn = new HBox(10);
        hBoxLogIn.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLogIn.getChildren().add(logInLink);

        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(usernameLabel, 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(cityLabel, 0, 4);
        grid.add(cityField, 1, 4);
        grid.add(hBoxRegisterButton, 1, 5);
        grid.add(hBoxLogIn, 0, 5);

        registerButton.setOnAction(actionEvent -> {
            if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || cityField.getText().isEmpty()) {
                tryAgain();
            } else if(controller.newTradingUser(nameField.getText(), usernameField.getText(), passwordField.getText(), cityField.getText())){
                nextGUI = new UserMainGUI(800, 800, tradeModel, usernameField.getText());
                nextGUI.showScreen();
            } else {
                usernameTaken(usernameField.getText());
            }
        });

        //backButton();
        return grid;
    }

    private void demoUserLogIn() {
        nextGUI = new DemoMainGUI(800, 800, tradeModel);
        nextGUI.showScreen();
    }

    private void logInHandler(String username, String password) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> userInfo = gson.fromJson(controller.logIn(username, password), type);

        if (userInfo.get("authenticated").equals("true")) {
            if (userInfo.get("userType").equals("admin")) {
                nextGUI = new AdminMainGUI( 800, 800, tradeModel);
            }
            else {
                nextGUI = new UserMainGUI(800, 800, tradeModel, username);
            }
            nextGUI.showScreen();
            //stage.hide();
        }
        else {
            invalidAccount();
        }
    }

    public void invalidAccount(){
        Text message = new Text("Incorrect username or password");
        message.setFill(Color.RED);
        HBox messageBox = new HBox(message);
        messageBox.setAlignment(Pos.CENTER);
        if(!grid.getChildren().contains(message)){
            grid.add(messageBox, 0, 5, 2, 1);
        }
    }

    public void usernameTaken(String username) {
        Text message = new Text("Username " + username + " is already taken");
        message.setFill(Color.RED);
        HBox messageBox = new HBox(message);
        messageBox.setAlignment(Pos.CENTER);
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void tryAgain() {
        Text message = new Text("Please try again");
        message.setFill(Color.RED);
        HBox messageBox = new HBox(message);
        messageBox.setAlignment(Pos.CENTER);
        if (!grid.getChildren().contains(message)) {
            grid.add(messageBox, 0, 6, 2, 1);
        }
    }
}

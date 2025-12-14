package ca.humber.vrund.inventory_manager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class InventoryManagerApp extends Application {

    private final TextField tfNumber = new TextField();
    private final TextField tfName = new TextField();
    private final TextField tfPrice = new TextField();

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // Top title bar (similar look to the lab screenshot)
        Label title = new Label("Inventory Manager");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setPadding(new Insets(18, 20, 18, 20));
        title.setStyle("-fx-background-color: #A9C7EF; -fx-font-size: 28px; -fx-font-weight: bold;");
        root.setTop(title);

        // Center content
        Label prompt = new Label("Enter product information:");
        prompt.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(30);
        form.setVgap(25);

        Label lblNumber = new Label("Number");
        Label lblName = new Label("Name");
        Label lblPrice = new Label("Price");
        String labelStyle = "-fx-font-size: 26px; -fx-font-weight: normal;";
        lblNumber.setStyle(labelStyle);
        lblName.setStyle(labelStyle);
        lblPrice.setStyle(labelStyle);

        tfNumber.setPrefWidth(340);
        tfName.setPrefWidth(340);
        tfPrice.setPrefWidth(340);

        form.add(lblNumber, 0, 0);
        form.add(tfNumber, 1, 0);
        form.add(lblName, 0, 1);
        form.add(tfName, 1, 1);
        form.add(lblPrice, 0, 2);
        form.add(tfPrice, 1, 2);

        Button btnQuit = new Button("Quit");
        Button btnSave = new Button("Save");
        btnQuit.setPrefWidth(170);
        btnSave.setPrefWidth(170);
        btnQuit.setStyle("-fx-font-size: 22px;");
        btnSave.setStyle("-fx-font-size: 22px;");

        btnQuit.setOnAction(e -> Platform.exit());
        btnSave.setOnAction(e -> saveProduct());

        HBox buttons = new HBox(80, btnQuit, btnSave);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20, 0, 0, 0));

        VBox center = new VBox(35, prompt, form, buttons);
        center.setPadding(new Insets(35, 35, 35, 35));
        center.setAlignment(Pos.TOP_LEFT);
        root.setCenter(center);

        Scene scene = new Scene(root, 620, 620);
        stage.setTitle("Inventory Manager");
        stage.setScene(scene);
        stage.show();

        tfNumber.requestFocus();
    }

    private void saveProduct() {
        String number = tfNumber.getText().trim();
        String name = tfName.getText().trim();
        String priceText = tfPrice.getText().trim();

        if (number.isEmpty() || name.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please fill Number, Name, and Price.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Price", "Price must be 0 or higher.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Price must be a number (example: 19.99).");
            return;
        }

        // Save in the project working directory (where pom.xml is, when using javafx:run)
        Path file = Paths.get(System.getProperty("user.dir"), "Products.txt");
        String line = number + "\t" + name + "\t" + String.format("%.2f", price) + System.lineSeparator();

        try {
            Files.writeString(file, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not write to Products.txt\n" + ex.getMessage());
            return;
        }

        // Clear fields for next entry
        tfNumber.clear();
        tfName.clear();
        tfPrice.clear();
        tfNumber.requestFocus();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

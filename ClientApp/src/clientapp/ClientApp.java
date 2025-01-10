package clientapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientApp extends Application {

    // Enum to represent the content views
    private enum ContentType {
        ADMIN("Admin Content"),
        USER("User Content"),
        TRACKS("Tracks Content");

        private final String displayText;

        ContentType(String displayText) {
            this.displayText = displayText;
        }

        public String getDisplayText() {
            return displayText;
        }
    }

    // Instance variable to store the body layout
    private VBox body;

    @Override
    public void start(Stage primaryStage) {
        // Create Header Section
        HBox header = createHeader();

        // Create Body Section and store it in the instance variable
        body = createBody();

        // Combine in BorderPane
        BorderPane layout = new BorderPane();
        layout.setTop(header);  // Header at the top
        layout.setCenter(body); // Body in the center

        // Scene and Stage
        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        primaryStage.setTitle("Barebones JavaFX GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(10); // Horizontal layout for header
        header.getStyleClass().add("header");

        // Create a StackPane to center the title
        StackPane titleContainer = createTitleContainer();

        // Create button container and buttons
        HBox buttonContainer = createButtonContainer();

        // Align buttons to the right
        HBox.setHgrow(titleContainer, Priority.ALWAYS); // Allow title container to grow and push buttons to the right

        // Add the title container and button container to the header
        header.getChildren().addAll(titleContainer, buttonContainer);
        return header;
    }

    private StackPane createTitleContainer() {
        StackPane titleContainer = new StackPane();
        Label title = new Label("My App Header");
        title.getStyleClass().add("header-title");
        titleContainer.getChildren().add(title);
        titleContainer.setMaxWidth(Double.MAX_VALUE); // Allow it to expand fully
        return titleContainer;
    }

    private HBox createButtonContainer() {
        HBox buttonContainer = new HBox(10); // Add spacing of 10px between buttons
        buttonContainer.getStyleClass().add("button-container");

        // Create buttons with images
        Button adminButton = createButton("/static/img/admin.png");
        Button userButton = createButton("/static/img/user.png");
        Button tracksButton = createButton("/static/img/tracks.png");

        // Add event listeners to buttons
        adminButton.setOnAction(event -> updateBodyContent(ContentType.ADMIN));
        userButton.setOnAction(event -> updateBodyContent(ContentType.USER));
        tracksButton.setOnAction(event -> updateBodyContent(ContentType.TRACKS));

        // Add buttons to the container
        buttonContainer.getChildren().addAll(adminButton, userButton, tracksButton);
        return buttonContainer;
    }

    private Button createButton(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        return new Button("", imageView); // Empty text, image only
    }

    private VBox createBody() {
        VBox body = new VBox(10); // Vertical layout for body
        body.getStyleClass().add("body");

        // Default content
        Label content = new Label(ContentType.ADMIN.getDisplayText());
        content.getStyleClass().add("body-content");
        body.getChildren().add(content);

        return body;
    }

    private void updateBodyContent(ContentType contentType) {
        // This method is responsible for switching the body content based on the selected button
        Label contentLabel = new Label(contentType.getDisplayText());
        contentLabel.getStyleClass().add("body-content");

        // Update the body layout by replacing existing content
        body.getChildren().setAll(contentLabel); // Replace existing content with new content
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package clientapp;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main Tab Pane
        TabPane tabPane = new TabPane();

        // Users Tab
        Tab usersTab = new Tab("Users", createUsersTab());

        // Audio Management Tab
        Tab audioTab = new Tab("Audio Management", createAudioTab());

        // Categories Tab
        Tab categoriesTab = new Tab("Categories", createCategoriesTab());

        // Subscriptions Tab
        Tab subscriptionsTab = new Tab("Subscriptions", createSubscriptionsTab());

        // Listening & Favorites Tab
        Tab listeningTab = new Tab("Listening & Favorites", createListeningTab());

        // Ratings Tab
        Tab ratingsTab = new Tab("Ratings", createRatingsTab());

        // Packages Tab
        Tab packagesTab = new Tab("Packages", createPackagesTab());

        // Add Tabs to TabPane
        tabPane.getTabs().addAll(usersTab, audioTab, categoriesTab, subscriptionsTab, listeningTab, ratingsTab, packagesTab);

        // Root Scene
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("Audio System GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createUsersTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Gender");

        TextField locationField = new TextField();
        locationField.setPromptText("Location");

        Button addButton = new Button("Add User");
        Button updateButton = new Button("Update User");

        TableView<String> usersTable = new TableView<>();

        vbox.getChildren().addAll(nameField, emailField, ageField, genderCombo, locationField, addButton, updateButton, usersTable);
        return vbox;
    }

    private VBox createAudioTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("Audio Name");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration (seconds)");

        DatePicker uploadDatePicker = new DatePicker();

        Button addAudioButton = new Button("Add Audio");
        Button editAudioButton = new Button("Edit Audio");

        TableView<String> audioTable = new TableView<>();

        vbox.getChildren().addAll(nameField, durationField, uploadDatePicker, addAudioButton, editAudioButton, audioTable);
        return vbox;
    }

    private VBox createCategoriesTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField categoryNameField = new TextField();
        categoryNameField.setPromptText("Category Name");

        Button addCategoryButton = new Button("Add Category");

        TableView<String> categoriesTable = new TableView<>();

        vbox.getChildren().addAll(categoryNameField, addCategoryButton, categoriesTable);
        return vbox;
    }

    private VBox createSubscriptionsTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<String> userCombo = new ComboBox<>();
        userCombo.setPromptText("Select User");

        ComboBox<String> packageCombo = new ComboBox<>();
        packageCombo.setPromptText("Select Package");

        Button subscribeButton = new Button("Subscribe");

        TableView<String> subscriptionsTable = new TableView<>();

        vbox.getChildren().addAll(userCombo, packageCombo, subscribeButton, subscriptionsTable);
        return vbox;
    }

    private VBox createListeningTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<String> userCombo = new ComboBox<>();
        userCombo.setPromptText("Select User");

        ComboBox<String> audioCombo = new ComboBox<>();
        audioCombo.setPromptText("Select Audio");

        TextField startField = new TextField();
        startField.setPromptText("Start Second");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration (seconds)");

        Button logListeningButton = new Button("Log Listening");

        ListView<String> favoritesList = new ListView<>();

        vbox.getChildren().addAll(userCombo, audioCombo, startField, durationField, logListeningButton, favoritesList);
        return vbox;
    }

    private VBox createRatingsTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<String> userCombo = new ComboBox<>();
        userCombo.setPromptText("Select User");

        ComboBox<String> audioCombo = new ComboBox<>();
        audioCombo.setPromptText("Select Audio");

        Slider ratingSlider = new Slider(1, 5, 3);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setShowTickMarks(true);

        Button addRatingButton = new Button("Add Rating");
        Button updateRatingButton = new Button("Update Rating");
        Button deleteRatingButton = new Button("Delete Rating");

        TableView<String> ratingsTable = new TableView<>();

        vbox.getChildren().addAll(userCombo, audioCombo, ratingSlider, addRatingButton, updateRatingButton, deleteRatingButton, ratingsTable);
        return vbox;
    }

    private VBox createPackagesTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField packageNameField = new TextField();
        packageNameField.setPromptText("Package Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        Button addPackageButton = new Button("Add Package");

        TableView<String> packagesTable = new TableView<>();

        vbox.getChildren().addAll(packageNameField, priceField, addPackageButton, packagesTable);
        return vbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

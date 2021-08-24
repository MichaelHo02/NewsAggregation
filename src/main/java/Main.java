import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Scene scene;
    private static FXMLLoader primaryFxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        primaryFxmlLoader = new FXMLLoader(Main.class.getResource("PrimaryView.fxml"));
        scene = new Scene(primaryFxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

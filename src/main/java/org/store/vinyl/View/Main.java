package org.store.vinyl.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.store.vinyl.Data.DemoData;
import org.store.vinyl.ViewModel.VinylBookViewModel;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        VinylBookViewModel viewModel = new VinylBookViewModel(
                DemoData.getVinyls(),
                DemoData.getUsers()
        );

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/org/store/vinyl/View/Vinyl.fxml")
        );

        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == VinylBookController.class) {
                return new VinylBookController(viewModel);
            }

            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(loader.load());
        stage.setTitle("Vinyl Library");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
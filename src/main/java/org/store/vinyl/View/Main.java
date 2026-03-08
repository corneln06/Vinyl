package org.store.vinyl.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.store.vinyl.Data.DemoData;
import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;
import org.store.vinyl.ViewModel.VinylBookViewModel;
import org.store.vinyl.Simulator.VinylUserSimulator;

import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        List<Vinyl> vinyls = DemoData.getVinyls();
        List<User> users = DemoData.getUsers();

        VinylBookViewModel viewModel =
                new VinylBookViewModel(vinyls, users);

        // ---------------- COMMENT FROM LINE 26 TO 37 TO AVOID THE SIMULATOR TO WORK --------------------------
        Thread t1 = new Thread(new VinylUserSimulator(viewModel, vinyls, users.get(0)));
        Thread t2 = new Thread(new VinylUserSimulator(viewModel, vinyls, users.get(1)));
        Thread t3 = new Thread(new VinylUserSimulator(viewModel, vinyls, users.get(2)));

        // Advised to be used on a simulator
        t1.setDaemon(true);
        t2.setDaemon(true);
        t3.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();
        // ---------------- COMMENT FROM LINE 26 TO 37 TO AVOID THE SIMULATOR TO WORK --------------------------

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
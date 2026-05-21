package org.store.vinyl.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.store.vinyl.Data.DemoData;
import org.store.vinyl.Model.User;
import org.store.vinyl.Server.Client;
import org.store.vinyl.Services.VinylsService;
import org.store.vinyl.ViewModel.VinylBookViewModel;

import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        User currentUser = resolveCurrentUser(getParameters().getRaw());

        Client client = new Client();
        client.connect();
        client.startListening();

        VinylsService service = new VinylsService(client, currentUser);
        VinylBookViewModel viewModel =
                new VinylBookViewModel(List.of(), currentUser);

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/org/store/vinyl/View/Vinyl.fxml")
        );

        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == VinylBookController.class) {
                VinylBookController controller = new VinylBookController(viewModel);
                controller.initService(service);
                return controller;
            }

            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(loader.load());
        stage.setTitle("Vinyl Library - " + currentUser.getName());
        stage.setScene(scene);
        stage.show();
    }

    private User resolveCurrentUser(List<String> args) {
        List<User> users = DemoData.getUsers();

        if (!args.isEmpty()) {
            String requestedUser = args.get(0).trim().toLowerCase();
            for (User user : users) {
                if (user.getUserId().equalsIgnoreCase(requestedUser)
                    || user.getName().equalsIgnoreCase(requestedUser)) {
                    return user;
                }
            }
        }

        String generatedId = "client-" + System.currentTimeMillis();
        return new User("Client " + generatedId.substring(generatedId.length() - 5), generatedId);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

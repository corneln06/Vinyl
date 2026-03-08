package org.store.vinyl.Simulator;

import javafx.application.Platform;
import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;
import org.store.vinyl.ViewModel.VinylBookViewModel;

import java.util.List;
import java.util.Random;

public class VinylUserSimulator implements Runnable {

    private final VinylBookViewModel viewModel;
    private final List<Vinyl> vinyls;
    private final User user;
    private final Random random = new Random();

    public VinylUserSimulator(VinylBookViewModel viewModel, List<Vinyl> vinyls, User user) {
        this.viewModel = viewModel;
        this.vinyls = vinyls;
        this.user = user;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(random.nextInt(5000) + 1000);

                if (vinyls.isEmpty()) {
                    continue;
                }

                Vinyl vinyl = vinyls.get(random.nextInt(vinyls.size()));
                int action = random.nextInt(3);

                Platform.runLater(() -> {
                    viewModel.selectedUserNameProperty().set(user.getName());

                    switch (action) {
                        case 0 -> tryBorrow(vinyl);
                        case 1 -> tryReserve(vinyl);
                        case 2 -> tryReturn(vinyl);
                    }

                    System.out.println(user.getName() + " performed an action on " + vinyl.getTitle());
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void tryBorrow(Vinyl vinyl) {
        String state = vinyl.getCurrentState().getStateName();

        if ("Available".equals(state)) {
            vinyl.borrow(user);
        } else if ("Reserved".equals(state) && user.getUserId().equals(vinyl.getReservedBy())) {
            vinyl.borrow(user);
        }
    }

    private void tryReserve(Vinyl vinyl) {
        String state = vinyl.getCurrentState().getStateName();

        if ("Borrowed".equals(state)
                && (vinyl.getReservedBy() == null || vinyl.getReservedBy().isEmpty())
                && !user.getUserId().equals(vinyl.getBorrowedBy())) {
            vinyl.reserve(user);
        }
    }

    private void tryReturn(Vinyl vinyl) {
        String state = vinyl.getCurrentState().getStateName();

        if ("Borrowed".equals(state) && user.getUserId().equals(vinyl.getBorrowedBy())) {
            vinyl.returnVinyl();
        }
    }
}
package org.store.vinyl.Strategy;

import org.store.vinyl.Model.Vinyl;
import org.store.vinyl.Services.VinylsService;

public interface VinylActionStrategy {

    void execute(Vinyl vinyl, VinylsService service);
}

package org.store.vinyl.Strategy;

import org.store.vinyl.Model.Vinyl;
import org.store.vinyl.Services.VinylsService;

public class ReturnStrategy implements VinylActionStrategy
{
    @Override
    public void execute(Vinyl vinyl, VinylsService service)
    {
        service.returnVinyl(vinyl.getTitle());
    }
}

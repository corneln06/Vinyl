package org.store.vinyl.Data;

import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;

import java.util.List;

public class DemoData {

    public static List<Vinyl> getVinyls() {
        return List.of(
                new Vinyl("Abbey Road", "The Beatles", 1969),
                new Vinyl("Thriller", "Michael Jackson", 1982),
                new Vinyl("Back in Black", "AC/DC", 1980),
                new Vinyl("The Dark Side of the Moon", "Pink Floyd", 1973),
                new Vinyl("Rumours", "Fleetwood Mac", 1977),
                new Vinyl("Hotel California", "Eagles", 1976),
                new Vinyl("Nevermind", "Nirvana", 1991),
                new Vinyl("Born in the U.S.A.", "Bruce Springsteen", 1984),
                new Vinyl("Purple Rain", "Prince", 1984),
                new Vinyl("Led Zeppelin IV", "Led Zeppelin", 1971)
        );
    }

    public static List<User> getUsers() {
        return List.of(
                new User("Deadpool", "deadpool"),
                new User("Daredevil", "daredevil"),
                new User("Iron Man", "ironman"),
                new User("Hulk", "hulk"),
                new User("Thanos", "thanos")
        );
    }
}
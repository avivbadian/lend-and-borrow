package nimpash.cinema.Init;

import nimpash.cinema.objects.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class InitializeDB {
    public static void main(String[] args) {

        ArrayList<String> movieIds = new ArrayList<>();

        for (int i = 0; i < 10; i++)
        	movieIds.add(ObjectId.get().toString());

        InitMovies.AddMoviesToDB(movieIds);
        ArrayList<String> hallIds = InitHalls.AddHallsToDB();
		ArrayList<String> screeningIds = InitScreenings.AddScreeningsToDB(movieIds, hallIds);
        ArrayList<User> users = InitUsers.AddUser();
    }
}

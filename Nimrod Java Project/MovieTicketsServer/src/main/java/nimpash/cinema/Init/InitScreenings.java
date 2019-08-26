package nimpash.cinema.Init;

import nimpash.cinema.objects.Screening;
import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.DataAccess.DataAccessObject;
import nimpash.cinema.objects.Row;
import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class InitScreenings
{
    public static void main(String[] args)
    {
        ArrayList<Screening> screenings = new ArrayList<>();

        ArrayList<Row> rows = new ArrayList<>();

        for (int i = 0; i < 11; i++)
            rows.add(new Row(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 1, 1, 0, 0))));

        for (int movie = 0; movie < 6; movie++)
		{
			for (int i = 0; i < 4; i++)
			{
				Screening scr = new Screening();
				scr.Time = LocalDateTime.now().plusDays(i + 1);
				scr.HallId = String.valueOf(i + 1);
				scr.Seats = rows;
				scr.MovieId = String.valueOf(i);//args[movie];

				screenings.add(scr);
			}
		}
		CRUD<Screening> crud = new DataAccessObject<>(Screening.class);
        crud.DeleteAll();
        screenings.parallelStream().forEach(crud::CreateOne);
    }

    public static ArrayList<String> AddScreeningsToDB(ArrayList<String> movieIds, ArrayList<String> hallIds)
	{
		ArrayList<String> screeningIds = new ArrayList<>();
		ArrayList<Screening> screenings = new ArrayList<>();

		Random rnd = new Random();

		ArrayList<Row> rows = new ArrayList<>();

		for (int i = 0; i < 11; i++)
			rows.add(new Row(new ArrayList<>(Arrays.asList(0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0))));

		for (int movieIndex = 0; movieIndex < movieIds.size(); movieIndex++)
		{
			for (int i = 0; i < hallIds.size(); i++)
			{
				for (int scrIndex = 0; scrIndex < 4; scrIndex++)
				{
					Screening scr = new Screening();
					scr.Time = LocalDateTime.now().plusDays(i - 1).plusHours(scrIndex);
					scr.HallId = hallIds.get(i);
					scr.Seats = rows;
					scr.MovieId = movieIds.get(movieIndex);
					scr.Id = ObjectId.get().toString();
					scr.Price = rnd.nextInt(20) + 30;


					screeningIds.add(scr.Id);
					screenings.add(scr);
				}
			}
		}

		CRUD<Screening> crud = new DataAccessObject<>(Screening.class);
		crud.DeleteAll();
		crud.CreateMany(screenings);

		return screeningIds;
	}
}




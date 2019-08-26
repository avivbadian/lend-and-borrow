package nimpash.cinema.objects;

import nimpash.cinema.objects.Converters.LocalDateTimeConverter;
import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;
import org.mongodb.morphia.annotations.Converters;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;

@Entity("screenings")
@Converters(LocalDateTimeConverter.class)
public class Screening
{
	@Id
    public String Id;
    public LocalDateTime Time;
    public String HallId;
    public ArrayList<Row> Seats;
    public int Price;
    public String MovieId;

    public Screening()
	{

    }
    public Screening(String screeningId, String movieId, LocalDateTime screeningTime, String hallId, int price, ArrayList<Row> seats)
	{
	    this.MovieId = movieId;
	    this.Id = screeningId;
        this.Time = screeningTime;
        this.HallId = hallId;
        this.Price = price;
        this.Seats = seats;
    }

	public Screening(String movieId, LocalDateTime screeningTime, String hallId, int price, ArrayList<Row> seats)
	{
		this (ObjectId.get().toString(), movieId, screeningTime, hallId, price, seats);
	}
}



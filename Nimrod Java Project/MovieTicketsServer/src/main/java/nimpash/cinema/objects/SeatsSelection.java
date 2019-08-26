package nimpash.cinema.objects;

import nimpash.cinema.objects.Converters.LocalDateTimeConverter;
import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;
import org.mongodb.morphia.annotations.Converters;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;

@Entity("selections")
@Converters(LocalDateTimeConverter.class)
public class SeatsSelection
{
	@Id
	public String Id;
	public String ScreeningId;
	public ArrayList<Seat> Seats;
	public LocalDateTime SelectionTime;

	public SeatsSelection()
	{

	}

	public SeatsSelection(String screeningId, ArrayList<Seat> seats, LocalDateTime selectionTime)
	{
		this.Id = ObjectId.get().toString();
		this.ScreeningId = screeningId;
		this.Seats = seats;
		this.SelectionTime = selectionTime;
	}
}

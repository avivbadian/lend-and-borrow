package nimpash.cinema.objects;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("halls")
public class Hall
{
	@Id
	public String HallId;
	public int Row;
	public int Column;
	public Hall() {
	}

	public Hall(String hallId , int row, int column)
	{
		this.HallId = hallId;
		this.Row = row;
		this.Column =column;
	}
}
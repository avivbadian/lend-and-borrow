package nimpash.cinema.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Row implements Serializable
{
	public ArrayList<Integer> Seats;

	public Row()
	{

	}

	public Row(int seats) {
		Seats = new ArrayList<>(Collections.nCopies(seats, SeatState.Free.getValue()));
	}

	public Row(ArrayList<Integer> seats)
	{
		Seats = seats;
	}
}

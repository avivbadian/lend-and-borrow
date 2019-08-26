package nimpash.cinema.objects;

// Constants
public enum SeatState
{
	Free(0),
	Occupied(1),
	Chosen(2);

	private int value;

	SeatState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

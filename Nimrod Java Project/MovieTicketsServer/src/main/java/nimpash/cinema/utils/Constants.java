package nimpash.cinema.utils;

public class Constants {

	public class Movie
	{
		public final static String ID = DB.ID;
		public final static String DIRECTOR = "director";
		public final static String DURATION = "duration";
		public final static String GENRES  = "genres";
		public final static String DESCRIPTION = "description";
		public final static String RELEASEDATE = "releasedate";
		public final static String ACTORS  = "actors";
		public final static String NAME = "name";
		public static final String IMAGE_NAME = "imagename";
		public static final String SCREENINGS = "screenings";
	}

	public class Halls
	{
		public final static String HALL_ID = DB.ID;
		public static final String INVALID_HALL_ID = "invalid hall id";
		public final static String HALLS_COLLECTION = "halls";
		public final static String HALLS_COLUMNS = "columns";
		public final static String HALLS_ROWS = "rows";

	}

	public class Screening
	{
		public final static String SCREENINGS_TIME = "Time";
		public final static String HALL_ID = "HallId";
		public final static String SEATS = "Seats";
		public final static String PRICE = "price";
		public static final String ID = DB.ID;
		public static final String MOVIE_ID = "MovieId";
	}


	public class Purchase
	{
		public final static String ID = DB.ID;
		public final static String GIVEN_NAME = "gname";
		public final static String LAST_NAME = "lname";
		public final static String PHONE = "phonenumber";
		public final static String EMAIL = "email";
		public final static String SCREENING_ID = "screeningid";
		public final static String MOVIE_ID = "MovieId";
		public final static String SEATS = "Seats";
		public static final String PURCHASE_TIME = "purchasetime";
	}

	public class DB
	{
		public static final String ID = "_id";
		public final static String TICKET_DATABASE = "tickets";
		public static final String HALL_COLLECTION = "halls";
		public final static String MOVIE_COLLECTION = "movies";
		public static final String SCREENINGS_COLLECTION = "screening";
		public static final String PURCHASES_COLLECTION = "purchases";
		public static final String SEATS_SELECTION_COLLECTION = "seatselection";
	}

	public class SeatsSelection
	{
		public static final String SELECTION_TIME = "selectiontime";
		public static final String SEATS = "Seats";
		public static final String SCREENING_ID = "screeningid";
		public static final String ID = DB.ID;
	}

	public class User
	{
		public static final String USER = "_user";
		public static final String PASSWORD = "_password";
	}
}

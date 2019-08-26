package nimpash.cinema.Init;

import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.DataAccess.DataAccessObject;
import nimpash.cinema.objects.MovieDetails;
import org.joda.time.LocalDate;

import java.util.ArrayList;

public class InitMovies
{
	public static void AddMoviesToDB(ArrayList<String> movieIds)
	{
        ArrayList<MovieDetails> movies =new ArrayList<>();

        movies.add(new MovieDetails(movieIds.get(0), "Fast and Furious 8", "After Dom is recruited by a mysterious villainess hacker," +
                " he turns against his family and most importantly," +
                " goes on the wrong side of the law again." +
                " Now, the crew must unite to bring home the man who made them a family and stop her from unleashing chaos on a grand scale.",
                                    "fast8.jpg", "F. Gary Gray", "Vin Diesel, Jason Statham, Dwayne Johnson, Michelle Rodriguez, Tyrese Gibson", new LocalDate(2017, 5, 6), "Action, Thriller, Crime", 136));

        movies.add(new MovieDetails(movieIds.get(1),"Wonder Woman ","Before she was Wonder Woman, she was Diana, princess of the Amazons, trained warrior. When a pilot crashes and tells of conflict in the outside world, she leaves home to fight a war, discovering her full powers and true destiny.",
                "wonder.jpg","Patty Jenkins","  Gal Gadot, Chris Pine, Robin Wright",new LocalDate(2017, 7, 11),"Adventure, Fantasy, Action",102));



        movies.add(new MovieDetails(movieIds.get(2),"McLaren","The story of Bruce McLaren, the New Zealander who founded the McLaren Motor Racing team. " +
                "A man who showed the world that a man of humble beginnings could take on the elite of motor racing and win." +
                " Now, the crew must unite to bring home the man who made them a family and stop her from unleashing chaos on a grand scale.",
                                    "Maclaren.jpg","Roger Donaldson"," Mario Andretti, Alastair Caldwell, Dwayne Cameron",new LocalDate(2017, 5, 6),"Documentary, Action,  Drama",92));



        movies.add(new MovieDetails(movieIds.get(3),"Pink Panther","The Pink Panther diamond is stolen once again from Lugash and the authorities call in Chief Inspector Clouseau from France." +
                " His plane disappears en-route. This time, famous French TV reporter Marie Jouvet sets out to solve the mystery and starts to interview everybody connected to Clouseau. " +
                "Each interviewee Dreyfus, Sir Charles & Lady Lytton (an ex-wife of Clouseau), George Lytton, Hercule Lajoy (assistant in A Shot In The Dark), " +
                "and Cato tell of their run-ins with Clouseau. She is also kidnapped by mobster Bruno Langlois who doesn't want Clouseau found but she continues and finds Clouseau Sr.," +
                " Clouseau's father. Is Clouseau alive or is he dead? Each interview has not-yet-seen or famous clips from the previous movies (since Peter Sellers has sadly passed away) " +
                "as Marie continues to get a honest view or impression of the great French detective",
                                    "PimkPanter.jpg", "Blake Edwards","Julie Andrews ,Denise Crosby,Claudia Cardinale ",new LocalDate(2017,5,1), "Comedy, Crime, Mystery",62 ));


        movies.add(new MovieDetails(movieIds.get(4),"Kung Fu Panda 3","When Po's long-lost panda father suddenly reappears, the reunited duo travels to a secret panda paradise to meet scores of hilarious new panda characters." +
                " But when the supernatural villain Kai begins to sweep across China defeating all the kung fu masters, Po must do the impossible-learn to train a village full of his fun-loving, " +
                "clumsy brethren to become the ultimate band of Kung Fu Pandas",
                                    "Panda.jpg","Jennifer Yuh","Angelina Jolie as Tigress,Seth Rogen as Mantis,Bryan Cranston as Li,Kate Hudson as Mei Mei",new LocalDate(2015,5,1),"Action,Animation,Comedy,Family,Fantasy",88 ));



        movies.add(new MovieDetails(movieIds.get(5),"The LEGO Batman Movie","There are big changes brewing in Gotham City, and if he wants to save the city from The Joker's hostile takeover," +
                " Batman may have to delete the lone vigilante thing, try to work with others and maybe," +
                " just maybe, learn to lighten up.",
                                    "Lego.jpg", "Chris McKay","Rosario Dawson as Batgirl,Channing Tatum as Superman,Ralph Fiennes as Alfred Pennyworth,Zoë Kravitz as Catwoman",new LocalDate(2017,3,3),"Action,Animation,Family,Fantasy",76 ));


        movies.add(new MovieDetails(movieIds.get(6),"Kong: Skull Island","A washed up monster chaser convinces the U.S. Government to fund a trip to an unexplored island in the South Pacific. " +
                "Under the guise of geological research, the team travels to Skull Island. Upon arrival, the group discover that their mission may be complicated by the wildlife which inhabits the island." +
                " The beautiful vistas and deadly creatures create a visually stunning experience that is sure to keep your attention." +
                " just maybe, learn to lighten up.",
                                    "KONG.jpg","Jordan Vogt-Roberts","Brie Larson as Mason Weaver,Tom Hiddleston as James Conrad,Samuel L. Jackson as Preston Packard",new LocalDate(2017,2,7),"Action,Adventure,Fantasy,Sci-Fi",102 ));


        movies.add(new MovieDetails(movieIds.get(7),"CHIPS","Jon Baker (Shepard) and Frank Ponch Poncherello (Peña) have just joined the California Highway Patrol (CHP) in Los Angeles, but for very different reasons." +
                " Baker is a beaten-up former pro motorbiker trying to put his life and marriage back together. Poncherello is a cocky undercover Federal agent investigating a multi-million dollar heist that may be an inside-job inside the CHP." +
                " The inexperienced rookie and the hardened pro are teamed together, but clash more than click, so kick-starting a real partnership is easier said than done. " +
                "But with Baker's unique bike skills and Ponch's street savvy it might just work...if they don't drive each other crazy first.",
                                    "CHIPS.jpg","Dax Shepard","Kristen Bell as Karen,Rosa Salazar as Ava,Vincent D'Onofrio as Vic Brown",new LocalDate(2017,8,21),"Action,Comedy,Crime",84 ));

//
        movies.add(new MovieDetails(movieIds.get(8),"The Mask","Stanley Ipkiss (Jim Carrey) is a bank clerk that is an incredibly nice man. Unfortunately," +
                " he is too nice for his own good and is a pushover when it comes to confrontations. After one of the worst days of his life, he finds a mask that depicts Loki, " +
                "the Norse night god of mischief. Now, when he puts it on, he becomes his inner, self: a cartoon romantic wild man. However, a small time crime boss, Dorian Tyrel (Peter Greene), " +
                "comes across this character dubbed The Mask by the media. After Ipkiss's alter ego indirectly kills his friend in crime," +
                " Tyrel now wants this green-faced goon destroyed.",
                                    "MASK.jpg","Chuck Russell","Jim Carrey as Stanley Ipkiss/The Mask,Cameron Diaz as Tina Carlyle,Amy Yasbeck as Peggy Brandt,Joely Fisher as Maggie",new LocalDate(1994,2,1),"Action,Comedy,CrimeAction,Family,Fantasy", 88));

		CRUD<MovieDetails> crud = new DataAccessObject<>(MovieDetails.class);
		crud.DeleteAll();
		movies.parallelStream().forEach(crud::CreateOne);
	}
}

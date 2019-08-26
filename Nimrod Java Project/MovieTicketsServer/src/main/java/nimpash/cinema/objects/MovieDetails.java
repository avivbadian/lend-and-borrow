package nimpash.cinema.objects;

import nimpash.cinema.objects.Converters.LocalDateConverter;
import org.bson.types.ObjectId;
import org.joda.time.LocalDate;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

@Entity("movies")
@Indexes(@Index(value = "Name", fields = @Field("Name"), unique = true))
@Converters(LocalDateConverter.class)
public class MovieDetails implements Serializable
{
	@Id
	public String Id;
	public String Name;
	public String Description;
	public String ImageName;
	public LocalDate ReleaseDate;
	public String Director;
	public int Duration;
	public String Genres;
	public String Actors;

	public MovieDetails()
	{

	}

	public MovieDetails(String id, String name, String description, String imageName, String director, String actors, LocalDate releaseDate, String genres, int duration)
	{
		Id = id;
		Name = name;
		Description = description;
		ImageName = imageName;
		Director = director;
		Actors = actors;
		ReleaseDate = releaseDate;
		Genres = genres;
		Duration = duration;
	}

	// Ctor, Auto generate id
	public MovieDetails(String name, String description, String imageName, String director, String actors, LocalDate releaseDate, String genres, int duration)
	{
		this (ObjectId.get().toString(), name, description, imageName, director, actors, releaseDate, genres, duration);
	}
}
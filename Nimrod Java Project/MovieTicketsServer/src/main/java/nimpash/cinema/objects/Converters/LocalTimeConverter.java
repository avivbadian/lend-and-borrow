package nimpash.cinema.objects.Converters;

import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

public class LocalTimeConverter extends TypeConverter implements SimpleValueConverter
{
	public LocalTimeConverter() {
		super(LocalTime.class);
	}

	@Override
	public Object decode(Class<?> aClass, Object o, MappedField mappedField)
	{
		final String dateAsString = o.toString();

		if (dateAsString.length() == 0)
			return null;
		else
			return ISODateTimeFormat.timeNoMillis().parseLocalTime(dateAsString);
	}

	@Override
	public Object encode(Object value, MappedField optionalExtraInfo)
	{
		if (value instanceof LocalTime)
			return ISODateTimeFormat.timeNoMillis().print((LocalTime)value);
		else
			return "";
	}
}

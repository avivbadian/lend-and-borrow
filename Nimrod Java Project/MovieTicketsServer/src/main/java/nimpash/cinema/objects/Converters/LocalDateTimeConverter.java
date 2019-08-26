package nimpash.cinema.objects.Converters;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

public class LocalDateTimeConverter extends TypeConverter implements SimpleValueConverter
{
	public LocalDateTimeConverter() {
		super(LocalDateTime.class);
	}

	@Override
	public Object decode(Class<?> aClass, Object o, MappedField mappedField)
	{
		final String dateAsString = o.toString() + "Z";

		if (dateAsString.length() == 0)
			return null;
		else
			return ISODateTimeFormat.dateTimeNoMillis().parseLocalDateTime(dateAsString);
	}

	@Override
	public Object encode(Object value, MappedField optionalExtraInfo)
	{
		if (value instanceof LocalDateTime)
			return ISODateTimeFormat.dateTimeNoMillis().print((LocalDateTime)value);
		else
			return "";
	}
}

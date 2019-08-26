package nimpash.cinema.objects.Converters;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

public class LocalDateConverter extends TypeConverter implements SimpleValueConverter
{
	public LocalDateConverter() {
		super(LocalDate.class);
	}

	@Override
	public Object decode(Class<?> aClass, Object o, MappedField mappedField)
	{
		final String dateAsString = o.toString();

		if (dateAsString.length() == 0)
			return null;
		else
			return ISODateTimeFormat.date().parseLocalDate(dateAsString);
	}

	@Override
	public Object encode(Object value, MappedField optionalExtraInfo)
	{
		if (value instanceof LocalDate)
			return ISODateTimeFormat.date().print((LocalDate)value);
		else
			return "";
	}
}

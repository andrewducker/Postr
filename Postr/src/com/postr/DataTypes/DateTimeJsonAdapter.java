package com.postr.DataTypes;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

class DateTimeJsonAdapter extends TypeAdapter<DateTime>{

	@Override
	public DateTime read(JsonReader arg0) throws IOException {
		DateTime dateTime =DateTime.parse(arg0.nextString());
		return dateTime.withZoneRetainFields(ThreadStorage.getDateTimeZone()).withZone(DateTimeZone.UTC);
	}

	@Override
	public void write(JsonWriter writer, DateTime dateTime) throws IOException {
		DateTime convertedDateTime = dateTime.withZone(ThreadStorage.getDateTimeZone()).withZoneRetainFields(DateTimeZone.UTC);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		writer.value(fmt.print(convertedDateTime));
	}
}
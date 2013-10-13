package com.postr.DataTypes;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DateTimeJsonAdapter extends TypeAdapter<DateTime>{

	@Override
	public DateTime read(JsonReader arg0) throws IOException {
		// TODO Auto-generated method stub
		return DateTime.parse(arg0.nextString());
	}

	@Override
	public void write(JsonWriter writer, DateTime dateTime) throws IOException {
		writer.value(DateTimeFormat.forPattern("yyyy-MM-dd - HH:mm:ss").withZoneUTC().print(dateTime));
	}
}

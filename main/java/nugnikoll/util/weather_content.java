package nugnikoll.util;

public class weather_content{
	public String city, update_time, temperature, humidity, pm_data;
	public String pm_quality, wind_direction, wind_strength, date;
	public String thermo_high, thermo_low, weather_type;
	
	public String toString() {
		return "weather_content{"
				+ "city=\"" + city + "\", "
				+ "update_time=\"" + update_time + "\", "
				+ "temperature=\"" + temperature + "\", "
				+ "humidity=\"" + humidity + "\", "
				+ "pm_data=\"" + pm_data + "\", "
				+ "pm_quality=\"" + pm_quality + "\", "
				+ "wind_direction=\"" + wind_direction + "\", "
				+ "wind_strength=\"" + wind_strength + "\", "
				+ "date=\"" + date + "\", "
				+ "thermo_high=\"" + thermo_high + "\", "
				+ "thermo_low=\"" + thermo_low + "\", "
				+ "weather_type=\"" + weather_type + "\""
				+ "}";
	}
}

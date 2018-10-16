package nugnikoll.weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import nugnikoll.util.net_util;

public class MainActivity extends Activity implements View.OnClickListener{
	private ImageView button_update;
	private TextView text_city, text_time, text_humidity, text_week, text_pm_data;
	private TextView text_pm_quality, text_temperature, text_climate, text_wind, text_city_name;
	private ImageView image_weather, image_pm;

	@Override
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.weather_info);

		button_update = (ImageView) findViewById(R.id.title_update_button);
		button_update.setOnClickListener(this);
    }

	private void init_view(){
		text_city_name = (TextView) findViewById(R.id.title_city_name);
		text_city = (TextView) findViewById(R.id.city);
		text_time = (TextView) findViewById(R.id.time);
		text_humidity = (TextView) findViewById(R.id.humidity);
		text_week = (TextView) findViewById(R.id.week_today);
		text_pm_data = (TextView) findViewById(R.id.pm_data);
		text_pm_quality = (TextView) findViewById(R.id.pm25_quality);
		image_pm = (ImageView) findViewById(R.id.pm25_image);
		text_temperature = (TextView) findViewById(R.id.temperature);
		text_climate = (TextView) findViewById(R.id.climate);
		text_wind = (TextView) findViewById(R.id.wind);
		image_weather = (ImageView) findViewById(R.id.weather_image);

		text_city.setText("N/A");
		text_time.setText("N/A");
		text_humidity.setText("N/A");
		text_week.setText("N/A");
		text_pm_data.setText("N/A");
		text_pm_quality.setText("N/A");
		text_temperature.setText("N/A");
		text_climate.setText("N/A");
		text_wind.setText("N/A");
		text_city_name.setText("N/A");
	}

	private void parse_xml(String xml_data){
		int wind_direction = 0;
		int wind_strength = 0;
		int date = 0;
		int thermo_high = 0;
		int thermo_low = 0;
		int weather_type = 0;

		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xml_parser = factory.newPullParser();
			xml_parser.setInput(new StringReader(xml_data));
			int event_type = xml_parser.getEventType();
			Log.d("my_weather", "parse XML");
			while(event_type != XmlPullParser.END_DOCUMENT){
				switch(event_type){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if(xml_parser.getName().equals("city")){
						xml_parser.next();
						Log.d("my_weather", "city: " + xml_parser.getText());
					}else if(xml_parser.getName().equals("updatetime")){
						xml_parser.next();
						Log.d("my_weather", "updatetime: " + xml_parser.getText());
					}else if(xml_parser.getName().equals("shidu")){
						xml_parser.next();
						Log.d("my_weather", "humidity: " + xml_parser.getText());
					}else if(xml_parser.getName().equals("wendu")){
						xml_parser.next();
						Log.d("my_weather", "temperature: " + xml_parser.getText());
					}else if(xml_parser.getName().equals("pm25")){
						xml_parser.next();
						Log.d("my_weather", "pm2.5: " + xml_parser.getText());
					}else if(xml_parser.getName().equals("quality")){
						xml_parser.next();
						Log.d("my_weather", "quality: " + xml_parser.getText());
					}else if(xml_parser.getName().equals("fengxiang") && wind_direction == 0){
						xml_parser.next();
						Log.d("my_weather", "wind direction: " + xml_parser.getText());
						wind_direction = 1;
					}else if(xml_parser.getName().equals("fengli") && wind_strength == 0){
						xml_parser.next();
						Log.d("my_weather", "wind strength: " + xml_parser.getText());
						wind_strength = 1;
					}else if(xml_parser.getName().equals("date") && date == 0){
						xml_parser.next();
						Log.d("my_weather", "date: " + xml_parser.getText());
						date = 1;
					}else if(xml_parser.getName().equals("high") && thermo_high == 0){
						xml_parser.next();
						Log.d("my_weather", "thermo high: " + xml_parser.getText());
						thermo_high = 1;
					}else if(xml_parser.getName().equals("low") && thermo_low == 0){
						xml_parser.next();
						Log.d("my_weather", "thermo_low: " + xml_parser.getText());
						thermo_low = 1;
					}else if(xml_parser.getName().equals("type") && weather_type == 0){
						xml_parser.next();
						Log.d("my_weather", "weather type: " + xml_parser.getText());
						weather_type = 1;
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event_type = xml_parser.next();
			}
		}catch(XmlPullParserException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void query_weather_code(String city_code){
		final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + city_code;
		Log.d("my_weather", address);
		new Thread(new Runnable(){
			@Override
			public void run(){
				HttpURLConnection conn = null;
				try{
					URL url = new URL(address);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					InputStream in = conn.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String str;
					while((str = reader.readLine()) != null){
						response.append(str);
						Log.d("my_weather", str);
					}
					String response_str = response.toString();
					Log.d("my_weather", response_str);
					parse_xml(response_str);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(conn != null){
						conn.disconnect();
					}
				}
			}
		}).start();
	}

	@Override
	public void onClick(View view){
		if(view.getId() == R.id.title_update_button){
			SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
			String city_code = sharedPreferences.getString("main_city_code", "101010100");
			Log.d("my_weather", city_code);

			if(net_util.get_network_state(this) != net_util.NETWORK_NONE){
				Log.d("my_weather", "network connection is fine");
				Toast.makeText(MainActivity.this, "network connection is fine.", Toast.LENGTH_LONG).show();
				query_weather_code(city_code);
			}else{
				Log.d("my_weather", "network connection is not available");
				Toast.makeText(MainActivity.this, "network connection is not available.", Toast.LENGTH_LONG).show();
			}

			init_view();
		}
	}
}

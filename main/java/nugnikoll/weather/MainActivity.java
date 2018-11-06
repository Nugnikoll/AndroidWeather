package nugnikoll.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import nugnikoll.util.weather_content;

public class MainActivity extends Activity implements View.OnClickListener{
	private static final int UPDATE_WEATHER = 1;

	private ImageView button_update;
	private ImageView button_select;
	private TextView text_city, text_time, text_humidity, text_week, text_pm_data;
	private TextView text_pm_quality, text_temperature, text_climate, text_wind, text_city_name;
	private ImageView image_weather, image_pm;
	private ProgressBar progress_update;

	private Handler handler_update = new Handler() {
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
			case UPDATE_WEATHER:
				update_weather((weather_content) msg.obj);
				button_update.setVisibility(View.VISIBLE);
				progress_update.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle save_instance_state){
	    super.onCreate(save_instance_state);
	    setContentView(R.layout.weather_info);

		button_update = (ImageView) findViewById(R.id.title_update_button);
		button_update.setOnClickListener(this);

		button_select = (ImageView) findViewById(R.id.title_city_manager);
		button_select.setOnClickListener(this);

		progress_update = (ProgressBar) findViewById(R.id.progress_update);

		init_view();

		if(net_util.get_network_state(this) != net_util.NETWORK_NONE){
			Log.d("my_weather", "network connection is fine");
			Toast.makeText(MainActivity.this, "网络连接正常", Toast.LENGTH_LONG).show();
		}else{
			Log.d("my_weather", "network connection is not available");
			Toast.makeText(MainActivity.this, "网络断开", Toast.LENGTH_LONG).show();
		}
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

	private void update_weather(weather_content content){
//		if(content.city.equals("null")){
//			
//		}
		text_city_name.setText(content.city + "天气");
		text_city.setText(content.city);
		text_time.setText(content.update_time + "发布");
		text_humidity.setText("湿度" + content.humidity);
		text_pm_data.setText(content.pm_data);
		text_pm_quality.setText(content.pm_quality);
		if(content.pm_data == null || content.pm_data.equals("")){
			image_pm.setVisibility(View.INVISIBLE);
		}else{
			image_pm.setVisibility(View.VISIBLE);
			if(Integer.valueOf(content.pm_data) <= 50){
				image_pm.setImageResource(R.drawable.biz_plugin_weather_0_50);
			}else if(Integer.valueOf(content.pm_data) <= 100){
				image_pm.setImageResource(R.drawable.biz_plugin_weather_51_100);
			}else if(Integer.valueOf(content.pm_data) <= 150){
				image_pm.setImageResource(R.drawable.biz_plugin_weather_101_150);
			}else if(Integer.valueOf(content.pm_data) < 200){
				image_pm.setImageResource(R.drawable.biz_plugin_weather_151_200);
			}else if(Integer.valueOf(content.pm_data) < 300){
				image_pm.setImageResource(R.drawable.biz_plugin_weather_201_300);
			}else{
				image_pm.setImageResource(R.drawable.biz_plugin_weather_greater_300);
			}
		}
			text_week.setText(content.date);
		text_temperature.setText(content.thermo_low + "~" + content.thermo_high);
		text_climate.setText(content.weather_type);
		if(content.weather_type == null){
			image_weather.setVisibility(View.INVISIBLE);
		}else{
			image_weather.setVisibility(View.VISIBLE);
			if(content.weather_type.equals("晴")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_qing);
			}else if(content.weather_type.equals("暴雪")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_baoxue);
			}else if(content.weather_type.equals("暴雨")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_baoyu);
			}else if(content.weather_type.equals("大暴雪")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
			}else if(content.weather_type.equals("大雪")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_daxue);
			}else if(content.weather_type.equals("大雨")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_dayu);
			}else if(content.weather_type.equals("多云")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_duoyun);
			}else if(content.weather_type.equals("雷阵雨")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
			}else if(content.weather_type.equals("雷阵雨冰雹")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
			}else if(content.weather_type.equals("沙尘暴")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
			}else if(content.weather_type.equals("特大暴雨")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
			}else if(content.weather_type.equals("雾")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_wu);
			}else if(content.weather_type.equals("小雪")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
			}else if(content.weather_type.equals("小雨")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
			}else if(content.weather_type.equals("阴")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_yin);
			}else if(content.weather_type.equals("雨夹雪")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
			}else if(content.weather_type.equals("阵雪")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
			}else if(content.weather_type.equals("阵雨")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
			}else if(content.weather_type.equals("中雪")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
			}else if(content.weather_type.equals("中雨")){
				image_weather.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
			}else{
				image_weather.setVisibility(View.INVISIBLE);
			}
		}
		text_wind.setText("风力" + content.wind_strength);
		Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
	}

	private weather_content parse_xml(String xml_data){
		weather_content content = null;
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
					if(xml_parser.getName().equals("resp")){
						content = new weather_content();
					}
					if(content != null){
						if(xml_parser.getName().equals("city")){
							xml_parser.next();
							content.city = xml_parser.getText();
						}else if(xml_parser.getName().equals("updatetime")){
							xml_parser.next();
							content.update_time = xml_parser.getText();
						}else if(xml_parser.getName().equals("shidu")){
							xml_parser.next();
							content.humidity = xml_parser.getText();
						}else if(xml_parser.getName().equals("wendu")){
							xml_parser.next();
							content.temperature = xml_parser.getText();
						}else if(xml_parser.getName().equals("pm25")){
							xml_parser.next();
							content.pm_data = xml_parser.getText();
						}else if(xml_parser.getName().equals("quality")){
							xml_parser.next();
							content.pm_quality = xml_parser.getText();
						}else if(xml_parser.getName().equals("fengxiang") && wind_direction == 0){
							xml_parser.next();
							content.wind_direction = xml_parser.getText();
							wind_direction = 1;
						}else if(xml_parser.getName().equals("fengli") && wind_strength == 0){
							xml_parser.next();
							content.wind_strength = xml_parser.getText();
							wind_strength = 1;
						}else if(xml_parser.getName().equals("date") && date == 0){
							xml_parser.next();
							content.date = xml_parser.getText();
							date = 1;
						}else if(xml_parser.getName().equals("high") && thermo_high == 0){
							xml_parser.next();
							content.thermo_high = xml_parser.getText();
							thermo_high = 1;
						}else if(xml_parser.getName().equals("low") && thermo_low == 0){
							xml_parser.next();
							content.thermo_low = xml_parser.getText();
							thermo_low = 1;
						}else if(xml_parser.getName().equals("type") && weather_type == 0){
							xml_parser.next();
							content.weather_type = xml_parser.getText();
							weather_type = 1;
						}
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
		
		return content;
	}

	private void query_weather_code(String city_code){
		final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + city_code;
		Log.d("my_weather", address);
		new Thread(new Runnable(){
			@Override
			public void run(){
				HttpURLConnection conn = null;
				weather_content content = null;
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
					content = parse_xml(response_str);

					if(content != null){
						Log.d("my_weather", content.toString());

						Message msg = new Message();
						msg.what = UPDATE_WEATHER;
						msg.obj = content;
						handler_update.sendMessage(msg);
					}

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
		if(view.getId() == R.id.title_city_manager){
			Intent itt = new Intent(this, act_city.class);
			startActivityForResult(itt, 1);
		}else if(view.getId() == R.id.title_update_button){
			button_update.setVisibility(View.INVISIBLE);
			progress_update.setVisibility(View.VISIBLE);

			SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
			String city_code = sharedPreferences.getString("select_city_code", "101010100");
			Log.d("my_weather", city_code);

			if(net_util.get_network_state(this) != net_util.NETWORK_NONE){
				Log.d("my_weather", "network connection is fine");
				//Toast.makeText(MainActivity.this, "网络连接正常", Toast.LENGTH_LONG).show();
				query_weather_code(city_code);
			}else{
				Log.d("my_weather", "network connection is not available");
				Toast.makeText(MainActivity.this, "网络断开", Toast.LENGTH_LONG).show();
			}
		}
	}

	protected void onActivityResult(int request_code, int result_code, Intent data){
		if(request_code == 1 && result_code == RESULT_OK){
			String new_city_code = data.getStringExtra("city_code");
			Log.d("my_weather", "the selected city code is " + new_city_code);

			if(net_util.get_network_state(this) != net_util.NETWORK_NONE){
				Log.d("my_weather", "network connection is fine");
				//Toast.makeText(MainActivity.this, "网络连接正常", Toast.LENGTH_LONG).show();
				query_weather_code(new_city_code);
			}else{
				Log.d("my_weather", "network connection is not available");
				Toast.makeText(MainActivity.this, "网络断开", Toast.LENGTH_LONG).show();
			}
		}
	}
}

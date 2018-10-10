package nugnikoll.weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import nugnikoll.util.net_util;

public class MainActivity extends Activity implements View.OnClickListener{
	private ImageView button_update;

	@Override
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.weather_info);

		button_update = (ImageView) findViewById(R.id.title_update_button);
		button_update.setOnClickListener(this);
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
		}
	}
}

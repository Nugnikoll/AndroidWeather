package nugnikoll.weather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import nugnikoll.util.net_util;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.weather_info);

		if(net_util.get_network_state(this) != net_util.NETWORK_NONE){
			Log.d("my_weather", "net connection is fine");
			Toast.makeText(MainActivity.this, "net connection is fine.", Toast.LENGTH_LONG).show();
		}else{
			Log.d("my_weather", "net connection is broken");
			Toast.makeText(MainActivity.this, "net connection is broken.", Toast.LENGTH_LONG).show();
		}
    }
}

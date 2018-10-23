package nugnikoll.app;

import android.app.Application;
import android.util.Log;

public class my_application extends Application{
	private static final String tag = "my_app";
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(tag, "my_application->OnCreate");
	}
}

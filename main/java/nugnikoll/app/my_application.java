package nugnikoll.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nugnikoll.db.city_db;
import nugnikoll.util.city_content;

public class my_application extends Application{
	private static final String tag = "my_app";

	private static my_application m_application;
	private city_db m_city_db;
	private List<city_content> m_city_list;

	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(tag, "my_application->OnCreate");
		m_application = this;
		m_city_db = open_city_db();
		init_city_list();
	}

	private void init_city_list(){
		m_city_list = new ArrayList<city_content>();

		new Thread(new Runnable(){
			@Override
			public void run(){
				prepare_city_list();
			}
		}).start();
	}

	private boolean prepare_city_list(){
		m_city_list = m_city_db.get_city_list();
		int i = 0;

		for(city_content item: m_city_list){
			++i;
			Log.d(tag, item.number + ":" + item.city);
		}
		Log.d(tag, "total=" + i);

		return true;
	}
	
	public List<city_content> get_city_list(){
		return m_city_list;
	}

	public static my_application get_instance(){
		return m_application;
	}

	private city_db open_city_db(){
		String str_database = "database_city";
		String path = File.separator + "data"
			+ Environment.getDataDirectory().getAbsolutePath()
			+ File.separator + getPackageName()
			+ File.separator + str_database
			+ File.separator + city_db.city_db_name;
		File fobj = new File(path);

		Log.d(tag, path);

		if(!fobj.exists()){
			String str_folder = File.separator + "data"
				+ Environment.getDataDirectory().getAbsolutePath()
				+ File.separator + getPackageName()
				+ File.separator + str_database
				+ File.separator;
			File folder_obj = new File(str_folder);
			if(!folder_obj.exists()){
				folder_obj.mkdirs();
				Log.i(tag, "make a directory for city database");
			}
			Log.i(tag, "create a database");
			try{
				InputStream fin = getAssets().open("city.db");
				FileOutputStream fout = new FileOutputStream(fobj);
				int len = -1;
				byte[] buffer = new byte[1024];
				while((len = fin.read(buffer)) != -1){
					fout.write(buffer, 0, len);
					fout.flush();
				}
				fout.close();
				fin.close();
			}catch(IOException err){
				err.printStackTrace();
				System.exit(0);
			}
		}
		return new city_db(this, path);
	}
}

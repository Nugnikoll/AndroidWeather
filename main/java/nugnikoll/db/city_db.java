package nugnikoll.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nugnikoll.util.city_content;

public class city_db{
	public static final String city_db_name = "city.db";
	private static final String city_table_name = "city";
	private SQLiteDatabase db;

	public city_db(Context context, String path){
		db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}

	public List<city_content> get_city_list(){
		List<city_content> city_list = new ArrayList<city_content>();
		Cursor cs = db.rawQuery("SELECT * from " + city_table_name, null);
		while(cs.moveToNext()){
			city_content item = new city_content();
			item.province = cs.getString(cs.getColumnIndex("province"));
			item.city = cs.getString(cs.getColumnIndex("city"));
			item.number = cs.getString(cs.getColumnIndex("number"));
			item.all_py = cs.getString(cs.getColumnIndex("allpy"));
			item.first_py = cs.getString(cs.getColumnIndex("firstpy"));
			item.all_first_py = cs.getString(cs.getColumnIndex("allfirstpy"));
			city_list.add(item);
		}
		cs.close();
		return city_list;
	}
}

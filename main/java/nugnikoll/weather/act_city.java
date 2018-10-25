package nugnikoll.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

import nugnikoll.app.my_application;
import nugnikoll.util.city_content;

public class act_city extends Activity implements View.OnClickListener{
	private ImageView button_back;
	private ListView list_city;
	private TextView text_city_select;
	
	private Vector<city_content> data_city;
	private city_content select_city_content;

	@Override
	protected void onCreate(Bundle save_instance_state){
		super.onCreate(save_instance_state);
		setContentView(R.layout.layout_city);

		button_back = (ImageView) findViewById(R.id.title_back);
		button_back.setOnClickListener(this);

		list_city = (ListView) findViewById(R.id.list_city);
		list_city.setOnItemClickListener(
			new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					select_city_content = data_city.elementAt(position);
					text_city_select.setText("当前城市：" + select_city_content.city);
				}
			}
		);
		
		my_application m_app = (my_application) getApplication();
		data_city = m_app.get_city_list();
		init_list();

		select_city_content = new city_content();
		
		SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		select_city_content.city = sharedPreferences.getString("select_city", "北京");
		select_city_content.number = sharedPreferences.getString("select_city_code", "101010100");
		
		text_city_select = (TextView) findViewById(R.id.title_city_select);
		text_city_select.setText("当前城市：" + select_city_content.city);
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.title_back:
			SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("select_city", select_city_content.city);
			editor.putString("select_city_code", select_city_content.number);
			editor.apply();

			Intent itt = new Intent();
			itt.putExtra("city_code", select_city_content.number);
			setResult(RESULT_OK, itt);
			finish();
			break;
		default:
			break;
		}
	}

	private void init_list(){
		Vector<String> data = new Vector<String>();
		
		for(city_content item: data_city){
			data.add(item.city + " " + item.number);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_list_item_1, data
		);
		list_city.setAdapter(adapter);
	}
}

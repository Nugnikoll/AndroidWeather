package nugnikoll.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;
import java.util.Vector;

import nugnikoll.app.my_application;
import nugnikoll.util.city_content;

public class act_city extends Activity implements View.OnClickListener{
	private ImageView button_back;
	private ListView list_city;

	@Override
	protected void onCreate(Bundle save_instance_state){
		super.onCreate(save_instance_state);
		setContentView(R.layout.layout_city);

		button_back = (ImageView) findViewById(R.id.title_back);
		button_back.setOnClickListener(this);

		list_city = (ListView) findViewById(R.id.list_city);
		
		init_list();
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.title_back:
			Intent itt = new Intent();
			itt.putExtra("city_code", "101160101");
			setResult(RESULT_OK, itt);
			finish();
			break;
		default:
			break;
		}
	}

	private void init_list(){
		my_application m_app = (my_application) getApplication();
		List<city_content> data_city = m_app.get_city_list();
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

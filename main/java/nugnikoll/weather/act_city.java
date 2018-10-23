package nugnikoll.weather;

import android.app.Activity;
import android.content.Intent;
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
	private int pos_select = -1;

	@Override
	protected void onCreate(Bundle save_instance_state){
		super.onCreate(save_instance_state);
		setContentView(R.layout.layout_city);

		text_city_select = (TextView) findViewById(R.id.title_city_select);

		button_back = (ImageView) findViewById(R.id.title_back);
		button_back.setOnClickListener(this);

		list_city = (ListView) findViewById(R.id.list_city);
		list_city.setOnItemClickListener(
			new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					pos_select = position;
					text_city_select.setText("当前城市：" + data_city.elementAt(position).city);
				}
			}
		);
		
		my_application m_app = (my_application) getApplication();
		data_city = m_app.get_city_list();
		init_list();
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.title_back:
			Intent itt = new Intent();
			if(pos_select >= 0){
				itt.putExtra("city_code", data_city.elementAt(pos_select).number);
			}else{
				itt.putExtra("city_code", "101160101");
			}
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

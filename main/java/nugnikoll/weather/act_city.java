package nugnikoll.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import nugnikoll.app.my_application;
import nugnikoll.util.city_content;

public class act_city extends Activity implements View.OnClickListener{
	private ImageView button_back;
	private ListView list_city;
	private TextView text_city_select;
	private EditText edit_search;

	private Vector<city_content> data_city;
	private Vector<city_content> data_search;
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
					select_city_content = data_search.elementAt(position);
					text_city_select.setText("当前城市：" + select_city_content.city);
				}
			}
		);
		
		my_application m_app = (my_application) getApplication();
		data_city = m_app.get_city_list();
		data_search = new Vector<city_content>(data_city);
		init_list();

		select_city_content = new city_content();
		
		SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		select_city_content.city = sharedPreferences.getString("select_city", "北京");
		select_city_content.number = sharedPreferences.getString("select_city_code", "101010100");
		
		text_city_select = (TextView) findViewById(R.id.title_city_select);
		text_city_select.setText("当前城市：" + select_city_content.city);

		edit_search = (EditText) findViewById(R.id.edit_search);
		edit_search.setOnEditorActionListener(
			new EditText.OnEditorActionListener(){
				@Override
				public boolean onEditorAction(TextView view, int id, KeyEvent event){
					if(id == EditorInfo.IME_ACTION_SEARCH){
						Log.d("my_weather", "action search");
						String str = edit_search.getText().toString();
						Pattern ptn;
						try{
							ptn = Pattern.compile(str);
						}catch (PatternSyntaxException err){
							Toast.makeText(act_city.this, err.getDescription(), Toast.LENGTH_LONG).show();
							return true;
						}
						data_search = new Vector<city_content>();
						for(city_content content: data_city){
							Matcher mtc_city = ptn.matcher(content.city);
							Matcher mtc_province = ptn.matcher(content.province);
							Matcher mtc_number = ptn.matcher(content.number);
							Matcher mtc_py = ptn.matcher(content.all_py);
							if(mtc_city.find() || mtc_province.find() || mtc_number.find() || mtc_py.find()){
								data_search.add(content);
							}
						}
						init_list();
						return true;
					}else{
						Log.d("my_weather", "action other");
						return false;
					}
				}
			}
		);
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
		
		for(city_content item: data_search){
			data.add(item.province + " " + item.city + " " + item.number);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_list_item_1, data
		);
		list_city.setAdapter(adapter);
	}
}

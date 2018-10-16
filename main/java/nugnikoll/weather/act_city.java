package nugnikoll.weather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class act_city extends Activity implements View.OnClickListener{
	private ImageView button_back;

	@Override
	protected void onCreate(Bundle save_instance_state){
		super.onCreate(save_instance_state);
		setContentView(R.layout.layout_city);

		button_back = (ImageView) findViewById(R.id.title_back);
		button_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
	}
}

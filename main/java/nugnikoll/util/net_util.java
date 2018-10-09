package nugnikoll.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class net_util{
	public static final int NETWORK_NONE = 0;
	public static final int NETWORK_WIFI = 1;
	public static final int NETWORK_MOBILE = 2;

	public static int get_network_state(Context context){
		ConnectivityManager conn_manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conn_manager.getActiveNetworkInfo();
		if(networkInfo == null){
			return NETWORK_NONE;
		}
		int n_type = networkInfo.getType();
		if(n_type == ConnectivityManager.TYPE_MOBILE){
			return NETWORK_MOBILE;
		}else if(n_type == ConnectivityManager.TYPE_WIFI){
			return NETWORK_WIFI;
		}
		return NETWORK_NONE;
	}
}

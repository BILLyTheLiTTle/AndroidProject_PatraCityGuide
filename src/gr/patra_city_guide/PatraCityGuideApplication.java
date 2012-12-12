package gr.patra_city_guide;

import java.util.Vector;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

public class PatraCityGuideApplication extends Application {
	
	private Vector<Activity> activities;
	
	@Override
	public void onCreate(){
		super.onCreate();
		activities=new Vector<Activity>(2);
	}

	public Vector<Activity> getActivitiesRunning(){
		activities.trimToSize();
		return activities;
	}
	
	public void exitActivities(PatraCityGuideApplication app){		
		/*
		 * An einai sto automato on/off kleista ola automata
		 */
		SharedPreferences prefs = PreferenceManager
        .getDefaultSharedPreferences(getBaseContext());
		String wifiPref=prefs.getString("wireless_list_preference", "0");
		String gpsPref=prefs.getString("gps_list_preference", "0");
		//do something for the gps
		if(gpsPref.equals("0")){
			String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			  if(provider.contains("gps")){
			    final Intent poke = new Intent();
			    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			    poke.setData(Uri.parse("3")); 
			    sendBroadcast(poke);
			    Toast.makeText(activities.get(0), "Το GPS απενεργοποιήθηκε",Toast.LENGTH_SHORT).show();
			  }
		}
		//do something for the wifi
		if(wifiPref.equals("0")){
			WifiManager wifiManager = (WifiManager)activities.get(0).getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(false);
		    Toast.makeText(activities.get(0), "Το wifi απενεργοποιήθηκε",Toast.LENGTH_SHORT).show();
		}
		
		Vector<Activity> activities=app.getActivitiesRunning();
		for(Activity a:activities){
			a.finish();	
		}
		System.runFinalizersOnExit(true);
		System.exit(0);		
		//android.os.Process.killProcess(android.os.Process.getUidForName(a.getLocalClassName()));
	}
}

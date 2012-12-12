package gr.patra_city_guide;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsPreferenceActivity extends PreferenceActivity {

	private PatraCityGuideApplication app;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_settings);
		
		app=(PatraCityGuideApplication)this.getApplication();
        app.getActivitiesRunning().add(this);
	}
}

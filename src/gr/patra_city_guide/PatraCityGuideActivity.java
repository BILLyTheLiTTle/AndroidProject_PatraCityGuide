package gr.patra_city_guide;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PatraCityGuideActivity extends Activity {
    /** Called when the activity is first created. */
	//private static final String EXTRA_KEY = "gr.patra-city-guide.CityGuide";
	private PatraCityGuideApplication app;
	private AnimationDrawable frameAnimation;
	private PatraCityGuideActivity activity=this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        
        ImageView logo=(ImageView)findViewById(R.id.logo);
        logo.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(activity, "Ελπίζουμε να σας αρέσει" +
						" το λογότυπό μας!", Toast.LENGTH_LONG).show();
				return true;
			}
		});
        
        //activate the animation
        ImageView anim=(ImageView)findViewById(R.id.animation);
        anim.setVisibility(ImageView.VISIBLE);
        anim.setBackgroundResource(R.drawable.patra_images);
        frameAnimation=(AnimationDrawable)anim.getBackground();
        anim.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(activity, "Απολαύστε τις φωτογραφίες μας" +
						" κρατώντας το κινητό σας τηλέφωνο" +
						" κατά μήκος!", Toast.LENGTH_LONG).show();
				return true;
			}
		});
        anim.post(new Runnable(){
        	public void run(){
        		frameAnimation.start();
        	}
        });        
        //execute exit from exitButton
        Button exitButton=(Button)findViewById(R.id.PatraCityGuideActivity_exitButton);
        exitButton.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(activity, "Πραγματοποιεί έξοδο από την" +
						" εφαρμογή", Toast.LENGTH_LONG).show();
				return true;
			}
		});
        exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishActivity();
				
			}
		});
        //proceed to the next screen.
        Button nextButton=(Button)findViewById(R.id.PatraCityGuideActivity_nextButton);
        nextButton.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(activity, "Συνεχίζει στην επόμενη" +
						" οθόνη", Toast.LENGTH_LONG).show();
				return true;
			}
		});
        nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*
				 * An einai sto automato on/off ανοιksε τα ola automata
				 */
				SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
				String wifiPref=prefs.getString("wireless_list_preference", "0");
				String gpsPref=prefs.getString("gps_list_preference", "0");
				//do something for the gps
				if(gpsPref.equals("0")){
					String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
					  if(!provider.contains("gps")){
					    final Intent poke = new Intent();
					    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
					    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
					    poke.setData(Uri.parse("3")); 
					    sendBroadcast(poke);
					    Toast.makeText(activity, "Το GPS ενεργοποιήθηκε",Toast.LENGTH_SHORT).show();
					  }
				}
				//do something for the wifi
				if(wifiPref.equals("0")){
					WifiManager wifiManager = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
					wifiManager.setWifiEnabled(true);
				    Toast.makeText(activity, "Το wifi ενεργοποιήθηκε",Toast.LENGTH_SHORT).show();
				}
				
				proceedToNextScreen();
			}
		});
        
        app=(PatraCityGuideApplication)this.getApplication();
        app.getActivitiesRunning().add(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.intro_menu, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if(item.getItemId()==R.id.PatraCityGuideActivity_menu_favorite_shops){
    		//proceed to next screen and show the favorite shops
    		
    		return true;
    	}
    	else if(item.getItemId()==R.id.PatraCityGuideActivity_menu_help){
    		//show a Toast
    		Toast.makeText(this, "Πιέστε παρατεταμένα ένα από τα αντικείμενα" +
    				" της οθόνης για να δείτε τη βοήθεια", Toast.LENGTH_LONG).show();
    		return true;
    	}
    	else if(item.getItemId()==R.id.PatraCityGuideActivity_menu_nearest_shops){
    		//proceed to the next screen and show the favorite shops
    		
    		return true;    		
    	}
    	else if(item.getItemId()==R.id.PatraCityGuideActivity_menu_find){
    		//proceed to the next screen and show the find screen
    		
    		return true;
    	}
    	else if(item.getItemId()==R.id.PatraCityGuideActivity_menu_settings){
    		//proceed to the preferences screen
    		Intent preferenceSettingsIntent = new Intent(this,
    				gr.patra_city_guide.SettingsPreferenceActivity.class);
    		startActivity(preferenceSettingsIntent);
    		return true;
    	}
    	else if(item.getItemId()==R.id.menu_shortcut){
    		//create the shortcut
    		Intent intentInserted=this.getIntent();
    		Intent shortcutIntent = new Intent(intentInserted);
	        shortcutIntent.setClassName(this, this.getClass().getName());
	        //shortcutIntent.putExtra(EXTRA_KEY, "Patra City-Guide");

	        // Then, set up the container intent (the response to the caller)

	        Intent intent = new Intent();
	        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
	        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(
	                this,  R.drawable.icon);
	        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

	        // Now, return the result to the launcher
	        //and remove the current shortcut
	        intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
	        sendBroadcast(intent);
	        //and install the fresh one
	        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	        sendBroadcast(intent);
	        //setResult(RESULT_OK, intent);
    		
    		return true;
    	}
    	return false;
    }
    
    @Override
    protected void onNewIntent(Intent intent){
    	super.onNewIntent(intent);
    }
    
    @Override
	protected void onDestroy(){
		super.onDestroy();
		//MyActions.exitActivities(app);
		//System.runFinalizersOnExit(true);
		//System.exit(0);
		//android.os.Process.killProcess(android.os.Process.myPid());
	}
    
    public void finishActivity(){
		((PatraCityGuideApplication)this.getApplication()).exitActivities(app);
	}
    
    public void proceedToNextScreen(){
    	Intent guideActivityIntent = new Intent(this,
				gr.patra_city_guide.guide.GuideActivity.class);
		startActivity(guideActivityIntent);
    }
    
}
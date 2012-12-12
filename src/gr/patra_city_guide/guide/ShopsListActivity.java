package gr.patra_city_guide.guide;

import gr.patra_city_guide.PatraCityGuideApplication;
import gr.patra_city_guide.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShopsListActivity extends ListActivity {
	
    private Intent intent=null;
	private String[] names,streets,tels,mobs,faxes,sites,emails,maps;
	private PatraCityGuideApplication app;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.shops_list);
	    
	    //get the incoming data from the intent
	    names=getIntent().getStringArrayExtra("names");
	    streets=getIntent().getStringArrayExtra("streets");
	    tels=getIntent().getStringArrayExtra("tels");
	    mobs=getIntent().getStringArrayExtra("mobs");
	    faxes=getIntent().getStringArrayExtra("faxes");
	    sites=getIntent().getStringArrayExtra("sites");
	    emails=getIntent().getStringArrayExtra("emails");
	    maps=getIntent().getStringArrayExtra("maps");
	    
	    setListAdapter(new ArrayAdapter<CharSequence>(this, 
	              android.R.layout.simple_list_item_1, names));
	    
	    app=(PatraCityGuideApplication)this.getApplication();
	    app.getActivitiesRunning().add(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.guide_menu, menu);
    	return true;
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if(item.getItemId()==R.id.PatraCityGuideActivity_menu_settings){
    		//proceed to the preferences screen
    		Intent preferenceSettingsIntent = new Intent(this,
    				gr.patra_city_guide.SettingsPreferenceActivity.class);
    		startActivity(preferenceSettingsIntent);
    		return true;
    	}
    	return false;
	}
    @Override
    protected void onNewIntent(Intent intent){
    	super.onNewIntent(intent);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
    	//create a new Intent
		intent=new Intent(this,ShopDetailsActivity.class);
		
		//parse all the arrays as Intents to next Activity
		intent.putExtra("name", names[position]);
		intent.putExtra("street", streets[position]);
		intent.putExtra("tel", tels[position]);
		intent.putExtra("mob", mobs[position]);
		intent.putExtra("fax", faxes[position]);
		intent.putExtra("site", sites[position]);
		intent.putExtra("email", emails[position]);
		intent.putExtra("map", maps[position]);
		
		//start the Activity through the intent
		startActivity(intent);
    }
    
    @Override
	protected void onDestroy(){
		super.onDestroy();
		//MyActions.exitActivities(app);
		//System.runFinalizersOnExit(true);
		//System.exit(0);
		//android.os.Process.killProcess(android.os.Process.myPid());
	}
}

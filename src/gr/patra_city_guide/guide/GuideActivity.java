package gr.patra_city_guide.guide;

import java.io.InputStream;

import gr.patra_city_guide.PatraCityGuideApplication;
import gr.patra_city_guide.R;
import gr.patra_city_guide.xml.MyXMLHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GuideActivity extends Activity {

	private ArrayAdapter<CharSequence> categoryAdapter,subcategoryAdapter,actionAdapter;
	private Spinner categorySpinner,subcategorySpinner,actionSpinner;
	//private static GuideActivity guideActivity=this;
	private EditText textArea;
	private MyXMLHandler handler=null;
	//private String[] names,streets,tels,mobs,faxes,sites,emails,maps;
	private PatraCityGuideApplication app;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category_guide);
        
        //add content to the category spinner
        categorySpinner = (Spinner)this.findViewById(R.id.category_spinner);
        categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(
        		new AdapterView.OnItemSelectedListener() {
        			@Override
        			public void onItemSelected(AdapterView<?> parent, View view,
        					int pos, long id) {
        				updateSubcategorySpinner(pos);
        			}
        			@Override
        			public void onNothingSelected(AdapterView<?> parent) {
        				// TODO Auto-generated method stub
        				
        			}
		});
        
      //add content to the subcategory spinner
         subcategorySpinner = (Spinner)this.findViewById(R.id.subcategory_spinner);
        subcategoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.subcategory_00, android.R.layout.simple_spinner_item);
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        subcategorySpinner.setAdapter(subcategoryAdapter);
        
      //add content to the action spinner
        actionSpinner = (Spinner)this.findViewById(R.id.action_spinner);
        actionAdapter = ArrayAdapter.createFromResource(
        		this, R.array.actions, android.R.layout.simple_spinner_item);
        actionAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        actionSpinner.setAdapter(actionAdapter);
        //update the screen by adding the search text
        actionSpinner.setOnItemSelectedListener(
        		new AdapterView.OnItemSelectedListener() {
        			@Override
        			public void onItemSelected(AdapterView<?> parent, View view,
        					int pos, long id) {
        				if(pos==1){
        					textArea=(EditText)findViewById(R.id.search_text_area);
        					textArea.setVisibility(View.VISIBLE);
        				}
        				else{
        					EditText textArea=(EditText)findViewById(R.id.search_text_area);
        					textArea.setVisibility(View.INVISIBLE);
        				}
        			}
        			@Override
        			public void onNothingSelected(AdapterView<?> parent) {
        				// TODO Auto-generated method stub
        				
        			}
		});
        
      //execute exit from exitButton
        Button exitButton=(Button)findViewById(R.id.GuideActivity_exitButton);
        exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishActivity();
			}
		});
        
        //proceed to the next screen.
        Button nextButton=(Button)findViewById(R.id.GuideActivity_nextButton);
        nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
	protected void onDestroy(){
		super.onDestroy();
		//MyActions.exitActivities(app);
		//System.runFinalizersOnExit(true);
		//System.exit(0);
		//android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
    protected void onNewIntent(Intent intent){
    	super.onNewIntent(intent);
    }
	
	public void finishActivity(){
		((PatraCityGuideApplication)this.getApplication()).exitActivities(app);
	}
	
	public void proceedToNextScreen(){
		ProgressDialog dialog = ProgressDialog.show(this, "Προβολή καταστημάτων", 
                "Φορτώνει. Παρακαλώ περιμένετε...", true);
		
		if(actionSpinner.getSelectedItemPosition()==0){
			//load shops data appropriate xml file
			prepareData();
			//I start the next activity
			startNextActivity();
		}
		else if(actionSpinner.getSelectedItemPosition()==1){
			//search and show some shops
			
		}
		else if(actionSpinner.getSelectedItemPosition()==2){
			//show nearest shops
			
		}
		else if(actionSpinner.getSelectedItemPosition()==3){
			//show favorite shops
			
		}
		dialog.dismiss();
	}
	/*
	 * This method loads the data from the appropriate xml file
	 */
	public void prepareData(){
		AssetManager manager=getAssets();
		InputStream is=null;
		int[] selectedIntValues=new int[]{
				categorySpinner.getSelectedItemPosition(),
				subcategorySpinner.getSelectedItemPosition()};
		String[] selectedStringValues=new String[selectedIntValues.length];
		for(int i=0;i<selectedIntValues.length;i++){
			if(selectedIntValues[i]<10){
				selectedStringValues[i]="0"+selectedIntValues[i];
			}
			else{
				selectedStringValues[i]=""+selectedIntValues[i];
			}
		}
		try{
			 is=manager.open(
					 "Category_"+selectedStringValues[0]+
					 "/Shoplist_"+selectedStringValues[1]+".xml");
		}
		catch(Exception e){
			try{
				is=manager.open("empty_shoplist.xml");
			}
			catch(Exception e1){
				//handle later
			} 
		}
		handler=new MyXMLHandler(is);
	}
	public void startNextActivity(){
		//create a new Intent
		Intent intent=new Intent(this,ShopsListActivity.class);
		
		//parse all the arrays as Intents to next Activity
		intent.putExtra("names", handler.getNames());
		intent.putExtra("streets", handler.getStreets());
		intent.putExtra("tels", handler.getTels());
		intent.putExtra("mobs", handler.getMobs());
		intent.putExtra("faxes", handler.getFaxes());
		intent.putExtra("sites", handler.getSites());
		intent.putExtra("emails", handler.getEmails());
		intent.putExtra("maps", handler.getMaps());
		
		//start the Activity through the intent
		startActivity(intent);
	}
	
	public void updateSubcategorySpinner(int pos){
		if(pos==0){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_00, android.R.layout.simple_spinner_item);
		}
		else if(pos==1){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_01, android.R.layout.simple_spinner_item);
		}
		else if(pos==2){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_02, android.R.layout.simple_spinner_item);
		}
		else if(pos==3){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_03, android.R.layout.simple_spinner_item);
		}
		else if(pos==4){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_04, android.R.layout.simple_spinner_item);
		}
		else if(pos==5){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_05, android.R.layout.simple_spinner_item);
		}
		else if(pos==6){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_06, android.R.layout.simple_spinner_item);
		}
		else if(pos==7){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_07, android.R.layout.simple_spinner_item);
		}
		else if(pos==8){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_08, android.R.layout.simple_spinner_item);
		}
		else if(pos==9){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_09, android.R.layout.simple_spinner_item);
		}
		else if(pos==10){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_10, android.R.layout.simple_spinner_item);
		}
		else if(pos==11){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_11, android.R.layout.simple_spinner_item);
		}
		else if(pos==12){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_12, android.R.layout.simple_spinner_item);
		}
		else if(pos==13){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_13, android.R.layout.simple_spinner_item);
		}
		else if(pos==14){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_14, android.R.layout.simple_spinner_item);
		}
		else if(pos==15){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_15, android.R.layout.simple_spinner_item);
		}
		else if(pos==16){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_16, android.R.layout.simple_spinner_item);
		}
		else if(pos==17){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_17, android.R.layout.simple_spinner_item);
		}
		else if(pos==18){
			subcategoryAdapter = ArrayAdapter.createFromResource(
					this, R.array.subcategory_18, android.R.layout.simple_spinner_item);
		}
		subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		subcategorySpinner.setAdapter(subcategoryAdapter);
	}
}

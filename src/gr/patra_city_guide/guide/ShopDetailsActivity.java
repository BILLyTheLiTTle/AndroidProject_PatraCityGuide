package gr.patra_city_guide.guide;

import java.util.ArrayList;
import java.util.Arrays;

import gr.patra_city_guide.PatraCityGuideApplication;
import gr.patra_city_guide.R;
import gr.patra_city_guide.my_listeners.MyOnLongClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShopDetailsActivity extends Activity{

	private String name,street,tel,mob,fax,site,email,map;
	private String[] streetSpinnerData,nameTitleData,
					siteSpinnerData,siteHelpSpinnerData,
					telNmobSpinnerData,
					faxSpinnerData,faxHelpSpinnerData,
					emailSpinnerData,emailHelpSpinnerData,
					mapDragableData;
	private ArrayAdapter<String> spinnersDataArrayAdapter;
	private boolean runAgain=true;
	private int selectedStreetItem=0;
	private Thread thr;
	private Spinner streetSpinner;
	private ShopDetailsActivity activity=this;
	private PatraCityGuideApplication app;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.shop_details);
	    
	    //get Intents
	    name=getIntent().getStringExtra("name");
	    street=getIntent().getStringExtra("street");
	    tel=getIntent().getStringExtra("tel");
	    mob=getIntent().getStringExtra("mob");
	    fax=getIntent().getStringExtra("fax");
	    fax=getIntent().getStringExtra("fax");
	    site=getIntent().getStringExtra("site");
	    email=getIntent().getStringExtra("email");
	    map=getIntent().getStringExtra("map");
	    
	    app=(PatraCityGuideApplication)this.getApplication();
	    app.getActivitiesRunning().add(this);
	    
	    //update the visible area
	    updateView();
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		thr=new Thread(){
			public void run(){
				while(runAgain)
				{
					if(streetSpinnerData==null || streetSpinnerData.length==1){
						runAgain=false;
						thr=null;
						break;
					}
					if(streetSpinner.getSelectedItemPosition()==selectedStreetItem){
						//do nothing
					}
					else{
						Log.e("THREAD ENTERED",""+selectedStreetItem);
						selectedStreetItem=streetSpinner.getSelectedItemPosition();
						activity.runOnUiThread(new Runnable(){
							public void run(){
								setContentView(R.layout.shop_details);
								updateView();
								//streetSpinner.setSelection(1);
							}
						});
					}
				}
			}
		};
		thr.start();
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.shop_details_menu, menu);
    	return true;
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if(item.getItemId()==R.id.ShopDetailsActivity_menu_help){
    		//show a Toast
    		Toast.makeText(this, "Πιέστε παρατεταμένα μία από τις διαθέσιμες" +
    				" πληροφορίες για το κατάστημα για" +
    				" περισσότερες λειτουργίες", Toast.LENGTH_LONG).show();
    		return true;
    	}
    	else if(item.getItemId()==R.id.PatraCityGuideActivity_menu_settings){
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
		//System.runFinalizersOnExit(true);
		//System.exit(0);
		//android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void updateView(){
		if(name.equals("Επωνυμία Καταστήματος")){
			Log.e("HERE","HERE");
			updateEmptyShopContents();
		}
		else{
			prepareDataArrays(selectedStreetItem);
			//NAME data
			if(nameTitleData.length==1){
				setTitle(nameTitleData[0]);
			}
			else{
				//I will do sth in future release
			}
			//STREET
			updateStreetContents();
			//Telephone N mobile data
			updateTelNMobContents();
			//Fax DATA
			updateFaxContents();
			//Website DATA
			updateSiteContents();
			//e-mail DATA
			updateEmailContents();
			//exit button actions
			Button exit=(Button)findViewById(R.id.exitButton_ShopDetailsActivity);
			exit.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage("Σίγουρα θέλετε να κάνετε έξοδο;")
					       .setCancelable(false)
					       .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                finishActivity();
					           }
					       })
					       .setNegativeButton("Όχι", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}
			});
		}
	}
	
	public void finishActivity(){
		runAgain=false;
		thr=null;
		((PatraCityGuideApplication)this.getApplication()).exitActivities(app);
	}
	
	public void prepareDataArrays(int item){
		//create the tables for every spinner
		Log.e("STREET",street);
		nameTitleData=createDataArray(name, "###");
		streetSpinnerData=createDataArray(street," ### ");
		siteHelpSpinnerData=createDataArray(site," ### ");
		siteSpinnerData=createDataArray(siteHelpSpinnerData[item]," , ");
		emailHelpSpinnerData=createDataArray(email," ### ");
		emailSpinnerData=createDataArray(emailHelpSpinnerData[item]," , ");
		mapDragableData=createDataArray(map," ### ");
		String[] helptels=createDataArray(tel," ### ");
		String[] helpmobs=createDataArray(mob, " ### ");
		String[] tels=createDataArray(helptels[item]," , ");
		String[] mobs=createDataArray(helpmobs[item], " , ");
		ArrayList<String> telsNmobs=new ArrayList<String>(
				tels.length+mobs.length);
		telsNmobs.addAll(Arrays.asList(tels));
		telsNmobs.addAll(Arrays.asList(mobs));
		//find the "Δε διατίθεται" value and remove it from the array
		//Maximum "Δε διατίθεται" value is going to be shown twice 
		//because there are 1 for telephone and 1 for mobile
		int maxValue=2;
		for(int i=0;i<=maxValue;i++)
			telsNmobs.remove("Δε διατίθεται");
		telsNmobs.trimToSize();
		telNmobSpinnerData=new String[telsNmobs.size()];
		telsNmobs.toArray(telNmobSpinnerData);
		faxHelpSpinnerData=createDataArray(fax, " #*# ");
		faxSpinnerData=createDataArray(faxHelpSpinnerData[item], " , ");
	}
	
	public void updateEmptyShopContents(){
					
		//hide some spinners and a textview
		findViewById(R.id.street_spinner_ShopDetailsActivity).
					setVisibility(View.GONE);
		findViewById(R.id.telNmob_textView_ShopDetailsActivity).
					setVisibility(View.GONE);
		findViewById(R.id.fax_spinner_ShopDetailsActivity).
					setVisibility(View.GONE);
		findViewById(R.id.site_spinner_ShopDetailsActivity).
					setVisibility(View.GONE);
		findViewById(R.id.email_spinner_ShopDetailsActivity).
					setVisibility(View.GONE);
		
		//fill the other Views
		setTitle(name);
		((TextView)findViewById(R.id.street_textView_ShopDetailsActivity)).
			setText(street);
		spinnersDataArrayAdapter=new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
		spinnersDataArrayAdapter.add(tel);
		spinnersDataArrayAdapter.add(mob);
		spinnersDataArrayAdapter.setDropDownViewResource(
				android.R.layout.simple_dropdown_item_1line);
		((Spinner)findViewById(R.id.telNmob_spinner_ShopDetailsActivity)).
			setAdapter(spinnersDataArrayAdapter);
		((TextView)findViewById(R.id.fax_textView_ShopDetailsActivity)).
			setText(fax);
		((TextView)findViewById(R.id.site_textView_ShopDetailsActivity)).
			setText(site);
		((TextView)findViewById(R.id.email_textView_ShopDetailsActivity)).
			setText(email);
	}
	
	public void updateStreetContents(){
		/* 
		 * if array length=1 then hide spinner else hide textview
		 * if sharedPrefernces=no_web || map=x,x hide button
		 * insert data
		 */
		if(streetSpinnerData.length==0){
			//hide all (TextViews, Spinner, Button)
			((TextView)findViewById(R.id.street_title_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((TextView)findViewById(R.id.street_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((Spinner)findViewById(R.id.street_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
		}
		else if(streetSpinnerData.length==1){
			if(streetSpinnerData[0].equals("Δε διατίθεται")){
				//hide all (TextViews, Spinner, Button)
				((TextView)findViewById(R.id.street_title_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((TextView)findViewById(R.id.street_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((Spinner)findViewById(R.id.street_spinner_ShopDetailsActivity)).
				setVisibility(View.GONE);				
			}
			else{
				findViewById(R.id.street_spinner_ShopDetailsActivity).
					setVisibility(View.GONE);
				TextView tx=((TextView)findViewById(R.id.street_textView_ShopDetailsActivity));
				tx.setText(streetSpinnerData[0]);
				//if(mapDragableData.length==1){
					if((mapDragableData[0].equals("x,x")==false)){
						//the dragNdrop of the street item is activated
						new MyOnLongClickListener(tx, this);
					}
					else{
						// deactivate the dragNdrop of the street item
						
					}
				}
				//else{
					// I will do sth in future release
				//}
			//}
		}
		else if(streetSpinnerData.length>1){
			((TextView)findViewById(R.id.street_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			spinnersDataArrayAdapter=new ArrayAdapter<String>(
	                this, android.R.layout.simple_spinner_item,
	                streetSpinnerData);
			spinnersDataArrayAdapter.setDropDownViewResource(
					android.R.layout.simple_dropdown_item_1line);
			streetSpinner=((Spinner)findViewById(R.id.street_spinner_ShopDetailsActivity));
			streetSpinner.setAdapter(spinnersDataArrayAdapter);
			streetSpinner.setSelection(selectedStreetItem);
			//new MyOnLongClickListener(sp,this);
			if((mapDragableData[selectedStreetItem].equals("x,x")==false)){
				//the dragNdrop of the street item is activated
				new MyOnLongClickListener(streetSpinner, this);
			}
			//else{
				// deactivate the dragNdrop of the street item
				
			//}
		}
	}
	
	public void updateTelNMobContents(){
		/*
		 * if array.length==0 then remove everything for tel n mob and the
		 * button to make the call
		 * else if array.length>1 then show spinner hide textview
		 * else show textview hide spinner
		 */
		if(telNmobSpinnerData.length==0){
			//hide all (TextViews, Spinner, Button)
			((TextView)findViewById(R.id.telNmob_title_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((TextView)findViewById(R.id.telNmob_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((Spinner)findViewById(R.id.telNmob_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
		}
		else if(telNmobSpinnerData.length>1){
			((TextView)findViewById(R.id.telNmob_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			spinnersDataArrayAdapter=new ArrayAdapter<String>(
	                this, android.R.layout.simple_spinner_item,
	                telNmobSpinnerData);
			spinnersDataArrayAdapter.setDropDownViewResource(
					android.R.layout.simple_dropdown_item_1line);
			Spinner sp=((Spinner)findViewById(R.id.telNmob_spinner_ShopDetailsActivity));
			sp.setAdapter(spinnersDataArrayAdapter);
			new MyOnLongClickListener(sp,this);
		}
		else{
			((Spinner)findViewById(R.id.telNmob_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
			TextView tx=((TextView)findViewById(R.id.telNmob_textView_ShopDetailsActivity));
			tx.setText(telNmobSpinnerData[0]);
			new MyOnLongClickListener(tx,this);
		}
	}
	
	public void updateFaxContents(){
		if(faxSpinnerData.length==0){
			//hide everything
			((TextView)findViewById(R.id.fax_title_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((TextView)findViewById(R.id.fax_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((Spinner)findViewById(R.id.fax_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
		}
		else if(faxSpinnerData.length>1){
			//hide textview, show spinner
			((TextView)findViewById(R.id.fax_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			spinnersDataArrayAdapter=new ArrayAdapter<String>(
	                this, android.R.layout.simple_spinner_item,
	                faxSpinnerData);
			spinnersDataArrayAdapter.setDropDownViewResource(
					android.R.layout.simple_dropdown_item_1line);
			((Spinner)findViewById(R.id.fax_spinner_ShopDetailsActivity)).
				setAdapter(spinnersDataArrayAdapter);
		}
		else{
			if(faxSpinnerData[0].equals("Δε διατίθεται")){
				//hide everything
				((TextView)findViewById(R.id.fax_title_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((TextView)findViewById(R.id.fax_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((Spinner)findViewById(R.id.fax_spinner_ShopDetailsActivity)).
				setVisibility(View.GONE);
			}
			else{
				//hide spinner show textview
				((Spinner)findViewById(R.id.fax_spinner_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((TextView)findViewById(R.id.fax_textView_ShopDetailsActivity)).
				setText(faxSpinnerData[0]);
			}
		}
	}
	
	public void updateSiteContents(){
		if(siteSpinnerData.length==0){
			//hide all
			((TextView)findViewById(R.id.site_title_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((TextView)findViewById(R.id.site_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((Spinner)findViewById(R.id.site_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
		}
		else if(siteSpinnerData.length>1){
			//hide textview and show spinner
			((TextView)findViewById(R.id.site_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			spinnersDataArrayAdapter=new ArrayAdapter<String>(
	                this, android.R.layout.simple_spinner_item,
	                siteSpinnerData);
			spinnersDataArrayAdapter.setDropDownViewResource(
					android.R.layout.simple_dropdown_item_1line);
			Spinner sp=((Spinner)findViewById(R.id.site_spinner_ShopDetailsActivity));
			sp.setAdapter(spinnersDataArrayAdapter);
			new MyOnLongClickListener(sp,this);
		}
		else{
			if(siteSpinnerData[0].equals("Δε διατίθεται")){
				//hide all
				((TextView)findViewById(R.id.site_title_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((TextView)findViewById(R.id.site_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((Spinner)findViewById(R.id.site_spinner_ShopDetailsActivity)).
				setVisibility(View.GONE);
			}
			//hide spinner and show textview
			((Spinner)findViewById(R.id.site_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
			TextView tx=((TextView)findViewById(R.id.site_textView_ShopDetailsActivity));
			tx.setText(siteSpinnerData[0]);
			new MyOnLongClickListener(tx,this);
		}
	}
	
	public void updateEmailContents(){
		if(emailSpinnerData.length==0){
			//hide all
			((TextView)findViewById(R.id.email_title_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((TextView)findViewById(R.id.email_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			((Spinner)findViewById(R.id.email_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
		}
		else if(emailSpinnerData.length>1){
			//hide textview and show spinner
			((TextView)findViewById(R.id.email_textView_ShopDetailsActivity)).
			setVisibility(View.GONE);
			spinnersDataArrayAdapter=new ArrayAdapter<String>(
	                this, android.R.layout.simple_spinner_item,
	                emailSpinnerData);
			spinnersDataArrayAdapter.setDropDownViewResource(
					android.R.layout.simple_dropdown_item_1line);
			Spinner sp=((Spinner)findViewById(R.id.email_spinner_ShopDetailsActivity));
			sp.setAdapter(spinnersDataArrayAdapter);
			new MyOnLongClickListener(sp,this);
		}
		else{
			if(emailSpinnerData[0].equals("Δε διατίθεται")){
				//hide all
				((TextView)findViewById(R.id.email_title_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((TextView)findViewById(R.id.email_textView_ShopDetailsActivity)).
				setVisibility(View.GONE);
				((Spinner)findViewById(R.id.email_spinner_ShopDetailsActivity)).
				setVisibility(View.GONE);
			}
			//hide spinner and show textview
			((Spinner)findViewById(R.id.email_spinner_ShopDetailsActivity)).
			setVisibility(View.GONE);
			TextView tx=((TextView)findViewById(R.id.email_textView_ShopDetailsActivity));
			tx.setText(emailSpinnerData[0]);
			new MyOnLongClickListener(tx,this);
		}
	}
	
	public String[] createDataArray(String data,String dilimiter){
		String[] list;
		try{
			list=data.split(dilimiter);
		}
		catch(NullPointerException npe){
			list=new String[]{data};
			Log.e("ERROR IN SPLIT",list[0]);
		}
		Log.e("ARRAY",list[0]);
		return list;
	}
	
	public String getSelectedCoordinates(){
		return mapDragableData[selectedStreetItem];
	}

}

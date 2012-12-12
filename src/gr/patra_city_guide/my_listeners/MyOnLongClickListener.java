package gr.patra_city_guide.my_listeners;

import gr.patra_city_guide.R;
import gr.patra_city_guide.guide.ShopDetailsActivity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MyOnLongClickListener implements OnLongClickListener {

	private ShopDetailsActivity activity;
	
	public MyOnLongClickListener(View v,ShopDetailsActivity activity){
		super();
		v.setOnLongClickListener(this);
		this.activity=activity;
	}

	@Override
	public boolean onLongClick(View v) {
		int id=v.getId();
		//Spinner for street to map
		if(id==R.id.street_spinner_ShopDetailsActivity){
			Intent intent=new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("geo:0,0"+
					"?z=7&q="+activity.getSelectedCoordinates()));
			activity.startActivity(intent);
		}
		//TextView for street to map
		else if(id==R.id.street_textView_ShopDetailsActivity){
			Intent intent=new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("geo:0,0"+
					"?z=6&q="+activity.getSelectedCoordinates()));
			activity.startActivity(intent);
		}
		//Spinner for Tel N Mob
		else if(id==R.id.telNmob_spinner_ShopDetailsActivity){
			String tel=(String)((Spinner)v.findViewById(R.id.telNmob_spinner_ShopDetailsActivity)).
			getSelectedItem();
			Intent intent=new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:"+tel));
			activity.startActivity(intent);
		}
		//TextView for Tel N Mob
		else if(id==R.id.telNmob_textView_ShopDetailsActivity){
			String tel=(String) ((TextView)v.findViewById(R.id.telNmob_textView_ShopDetailsActivity)).
			getText();
			Intent intent=new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:"+tel));
			activity.startActivity(intent);
		}
		//Spinner for site
		else if(id==R.id.site_spinner_ShopDetailsActivity){
			String site=(String)((Spinner)v.findViewById(R.id.site_spinner_ShopDetailsActivity)).
			getSelectedItem();
			Intent intent=new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(site));
			activity.startActivity(intent);
		}
		//TextView for site
		else if(id==R.id.site_textView_ShopDetailsActivity){
			String site=(String)((TextView)v.findViewById(R.id.site_textView_ShopDetailsActivity)).
			getText();
			Intent intent=new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(site));
			activity.startActivity(intent);
		}
		//Spinner for email
		else if(id==R.id.email_spinner_ShopDetailsActivity){
			String email=(String)((Spinner)v.findViewById(R.id.email_spinner_ShopDetailsActivity)).
			getSelectedItem();
			Intent intent=new Intent(Intent.ACTION_SEND);
			intent.setType("plain/text");
			intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
			activity.startActivity(intent);
		}
		//TextView for email
		else if(id==R.id.email_textView_ShopDetailsActivity){
			String email=(String)((TextView)v.findViewById(R.id.email_textView_ShopDetailsActivity)).
			getText();
			Intent intent=new Intent(Intent.ACTION_SEND);
			intent.setType("plain/text");
			intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
			activity.startActivity(intent);
		}
		return true;
	}
	
}

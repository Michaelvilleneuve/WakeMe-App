package com.twaa9l.photosend.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import fr.michaelvilleneuve.wakemeapp.R;

public class ViewImageActivity extends Activity {
	/////////////////////
	String extStorageDirectory;

	Bitmap bm;
	private AdView mAdView;

	
	
	
	
	
	
	////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
		
		
		//ADS
		Resources res = getResources();
		boolean allowAds = res.getBoolean(R.bool.adViewImage);
		
		if(allowAds){
			mAdView = (AdView) findViewById(R.id.adView);
	        mAdView.loadAd(new AdRequest.Builder().build());
		}
		//ADS
		
		
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		ImageView imageView = (ImageView)findViewById(R.id.imageView);
		
		Uri imageUri = getIntent().getData();
		
		Picasso.with(this).load(imageUri.toString()).into(imageView);
		
		
		//Is app Self destroy the image ? 

		boolean isSelfDestroy = res.getBoolean(R.bool.self_destroy);
		int timeToDestroy = res.getInteger(R.integer.time_to_destroy);
		
		if(isSelfDestroy){
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					finish();
				}
			}, timeToDestroy*1000);
		}
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	
	
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
            
		}
		return super.onOptionsItemSelected(item);
	}

}

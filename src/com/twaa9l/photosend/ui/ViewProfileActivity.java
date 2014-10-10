package com.twaa9l.photosend.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import fr.michaelvilleneuve.wakemeapp.R;

public class ViewProfileActivity extends Activity {
	protected static final BufferType Date = null;
	public String UNAME;
	public ParseFile uProfilePicture;
	public String FullName;
	public String homeTown;
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_profile);
		
		//ADS
		Resources res = getResources();
		boolean allowAds = res.getBoolean(R.bool.adViewProfile);
		
		if(allowAds){
			mAdView = (AdView) findViewById(R.id.adView);
	        mAdView.loadAd(new AdRequest.Builder().build());
		}
		//ADS

		
		setProgressBarIndeterminateVisibility(true);
	    Intent intent = getIntent();
	    String fId = intent.getStringExtra("FriendID");
	   
	    ParseQuery<ParseUser> querys = ParseUser.getQuery();
	    querys.whereEqualTo("objectId", fId);
	    querys.findInBackground(new FindCallback<ParseUser>() {
	      public void done(List<ParseUser> objects, ParseException e) {
	        if (e == null) {
	            // The query was successful.
	        	for(ParseObject thisUser : objects){
	        	       //access the data associated with the ParseUser using the get method
	        	       //pu.getString("key") or pu.get("key")
	        		UNAME = thisUser.getString("username");
	        		uProfilePicture = thisUser.getParseFile("profilePicture");
	        	}
	        } else {
	            // Something went wrong.
	        }
	      }
	    });
	    
	    
	    
		    
	    /////parse Query
	    
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("userinfo");
	    query.whereEqualTo("UserId", fId);
	    query.findInBackground(new FindCallback<ParseObject>() {
	        public void done(List<ParseObject> objects, ParseException e) {
	        	if (e == null) {
	        		for(ParseObject thisUser : objects){
		        	       //access the data associated with the ParseUser using the get method
		        	       //pu.getString("key") or pu.get("key")
		        		//UNAME = thisUser.getUsername();
		        		FullName = thisUser.getString("FullName");
		        		homeTown = thisUser.getString("HomeTown");

		        	}
		        	setProgressBarIndeterminateVisibility(false);

	        		TextView fullName = (TextView)findViewById(R.id.fullName);
		        	fullName.setText(FullName);
		        	
		        	TextView userName = (TextView)findViewById(R.id.userName);
		        	userName.setText(UNAME);
		        	
		        	TextView HomeTown = (TextView)findViewById(R.id.HomeTown);
		        	HomeTown.setText(homeTown);
		        	
		        	final ParseImageView imageView = (ParseImageView)findViewById(R.id.defaultAvatar);
		        	imageView.setParseFile(uProfilePicture);
		        	imageView.loadInBackground(new GetDataCallback() {
		        	     public void done(byte[] data, ParseException e) {
		        	     // The image is loaded and displayed!                    
		        	     int oldHeight = imageView.getHeight();
		        	     int oldWidth = imageView.getWidth();                 
		        	     Log.v("LOG!!!!!!", "imageView height = " + oldHeight);      // DISPLAYS 90 px
		        	     Log.v("LOG!!!!!!", "imageView width = " + oldWidth);        // DISPLAYS 90 px      
		        	     }
		        	});
		        	
	        		
	            } else {
	                Log.d("score", "Error: " + e.getMessage());
	            }
	        }
	    });
	    
	    	    
	    ////
	    
	    
	    
	    
	    
	    
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	
	

}

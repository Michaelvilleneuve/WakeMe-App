package com.twaa9l.photosend;

import android.app.Application;
import android.content.res.Resources;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.twaa9l.photosend.ui.MainActivity;
import com.twaa9l.photosend.utils.ParseConstants;
import fr.michaelvilleneuve.wakemeapp.R;

public class PhotoSendApplication extends Application {
	
	@Override
	public void onCreate() { 
		super.onCreate();
		
		
		Resources res = getResources();
		String PARSE_APP_ID = res.getString(R.string.parse_app_id);
		String PARSE_CLIENT_KEY = res.getString(R.string.parse_client_key);
		
		
	    Parse.initialize(this,PARSE_APP_ID, PARSE_CLIENT_KEY);
	    
	    //ParseFacebookUtils.initialize(FB_APP_ID);
	    
	    PushService.setDefaultPushCallback(this, MainActivity.class);
	    ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
	public static void updateParseInstallation(ParseUser user) {
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
		installation.saveInBackground();
	}
}

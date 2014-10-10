package com.twaa9l.photosend.ui;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import fr.michaelvilleneuve.wakemeapp.R;

public class ProfileActivity extends Activity {
	protected EditText mEmail;
	protected EditText mFullName;
	protected EditText mHomeTown;

	protected String FullName;
	protected String HomeTown;
	
	protected EditText mPassword;
	protected String cu;
	protected String UserId;
	protected String oid;
	protected Button mSignUpButton;
	protected ImageView mProfilePicture;
	private static int RESULT_LOAD_IMAGE = 1;
	private AdView mAdView;
	protected ParseUser ThisUser = ParseUser.getCurrentUser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		
		//ADS
		Resources res = getResources();
		boolean allowAds = res.getBoolean(R.bool.adProfile);
		
		if(allowAds){
			mAdView = (AdView) findViewById(R.id.adView);
	        mAdView.loadAd(new AdRequest.Builder().build());
		}
		//ADS

		

		mEmail = (EditText)findViewById(R.id.emailField);
		mFullName = (EditText)findViewById(R.id.fullnameField);
		mHomeTown = (EditText)findViewById(R.id.hometownField);
		mPassword = (EditText)findViewById(R.id.passwordField);
		
		
		final ParseUser currentUser = ParseUser.getCurrentUser();
		

		UserId = currentUser.getObjectId();
		
		
		
		
		////////////Changer photo profil ///////////////
		
		
		
		mProfilePicture = (ImageView)findViewById(R.id.ProfilePicture);
		mProfilePicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Chose Image From Gallery
				 Intent i = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                 
	                startActivityForResult(i, RESULT_LOAD_IMAGE);
				
				
			}
		});
		
		
		////////////Changr///////////////
		
		
		
		
		
		

		 ParseQuery<ParseObject> query = ParseQuery.getQuery("userinfo");
		    query.whereEqualTo("UserId", UserId);
		    query.findInBackground(new FindCallback<ParseObject>() {
		        public void done(List<ParseObject> objects, ParseException e) {
		        	if (e == null) {
		        		for(ParseObject thisUser : objects){
		        	 	       //access the data associated with the ParseUser using the get method
		        	 	       //pu.getString("key") or pu.get("key")
		        	 		//UNAME = thisUser.getUsername();
		        	 		FullName = thisUser.getString("FullName");
		        	 		HomeTown = thisUser.getString("HomeTown");
		        	 		oid = thisUser.getObjectId();
		        	 	}
		        	 	
		        	 	mEmail.setText(currentUser.getEmail());
		        	 	mFullName.setText(FullName);
		        	 	mHomeTown.setText(HomeTown);
		        	 	
		        		
		            } else {
		                Log.d("score", "Error: " + e.getMessage());
		            }
		        }
		    });
		
	    
 	
 	
 	
		mSignUpButton = (Button)findViewById(R.id.updateProfile);
		mSignUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setProgressBarIndeterminateVisibility(true);

				final String fullname = mFullName.getText().toString();
				final String hometown = mHomeTown.getText().toString();
				final String password = mPassword.getText().toString();
				final String email = mEmail.getText().toString();
					
				
				if(fullname.isEmpty()){
					AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
					builder.setMessage(R.string.empty_full_name)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}else if(email.isEmpty()){
					AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
					builder.setMessage(R.string.empty_email_address)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}else{
					
				
				
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("userinfo");
				 
				// Retrieve the object by id
				query.getInBackground(oid, new GetCallback<ParseObject>() {
				  public void done(ParseObject UserInfoUpdate, ParseException e) {
				    if (e == null) {
				      // Now let's update it with some new data. In this case, only cheatMode and score
				      // will get sent to the Parse Cloud. playerName hasn't changed.
				      UserInfoUpdate.put("FullName", fullname);
				      UserInfoUpdate.put("HomeTown", hometown);
				      UserInfoUpdate.saveInBackground();
				    }
				  }
				});
				
				
				
				
				
				if(password.isEmpty()){
					//No Update on Password
				}else{
					//Update Password
					currentUser.setPassword(password);
					currentUser.saveInBackground();
					
				}
				
				if(email.isEmpty()){
					//No Update on Email
				}else{
					//Update Password
					currentUser.setEmail(email);
					currentUser.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							if(e == null){
								AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
								builder.setMessage(R.string.profile_update_success)
									.setTitle(R.string.success_update)
									.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
							}
							else{
								AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
								builder.setMessage(e.getMessage())
									.setTitle(R.string.signup_error_title)
									.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
					});
					
				}
				
				
				}//Else
				
				
				

			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	
	
	
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            ImageView imageView = (ImageView) findViewById(R.id.ProfilePicture);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            // Convert it to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();

            // Create the ParseFile
            final ParseFile file = new ParseFile(ThisUser.getObjectId()+"_Profile.png", image);
            // Upload the image into Parse Cloud
            file.saveInBackground();

            // Create a New Class called "ImageUpload" in Parse
            //ParseObject imgupload = new ParseObject("ImageUpload");

            // Create a column named "ImageName" and set the string
            //imgupload.put("ImageName", "AndroidBegin Logo");

            // Create a column named "ImageFile" and insert the image
            //imgupload.put("ImageFile", file);

            // Create the class and the columns
            //imgupload.saveInBackground();

                     
            
            ThisUser.put("profilePicture", file);
            ThisUser.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					if(e == null){
						AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
						builder.setMessage(R.string.Image_upload_success)
							.setTitle(R.string.success_update)
							.setPositiveButton(android.R.string.ok, null);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
					else{
						AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
						builder.setMessage(e.getMessage())
							.setTitle(R.string.signup_error_title)
							.setPositiveButton(android.R.string.ok, null);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				}
			});
            
        }
     
     
    }
	
	
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	
	

}

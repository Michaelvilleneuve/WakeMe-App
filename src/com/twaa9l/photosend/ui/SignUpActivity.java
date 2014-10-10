package com.twaa9l.photosend.ui;

import java.io.ByteArrayOutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.twaa9l.photosend.PhotoSendApplication;
import fr.michaelvilleneuve.wakemeapp.R;
import com.twaa9l.photosend.utils.ParseConstants;

public class SignUpActivity extends Activity {
	
	protected EditText mUsername;
	protected EditText mPassword;
	protected EditText mEmail;
	protected EditText mFullName;
	protected EditText mHomeTown;
	protected Button mSignUpButton;
	protected Button mCancelButton;
	protected String gender = null;
	protected ParseRelation<ParseUser> mUserInfo;
	private AdView mAdView;
	
	
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_male:
			if (checked)
	                gender = "Male";
	            break;
	        case R.id.radio_female:
	            if (checked)
	                gender = "Female";
	            break;
	    }
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_sign_up);

		//ADS
		Resources res = getResources();
		boolean allowAds = res.getBoolean(R.bool.adSignup);
		
		if(allowAds){
			mAdView = (AdView) findViewById(R.id.adView);
	        mAdView.loadAd(new AdRequest.Builder().build());
		}
		//ADS
		
		

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		mUsername = (EditText)findViewById(R.id.usernameField);
		mPassword = (EditText)findViewById(R.id.passwordField);
		mEmail = (EditText)findViewById(R.id.emailField);
		mFullName = (EditText)findViewById(R.id.fullnameField);
		mHomeTown = (EditText)findViewById(R.id.hometownField);
		
		mCancelButton = (Button)findViewById(R.id.cancelButton);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		mSignUpButton = (Button)findViewById(R.id.signupButton);
		mSignUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				String email = mEmail.getText().toString();
				final String fullname = mFullName.getText().toString().trim();
				final String hometown = mHomeTown.getText().toString().trim();
				
				username = username.trim();
				password = password.trim();
				email = email.trim();
				
				
				if (username.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage(R.string.empty_username)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}else if (fullname.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage(R.string.empty_full_name)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}else if (password.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage(R.string.empty_password)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}else if (email.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage(R.string.empty_email_address)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}else if (gender == null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage(R.string.empty_gender)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				


				else {
					// create the new user!
					setProgressBarIndeterminateVisibility(true);
					
					
					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(email);
					
					newUser.signUpInBackground(new SignUpCallback() {
						@Override
						public void done(ParseException e) {
							setProgressBarIndeterminateVisibility(false);
							
							if (e == null) {
								// Success!
								PhotoSendApplication.updateParseInstallation(
										ParseUser.getCurrentUser());
								final String mu;
								
								ParseUser currentUser = ParseUser.getCurrentUser();
								mu = currentUser.getObjectId();
																
								ParseObject UserInfos = new ParseObject(ParseConstants.KEY_USER_INFO);
								UserInfos.put("FullName", fullname);
								UserInfos.put("HomeTown", hometown);
								UserInfos.put("Gender", gender);
								UserInfos.put("UserId", mu);
								UserInfos.saveInBackground();

								/////PROFILE PICTURE/////////
								
								
								// Locate the image in res > drawable-hdpi
				                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				                        R.drawable.avatar_empty);
				                // Convert it to byte
				                ByteArrayOutputStream stream = new ByteArrayOutputStream();
				                // Compress image to lower quality scale 1 - 100
				                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				                byte[] image = stream.toByteArray();
				 
				                // Create the ParseFile
				                ParseFile file = new ParseFile(mu+"_profile.png", image);
				                // Upload the image into Parse Cloud
				                file.saveInBackground();
				 
				                // Create a New Class called "ImageUpload" in Parse
				                //ParseObject imgupload = new ParseObject("ImageUpload");
				                currentUser.put("profilePicture", file);
				                // Create the class and the columns
				                currentUser.saveInBackground();
				 
				                
								/////////////
								
								
								Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
							}
							else {
								AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
		});
	}
}

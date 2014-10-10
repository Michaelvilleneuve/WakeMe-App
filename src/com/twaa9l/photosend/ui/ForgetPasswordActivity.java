package com.twaa9l.photosend.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import fr.michaelvilleneuve.wakemeapp.R;

public class ForgetPasswordActivity extends Activity{
	
	protected EditText mEmail;
	protected Button mSubmit;
	protected TextView mLoginBack;
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_rest_password);
		
		
		//ADS
		Resources res = getResources();
		boolean allowAds = res.getBoolean(R.bool.adForgetPassword);
		
		if(allowAds){
			mAdView = (AdView) findViewById(R.id.adView);
	        mAdView.loadAd(new AdRequest.Builder().build());
		}
		//ADS
		
		mLoginBack = (TextView)findViewById(R.id.login);
		mLoginBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		mEmail = (EditText)findViewById(R.id.emailField);
		mSubmit = (Button)findViewById(R.id.resetPassword);
		mSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = mEmail.getText().toString();
				
				email = email.trim();
				
				if (email.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
					builder.setMessage(R.string.empty_email_rest)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else {
					// create the new user!
					setProgressBarIndeterminateVisibility(true);
					
					ParseUser.requestPasswordResetInBackground(email,
                            new RequestPasswordResetCallback() {
						public void done(ParseException e) {
							if (e == null) {
								// Success!
								setProgressBarIndeterminateVisibility(false);
								
								String requestMessage = getString(R.string.success_rest_message);
								
								AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
								builder.setMessage(requestMessage)
									.setTitle(R.string.email_sent)
									.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											
											final Handler handler = new Handler();
								    		 handler.postDelayed(new Runnable() {
								    		   @Override
								    		   public void run() {
								    		    
													Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
													intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
													intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
													startActivity(intent);
								    			}
								    		 
								    		 }, 1000);
										}
								});

								AlertDialog dialog1 = builder.create();
								dialog1.show();
							}
							else {
								setProgressBarIndeterminateVisibility(false);

								AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
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

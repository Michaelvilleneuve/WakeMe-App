package com.twaa9l.photosend.ui;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import fr.michaelvilleneuve.wakemeapp.R;
import com.twaa9l.photosend.adapters.UserAdapter;
import com.twaa9l.photosend.utils.ParseConstants;

public class EditFriendsActivity extends Activity {
	
	
	
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected GridView mGridView;
	protected EditText sUsername;
	protected Button mSearchButton;
	private AdView mAdView;

	public static final String TAG = EditFriendsActivity.class.getSimpleName();
	
	protected List<ParseUser> mUsers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.user_grid);
		
		
		//ADS
		Resources res = getResources();
		boolean allowAds = res.getBoolean(R.bool.adEditFriends);
		
		if(allowAds){
			mAdView = (AdView) findViewById(R.id.adView);
	        mAdView.loadAd(new AdRequest.Builder().build());
		}
		//ADS
		
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		mGridView = (GridView)findViewById(R.id.friendsGrid);
		mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		mGridView.setOnItemClickListener(mOnItemClickListener);
		
		TextView emptyTextView = (TextView)findViewById(android.R.id.empty);
		mGridView.setEmptyView(emptyTextView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		sUsername = (EditText)findViewById(R.id.searchFriend);
		mSearchButton = (Button)findViewById(R.id.searchButton);

		 mSearchButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {

	            	//Get text from each field in register
	                String username = sUsername.getText().toString();
	                //String password = mPassword.getText().toString();

	                ///Remove white spaces from any field
	                /// and make sure they are not empty
	                username = username.trim();
	                //password = password.trim();

	                //Check if fields not empty
	                if(username.isEmpty()){

	                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
	                    builder.setMessage(R.string.login_error_message)
	                        .setTitle(R.string.login_error_title)
	                        .setPositiveButton(android.R.string.ok, null);
	                    AlertDialog dialog = builder.create();
	                    dialog.show();
	                }


	                else{
	                    //Login User
	                    setProgressBarIndeterminateVisibility(true);
	                    ParseQuery<ParseUser> query = ParseUser.getQuery();
	                    query.whereEqualTo("username", username);
	                    query.findInBackground(new FindCallback<ParseUser>() {

	                        @Override
	                        public void done(List<ParseUser> users, ParseException e) {
	                            setProgressBarIndeterminateVisibility(false);
	                            
	                            mUsers = users; 
                                //store users in array
                                //String[] usernames = new String[mUsers.size()];
	                            if(mUsers.size() == 0){
	                            	AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
	                                builder.setMessage("No Users Found")
	                                    .setTitle(R.string.error_title)
	                                    .setPositiveButton(android.R.string.ok, null);
	                                AlertDialog dialog = builder.create();
	                                dialog.show();
	                            }
	                            
	                            if(e == null){
	                                //Success we have Users to display
	                                //Get users match us
	                                mUsers = users; 
	                                //store users in array
	                                String[] usernamess = new String[mUsers.size()];
	                                //Loop Users
	                                int i = 0;
	                                for(ParseUser user : mUsers){
	                                    usernamess[i] = user.getUsername();
	                                    i++;
	                                }

	                                if (mGridView.getAdapter() == null) {
	            						UserAdapter adapter = new UserAdapter(EditFriendsActivity.this, mUsers);
	            						mGridView.setAdapter(adapter);
	            					}
	            					else {
	            						((UserAdapter)mGridView.getAdapter()).refill(mUsers);
	            					}
	            					
	            					addFriendCheckmarks();
	                            }
	                            else{
	                                //No Users to Display
	                                //Log.e(TAG, e.getMessage());
	                                AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
	                                builder.setMessage(e.getMessage())
	                                    .setTitle(R.string.error_title)
	                                    .setPositiveButton(android.R.string.ok, null);
	                                AlertDialog dialog = builder.create();
	                                dialog.show();
	                            }
	                        }
	                    });
	                }

	            }
	        });

		
		
		///Old Code get all users from parse.com
		
		
		//setProgressBarIndeterminateVisibility(true);
		 mCurrentUser = ParseUser.getCurrentUser();
		 mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
			
		 ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
		
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				
				if (e == null) {
					// Success
					mUsers = users;
					String[] usernames = new String[mUsers.size()];
					int i = 0;
					for(ParseUser user : mUsers) {
						usernames[i] = user.getUsername();
						i++;
					}
					if (mGridView.getAdapter() == null) {
						UserAdapter adapter = new UserAdapter(EditFriendsActivity.this, mUsers);
						mGridView.setAdapter(adapter);
					}
					else {
						((UserAdapter)mGridView.getAdapter()).refill(mUsers);
					}
					
					addFriendCheckmarks();
				}
				else {
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
					builder.setMessage(e.getMessage())
						.setTitle(R.string.error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}
	
	
	////end of old code
	
	
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

	private void addFriendCheckmarks() {
		mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				if (e == null) {
					// list returned - look for a match
					for (int i = 0; i < mUsers.size(); i++) {
						ParseUser user = mUsers.get(i);
						
						for (ParseUser friend : friends) {
							if (friend.getObjectId().equals(user.getObjectId())) {
								mGridView.setItemChecked(i, true);
							}else{
								mGridView.setItemChecked(i, false);
							}
						}
					}
				}
				else {
					Log.e(TAG, e.getMessage());
				}
			}
		});
	}
	
	

	protected OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ImageView checkImageView = (ImageView)view.findViewById(R.id.checkImageView);
			checkImageView.setVisibility(View.INVISIBLE);

			if (mGridView.isItemChecked(position)) {
				// add the friend
				mFriendsRelation.add(mUsers.get(position));
				checkImageView.setVisibility(View.VISIBLE);
			}
			else {
				// remove the friend
				mFriendsRelation.remove(mUsers.get(position));
				checkImageView.setVisibility(View.INVISIBLE);
			}
	
			mCurrentUser.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e != null) {
						Log.e(TAG, e.getMessage());
					}
				}
			});
			
		}
	};
}











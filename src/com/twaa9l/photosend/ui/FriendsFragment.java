package com.twaa9l.photosend.ui;

import java.util.List;

import android.R.string;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import fr.michaelvilleneuve.wakemeapp.R;
import com.twaa9l.photosend.adapters.UserAdapter;
import com.twaa9l.photosend.utils.ParseConstants;

public class FriendsFragment extends Fragment {
	
	public static final String TAG = FriendsFragment.class.getSimpleName();

	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;	
	protected List<ParseUser> mFriends;
	protected GridView mGridView;
	protected string mUsers;
	protected ParseUser mMm;
	private AdView mAdView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_grid2,
				container, false);
		
		//ADS
		Resources res = getResources();
		boolean allowAds = res.getBoolean(R.bool.adFriends);
		
		if(allowAds){
			mAdView = (AdView)rootView.findViewById(R.id.adView);
	        mAdView.loadAd(new AdRequest.Builder().build());
		}
		//ADS
		
		
		mGridView = (GridView)rootView.findViewById(R.id.friendsGrid);
		
		TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);
		mGridView.setEmptyView(emptyTextView);
		mGridView.setOnItemClickListener(mOnItemClickListener);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				
				if (e == null) {
					mFriends = friends;
					
					String[] usernames = new String[mFriends.size()];
					int i = 0;
					for(ParseUser user : mFriends) {
						usernames[i] = user.getUsername();
						i++;
					}
					if (mGridView.getAdapter() == null) {
						UserAdapter adapter = new UserAdapter(getActivity(), mFriends);
						mGridView.setAdapter(adapter);
					}
					else {
						((UserAdapter)mGridView.getAdapter()).refill(mFriends);
					}
				}
				else {
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(e.getMessage())
						.setTitle(R.string.error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}
	
	
	
				

	///////////////////////
	
				protected OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						//ImageView checkImageView = (ImageView)view.findViewById(R.id.checkImageView);
							
						ParseObject friend = mFriends.get(position);
						String friendId = friend.getObjectId();
						
						Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
						intent.putExtra("FriendID", friendId);
						startActivity(intent);
						
							Log.d("Clicked item id", " "+ friendId); 

					}
				};
	
				
	//////////////////////
	

}

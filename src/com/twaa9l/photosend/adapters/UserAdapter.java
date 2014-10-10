package com.twaa9l.photosend.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import fr.michaelvilleneuve.wakemeapp.R;
import com.twaa9l.photosend.utils.MD5Util;

public class UserAdapter extends ArrayAdapter<ParseUser> {
	
	protected Context mContext;
	protected List<ParseUser> mUsers;
	
	public UserAdapter(Context context, List<ParseUser> users) {
		super(context, R.layout.message_item, users);
		mContext = context;
		mUsers = users;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
			holder = new ViewHolder();
			holder.userImageView = (ImageView)convertView.findViewById(R.id.userImageView);
			holder.nameLabel = (TextView)convertView.findViewById(R.id.nameLabel);
			holder.checkImageView = (ImageView)convertView.findViewById(R.id.checkImageView);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		ParseUser user = mUsers.get(position);
		ParseFile pPicture = user.getParseFile("profilePicture");
		
			final ParseImageView imageView = (ParseImageView)holder.userImageView;
        	imageView.setParseFile(pPicture);
        	imageView.loadInBackground();

		
		holder.nameLabel.setText(user.getUsername());
		
		GridView gridView = (GridView)parent;
		if (gridView.isItemChecked(position)) {
			holder.checkImageView.setVisibility(View.VISIBLE);
		}
		else {
			holder.checkImageView.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView userImageView;
		ImageView checkImageView;
		TextView nameLabel;
	}
	
	public void refill(List<ParseUser> users) {
		mUsers.clear();
		mUsers.addAll(users);
		notifyDataSetChanged();
	}
}







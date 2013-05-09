package com.mobileharvest.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobileharvest.ImageLoadingFramework.ImageLoader;
import com.mobileharvest.adapters.DBAdapter;
import com.mobileharvest.constants.MHConstants;
import com.mobileharvest.model.AdminPreferences;
import com.mobileharvest.model.FriendsModel;
import com.mobileharvest.model.MediaInfo;
import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class ListeningFrdVotingAndMediaActivity extends BaseProfile implements OnItemClickListener {
	ImageView like;
	boolean voted;
	ImageView vote,addcnt,home,friends,settings,bookmarks,profilephoto;
	private String phoneNumber;
	private StackMob stackmob;
	int user_given_votes;
	int vote_count = 0;
	 
	
	String newNumber;
	String friendsNumber;
	boolean isvoted;
	int friends_votes;
	List<String> list;
	List<String> votesreceicveList; 
	List<String> taggedUserList;
	Map<String,String> friendsprofilepiclist ;
	String[] tableName= {"user_tags","topic_tags","promoted_contacts_tags"};
	String[] votess_list; 
	 
	List<MediaInfo> rowItems ;
	ListView listView;
	List<String> fields;
	List<String> friends_list  ;
	List<String> topics_list ;
	List<String> topicsIds  ;
	List<String> keysList  ;
    String mediaUsername;
    String flag, usern,newflag;
	String pic_url;
	String displayNumber;
	 MediaInfo mediaInfoObj  ;
	 List<MediaInfo> mediaObjList ;
	 HashMap<String, MediaInfo> mediaInfoObjMap  ;
	 FriendsModel frdmodel;
	 public ImageLoader imageLoader;
	 Map<String,MediaInfo> mediaidmap  ;
	// HashMap<String, String> topicsDetails = new HashMap<String,String>(0);
//	 /HashMap<String, PrmContactMdel> prmcontactsDetails = new HashMap<String,PrmContactModel>(0);
	// HashMap<String, FriendsModel> friendsModelDetails = new HashMap<String,FriendsModel>(0);
	 private ListeningAudio listeningAudio;
	 private ProgressDialog progressDialog;
	 private static final int PROGRESSDIALOG_ID = 0;
	 String likestatus,url,mediauserurl,ntfrnduserurl;
	 String pharse,Usernam,nonuser;
	 String parsedresult;
	 int cnt; 
	 List<String> frndsKeys;
	 Long onestar,twostar,threestar,fourstar, fivestar;
	 List<String> userVoteReceivedList;
	 int givenVotes;
	 List <String> friendslis;
	 String profpic = null,from;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.voting);
		imageLoader=new ImageLoader(getApplicationContext());
		StackMobAndroid.init(this.getApplicationContext(),MHConstants.DEV_PROD_VERSION,MHConstants.PUBLIC_KEY);
		stackmob = StackMob.getStackMob();
		flag = getIntent().getStringExtra("flag");
		from = getIntent().getStringExtra("from");
		newflag = getIntent().getStringExtra("newflag");
		like = (ImageView) findViewById(R.id.likeimg);
		addcnt=(ImageView) findViewById(R.id.add);
		ImageView profile = (ImageView) findViewById(R.id.imageView4);
		home = (ImageView) findViewById(R.id.imageView1);
		friends = (ImageView) findViewById(R.id.friends_btn);
		bookmarks = (ImageView) findViewById(R.id.btn_bookmarked);
		settings = (ImageView) findViewById(R.id.settings_btn);
		profilephoto = (ImageView) findViewById(R.id.profilephoto);
		 //initiliazing all list
		  taggedUserList = new ArrayList<String>();
		  friendsprofilepiclist = new LinkedHashMap<String,String>(0);
		  friends_list = new ArrayList<String>();
		  topics_list = new ArrayList<String>();
		  topicsIds = new ArrayList<String>();
		  keysList = new ArrayList<String>();
		  rowItems = new ArrayList<MediaInfo>();
		  frndsKeys = new ArrayList<String>(0);
		  mediaObjList = new ArrayList<MediaInfo>();
		  mediaidmap = new HashMap<String,MediaInfo>(0);
		  userVoteReceivedList = new ArrayList<String>(0);
		  mediaInfoObjMap = new HashMap<String, MediaInfo>();
		  friendslis = new ArrayList<String>(0);
		if(null!=newflag && newflag.equals("user")){
		  like.setVisibility(View.VISIBLE); 
		}else{
			like.setVisibility(View.GONE);
		}
		Intent intent = getIntent();
		phoneNumber = intent.getStringExtra("phoneNumber");
		pic_url = intent.getStringExtra("pic_url");
		displayNumber = intent.getStringExtra("displayNumber"); 
		friends_list = (ArrayList<String>) getIntent().getSerializableExtra("friendsss");
		topics_list = (ArrayList<String>) getIntent().getSerializableExtra("topics");
		topicsIds  =(ArrayList<String>) getIntent().getSerializableExtra("topicsIDS");
		friendsprofilepiclist = (Map<String,String>)getIntent().getSerializableExtra("imageurls");
		mediaObjList.clear();
		mediaObjList = (ArrayList<MediaInfo>) getIntent().getSerializableExtra("mediaidshashmap");
		friendsNumber = intent.getStringExtra("friendsNumber"); 
		isvoted = intent.getBooleanExtra("isvot&&&&&&&&&&&&&&&&&&&&&&ed", isvoted);
		System.out.println("" + isvoted); 
		 
		mediaInfoObj = new MediaInfo();
		DBAdapter db = new DBAdapter(getApplicationContext()); 
		db.open();
		Cursor c = db.getUserInfo(); 
		profpic = c.getString(1);
		if (null == phoneNumber)
		{ 
			phoneNumber = c.getString(0);
			 profpic = c.getString(1);
	         c.close();
	         db.close();
		
		}else{
			profpic = c.getString(1);
	        c.close();
	        db.close();	
		} 
		synchronized (this) {
			fetchuserobjects();
		}
		
		givenVotes=0;   
		imageLoader.DisplayImage(profpic, profilephoto);
 		imageLoader.DisplayImage(pic_url, profile);
 		TextView numberTxt = (TextView) findViewById(R.id.textView1);
 		TextView liketxt = (TextView) findViewById(R.id.likestext);
 		if(flag.equals("100")){
			addcnt.setVisibility(View.VISIBLE); 
		} 
			
		
		if(null!=flag && !flag.equals("4") && !flag.equals("profile") && flag.equals("notprofile")||flag.equals("150") ){
			checkvotes();
		}
		if(null!=flag && flag.equals("profile")){
			FriendsModel frdmodelfrienduser = MHConstants.friendsModelDetails.get(friendsNumber);
	        int recvotes = frdmodelfrienduser.getNoOfReceivedVotes();
			liketxt.setText(Integer.toString(recvotes));
		}
				
		AdminPreferences adminpref = MHConstants.adminPreferences.get("ADMIN_PREF");
         onestar = adminpref.getStar1();  
	     twostar = adminpref.getStar2();
	     threestar = adminpref.getStar3();
	     fourstar = adminpref.getStar4();
	     fivestar = adminpref.getStar5();
		 listView = (ListView) findViewById(android.R.id.list);
		 listView.invalidate();  	
		 
		    if(null!= mediaObjList && mediaObjList.size()>0 && !flag.equals("profile")){ 
			for (int i = 0; i < mediaObjList.size(); i++)
			{
			      MediaInfo mediaObj = mediaObjList.get(i);
				  rowItems.add(mediaObj); 
				  
			} 
		}
		if(flag.equals("profile"))
		{
			for (int j = 0; j < MHConstants.mediaProfileObjList.size(); j++)
			{
			      MediaInfo mediaObj = MHConstants.mediaProfileObjList.get(j);
				  rowItems.add(mediaObj); 
			} 
		}if(flag.equals("back")) {

			 MediaInfo mediaObj = MHConstants.selectedmediaobj.get("mediaInfoObj");
			 rowItems.add(mediaObj);
		}
		
		if(null!=rowItems && rowItems.size()>0 && rowItems.get(0)!=null){
		ItemAdapter adapter = new ItemAdapter(getApplicationContext(),R.layout.media_audio,rowItems); 
		listView.setAdapter(adapter);
		}
listView.setOnItemClickListener(this);


addcnt.setOnClickListener(new View.OnClickListener() {
	
	       List <String> newList = new ArrayList<String>(0); 	 
			@Override
		   public void onClick(View v) { 
				v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); 
				addcnt.setVisibility(View.INVISIBLE);
				newList.add(displayNumber); 
 				stackmob.getDatastore().putRelated("user",phoneNumber,"friendslist",newList, new StackMobCallback() { 
 					@Override
					public void failure(
							StackMobException arg0) {
						Log.d("ADDCONTACTS::::::", "Updating friends listt failureee"+arg0.getMessage());
						}

					@Override
					public void success(String arg0) {
						Log.d("ADDCONTACTS::::::", "Updating friends succes"+arg0);
						
 
				}
				}); 
 				friendsNumber = displayNumber;
				like.setVisibility(View.VISIBLE);
				checkvotes();
				  }
			    });
	       
home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); 
				 Intent home = new Intent(ListeningFrdVotingAndMediaActivity.this,ListeningAndSharingHomeActivity.class);
				 startActivity(home);
				
			}
		});

profilephoto.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) { 
			v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
			 mediaRetrivalForProfile(phoneNumber, profpic,"friends");
	}
});


bookmarks.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); 
		 Intent book =new Intent(getApplicationContext(),BookmarksActivity.class);
		 book.putExtra("imagelink", profpic);
		 startActivity(book);
	}
});

friends.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); 
		Intent _friends=new Intent(ListeningFrdVotingAndMediaActivity.this,ListeningWorkflowHomeActivity.class);
		_friends.putExtra("phoneNumber", phoneNumber);
		System.gc();
		startActivity(_friends);
		
			}
		});


settings.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent startSettings = new Intent(ListeningFrdVotingAndMediaActivity.this,SettingsAcivity.class);
		startSettings.putExtra("phoneNumber", phoneNumber);	
		System.gc();
		startActivity(startSettings);
	}
});

like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); 
				FriendsModel frduser  =  MHConstants.friendsModelDetails.get(phoneNumber);
				FriendsModel frdreceived = MHConstants.friendsModelDetails.get(friendsNumber);
				if (voted) { 
					isvoted = false;
					like.setImageResource(R.drawable.vote);
					System.out.println("her ein likeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
					voted = false;
					user_given_votes = frduser.getNoOfGivenVotes();
					if(user_given_votes>0)
					{
						user_given_votes = user_given_votes - 1;
				    }
					friends_votes = frdreceived.getNoOfReceivedVotes();
					if(friends_votes>0)
					{ 
					friends_votes = friends_votes - 1;
					} 
				    stackmobcalls();
					
				} else {
					    voted = true;
						isvoted = true; 
						like.setImageResource(R.drawable.unvote);
						System.out.println("user votesssssssssssssss"+ user_given_votes + "friendsssssssssssss"+ friends_votes); 
						
						user_given_votes = frduser.getNoOfGivenVotes();
						user_given_votes = user_given_votes + 1;
						
						friends_votes = frdreceived.getNoOfReceivedVotes();
						friends_votes = friends_votes + 1; 
						
					    stackmobcalls();
					   }
 
				
			}
		});

	}

private void checkvotes() {  
	
	FriendsModel frdmodelfrienduser = MHConstants.friendsModelDetails.get(friendsNumber);
	if(null!=frdmodelfrienduser){
	if(frdmodelfrienduser.getUserReceivedVotingList().size()>0 && null!=frdmodelfrienduser.getUserReceivedVotingList())
	{
	userVoteReceivedList.addAll(frdmodelfrienduser.getUserReceivedVotingList());
	}
	if(givenVotes>=5)
	{
		like.setEnabled(false);
	}
	else
	    {
		   if(userVoteReceivedList.contains(phoneNumber))
		   { 
			like.setImageResource(R.drawable.unvote);
			voted = true;
		   }
		   else
		   {
			 like.setImageResource(R.drawable.vote);
             voted = false;
		   }
	}
	} 	
	}

private void fetchuserobjects() { 
	
		StackMobQuery query = new StackMobQuery("user").fieldIsEqualTo ("username", phoneNumber)  ;
		List<String> fields = new ArrayList<String>();
		fields.add("username");
		fields.add("profile_picture");
		fields.add("profilepic");
		fields.add("no_of_given_votes");
		fields.add("no_of_received_votes");
		fields.add("user_given_voting");
		fields.add("user_received_voting");
		stackmob.getDatastore().get(query, StackMobOptions.selectedFields(fields),new StackMobCallback() { 
			@Override
		public void success(String response) { 
					try { 
					JSONArray array = new JSONArray(response);
					String imageUrl =null;
					String friendPhno = null;
					int noOfGivenVotes = 0;
					int noOfReceivedVotes = 0; 
					JSONArray userGivenVotingArray = new JSONArray(); 
					JSONArray userReceivedVotingArray = new JSONArray();
					List<String> userGivenVotingList = new ArrayList<String>();
					List<String> userReceivedVotingList = new ArrayList<String>();  
	       for (int i = 0; i < array.length(); i++) {
	    	        userReceivedVotingList.clear();
	         	    userGivenVotingList.clear();
					JSONObject obj = array.getJSONObject(i);
					imageUrl = obj.optString("profile_picture");
					if(imageUrl.equals(null)){
						obj.optString("profilepic");
					}
					friendPhno  = obj.optString("username");
					noOfGivenVotes =obj.optInt("no_of_given_votes");
					noOfReceivedVotes =obj.optInt("no_of_received_votes");  
					userGivenVotingArray = obj.optJSONArray("user_given_voting");
					userReceivedVotingArray = obj.optJSONArray("user_received_voting");
					if(null!=userGivenVotingArray){
						for (int userGivencount = 0; userGivencount < userGivenVotingArray.length(); userGivencount++) { 
							userGivenVotingList.add(userGivenVotingArray.get(userGivencount).toString());
						} 
					}
					if(null!=userReceivedVotingArray){
						for (int userReceivedcount = 0; userReceivedcount < userReceivedVotingArray.length(); userReceivedcount++) { 
							
							userReceivedVotingList.add(userReceivedVotingArray.get(userReceivedcount).toString());
							FriendsModel frdmodel = MHConstants.friendsModelDetails.get(friendPhno); 
							frdmodel.setUserReceivedVotingList(userReceivedVotingList);
						} 
					}
					  
					MHConstants.friendsModelDetails.put(friendPhno,new FriendsModel(friendPhno,imageUrl,"false",noOfGivenVotes,noOfReceivedVotes,userGivenVotingList,userReceivedVotingList));
						 } 
			             } catch (JSONException e1) {
						Log.d("ListeningFRDSharingHomeActivity :: ListeningHome ","stackmobLoadListeningScreen :: Success :: JSONException "+e1.getMessage());
						e1.getStackTrace();
						} catch (ParseException e1) {
						Log.d("ListeningFRDSharingHomeActivity :: ListeningHome ","stackmobLoadListeningScreen :: Success :: ParseException "+e1.getMessage());
						e1.printStackTrace();
						}
						
						Log.d("ListeningFRDSharingHomeActivity :: ListeningHome ","stackmobLoadListeningScreen :: Success :: END ");
						}
						
						
						
						@Override
						public void failure(StackMobException e) {
						Log.d("ListeningFRDSharingHomeActivity :: ListeningHome ","stackmobLoadListeningScreen :: Success :: 2 StackMobException "+e.getMessage());
						
						e.getStackTrace();
						}
						});  
	}

	public void stackmobcalls() {
		
		list = new ArrayList<String>(0); 
//update no_of_given_votes for owner
		Map<String, Object> uservotes = new HashMap<String, Object>();
		uservotes.put("no_of_given_votes", user_given_votes);

		stackmob.getDatastore().put("user", phoneNumber, uservotes,
				new StackMobCallback() {
					@Override
					public void success(String responseBody) {
						System.out.println("hee in sucesss votesssssssssssss"+ responseBody);
						FriendsModel frdusergivenvotes = MHConstants.friendsModelDetails.get(phoneNumber);
						frdusergivenvotes.setNoOfGivenVotes(user_given_votes);
					}

					@Override
					public void failure(StackMobException e) {
						System.out.println("hee in failed votesssssssssssss"+ e.getMessage());
					}
				});
		
	 
//update user_given_votinglist for owner	
		FriendsModel frdgetUserGivenList = MHConstants.friendsModelDetails.get(phoneNumber);
		if(null!=frdgetUserGivenList.getUserGivenVotingList())
		{
		list.addAll(frdgetUserGivenList.getUserGivenVotingList());
		}
		if(!isvoted){
		if(list.contains(friendsNumber)){	
		list.remove(friendsNumber);
		deletefromUserGivenList();
		}
		} else{
			list.add(friendsNumber);
			addingtoUserGivenList();
		}
		 
	
//update no_of_received_votes FOR FRIEND
		
		Map<String, Object> friendsvotes = new HashMap<String, Object>();
		friendsvotes.put("no_of_received_votes", friends_votes);

		stackmob.getDatastore().put("user", friendsNumber, friendsvotes,
				new StackMobCallback() {
					@Override
					public void success(String responseBody) {
						System.out.println("hee in sucesss votesssssssssssss"+ responseBody);
						FriendsModel frdreceivedvotes = MHConstants.friendsModelDetails.get(friendsNumber);
						frdreceivedvotes.setNoOfReceivedVotes(friends_votes);
					}
					public void failure(StackMobException e) {
						System.out.println("here in failure of Friends List."+ e.getMessage());
					}
				});
		
		
		
 //update user_received_votinglist for friend
		 votesreceicveList =  new ArrayList<String>(0);
		 FriendsModel frdgetUserReceivedList = MHConstants.friendsModelDetails.get(friendsNumber);
		 votesreceicveList.addAll(frdgetUserReceivedList.getUserReceivedVotingList());
		 
			if(!isvoted){
				if(votesreceicveList.contains(phoneNumber)){
					votesreceicveList.remove(phoneNumber);
					removeFromReceivedList();
				}
			}else{ 
				votesreceicveList.add(phoneNumber);
				addToRecevingList();
			}
			
		
		
	}

	private void addToRecevingList() {
		stackmob.getDatastore().putRelated("user", friendsNumber, "user_received_voting",votesreceicveList,  
				new StackMobCallback() {
					@Override
					public void success(String responseBody) {
						System.out.println("here in successssssss of Friends List"+ responseBody);
						FriendsModel frduserreceivedlist = MHConstants.friendsModelDetails.get(friendsNumber);
						frduserreceivedlist.setUserReceivedVotingList(votesreceicveList);
					}

					@Override
					public void failure(StackMobException e) {
						System.out.println("here in failure of Friends List."+ e.getMessage());
					}
				}); 
		
	}

	private void removeFromReceivedList() {
			stackmob.getDatastore().deleteIdFrom("user", friendsNumber,"user_received_voting",phoneNumber,false,
					new StackMobCallback() {
					@Override
					public void success(String responseBody) {
						System.out.println("here in successssssss of Friends List"+ responseBody);
						FriendsModel frduserreceivedlist = MHConstants.friendsModelDetails.get(friendsNumber);
						frduserreceivedlist.setUserReceivedVotingList(votesreceicveList);
					}

					@Override
					public void failure(StackMobException e) {
						System.out.println("here in failure of Friends List."+ e.getMessage());
					}
				});	
	 	
	}

	private void addingtoUserGivenList() {
		stackmob.getDatastore().putRelated("user", phoneNumber, "user_given_voting",list,  
				new StackMobCallback() {
					@Override
					public void success(String responseBody) {
						System.out.println("here in successssssss of Friends List"+ responseBody);
						FriendsModel frdusergivenuserlist = MHConstants.friendsModelDetails.get(friendsNumber);
						frdusergivenuserlist.setUserGivenVotingList(list);
						}

					@Override
					public void failure(StackMobException e) {
						System.out.println("here in failure of Friends List."+ e.getMessage());
					}
				});
	}

	private void deletefromUserGivenList() {
		stackmob.getDatastore().deleteIdFrom("user", phoneNumber, "user_given_voting", friendsNumber, false,  
				new StackMobCallback() {
					@Override
					public void success(String responseBody) {
						System.out.println("here in successssssss of Friends List"+ responseBody);
						FriendsModel frdusergivenuserlist = MHConstants.friendsModelDetails.get(friendsNumber);
						frdusergivenuserlist.setUserGivenVotingList(list);
					}

					@Override
					public void failure(StackMobException e) {
						System.out.println("here in failure of Friends List."+ e.getMessage());
					}
				});
	}

	class ItemAdapter extends ArrayAdapter<MediaInfo> {

		Context context;

		public ItemAdapter(Context context, int resourceId, List<MediaInfo> rowItems) {

			super(context, resourceId,rowItems);

			this.context = context;

		}

		private class ViewHolder {
			public TextView media_name; 
			public RatingBar rating_bar;
			public ImageView media_type;
			public ImageView ownerPic;
		 
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			MediaInfo rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) context

			.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.media_audio, null);

				holder = new ViewHolder();	

				holder.media_name = (TextView) convertView.findViewById(R.id.names);
				holder.rating_bar = (RatingBar) convertView.findViewById(R.id.ratingbar);
				holder.media_type = (ImageView) convertView.findViewById(R.id.mediaType);
				holder.ownerPic = (ImageView) convertView.findViewById(R.id.profilePicImage);
				//holder.bookmrk = (ImageView) convertView.findViewById(R.id.book);
				convertView.setTag(holder);

			} else 
				holder = (ViewHolder) convertView.getTag();
			
			int likcnt = 0 ;
			int stars = 0;
			 
			if(null!=rowItem && rowItem.getLikesCount()!=0)
			{
			likcnt = rowItem.getLikesCount();
			}
			
			//holder.bookmrk.setVisibility(View.INVISIBLE);
			if(likcnt!= 0 && likcnt <= onestar)
           {
        	   stars = 1;  
           }else if(likcnt > onestar && likcnt <= twostar){
        	   stars = 2;
           }else if(likcnt > twostar && likcnt <= threestar){
        	   stars = 3;
           }else if(likcnt > threestar && likcnt <= fourstar){
        	   stars = 4;
           }else if(likcnt > fourstar){
        	   stars = 5;
           }else if(likcnt == 0){
        	   stars = 0;
           } 
			if(null!=rowItem && "PNG".equals(rowItem.getMediaType())){
				 holder.media_type.setImageResource(R.drawable.image_type);			 
			}else if(null!=rowItem && "3GP".equals(rowItem.getMediaType())){
				holder.media_type.setImageResource(R.drawable.audio_type);
			}
			String imageurl=null;
			if(null!=rowItem && null!= rowItem.getMediaOwnerProfilePic()){
				imageurl = rowItem.getMediaOwnerProfilePic(); 
			}
			holder.media_name.setText(rowItem.getMediaName());
			
			holder.rating_bar.setRating(stars);
			imageLoader.DisplayImage(imageurl,holder.ownerPic);
			return convertView;

		}

	}

	 

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	    	//String[] mediaInfoKeys = mediaidmap.keySet().toArray(new String[mediaidmap.size()]);
	    	if(null!=mediaObjList && mediaObjList.size()>0 ){
		    mediaInfoObj = mediaObjList.get(position); 
	    	}else{
	    		 mediaInfoObj = MHConstants.mediaProfileObjList.get(position); 
	    	} 
	    	MHConstants.selectedmediaobj.clear();
	    	MHConstants.selectedmediaobj.put("mediaInfoObj", mediaInfoObj);
		showDialog(PROGRESSDIALOG_ID);
		 
		
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESSDIALOG_ID:
			removeDialog(PROGRESSDIALOG_ID);
			progressDialog = new ProgressDialog(this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setCancelable(true);

			progressDialog.setOnCancelListener(new OnCancelListener() {

				public void onCancel(DialogInterface dialog) {
					if (listeningAudio != null
							&& listeningAudio.getStatus() != AsyncTask.Status.FINISHED)
						listeningAudio.cancel(true); 
				}

			});
			break;
		default:
			progressDialog = null;
		}
		return progressDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case PROGRESSDIALOG_ID:
			System.out.println("her eiin barrrrrrrrrrrrrrrrrr");
			if (listeningAudio != null
					&& listeningAudio.getStatus() != AsyncTask.Status.FINISHED)
				listeningAudio.cancel(true);
			listeningAudio = new ListeningAudio();
			listeningAudio.execute();
			break;
		}
	}

	class ListeningAudio extends AsyncTask<Void, Integer, String> {

		@Override
		protected String doInBackground(Void... arg0) {
 
			cnt=0; 
			taggedUserList.clear();	 
			stackmobLoadingScreen();
			querytofectchtaggedusers();
		    querytofectchtaggedtopics();
		    querytofectchtaggedprmcontacts();
			return null;
		} 
	} 

	public void stackmobLoadingScreen() {
		List<String> medialikes = new ArrayList<String>();
		medialikes.add("likes_dislikes");
		medialikes.add("media_analytics_id");
		medialikes.add("username");
		StackMobQuery q = new StackMobQuery("media_analytics").fieldIsEqualTo("media_id", mediaInfoObj.getMediaId()).fieldIsEqualTo("username",phoneNumber);
		stackmob.getDatastore().get(q,StackMobOptions.selectedFields(medialikes), new StackMobCallback() { 
            @Override
            public void success(String responseBody) {
            	Log.d("ListeningAudioPlay :: ","Querying :: Media_Analytics :: "+responseBody);
            	if(responseBody.length()>2){
            		try {
						JSONArray jsonArray = new JSONArray(responseBody);
  						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							likestatus = jsonObject.getString("likes_dislikes");
							 Log.v("ListeningAudioPlay::", "like or dislike::"+likestatus);
							   
  						}
  						pharse="true";
  					}catch(Exception e){
            		Log.v("ListeningAudioPlay::", "like or dislike::Exception"+e.getMessage());
            	}
            	}
            	else{
            		Log.v("ListeningAudioPlay::", "No like or dislike::likestatus changed to false" );
            		   likestatus="none"; 
            		   pharse="true"; 
            		 }
            	 
      		 
            }
  			@Override
			public void failure(StackMobException arg0) {
				Log.d("ListeningFRd :: ","Querying :: Media_Analytics ::  "+arg0.getMessage()); 
				
			}
  			
		
		});
	 
	}
 		public void querytofectchtaggedusers()
		{
 		 
		StackMobQuery query = new StackMobQuery("user_tags").fieldIsEqualTo("media_id",mediaInfoObj.getMediaId()).fieldIsEqualTo("username",mediaInfoObj.getMediaownernumber());;
		 
		stackmob.getDatastore().get(query,StackMobOptions.depthOf(2), new StackMobCallback() {
			@Override
			public void success(String response) { 
				Log.v("ListeningFRd::", "Query::Response::User_tags"+ response);
				 if(null!=response && response.length()>2){
					 parsedresult = null;
					 try {
						 JSONArray jsonArray = new JSONArray(response);
					 
			    		if(null!=jsonArray){
			    		for (int i = 0; i < jsonArray.length(); i++) {
			    			String mediaOwnerProfilePic = null; 
			    			JSONObject jsonObject = jsonArray.optJSONObject(i);
			    			parsedresult = jsonObject.optString("tagged_user"); 
						    JSONObject mediaUser =  jsonObject.optJSONObject("usertags_user");
			    			if(null!= mediaUser){  
				    			    mediaOwnerProfilePic = mediaUser.optString("profile_picture"); 
				    			    if(mediaOwnerProfilePic.equals(null)){
				    			    	mediaUser.optString("profilepic");
									}
				    		}
			    			 if(!taggedUserList.contains("user_"+parsedresult)){
			    			taggedUserList.add("user_"+parsedresult);  
			    			 }
			    			if(!MHConstants.friendsModelDetails.containsKey(parsedresult)){
			    				 MHConstants.friendsModelDetails.put(parsedresult,new FriendsModel(parsedresult,mediaOwnerProfilePic,"false"));
  			    			}
			    			cnt++;
							 if(cnt>2 && pharse.equals("true")){
								calls();
								progressDialog.dismiss();
							}
			    		}
					 }
					 }catch(Exception e){
						 Log.v("ListeningFRd::","MediaRetreival :: User_tags :: TaggedUser :: exception :: "+ e.getMessage()); 
					 }
				 }else{
					 cnt++;
					 if(cnt>2 && pharse.equals("true")){
						calls();
						progressDialog.dismiss();
					}
				 }
			}
			 			 

			@Override
			public void failure(StackMobException e) {
				Log.v("ListeningFRd::","MediaRetreival :: User_tags :: TaggedUser :: failure :: "+ e.getMessage());
				cnt++;
				 if(cnt>2 && pharse.equals("true")){
					calls();
					progressDialog.dismiss();
				}
			}
		});
		}
		 
		public void querytofectchtaggedtopics()
		{
		fields = new ArrayList<String>();
		StackMobQuery query = new StackMobQuery("topic_tags").fieldIsEqualTo("media_id",mediaInfoObj.getMediaId()).fieldIsEqualTo("username",mediaInfoObj.getMediaownernumber());
		fields.add("tagged_topic");
		stackmob.getDatastore().get(query,StackMobOptions.selectedFields(fields), new StackMobCallback() {
			@Override
			public void success(String response) {
				Log.v("ListeningFRD::", "Query::Response::topic_tags"+ response);
				if(response.length()>2){
				 try {

					String parsedresult = null;
					JSONArray jsonArray = new JSONArray(response);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						parsedresult = jsonObject.getString("tagged_topic");
						Log.v("ListeningFRd::", "parsed result::topic_tags"+ parsedresult);
						if(!taggedUserList.contains("topic_"+parsedresult)){
						taggedUserList.add("topic_"+parsedresult);
						}
						cnt++;
						if(cnt>2 && pharse.equals("true")){
							calls();
							progressDialog.dismiss();
						}
					}

					Log.v("ListeningFRd::","MediaRetreival::TaggedUser::for table::topic_tags"+ taggedUserList);
					 
				} catch (Exception e) {
					cnt++;
					 if(cnt>2 && pharse.equals("true") ){
						calls();
						progressDialog.dismiss();
					}
					Log.v("ListeningFRd::","MediaRetreival::TaggedUser::catch::topic_tags"+ e.getMessage());
				}
				}else{
					cnt++;
					 if(cnt>2 && pharse.equals("true") ){
						calls();
						progressDialog.dismiss();
					}
				}
			      }
	       public void failure(StackMobException e) {
					Log.v("ListeningFRd::","MediaRetreival ::topic_tags :: TaggedUser :: failure :: "+ e.getMessage());
					cnt++;
					 if(cnt>2 && pharse.equals("true")){
						calls();
						progressDialog.dismiss();
					}
				}
			});
			
			}
			public void querytofectchtaggedprmcontacts()
			{
				 fields = new ArrayList<String>();
			StackMobQuery query = new StackMobQuery("promoted_contacts_tags").fieldIsEqualTo("media_id",mediaInfoObj.getMediaId()).fieldIsEqualTo("username",mediaInfoObj.getMediaownernumber());
			fields.add("tagged_prm_contact");
			stackmob.getDatastore().get(query,StackMobOptions.selectedFields(fields), new StackMobCallback() {
				@Override
				public void success(String response) {
					Log.v("ListeningFRd::", "Query::Response::promoted_contacts_tags"+ response);
					if(response.length()>2){
					try {

						  String parsedresult = null;
						  JSONArray jsonArray = new JSONArray(response);
						    for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							parsedresult = jsonObject.getString("tagged_prm_contact");
							Log.v("ListeningFRd::", "parsed result::promoted_contacts_tags"+ parsedresult);
							if(!taggedUserList.contains("prmcontact_"+parsedresult)){
							taggedUserList.add("prmcontact_"+parsedresult); 
							}
							cnt++;
							 if(cnt>2 && pharse.equals("true")  ){
								calls();
								progressDialog.dismiss();
							}
						}
						    
						    Log.v("ListeningFRd::","MediaRetreival::TaggedUser::for table::promoted_contacts_tags"+ taggedUserList);
						 
					} catch (Exception e) {
						Log.v("ListeningFRd::","MediaRetreival::TaggedUser::catch::promoted_contacts_tags"+ e.getMessage());
						cnt++;
						 if(cnt>2 && pharse.equals("true")  ){
							calls();
							progressDialog.dismiss();
						}
						 
					}
					}
					else{
						cnt++;
						 if(cnt>2 && pharse.equals("true")){
							calls();
							progressDialog.dismiss();
						}
					}
				}

		    	@Override
			 public void failure(StackMobException e) {
				Log.v("ListeningFRd::","MediaRetreival :: promoted_contacts_tags :: TaggedUser :: failure :: "+ e.getMessage());
				cnt++;
				 if(cnt>2 && pharse.equals("true")){
				
					calls();
					progressDialog.dismiss();
				}
			}
		});
		}

		
	

	public void calls()
	{
		 Log.v("ListeningAudioPlay::","TaggedUserList::After Query"+ taggedUserList); 
		 Log.d("ListeningFrdVoting:: ","likestatus ::  in intent ::"+likestatus);
		 Intent audioplay = new Intent(ListeningFrdVotingAndMediaActivity.this,ListeningAudioPlay.class);
		 audioplay.putStringArrayListExtra("taggedusers", (ArrayList<String>)taggedUserList);
		 audioplay.putExtra("mediaidshashmap",(ArrayList<MediaInfo>)mediaObjList); 
		 audioplay.putExtra("phoneNumber",phoneNumber); 
		 audioplay.putExtra("likestatus",likestatus); 
		 audioplay.putExtra("mediauserurl",mediauserurl); 
		 audioplay.putExtra("imageurls", (HashMap<String,String>)friendsprofilepiclist);
		 audioplay.putExtra("friendsNumber", friendsNumber);
		 audioplay.putExtra("isvoted", isvoted);
		 audioplay.putExtra("user_given_votes", user_given_votes);
		 audioplay.putExtra("friends_votes", friends_votes); 
		 audioplay.putExtra("pic_url", pic_url);
		 audioplay.putExtra("displayNumber", friendsNumber);
		 audioplay.putExtra("flag",flag);
		 audioplay.putExtra("media_id",mediaInfoObj.getMediaId());
		 audioplay.putExtra("friendsss",(ArrayList<String>)friends_list);
		 audioplay.putExtra("topics",(ArrayList<String>)topics_list);
		 audioplay.putExtra("topicsIDS",(ArrayList<String>)topicsIds);
		 startActivity(audioplay);
 	}
	 
	 @Override
		public void onBackPressed() {
			if(null!=from && from.equals("HOME")){
				Intent listen =  new Intent(ListeningFrdVotingAndMediaActivity.this,ListeningWorkflowHomeActivity.class);
				listen.putExtra(phoneNumber, "phoneNumber");
				imageLoader.clearCache(); 
				startActivity(listen);
			}else{
				imageLoader.clearCache(); 
				finish();
			}
		    }
}

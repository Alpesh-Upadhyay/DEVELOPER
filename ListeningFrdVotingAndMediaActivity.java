/* DEVELOPER NAME: ALPESH UPADHYAY CONTACT: +91-9555859884
*/

package com.mobileharvest.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileharvest.ImageLoadingFramework.ImageLoader;
import com.mobileharvest.activity.BookmarksActivity.ItemAdapter;
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
public  class ListeningFrdVotingAndMediaActivity extends BaseProfile implements OnItemClickListener {
	// Button btnLoadMore;
	int  media_id_size=0;
	int test_flag=0;
	int start=10;
	int click=1;
	int end=0;
	int size=10;
	int start_value=10;
	int click_value=1;
	int end_value=0;
	int pagination_Size=10;
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
	List<String> media_ids  ;
	List<String>media_idss;
	List<String> topics_list ;
	List<String> topicsIds  ;
	List<String> keysList  ;
    String mediaUsername;
    String flag, usern,newflag;
	String pic_url;
	String displayNumber;
	String  number = null; 
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
	 //ItemAdapter adapter;
	 
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.voting);
		
		final ImageButton b1=(ImageButton)findViewById(R.id.button1);
		final ImageButton b2=(ImageButton)findViewById(R.id.button2);
		b1.setVisibility(View.INVISIBLE);
		b2.setVisibility(View.INVISIBLE);
		imageLoader=new ImageLoader(getApplicationContext());
		StackMobAndroid.init(this.getApplicationContext(),MHConstants.DEV_PROD_VERSION,MHConstants.PUBLIC_KEY);
		stackmob = StackMob.getStackMob();
		flag = getIntent().getStringExtra("flag");
		from = getIntent().getStringExtra("from");
		newflag = getIntent().getStringExtra("newflag");

		   final ImageButton back_button=(ImageButton)findViewById(R.id.back_button);
		final ImageButton next_button=(ImageButton)findViewById(R.id.next_button);
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
		  media_ids=new ArrayList<String>();
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
		  final List<String> mylist = new ArrayList<String>();
		  final List<String> audioList = new ArrayList<String>();

		if(null!=newflag && newflag.equals("user")){
		  like.setVisibility(View.VISIBLE); 
		}else{
			like.setVisibility(View.GONE);
		}
		Intent intent = getIntent();
		int new_value = intent.getIntExtra("new_value", 0); 
	System.out.println("alpesh"+new_value);
		
		media_idss = (ArrayList<String>) getIntent().getSerializableExtra("media_id");
	System.out.println("the media ids_contains"+media_idss.size());
	 media_ids = new ArrayList(new LinkedHashSet(media_idss)); //no order
	 System.out.println("the media id after removing duplicates is:"+media_ids.size());
	 System.out.println("the media id is"+media_idss);
	 System.out.println("the media id is"+media_ids);
	 media_id_size=media_ids.size()-10;
		System.out.println("the media_ids contains value after deduct 10 :"+media_id_size);
		
		if(media_ids.isEmpty())
		{
			b1.setVisibility(View.INVISIBLE);
			next_button.setVisibility(View.INVISIBLE);
		}
		if(media_id_size<=0 )
		{
			b1.setVisibility(View.INVISIBLE);
			next_button.setVisibility(View.INVISIBLE);
		}
		//if using the below line the media under self profile is getting crashed.
		System.out.println("hi media_id recieved on listeningfrndvoting page is :"+media_ids);
		phoneNumber = intent.getStringExtra("phoneNumber");
		pic_url = intent.getStringExtra("pic_url");
		displayNumber = intent.getStringExtra("displayNumber"); 
		friends_list = (ArrayList<String>) getIntent().getSerializableExtra("friendsss");
		topics_list = (ArrayList<String>) getIntent().getSerializableExtra("topics");
		topicsIds  =(ArrayList<String>) getIntent().getSerializableExtra("topicsIDS");
		friendsprofilepiclist = (Map<String,String>)getIntent().getSerializableExtra("imageurls");
		mediaObjList.clear();
		mediaObjList = (ArrayList<MediaInfo>) getIntent().getSerializableExtra("mediaidshashmap");
	//	System.out.println("testing the mediaOnjList coming from home is"+mediaObjList);
		
		//System.out.println("testing 7"+mediaObjList);
		friendsNumber = intent.getStringExtra("friendsNumber"); 
		isvoted = intent.getBooleanExtra("isvot&&&&&&&&&&&&&&&&&&&&&&ed", isvoted);
		System.out.println("" + isvoted); 
		
		// FOr the Pagination of Self Media Queue
		if (new_value==1)
		{
			next_button.setVisibility(View.INVISIBLE);
			if(start_value==10)
			{
				b2.setVisibility(View.INVISIBLE);
			}
			System.out.println("alpesh its in condition of if on listeningFrdVoting");
			b1.setVisibility(View.VISIBLE);
			b1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) { 
						v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
						progressDialog = new ProgressDialog(ListeningFrdVotingAndMediaActivity.this);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setCancelable(true);
						progressDialog.show();
						//Toast.makeText(getApplicationContext(), "Onclick works", Toast.LENGTH_SHORT).show();
						
						media_id_size=media_id_size-10;
						if (end_value>=media_ids.size())
						{
							
							b1.setVisibility(View.INVISIBLE);
							progressDialog.dismiss();
							return;
							}
						start_value=pagination_Size*click_value;
						end_value=(start_value+pagination_Size)-1;
						click_value++;
						System.out.println("the start_value and end value is"+start_value+","+end_value);
				System.out.println("paging:: the phone no,profile pic and Friends contains::"+ phoneNumber+"," +profpic+","+"friends");
				System.out.println("paging the value of media_ids is"+media_ids);
				mediaObjList = new ArrayList<MediaInfo>();
				if (null != media_ids && media_ids.size() > 0) { 
					List<String> mediaFields = new ArrayList<String>();
					mediaFields.add("media_name");
					mediaFields.add("audio");
					mediaFields.add("media_id");
					mediaFields.add("media_type");
					mediaFields.add("media_analytics");	
				
					StackMobQuery mediaInfoRetreival = new StackMobQuery("media").isInRange(start_value,end_value).fieldIsIn("media_id", media_ids).fieldIsOrderedBy("createddate",
									StackMobQuery.Ordering.DESCENDING); 
					stackmob.getDatastore().get(mediaInfoRetreival,StackMobOptions.depthOf(2),new StackMobCallback() {
				
					    @Override
					    public void failure(StackMobException arg0) {

					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::failure"+ arg0.getMessage());
					    }

					    @Override
					    public void success(String arg0) {
					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::success"+ arg0);
					    	try {    		
					    		System.out.println("paging success");
					    		MHConstants.mediaProfileObjList.clear();
					    		mediaObjList.clear();
					    		mediaidmap = new HashMap<String,MediaInfo>(0);
					    		mediaidmap.clear();
					    		 
					    		JSONArray jsonArray = new JSONArray(arg0);
					    		int test=0;
					    		test=jsonArray.length();
					    		System.out.println("paging  the lengh is"+test);
					    		if(null!=jsonArray){
					    		for (int i = 0; i < jsonArray.length(); i++) {
					    			
					    			System.out.println("pagging here in for loop");
					    			int likesCount = 0;
					    			String mediaOwnerProfilePic = null;
					    			JSONObject jsonObject = jsonArray.optJSONObject(i);
					    			String mediaId = jsonObject.optString("media_id");
					    			String mediaNam = jsonObject.optString("media_name");
					    			
					    			String audioUrl = jsonObject.optString("audio");
					    			String mediaName = jsonObject.optString("media_name");
					    			String mediaType = jsonObject.optString("media_type");
					    			System.out.println("the media name is"+mediaNam);
					    			System.out.println("the media type is"+mediaType);
					    			JSONArray mediaAnalytics =  jsonObject.optJSONArray("media_analytics");
					    		//	System.out.println("the mediaAnalytics size is"+mediaAnalytics.length());
//					    			
					    			if(null!= mediaAnalytics){
						    			for(int mu=0;mu<mediaAnalytics.length();mu++){
					    				JSONObject userObj = mediaAnalytics.optJSONObject(mu);
						    			    if("like".equals(userObj.optString("likes_dislikes"))){
						    			    	likesCount+=1;
						    			    }						    			}
					    			}
					    			JSONObject mediaUser =  jsonObject.optJSONObject("media_user");
					    			if(null!= mediaUser){  
						    			    mediaOwnerProfilePic = mediaUser.optString("profile_picture"); 
						    			    System.out.println("the profile pic of media user is"+mediaOwnerProfilePic);
						    		}
					    			
					    			
					    			MediaInfo mediaInfo = new MediaInfo();
					    			mediaInfo.setMediaId(mediaId);
					    			mediaInfo.setAudioURL(audioUrl);
					    			mediaInfo.setMediaName(mediaName);
					    			mediaInfo.setMediaType(mediaType);
					    			mediaInfo.setLikesCount(likesCount);
					    			mediaInfo.setMediaOwnerProfilePic(mediaOwnerProfilePic);
					    			
					    			MHConstants.mediaProfileObjList.add(mediaInfo);
						    		mediaObjList.add(mediaInfo);
					    			mediaidmap.put(mediaId,mediaInfo);
					    			 
						    		}
					    		
					    		Thread thread = new Thread() 
					    		{
					    		  @Override
					    		  public void run() 
					    		  {
					    		    try
					    		    {
					    		      synchronized(this) 
					    		      {
					    		  runOnUiThread(new Runnable() 
					    		  { 
					    		           @Override
					    		           public void run() 
					    		           {   
					    		        	   ItemAdapter adapter = new ItemAdapter(getApplicationContext(),R.layout.media_audio,rowItems); 
					    		        	   adapter.clear();
					    		        	   adapter.notifyDataSetChanged();
					    		        	   
					    		        	   
					    		            SetToAdapter();
					    		            ((ItemAdapter) listView.getAdapter()).notifyDataSetChanged();
					    		            if(media_id_size<=0)
					    		            {
					    		            	b1.setVisibility(View.INVISIBLE);
					    		            	
					    		            }
					    		           
					    		           b2.setVisibility(View.VISIBLE); 
					    		            progressDialog.dismiss();
					    		           }
					    		       });                  
					    		     }
					    		  }
					    		  catch(Exception e)
					    		  {
					    		     e.printStackTrace();
					    		  }         
					    		 };
					    		};
					    		thread.start();  
					    		
					    		}  
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::mediaIds hash map"+ mediaidmap);
					    		  
					    	}
					    	
					    	
					    	
					    	
					    	catch (Exception e) {
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::catchException"+ e.getMessage());
					    	}
					   
				    }			    					
					});

				}
				}			 
				
			});
			
			
// for the back Button click
			b2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) { 
					 b1.setVisibility(View.VISIBLE);
						v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
						progressDialog = new ProgressDialog(ListeningFrdVotingAndMediaActivity.this);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setCancelable(true);
						progressDialog.show();
						//Toast.makeText(getApplicationContext(), "Onclick works", Toast.LENGTH_SHORT).show();
					
						start_value=start_value-10;
						end_value=end_value-10;
						click_value--;
						if(start_value==0)
						{
						b2.setVisibility(View.INVISIBLE);	
						}
						System.out.println("the start_value and end value is"+start_value+","+end_value);					
				System.out.println("paging:: the phone no,profile pic and Friends contains::"+ phoneNumber+"," +profpic+","+"friends");
				System.out.println("paging the value of media_ids is"+media_ids);
				mediaObjList.clear();
				mediaObjList = new ArrayList<MediaInfo>();
				if (null != media_ids && media_ids.size() > 0) { 
					List<String> mediaFields = new ArrayList<String>();
					mediaFields.add("media_name");
					mediaFields.add("audio");
					mediaFields.add("media_id");
					mediaFields.add("media_type");
					mediaFields.add("media_analytics");	
					
					
					StackMobQuery mediaInfoRetreival = new StackMobQuery("media").isInRange(start_value,end_value).fieldIsIn("media_id", media_ids).fieldIsOrderedBy("createddate",
									StackMobQuery.Ordering.DESCENDING);
				 
					stackmob.getDatastore().get(mediaInfoRetreival,StackMobOptions.depthOf(2),new StackMobCallback() {
					    
					    @Override
					    public void failure(StackMobException arg0) {

					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::failure"+ arg0.getMessage());
					    }

					    @Override
					    public void success(String arg0) {
					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::success"+ arg0);
					    	try {
					    		
					    		System.out.println("paging success");
					    		MHConstants.mediaProfileObjList.clear();
					    		mediaObjList.clear();
					    		mediaidmap = new HashMap<String,MediaInfo>(0);
					    		mediaidmap.clear();
					    		 
					    		JSONArray jsonArray = new JSONArray(arg0);
					    		int test=0;
					    		test=jsonArray.length();
					    		System.out.println("paging  the lengh is"+test);
					    		if(null!=jsonArray){
					    		for (int i = 0; i < jsonArray.length(); i++) {
					    		
					    			System.out.println("pagging here in for loop");
					    			int likesCount = 0;
					    			String mediaOwnerProfilePic = null;
					    			JSONObject jsonObject = jsonArray.optJSONObject(i);
					    			String mediaId = jsonObject.optString("media_id");
					    			String mediaNam = jsonObject.optString("media_name");
					    			
					    			String audioUrl = jsonObject.optString("audio");
					    			String mediaName = jsonObject.optString("media_name");
					    			String mediaType = jsonObject.optString("media_type");
					    			System.out.println("the media name is"+mediaNam);
					    			System.out.println("the media type is"+mediaType);
					    			JSONArray mediaAnalytics =  jsonObject.optJSONArray("media_analytics");
					    		//	System.out.println("the mediaAnalytics size is"+mediaAnalytics.length());
//					    			
					    			if(null!= mediaAnalytics){
						    			for(int mu=0;mu<mediaAnalytics.length();mu++){
					    				JSONObject userObj = mediaAnalytics.optJSONObject(mu);
						    			    if("like".equals(userObj.optString("likes_dislikes"))){
						    			    	likesCount+=1;
						    			    }						    			}
					    			}
					    			JSONObject mediaUser =  jsonObject.optJSONObject("media_user");
					    			if(null!= mediaUser){  
						    			    mediaOwnerProfilePic = mediaUser.optString("profile_picture"); 
						    			    System.out.println("the profile pic of media user is"+mediaOwnerProfilePic);
						    		}
					    			
					    			
					    			MediaInfo mediaInfo = new MediaInfo();
					    			mediaInfo.setMediaId(mediaId);
					    			mediaInfo.setAudioURL(audioUrl);
					    			mediaInfo.setMediaName(mediaName);
					    			mediaInfo.setMediaType(mediaType);
					    			mediaInfo.setLikesCount(likesCount);
					    			mediaInfo.setMediaOwnerProfilePic(mediaOwnerProfilePic);	
					    			MHConstants.mediaProfileObjList.add(mediaInfo);
						    		mediaObjList.add(mediaInfo);
					    			mediaidmap.put(mediaId,mediaInfo);
					    			 
						    		}
					    		
					    		Thread thread = new Thread() 
					    		{
					    		  @Override
					    		  public void run() 
					    		  {
					    		    try
					    		    {
					    		      synchronized(this) 
					    		      {
					    		  runOnUiThread(new Runnable() 
					    		  { 
					    		           @Override
					    		           public void run() 
					    		           {   
					    		        	   ItemAdapter adapter = new ItemAdapter(getApplicationContext(),R.layout.media_audio,rowItems); 
					    		        	   adapter.clear();
					    		        	   adapter.notifyDataSetChanged();
					    		        	   
					    		        	   
					    		            SetToAdapter();
					    		            ((ItemAdapter) listView.getAdapter()).notifyDataSetChanged();
					    		           
					    		            
					    		            progressDialog.dismiss();
					    		           }
					    		       });                  
					    		     }
					    		  }
					    		  catch(Exception e)
					    		  {
					    		     e.printStackTrace();
					    		  }         
					    		 };
					    		};
					    		thread.start();  
					    		}  
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::mediaIds hash map"+ mediaidmap);
					    		  
					    	}
					    	
					    	catch (Exception e) {
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::catchException"+ e.getMessage());
					    	}
					    	
					    	
				    }			    					
					});

				}
				}			 
				
			});	
		}		
		
		
		 
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
	
	  back_button.setVisibility(View.INVISIBLE);
// pagination logic for back button for Listening WorkFlow
	back_button.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
			next_button.setVisibility(View.VISIBLE);
			//for deleting the mediaObjList Information on button click
			mediaObjList.clear();
			//System.out.println("mediadata mediaObjList just after clickcontains"+mediaObjList);
			progressDialog = new ProgressDialog(ListeningFrdVotingAndMediaActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
			start=start-10;
			end=end-10;
			click--;
			
			
			 if(start==0)
			 {
				 back_button.setVisibility(View.INVISIBLE);
				 
			 }
			System.out.println("hi  the start and end on back button is"+start+","+end);
		//	Toast.makeText(getApplicationContext(), "Onclick works", Toast.LENGTH_SHORT).show();
					StackMobQuery mediaInfoRetreival = new StackMobQuery("media").isInRange(start,end).fieldIsIn("media_id", media_ids).fieldIsOrderedBy("createddate",
							StackMobQuery.Ordering.DESCENDING);
				 
					stackmob.getDatastore().get(mediaInfoRetreival,StackMobOptions.depthOf(2),new StackMobCallback() {
					    
					    @Override
					    public void failure(StackMobException arg0) {

					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::failure"+ arg0.getMessage());
					    }

					    @Override
					    public void success(String arg0) {
					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::success"+ arg0);
					    	
					    	try {
					    		mediaObjList = new ArrayList<MediaInfo>();
					    		mediaidmap = new HashMap<String,MediaInfo>(0);
					    		mediaidmap.clear();
					    		 
					    		JSONArray jsonArray = new JSONArray(arg0);
					    		if(null!=jsonArray){
					    		for (int i = 0; i < jsonArray.length(); i++) {
					    			int likesCount = 0;
					    			String mediaOwnerProfilePic = null;
					    			JSONObject jsonObject = jsonArray.optJSONObject(i);
					    			String mediaId = jsonObject.optString("media_id");
					    		//	System.out.println("testing after click the media id is"+ mediaId);
					    			
					    			String mediaNam = jsonObject.optString("media_name");
					    			
					    			String audioUrl = jsonObject.optString("audio");
					    			String mediaName = jsonObject.optString("media_name");
					    			String mediaType = jsonObject.optString("media_type");
					    			JSONArray mediaAnalytics =  jsonObject.optJSONArray("media_analytics");
					    			//System.out.println("testing the media_name contains"+ mediaNam);
					    			
					    			if(null!= mediaAnalytics){
						    			for(int mu=0;mu<mediaAnalytics.length();mu++){
						    				JSONObject userObj = mediaAnalytics.optJSONObject(mu);
						    			    if("like".equals(userObj.optString("likes_dislikes"))){
						    			    	likesCount+=1;
						    			    }
						    			}
					    			}
					    			JSONObject mediaUser =  jsonObject.optJSONObject("media_user");
					    			if(null!= mediaUser){  
						    			    mediaOwnerProfilePic = mediaUser.optString("profile_picture"); 
						    		}
					    			
					    	
					    			MediaInfo mediaInfo = new MediaInfo();
					    			mediaInfo.setMediaId(mediaId);
					    			System.out.println("testing the get mediaid after click"+mediaInfo.getMediaId());
					    			mediaInfo.setAudioURL(audioUrl);
					    			mediaInfo.setMediaName(mediaName);
					    			mediaInfo.setMediaType(mediaType);
					    			mediaInfo.setLikesCount(likesCount);
					    			mediaInfo.setMediaOwnerProfilePic(mediaOwnerProfilePic);
						    			if(null!= number && number.equals(phoneNumber))
						    			 {
						    				MHConstants.mediaProfileObjList.add(mediaInfo);
						    			 } 
						   			
					    			mediaObjList.add(mediaInfo);
					    			mediaidmap.put(mediaId,mediaInfo);
					    			mylist.add(mediaNam);
					    			audioList.add(audioUrl); 
					    			
						    		}
					    		
					    	
					    		
					    		}
					    		
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::mediaNames"+ mylist);
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::audiourls"+ audioList);
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::mediaIds hash map"+ mediaidmap);
					  // listView.invalidateViews();
					    		Thread thread = new Thread() 
					    		{
					    		  @Override
					    		  public void run() 
					    		  {
					    		    try
					    		    {
					    		      synchronized(this) 
					    		      {
					    		  runOnUiThread(new Runnable() 
					    		  { 
					    		           @Override
					    		           public void run() 
					    		           {   
					    		        	   ItemAdapter adapter = new ItemAdapter(getApplicationContext(),R.layout.media_audio,rowItems); 
					    		        	   adapter.clear();
					    		        	   adapter.notifyDataSetChanged();
					    		        	   
					    		            SetToAdapter();
					    		           
					    		            
					    		            progressDialog.dismiss();
					    		           }
					    		       });                  
					    		     }
					    		  }
					    		  catch(Exception e)
					    		  {
					    		     e.printStackTrace();
					    		  }         
					    		 };
					    		};
					    		thread.start();   
					    
					    	}
					    
					    	catch (Exception e) {
					    		System.out.println("testing error"+e.getMessage());
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::catchException"+ e.getMessage());
					    	}
				    }	
					   
					});
					
					//testing();
					
				}
		
		
		
	});
	
	
	
	
	
	next_button.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
		  
			b2.setVisibility(View.INVISIBLE);
			v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); 
			//for deleting the mediaObjList Information on butto click
			mediaObjList.clear();
			System.gc();
			//System.out.println("mediadata mediaObjList just after clickcontains"+mediaObjList);
			progressDialog = new ProgressDialog(ListeningFrdVotingAndMediaActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			//progressDialog.setCancelable(true);
			progressDialog.show();
	
			System.out.println("the media id has"+media_id_size+"value");
			media_id_size=media_id_size-10;
			System.out.println("the media_id after click contains"+media_id_size);

			start=size*click;
			end=(start+size)-1;
			click++;
			
			
			//start=start+1;
			
//			if (end>=media_ids.size())
//			{
//				progressDialog.dismiss();
//				next_button.setVisibility(View.INVISIBLE);
//				
//				return;				
//				}
		
							    
				    System.out.println("hi the start and end on next_button is"+start+","+end);     
			//	    System.out.println("hi the media ids is"+media_ids);
					StackMobQuery mediaInfoRetreival = new StackMobQuery("media").isInRange(start,end).fieldIsIn("media_id", media_ids).fieldIsOrderedBy("createddate",
							StackMobQuery.Ordering.DESCENDING);
				 
					stackmob.getDatastore().get(mediaInfoRetreival,StackMobOptions.depthOf(2),new StackMobCallback() {
					    
					    @Override
					    public void failure(StackMobException arg0) {
					    	
					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::failure"+ arg0.getMessage());
					    	
					    }

					    @Override
					    public void success(String arg0) {
					    	Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::success"+ arg0);
					    	
					    	try {
					    		mediaObjList = new ArrayList<MediaInfo>();
					    		mediaidmap = new HashMap<String,MediaInfo>(0);
					    		mediaidmap.clear();
					    		 
					    		JSONArray jsonArray = new JSONArray(arg0);
					    		if(null!=jsonArray){
					    		for (int i = 0; i < jsonArray.length(); i++) {
					    			int likesCount = 0;
					    			String mediaOwnerProfilePic = null;
					    			JSONObject jsonObject = jsonArray.optJSONObject(i);
					    			String mediaId = jsonObject.optString("media_id");
					    			String mediaNam = jsonObject.optString("media_name");
					    			String audioUrl = jsonObject.optString("audio");
					    			String mediaName = jsonObject.optString("media_name");
					    			String mediaType = jsonObject.optString("media_type");
					    			JSONArray mediaAnalytics =  jsonObject.optJSONArray("media_analytics");
					    			System.out.println("testing the media_name contains"+ mediaNam);
					    			
					    			if(null!= mediaAnalytics){
						    			for(int mu=0;mu<mediaAnalytics.length();mu++){
						    				JSONObject userObj = mediaAnalytics.optJSONObject(mu);
						    			    if("like".equals(userObj.optString("likes_dislikes"))){
						    			    	likesCount+=1;
						    			    }
						    			}
					    			}
					    			JSONObject mediaUser =  jsonObject.optJSONObject("media_user");
					    			if(null!= mediaUser){  
						    			    mediaOwnerProfilePic = mediaUser.optString("profile_picture"); 
						    		}
					    			
					    	
					    			MediaInfo mediaInfo = new MediaInfo();
					    			mediaInfo.setMediaId(mediaId);
					    			mediaInfo.setAudioURL(audioUrl);
					    			mediaInfo.setMediaName(mediaName);
					    			mediaInfo.setMediaType(mediaType);
					    			mediaInfo.setLikesCount(likesCount);
					    			mediaInfo.setMediaOwnerProfilePic(mediaOwnerProfilePic);
						    			if(null!= number && number.equals(phoneNumber))
						    			 {
						    				MHConstants.mediaProfileObjList.add(mediaInfo);
						    			 } 
						   			
					    			mediaObjList.add(mediaInfo);
					    			mediaidmap.put(mediaId,mediaInfo);
					    			mylist.add(mediaNam);
					    			audioList.add(audioUrl); 
					    			
						    		}
					    		
					    		}
					    		
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::mediaNames"+ mylist);
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::audiourls"+ audioList);
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::mediaIds hash map"+ mediaidmap);
					  // listView.invalidateViews();
					    		Thread thread = new Thread() 
					    		{
					    		  @Override
					    		  public void run() 
					    		  {
					    		    try
					    		    {
					    		      synchronized(this) 
					    		      {
					    		  runOnUiThread(new Runnable() 
					    		  { 
					    		           @Override
					    		           public void run() 
					    		           {   
					    		        	   ItemAdapter adapter = new ItemAdapter(getApplicationContext(),R.layout.media_audio,rowItems); 
					    		        	   adapter.clear();
					    		        	   adapter.notifyDataSetChanged();
					    		        	   
					    		            SetToAdapter();
					    		            back_button.setVisibility(View.VISIBLE);
					    		            progressDialog.dismiss();
					    		            if (media_id_size<=0)
					    					{
					    						next_button.setVisibility(View.INVISIBLE);
					    					}
					    		           }
					    		       });                  
					    		     }
					    		  }
					    		  catch(Exception e)
					    		  {
					    		     e.printStackTrace();
					    		  }         
					    		 };
					    		};
					    		thread.start();  
					    		System.gc();
					    		//progressDialog.dismiss();
					
					    		
					    	
					    		//progressDialog.dismiss();
					    		//listView.invalidateViews();
					    		
//					    		if(column.equals("PrmContacts")){
//					    			prmcalls();
//					    		}else{
//					    		calls();
//					    		}
					    	}
					    	
					    
					    	catch (Exception e) {
					    		System.out.println("testing error"+e.getMessage());
					    		Log.v("ListeningWorkflowHome::","MediaRetreival::mediaList::mediaInfoRetreival::catchException"+ e.getMessage());
					    	
					    	}
				    }	
					   
					});
				
		}
		
	});
		
		AdminPreferences adminpref = MHConstants.adminPreferences.get("ADMIN_PREF");
	     onestar = adminpref.getStar1();
	     twostar = adminpref.getStar2();
	     threestar = adminpref.getStar3();
	     fourstar = adminpref.getStar4();
	     fivestar = adminpref.getStar5();
		 listView = (ListView) findViewById(android.R.id.list);
//			for loading dynamic button 
//			ImageView btnLoadMore = new ImageView(this);
//			btnLoadMore.setImageResource(R.drawable.back);
//			// Adding button to listview at footer
//			listView.addFooterView(btnLoadMore);
		
		 listView.invalidate();  	
			SetToAdapter();
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
//			Intent start_SelfProfileActivity= new Intent(ListeningFrdVotingAndMediaActivity.this,SelfProfileActivity.class);
//			startActivity(start_SelfProfileActivity);
//		 
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

ImageView logout=(ImageView)findViewById(R.id.logout_button);

logout.setOnClickListener(new View.OnClickListener() {

	@Override
	public void onClick(View v) {
		v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            
            Toast.makeText(getBaseContext(), "clicked", Toast.LENGTH_SHORT).show();
           Intent i=new Intent(getApplicationContext(),LogoutActivity.class);
          // ent.putExtra("true","shared");
           
           
           startActivity(i);
         
			
        
      
		
	}


});


	}



public void SetToAdapter() {
	//listView.invalidateViews();
    if(null!= mediaObjList && mediaObjList.size()>0 && !flag.equals("profile")){
	for (int i = 0; i < mediaObjList.size(); i++)
	{
	      MediaInfo mediaObj = mediaObjList.get(i);
	    //  System.out.println("testing the mediaObj contains "+mediaObj);
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
//ItemAdapter adapter=new ItemAdapter(this);
listView.setAdapter(adapter);

}

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

		
		
		
		public void notifyDataSetChanged() // Create this function in your adapter class
		{
		    super.notifyDataSetChanged();
		}	
		Context context;
		public ItemAdapter(Context context, int resourceId, List<MediaInfo> rowItems) {
			
			super(context, resourceId,rowItems);
			//((BaseAdapter)((ListView) list).getAdapter()).notifyDataSetChanged(); 
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
 		 
		StackMobQuery query = new StackMobQuery("user_tags").fieldIsEqualTo("media_id",mediaInfoObj.getMediaId());
		 
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
				    		}
			    			 
			    			taggedUserList.add("user_"+parsedresult);  
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
		StackMobQuery query = new StackMobQuery("topic_tags").fieldIsEqualTo("media_id",mediaInfoObj.getMediaId());
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
						taggedUserList.add("topic_"+parsedresult);
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
			StackMobQuery query = new StackMobQuery("promoted_contacts_tags").fieldIsEqualTo("media_id",mediaInfoObj.getMediaId());
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
							taggedUserList.add("prmcontact_"+parsedresult); 
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

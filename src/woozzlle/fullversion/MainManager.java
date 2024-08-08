package woozzlle.fullversion;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.ImageView.ScaleType;

public class MainManager extends BroadcastReceiver {
	
	public MainScreenActivity mActivity;
	
	public SharedPreferences mSettings;
	
	public ArrayList<String> mPuzzleIndex;
	public ArrayList<String> mPuzzleIndexDisabled;
	
	public ArrayList<Integer> mPuzzleScrollSize;
	
	public static String PUZZLE_NUMBER_EXTRA = "puzzle_number";		
	
	public ArrayList<PointF> mLanguagePointLocations = null;
	
	public ArrayList<String> mLanguageNames = null;
	
	private int mCurrentLanguage;
	
	private ArrayList<Activity> activityCounter;
	
	public int getmCurrentLanguage() {
		return mCurrentLanguage;
	}

	public void setmCurrentLanguage(int mCurrentLanguage) {
		this.mCurrentLanguage = mCurrentLanguage;
	}

	public boolean ismSoundsOn() {
		return mSoundsOn;
	}

	public void setmSoundsOn(boolean mSoundsOn) {
		this.mSoundsOn = mSoundsOn;
	}

	public boolean ismMusicOn() {
		return mMusicOn;
	}

	public void setmMusicOn(boolean mMusicOn) {				
		if (this.mMusicOn && !mMusicOn)
		{
			MainManager.getInstance().stopMusic();
		}
		else if (!this.mMusicOn && mMusicOn)
		{
			MainManager.getInstance().playMusic();
		}
		this.mMusicOn = mMusicOn;
	}

	private void stopMusic() {
		if(musicPlayer != null)
		{
			musicPlayer.release();
			musicPlayer = null;
		}
	}

	private boolean mSoundsOn;
	
	private boolean mMusicOn;
	
	private MediaPlayer musicPlayer;
	
	private MediaPlayer soundsPlayer;
	
	public static final String SETTINGS_FILE = "settings.file";
	
	public static final String SETTINGS_MUSIC = "music";	
	public static final String SETTINGS_SOUND = "sound";	
	public static final String SETTINGS_LANGUAGE = "language";	
		
	private MainManager(MainScreenActivity activity)
	{
		mActivity = activity;
		mPuzzleIndex = new ArrayList<String>();
		
		mPuzzleIndexDisabled = new ArrayList<String>();
		
		activityCounter = new ArrayList<Activity>();
		
		mPuzzleScrollSize = new ArrayList<Integer>();
	}	
	
	private static MainManager mInstance = null;
	public static MainManager createInstance(MainScreenActivity activity)
	{
		if(mInstance == null)
			mInstance = new MainManager(activity);
		
		return mInstance;
	}
	
	public static void destroyInstance()
	{
		if (mInstance != null)
		{
			mInstance.stopMusic();
			mInstance = null;
		}
	}
	
	public static MainManager getInstance()
	{
		return mInstance;
	}
	
	public void loadSettings()
	{	
		boolean settingsLoaded = false;
		
		SharedPreferences prefs = mActivity.getSharedPreferences(SETTINGS_FILE, 0);
		
		mLanguageNames = new ArrayList<String>();
		mLanguagePointLocations = new ArrayList<PointF>();
		
		mLanguagePointLocations.add(new PointF(273.25f, 622.00f)); mLanguageNames.add("English".toLowerCase());
		mLanguagePointLocations.add(new PointF(272.75f, 694.00f)); mLanguageNames.add("French".toLowerCase());
		mLanguagePointLocations.add(new PointF(272.50f, 766.00f)); mLanguageNames.add("German".toLowerCase());
		mLanguagePointLocations.add(new PointF(270.75f, 838.00f)); mLanguageNames.add("Chinese".toLowerCase());
		mLanguagePointLocations.add(new PointF(269.75f, 910.75f)); mLanguageNames.add("Russian".toLowerCase());
		
		// Arabic will use English
		mLanguagePointLocations.add(new PointF(647.50f, 621.75f)); mLanguageNames.add("Arabic".toLowerCase());
		// Japanese will use English
		mLanguagePointLocations.add(new PointF(646.50f, 694.00f)); mLanguageNames.add("English".toLowerCase());
		mLanguagePointLocations.add(new PointF(647.25f, 766.00f)); mLanguageNames.add("Italian".toLowerCase());
		mLanguagePointLocations.add(new PointF(644.50f, 837.50f)); mLanguageNames.add("Dutch".toLowerCase());
		mLanguagePointLocations.add(new PointF(643.25f, 910.75f)); mLanguageNames.add("Spanish".toLowerCase());

		
		if(prefs != null
				&& prefs.contains(SETTINGS_LANGUAGE))
		{
			mCurrentLanguage = prefs.getInt(SETTINGS_LANGUAGE, 0);
			mMusicOn = prefs.getBoolean(SETTINGS_MUSIC, true);
			mSoundsOn = prefs.getBoolean(SETTINGS_SOUND, true);
			settingsLoaded = true;
			
			int i = 0;
			while(true) {
				
				if(prefs.contains("puzzle"+i))
				{
					mPuzzleIndex.add(prefs.getString("puzzle" +i, ""));
				}
				else
				{
					break;
				}
				++i;
			}
			
			i = 0;
			while(true) {
				
				if(prefs.contains("puzzle_disabled"+i))
				{
					mPuzzleIndexDisabled.add(prefs.getString("puzzle_disabled" +i, ""));
				}
				else
				{
					break;
				}
				++i;
			}						
		}
		
		if (!settingsLoaded)
		{
			mCurrentLanguage = 0;
			mMusicOn = true;
			mSoundsOn = true;
						
			mPuzzleIndex.add("98");
			mPuzzleIndex.add("100");
			mPuzzleIndex.add("44");
						
			mPuzzleIndex.add("23");
			mPuzzleIndex.add("28");
			mPuzzleIndex.add("29");
			mPuzzleIndex.add("49");
			mPuzzleIndex.add("54");
			mPuzzleIndex.add("64");
			mPuzzleIndex.add("65");
			mPuzzleIndex.add("66");
			mPuzzleIndex.add("67");
			mPuzzleIndex.add("69");
			mPuzzleIndex.add("70");
			mPuzzleIndex.add("76");
			mPuzzleIndex.add("78");
			mPuzzleIndex.add("79");
			mPuzzleIndex.add("84");
			mPuzzleIndex.add("88");
			mPuzzleIndex.add("89");
			mPuzzleIndex.add("95");
			mPuzzleIndex.add("96");
			mPuzzleIndex.add("97");
			mPuzzleIndex.add("99");
			
			saveSettings(true);
		}
		
		for (int j = 0; j < mPuzzleIndex.size(); j++) {
			mPuzzleScrollSize.add(new Integer(-1));
		}
		
		if (mMusicOn)
		{
			playMusic();
		}
	}
	
	public void saveSettings(boolean savePuzzles)
	{
		SharedPreferences prefs = mActivity.getSharedPreferences(SETTINGS_FILE, 0);
		
		if(prefs != null)
		{
			SharedPreferences.Editor editor = prefs.edit();
			
			editor.putBoolean(SETTINGS_MUSIC, mMusicOn);
			editor.putBoolean(SETTINGS_SOUND, mSoundsOn);
			editor.putInt(SETTINGS_LANGUAGE, mCurrentLanguage);
			
			for (int i = 0; i < mPuzzleIndex.size(); i++) {					
				editor.putString("puzzle"+i, mPuzzleIndex.get(i));
			}
			
			for (int i = 0; i < mPuzzleIndexDisabled.size(); i++) {					
				editor.putString("puzzle_disabled"+i, mPuzzleIndexDisabled.get(i));
			}
			
			editor.commit();
		}
	}
	
	public Bitmap getBitmapForButtonDisabled(String puzzle)
	{
		Bitmap output = null;
		
		InputStream imageStream;
		try {
			imageStream = mActivity.getAssets().open("main_buttons/main_item"+ puzzle +"_ribbon.png");
			output = BitmapFactory.decodeStream(imageStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
		
		return output;
	}		
	
	public Bitmap getBitmapForButton(String puzzle)
	{
		Bitmap output = null;
		
		InputStream imageStream;
		try {
			imageStream = mActivity.getAssets().open("main_buttons/item"+ puzzle +".png");
			output = BitmapFactory.decodeStream(imageStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
		
		return output;
	}
		
	public void playSound(int resId)
	{
		if (soundsPlayer !=  null)
		{
			soundsPlayer.release();
			soundsPlayer = null;
		}
		
		soundsPlayer = MediaPlayer.create(mActivity, resId);
		soundsPlayer.start();
	}
	
	public void playMusic()
	{
		if(musicPlayer != null)
		{
			musicPlayer.release();
			musicPlayer = null;
		}
		musicPlayer = MediaPlayer.create(mActivity, R.raw.gamemusic);
		musicPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				MainManager.this.musicPlayer.start();				
			}
		});
		musicPlayer.start();
	}
	
	public void puzzleFinished()
	{
		if (!mSoundsOn)
			return;				
		
		Random r = new Random();		
		
		String sound = "hurray";
		
		switch(r.nextInt(5)){
		
		case 0:
			sound = "hurray";
			break;
		
		case 1:
			sound = "excellent";
			break;
		
		case 2:
			sound = "welldone";
			break;
		
		case 3:
			sound = "youareagenius";
			break;
		case 4:
			sound = "youaredoinggreat";
			break;
		}
		
		int rid = mActivity.getResources().getIdentifier(mLanguageNames.get(mCurrentLanguage) + "_"+ sound, "raw", mActivity.getPackageName());
		
		/*
		 switch(r.nextInt(5)){
		
		case 0:
			if(mCurrentLanguage == 0) soundId =  R.raw.sound1;
			if(mCurrentLanguage == 1) soundId =  R.raw.hooraysp;
			if(mCurrentLanguage == 2) soundId =  R.raw.chinesehorray;
			break;
		
		case 1:
			if(mCurrentLanguage == 0) soundId =  R.raw.sound2;
			if(mCurrentLanguage == 1) soundId =  R.raw.excellentsp;
			if(mCurrentLanguage == 2) soundId =  R.raw.chineseexcellent;
			break;
		
		case 2:
			if(mCurrentLanguage == 0) soundId =  R.raw.sound3;
			if(mCurrentLanguage == 0) soundId =  R.raw.welldonesp;
			if(mCurrentLanguage == 0) soundId =  R.raw.chineseurdoinggeat;
			break;
		
		case 3:
			if(mCurrentLanguage == 0) soundId =  R.raw.sound4;
			if(mCurrentLanguage == 1) soundId =  R.raw.youareageniussp;
			if(mCurrentLanguage == 2) soundId =  R.raw.chineseurgenious;
			break;
		case 4:
			if(mCurrentLanguage == 0) soundId =  R.raw.sound5;
			if(mCurrentLanguage == 1) soundId =  R.raw.youaredoinggreatsp;
			if(mCurrentLanguage == 2) soundId =  R.raw.chinesewelldone;
			break;
		}
		  
		 */
		
		if (rid > 0)
			playSound(rid);
	}

	public int getPuzzleIndex(String puzzleNumber) {
		for (int i = 0; i < mPuzzleIndex.size(); i++) {
			if(mPuzzleIndex.get(i).equalsIgnoreCase(puzzleNumber))
			{
				return i;
			}
		}
		return 0;
	}
	
	public int getPuzzleNumber(int puzzleIndex) {		
		return Integer.parseInt(mPuzzleIndex.get(puzzleIndex));
	}
	
	public int getPrevPuzzleIndex(int index)
	{
		int output = (index - 1 + mPuzzleIndex.size()) % mPuzzleIndex.size();
		
		return output;
	}
	
	public int getNextPuzzleIndex(int index)
	{
		int output = (index + 1) % mPuzzleIndex.size();
		
		return output;
	}
	
	public void activityOpened(Activity activity)
	{
		if(activityCounter.size() == 0
				&& mMusicOn && musicPlayer == null)
		{		
			playMusic();
		}
		
		if(!activityCounter.contains(activity))
			activityCounter.add(activity);
	}
	
	public void activityClosed(Activity activity)
	{
		if(activityCounter.contains(activity))
			activityCounter.remove(activity);
		
		if(activityCounter.size() == 0)
		{		
			stopMusic();
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_SCREEN_OFF.equalsIgnoreCase(intent.getAction())) {
            activityCounter.clear();
            stopMusic();
        }	
	}
}

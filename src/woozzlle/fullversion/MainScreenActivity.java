package woozzlle.fullversion;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Matrix.ScaleToFit;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class MainScreenActivity extends Activity {
	
	public MainManager mManager;
	
	public HashMap<ImageView, String> mPuzzleMap;
	
	public Point mScreenResolution;
	
	private Point mBaseScreenResolution = new Point(340, 480);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_screen);
	        
		mManager = MainManager.createInstance(this);		
		mManager.loadSettings();
		//mManager.activityOpened();
		
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(MainManager.getInstance(), filter);
		
		mPuzzleMap = new HashMap<ImageView, String>();
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mScreenResolution = new Point(dm.widthPixels, dm.heightPixels);
	        
		loadMenu();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		MainManager.getInstance().activityOpened(this);
	}




	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MainManager.getInstance().activityClosed(this);
	}

	public Point translateCoordinates(Point loc)
	{
		Point output = new Point();
		
		output.x = (int) ((mScreenResolution.x * 1.0f / mBaseScreenResolution.x) * loc.x);
		output.y = (int) ((mScreenResolution.y * 1.0f / mBaseScreenResolution.y) * loc.y);
		
		return output;
	}
	
	public Point translateCoordinates(PointF loc)
	{
		Point output = new Point();
		
		output.x = (int) ((mScreenResolution.x * 1.0f / mBaseScreenResolution.x) * loc.x);
		output.y = (int) ((mScreenResolution.y * 1.0f / mBaseScreenResolution.y) * loc.y);
		
		return output;
	}


	@Override
	protected void onDestroy() {		
    	MainManager.destroyInstance();
    	
		super.onDestroy();
	}
	 
	public void loadMenu()
	{
		Point sizeAppName = translateCoordinates(new Point(210, 50));
		Point locAppName = translateCoordinates(new Point(0, 3));
		ImageView appName = (ImageView) findViewById(R.id.app_name);
		
		appName.setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(sizeAppName.x, sizeAppName.y);
		params.setMargins(0, locAppName.y, 0, locAppName.y);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		appName.setLayoutParams(params);
		
		
		Point sizeSettingIcon = translateCoordinates(new Point(40, 29));
		Point locSettingIcon = translateCoordinates(new Point(13, 13));
		ImageView settingIcon = (ImageView) findViewById(R.id.settingsIcon);
		
		settingIcon.setScaleType(ScaleType.FIT_XY);
		params = new RelativeLayout.LayoutParams(sizeSettingIcon.x, (int)((sizeSettingIcon.x * 29.0f) / 40));
		params.setMargins(locSettingIcon.x, locSettingIcon.y , 0, 0);
		settingIcon.setLayoutParams(params);
		
		
		
		LinearLayout layoutView = (LinearLayout) findViewById(R.id.main_screen_layout);
		
		ArrayList<String> puzzleIndex= mManager.mPuzzleIndex;
		
		int count = puzzleIndex.size();
		String filename = "";
		RelativeLayout relativeLayout = null;
		ImageView imageView = null;
		LinearLayout internalLayout = null;
		
		int column = 0;
		for (int i=0; i<count; ++i)
		{
			String puzzle = puzzleIndex.get(i);
			column = i % 3;
			if(column ==0 )
			{
				internalLayout = new LinearLayout(this);
				internalLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				internalLayout.setOrientation(LinearLayout.HORIZONTAL);
			
				relativeLayout = new RelativeLayout(this);
				relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
				internalLayout.addView(relativeLayout);
				layoutView.addView(internalLayout);
			}
			
			imageView = new ImageView(this);
			int w = LayoutParams.WRAP_CONTENT;
			//if (mScreenResolution.x > 320)
			{
				w = mScreenResolution.x / 4;
				imageView.setScaleType(ScaleType.FIT_XY);
			}
			
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, w);
			int align = i % 3;			
			switch (align)
			{
				case 0:
				{
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				}
				break;
				
				case 1:
				{
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
				}
				break;
				
				case 2:
				{
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				}
				break;
			}
											
			imageView.setImageBitmap(mManager.getBitmapForButton(puzzle));			
			imageView.setLayoutParams(layoutParams);
			
			final String index = puzzle;
			
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {					
					String puzzleNumber = index;
					
					Intent intent = new Intent(MainScreenActivity.this, WoozleActivity.class);												
					
					intent.putExtra(MainManager.PUZZLE_NUMBER_EXTRA, puzzleNumber);
										
					MainScreenActivity.this.startActivity(intent);													
				}
			});
			
			relativeLayout.addView(imageView);
			
			ImageView settingsIcon = (ImageView) findViewById(R.id.settingsIcon);			
			
			settingsIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);																				
										
					MainScreenActivity.this.startActivity(intent);					
				}
			});	
		}
		
		// 	locked items
		
		ArrayList<String> puzzleIndexDisabled= mManager.mPuzzleIndexDisabled;
		
		count = puzzleIndexDisabled.size();				
		
		ImageView lockImage = null;
		
		column = 0;
		for (int i=0; i<count; ++i)
		{
			String puzzle = puzzleIndexDisabled.get(i);
			column = i % 3;
			if(column ==0 )
			{
				internalLayout = new LinearLayout(this);
				internalLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				internalLayout.setOrientation(LinearLayout.HORIZONTAL);
			
				relativeLayout = new RelativeLayout(this);
				relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
				internalLayout.addView(relativeLayout);
				layoutView.addView(internalLayout);
			}
						
			imageView = new ImageView(this);
			int w = LayoutParams.WRAP_CONTENT;
			//if (mScreenResolution.x > 320)
			{
				w = mScreenResolution.x / 4;
				imageView.setScaleType(ScaleType.FIT_XY);
			}
			
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, w);
			int align = i % 3;			
			switch (align)
			{
				case 0:
				{
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				}
				break;
				
				case 1:
				{
					layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
				}
				break;
				
				case 2:
				{
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				}
				break;
			}

			imageView.setImageBitmap(mManager.getBitmapForButtonDisabled(puzzle));
			imageView.setAlpha(130);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(layoutParams);			
			
			/*lockImage = new ImageView(this);
			lockImage.setScaleType(ScaleType.FIT_XY);
			
			RelativeLayout.LayoutParams lockImageLayoutParams = new RelativeLayout.LayoutParams(w / 3, w / 3);
			lockImageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lockImageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lockImage.setLayoutParams(lockImageLayoutParams);
			lockImage.setImageResource(R.drawable.lock);
			lockImage.setAlpha(130);
			
			relativeImageLayout.addView(lockImage);
			
			final String index = puzzle;*/
			
			/*imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {					
					String puzzleNumber = index;
					
					Intent intent = new Intent(MainScreenActivity.this, WoozleActivity.class);												
					
					intent.putExtra(MainManager.PUZZLE_NUMBER_EXTRA, puzzleNumber);
					
					MainScreenActivity.this.startActivity(intent);													
				}
			});*/
			
			relativeLayout.addView(imageView);							
		}
	}
}

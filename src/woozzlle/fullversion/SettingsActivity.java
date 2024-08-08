package woozzlle.fullversion;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class SettingsActivity extends Activity {	
	
	private Point mScreenSize;
	
	private PointF rewardSoundLoc = new PointF(599.50f, 403.50f);
	private PointF musicLoc = new PointF(518.00f, 288.75f);		
	
	private PointF backLoc = new PointF(2.50f, 38.33f);
	
	//private PointF backLoc = new PointF(25.67f, 150.33f);
	private PointF backSize = new PointF(85.00f, 63.33f);
	
	ArrayList<Boolean> mLanguagesChecked = null;
	
	ArrayList<ImageView> mLanguagesViews = null;
	
	private Point mBaseScreenResolution = new Point(768, 1024);
	
	private Point mTickSize = new Point(98, 86);
	
	private Point mBoxSize = new Point(47, 47);
	
	private Point mCurrenScreenResolution = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_final2);
        
        DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mCurrenScreenResolution = new Point(dm.widthPixels, dm.heightPixels);
		
		
		initialize();
		
		
		
		
		// loading views
		
		AbsoluteLayout absoluteLayout = (AbsoluteLayout) findViewById(R.id.main_layout);
		
		Bitmap bitmap = null;
        
        InputStream imageStream;
		imageStream = getResources().openRawResource(R.drawable.box);
		bitmap = BitmapFactory.decodeStream(imageStream);
		
		//Point size = translateCoordinates(new Point(bitmap.getWidth(), bitmap.getHeight()));
		Point size = translateCoordinates(mBoxSize);
		
		Point loc = translateCoordinates(rewardSoundLoc);
			
		ImageView imageView = new ImageView(this);
		if (MainManager.getInstance().ismSoundsOn())
		{
			imageView.setImageResource(R.drawable.check_selected);	
		}
		else
		{
			imageView.setImageResource(R.drawable.check_normal);
		}
		
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setLayoutParams(new AbsoluteLayout.LayoutParams(size.x, size.y, loc.x, loc.y));
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageView imageView = (ImageView) v;
				if (MainManager.getInstance().ismSoundsOn())
				{
					imageView.setImageResource(R.drawable.check_normal);	
					MainManager.getInstance().setmSoundsOn(false);
				}
				else
				{
					imageView.setImageResource(R.drawable.check_selected);
					MainManager.getInstance().setmSoundsOn(true);
				}
			}
		});
    
        absoluteLayout.addView(imageView);
                        
        imageStream = getResources().openRawResource(R.drawable.box);
		bitmap = BitmapFactory.decodeStream(imageStream);
		
		//Point size = translateCoordinates(new Point(bitmap.getWidth(), bitmap.getHeight()));
		size = translateCoordinates(mBoxSize);
		
		loc = translateCoordinates(musicLoc);
			
		imageView = new ImageView(this);
		if (MainManager.getInstance().ismMusicOn())
		{
			imageView.setImageResource(R.drawable.check_selected);	
		}
		else
		{
			imageView.setImageResource(R.drawable.check_normal);
		}
		
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setLayoutParams(new AbsoluteLayout.LayoutParams(size.x, size.y, loc.x, loc.y));
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageView imageView = (ImageView) v;
				if (MainManager.getInstance().ismMusicOn())
				{
					imageView.setImageResource(R.drawable.check_normal);	
					MainManager.getInstance().setmMusicOn(false);
				}
				else
				{
					imageView.setImageResource(R.drawable.check_selected);
					MainManager.getInstance().setmMusicOn(true);
				}
			}
		});
    
        absoluteLayout.addView(imageView);
        
        
        imageStream = getResources().openRawResource(R.drawable.box);
		bitmap = BitmapFactory.decodeStream(imageStream);
		
		//Point size = translateCoordinates(new Point(bitmap.getWidth(), bitmap.getHeight()));
		size = translateCoordinates(backSize);
		
		loc = translateCoordinates(backLoc);
			
		imageView = new ImageView(this);		
		imageView.setImageResource(R.drawable.check_normal);		
		
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setLayoutParams(new AbsoluteLayout.LayoutParams(size.x, size.y, loc.x, loc.y));
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettingsActivity.this.finish();
			}
		});
    
        absoluteLayout.addView(imageView);
        
        
        for (int i = 0; i < MainManager.getInstance().mLanguagePointLocations.size(); i++) {
        	
        	PointF point = MainManager.getInstance().mLanguagePointLocations.get(i);
        
	        imageStream = getResources().openRawResource(R.drawable.box);
			bitmap = BitmapFactory.decodeStream(imageStream);
						
			size = translateCoordinates(mBoxSize);
			
			loc = translateCoordinates(point);				
			
			imageView = new ImageView(this);
			if(mLanguagesChecked.get(i).booleanValue())
			{
				imageView.setImageResource(R.drawable.check_selected);
			}
			else
			{
				imageView.setImageResource(R.drawable.check_normal);
			}
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new AbsoluteLayout.LayoutParams(size.x, size.y, loc.x, loc.y));
			
			mLanguagesViews.add(imageView);
			
			final int languageNumber = i;
			
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {															
					Boolean checked = mLanguagesChecked.get(languageNumber);
					if(!checked)					
					{
						for (int i = 0; i < mLanguagesChecked.size(); i++) {						
							if(mLanguagesChecked.get(i))					
							{
								mLanguagesViews.get(i).setImageResource(R.drawable.check_normal);
								mLanguagesChecked.set(i, new Boolean(false));
							}
						}
						
						mLanguagesViews.get(languageNumber).setImageResource(R.drawable.check_selected);
						mLanguagesChecked.set(languageNumber, new Boolean(true));
						MainManager.getInstance().setmCurrentLanguage(languageNumber);
					}
				}
			});
	    
	        absoluteLayout.addView(imageView);
        }
    }
	
	private void initialize() {		
		mLanguagesChecked = new ArrayList<Boolean>();
		
		mLanguagesViews = new ArrayList<ImageView>();
		
		
		for (int i = 0; i < MainManager.getInstance().mLanguagePointLocations.size(); i++) {
			if(i == MainManager.getInstance().getmCurrentLanguage())
			{
				mLanguagesChecked.add(new Boolean(true));
			}
			else
			{
				mLanguagesChecked.add(new Boolean(false));	
			}			
		}
		
	}		

	public Point translateCoordinates(Point loc)
	{
		Point output = new Point();
		
		output.x = (int) ((mCurrenScreenResolution.x * 1.0f / mBaseScreenResolution.x) * loc.x);
		output.y = (int) ((mCurrenScreenResolution.y * 1.0f / mBaseScreenResolution.y) * loc.y);
		
		return output;
	}
	
	public Point translateCoordinates(PointF loc)
	{
		Point output = new Point();
		
		output.x = (int) ((mCurrenScreenResolution.x * 1.0f / mBaseScreenResolution.x) * loc.x);
		output.y = (int) ((mCurrenScreenResolution.y * 1.0f / mBaseScreenResolution.y) * loc.y);
		
		return output;
	}
	
	/*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_final);
        
        DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mScreenSize = new Point(dm.widthPixels, dm.heightPixels);
        
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        
        RelativeLayout.LayoutParams relativeParams = null;
        LinearLayout.LayoutParams linearParams = null;
        RelativeLayout relativeLayout = null;
        RelativeLayout relativeLayout2 = null;
        LinearLayout linearLayout = null;
        
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));                                
        
        ImageView imageView = new ImageView(this);
        ImageView checkbox = null;
        //imageView.setImageResource(R.drawable.settingstitle);
        
        Bitmap bitmap = null;
        
        InputStream imageStream;
		imageStream = getResources().openRawResource(R.drawable.settingstitle);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        int w = mScreenSize.x / 2;
        int h = (w * bitmap.getHeight()) / bitmap.getWidth();  
        
        relativeParams = new RelativeLayout.LayoutParams(w, h);
        relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeParams.setMargins(0, mScreenSize.y / 96, 0, 0);
        
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(relativeParams);
        imageView.setScaleType(ScaleType.FIT_XY);
        
        relativeLayout.addView(imageView);
        mainLayout.addView(relativeLayout);
        
        
        
        
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));                                
        
        imageView = new ImageView(this);
        //imageView.setImageResource(R.drawable.settingstitle);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.arrow);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 8;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();  
        
        relativeParams = new RelativeLayout.LayoutParams(w, h);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeParams.setMargins(mScreenSize.x / 64, 0, 0, 0);
        
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(relativeParams);
        imageView.setScaleType(ScaleType.FIT_XY);
        
        relativeLayout.addView(imageView);
        mainLayout.addView(relativeLayout);
        
        
        
        // music checkbox                
                
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeParams.setMargins(0, mScreenSize.y / 20 , 0, mScreenSize.y / 48);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);        
        mainLayout.addView(relativeLayout);
        
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.musicnote);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 9;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.music);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 4;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(mScreenSize.x / 40, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        
        
        checkbox = getCheckBox("music");                        
        
        linearLayout.addView(checkbox);
        
        // music checkbox end
        
        
        // reward sound checkbox                
                
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);        
        relativeParams.setMargins(0, mScreenSize.y / 48 , 0, mScreenSize.y / 48);
        
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(relativeParams);
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeParams.setMargins(0, mScreenSize.y / 48 , 0, mScreenSize.y / 48);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);        
        mainLayout.addView(relativeLayout);
        
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.speaker);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 9;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.rewardsounds);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = (int) (mScreenSize.x / 1.6);
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(mScreenSize.x / 40, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        
        
        checkbox = getCheckBox("sounds");                        
        
        linearLayout.addView(checkbox);
        
        // sounds checkbox end
        
        // language heading
        
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));                                
        
        imageView = new ImageView(this);        
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.language);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 2;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();  
        
        relativeParams = new RelativeLayout.LayoutParams(w, h);
        relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeParams.setMargins(0, mScreenSize.y / 20, 0, mScreenSize.y / 20);
        
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(relativeParams);
        imageView.setScaleType(ScaleType.FIT_XY);
        
        relativeLayout.addView(imageView);
        mainLayout.addView(relativeLayout);
        
        
        // languages english/arabic
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);        
        relativeParams.setMargins(0, mScreenSize.x / 20, 0, 0);
        
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeParams.setMargins(mScreenSize.x / 20, 0, 0, 0);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);
        mainLayout.addView(relativeLayout);
        
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.english);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 4;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                		       
        checkbox = getCheckBox("english");                        
        
        linearLayout.addView(checkbox);
        
        
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeParams.setMargins(0, 0, mScreenSize.x / 20, 0);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.arabic);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 4;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                		       
        checkbox = getCheckBox("arabic");                        
        
        linearLayout.addView(checkbox);

        
        
        // languages french/japanese
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);        
        relativeParams.setMargins(0, mScreenSize.x / 20, 0, 0);
        
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeParams.setMargins(mScreenSize.x / 20, 0, 0, 0);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);
        mainLayout.addView(relativeLayout);
        
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.french);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 4;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                		       
        checkbox = getCheckBox("french");                        
        
        linearLayout.addView(checkbox);
        
        
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeParams.setMargins(0, 0, mScreenSize.x / 20, 0);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.japanese);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 4;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                		       
        checkbox = getCheckBox("japanese");                        
        
        linearLayout.addView(checkbox);

        // languages german/italian
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);        
        relativeParams.setMargins(0, mScreenSize.x / 20, 0, 0);
        
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeParams.setMargins(mScreenSize.x / 20, 0, 0, 0);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);
        mainLayout.addView(relativeLayout);
        
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.german);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 4;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                		       
        checkbox = getCheckBox("german");                        
        
        linearLayout.addView(checkbox);
        
        
        
        relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeParams.setMargins(0, 0, mScreenSize.x / 20, 0);
        
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(relativeParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        relativeLayout.addView(linearLayout);
        
        imageView = new ImageView(this);
        
        bitmap = null;
                
		imageStream = getResources().openRawResource(R.drawable.italian);
		bitmap = BitmapFactory.decodeStream(imageStream);		
        
        w = mScreenSize.x / 4;
        h = (w * bitmap.getHeight()) / bitmap.getWidth();
        
        imageView.setImageBitmap(bitmap);
        linearParams = new LinearLayout.LayoutParams(w, h);
        linearParams.setMargins(0, 0, 0, 0);
        imageView.setLayoutParams(linearParams);
        imageView.setScaleType(ScaleType.FIT_XY);        
        
        linearLayout.addView(imageView);
        
        imageView = new ImageView(this);       
        
        bitmap = null;
                		       
        checkbox = getCheckBox("italian");                        
        
        linearLayout.addView(checkbox);

        
        
       /* Spinner languageSelection = (Spinner) findViewById(R.id.spinner1);
        Size.y / 96
        languageSelection.setSelection(MainManager.getInstance().getmCurrentLanguage());
        languageSelection.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MainManager.getInstance().setmCurrentLanguage(arg2);		
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
				arg0.setSelection(MainManager.getInstance().getmCurrentLanguage());
			}        	        	
		}); 
        
       CheckBox musicCheckBox = (CheckBox) findViewById(R.id.ch_soundenable);
       CheckBox soundCheckBox = (CheckBox) findViewById(R.id.ch_speechenable);
       
       musicCheckBox.setChecked(MainManager.getInstance().ismMusicOn());
       soundCheckBox.setChecked(MainManager.getInstance().ismSoundsOn());
              
       musicCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			MainManager.getInstance().setmMusicOn(isChecked);
			
		}
	});
       
       soundCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
   		
   		@Override
   		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
   			MainManager.getInstance().setmSoundsOn(isChecked);   			
   		}
   	});
        
    }*/
	
	public ImageView getCheckBox(String checkBoxName)
	{
		ImageView checkbox = new ImageView(this);
		checkbox.setImageResource(R.drawable.box);
		
		int w = mScreenSize.x / 15;
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, w);
		layoutParams.setMargins(mScreenSize.x / 20, 0, 0, 0);
		
		checkbox.setLayoutParams(layoutParams);
		
		final String checkBoxNameFinal = checkBoxName;
		
		checkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ImageView checkBox = (ImageView) arg0;
				checkBox.setImageResource(R.drawable.box);
				checkBoxClicked(checkBoxNameFinal);
			}
		});
		
		return checkbox;
	}
	
	public void checkBoxClicked(String checkBoxName)
	{
		Log.v("checkboxchecked", checkBoxName);
	}

	@Override
	public void onBackPressed() { 				
		saveSettings();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		saveSettings();
		MainManager.getInstance().activityClosed(this);
		super.onDestroy();
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
	
	private void saveSettings()
	{
		MainManager.getInstance().saveSettings(false);
	}
}

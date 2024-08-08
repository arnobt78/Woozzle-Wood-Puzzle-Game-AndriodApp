package woozzlle.fullversion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class WoozleActivity extends Activity {
    /** Called when the activity is first created. */		
	
	public String fullImage;
	public String backImage;
	
	public ArrayList<String> piecesFileName;
	public ArrayList<PointF> piecesLocation;
	public ArrayList<ImageView> imageViews;
	public ArrayList<ImageView> imageViewsRetained;
	
	public ArrayList<PointF> piecesFinalLocation;
	public ArrayList<PointF> piecesFinalDimensions;
	
	public PointF fullImageLocation;
	
	public PointF screenResolution;
	
	public PointF baseScreenSize = new PointF(640, 960);
	
	public String useDeviceString = "iphone";
	
	public boolean useiPad = true;
	
	public boolean maintainAspectRation = false;
	
	public int currentPiece = 0;
	
	public static float IPHONE_ASPECT_RATIO = 320/480.0f;
	
	public static float IPAD_ASPECT_RATIO = 768/1024.0f;
	
	public static float MIDDLE_ASPECT_RATIO = IPHONE_ASPECT_RATIO + (IPAD_ASPECT_RATIO - IPHONE_ASPECT_RATIO) / 2.0f;
	
	public static final int PUZZLE_STATE_NOT_STARTED = 0;
	public static final int PUZZLE_STATE_STARTED = 1;
	public static final int PUZZLE_STATE_FINISHED = 2;
	public static final int PUZZLE_STATE_ERROR = 3;
	public static final int PUZZLE_STATE_NOT_FINISHED = 3;
		
	public int puzzleState;	
	
	public int maximumScrollSize = -1;
	
	public AbsoluteLayout mAbsoluteLayout;
	
	public LinearLayout mPiecesView;
	
	public CustomScrollView mScrollView;
	
	public Point mLastTouch;
	
	public int mPiecesCount = 0;
	
	public int mPlacedPieceCount = 0;
	
	public ImageView mLeftButton = null;
	
	public ImageView mRightButton = null;
	
	public int mPuzzleIndex = -1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.puzzle_layout);
        
        puzzleState = PUZZLE_STATE_NOT_STARTED;
        
        
        AbsoluteLayout layout =  (AbsoluteLayout) findViewById(R.id.absolute_layout);
        
        mAbsoluteLayout = layout;
        mLastTouch = new Point(-1, -1);
        
        String puzzleNumber = getIntent().getExtras().getString(MainManager.PUZZLE_NUMBER_EXTRA);
        
        mPuzzleIndex = MainManager.getInstance().getPuzzleIndex(puzzleNumber);
        
        initialize();
        
        startPuzzle(Integer.parseInt(puzzleNumber) , layout);
        
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
			
			@Override
			public void run() {
				breakPuzzle();			
			}
		});        
    }
    
    public void initialize()
    {
    	ImageView settingsIcon = (ImageView) findViewById(R.id.settingsIcon);
		ImageView homeIcon = (ImageView) findViewById(R.id.homeicon);						
		
		settingsIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WoozleActivity.this, SettingsActivity.class);																				
				
				WoozleActivity.this.startActivity(intent);					
			}
		});
		
		homeIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WoozleActivity.this.homeIconClicked();				
			}
		});
		
		mAbsoluteLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (puzzleState == PUZZLE_STATE_NOT_STARTED && event.getAction() == MotionEvent.ACTION_UP)
				{
					breakPuzzle();
			    	
					return false;
				}
				
				if (puzzleState == PUZZLE_STATE_NOT_FINISHED && (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP))
				{
					puzzleState = PUZZLE_STATE_FINISHED;
					return false;
				}
				
				if (puzzleState == PUZZLE_STATE_FINISHED && event.getAction() == MotionEvent.ACTION_UP)
				{
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WoozleActivity.this);
					alertDialogBuilder.setCancelable(true);
				    alertDialogBuilder.setTitle("Play Again");  
				    alertDialogBuilder.setMessage("Do you want to play this puzzle again?");
				    
				    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
				    	public void onClick(DialogInterface dialog, int which) {
				    		puzzleState = PUZZLE_STATE_NOT_STARTED;
				    		cleanAndStartPuzzle();  
				    		Handler handler = new Handler(WoozleActivity.this.getMainLooper());
				            handler.post(new Runnable() {
				    			
				    			@Override
				    			public void run() {
				    				breakPuzzle();			
				    			}
				    		});
				    	} 
				    });
				    
				    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {  
					      public void onClick(DialogInterface dialog, int which) {  
					        
					    } 
				    });
				    
				    alertDialogBuilder.create().show();
				}
				
				if (puzzleState != PUZZLE_STATE_STARTED)
					return true;
				
				if((event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) && currentPiece != -1)
				{
					ImageView view = imageViews.get(currentPiece);
					
					if(view == null)
					{
						currentPiece = -1;
						return false;
					}
					
					mAbsoluteLayout.removeView(view);
					
					PointF dim = piecesFinalDimensions.get(currentPiece);
					view.setLayoutParams(new android.view.ViewGroup.LayoutParams((int)dim.x, (int)dim.y));
					mPiecesView.addView(view);
					
					currentPiece = -1;
					return false;
				}
				
				if(event.getAction() == MotionEvent.ACTION_MOVE && currentPiece != -1)
				{
					ImageView view = imageViews.get(currentPiece);
					
					if(view == null)
					{
						currentPiece = -1;
						return false;
					}
					
					int w = view.getWidth();
					int h = view.getHeight();
					
					int cx = (int)(event.getRawX() - w/2.0f);
					int cy = (int)(event.getRawY() - h/2.0f);
					
					view.setLayoutParams(new AbsoluteLayout.LayoutParams(w, h, cx, cy));
					
					int finalX = (int) piecesFinalLocation.get(currentPiece).x;
					int finalY = (int) piecesFinalLocation.get(currentPiece).y;
					
					if(isNear(cx, finalX)
							&& isNear(cy, finalY))
					{
						imageViews.set(currentPiece, null);
						currentPiece = -1;
						++mPlacedPieceCount;
						view.setLayoutParams(new AbsoluteLayout.LayoutParams(w, h, finalX, finalY));
						
						MainManager.getInstance().playSound(R.raw.woodsoundspeicekeptoncorrectplace);
						
						if(mPlacedPieceCount == mPiecesCount)
						{
							puzzleSolved();
						}
					}
											
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					currentPiece = -1;
				}
				
				return true;
			}			
		});
		
		mScrollView = new CustomScrollView(this, this);
		mScrollView.setFillViewport(true);
		
		mPiecesView = new LinearLayout(this);			
		mPiecesView.setLayoutParams(new android.view.ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mPiecesView.setOrientation(LinearLayout.HORIZONTAL);
		
		mScrollView.addView(mPiecesView);
		mAbsoluteLayout.addView(mScrollView);
		
		mLeftButton = (ImageView) findViewById(R.id.leftButton);
		mRightButton = (ImageView) findViewById(R.id.rightButton);
		
		mLeftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WoozleActivity.this.LeftPressed();				
			}
		});
		
		mRightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WoozleActivity.this.rightPressed();				
			}
		});
    }
    
    private void breakPuzzle() {
		HorizontalScrollView scrollView = mScrollView;
		scrollView.setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, maximumScrollSize, 0, (int) (screenResolution.y - maximumScrollSize)));
		
		LinearLayout layoutView = mPiecesView;				
		
		for (int i = 0; i < imageViews.size(); i++) {
			
			ImageView pieceView = imageViews.get(i);
			
			mAbsoluteLayout.removeView(pieceView);
			pieceView.setVisibility(View.VISIBLE);
			PointF dim = piecesFinalDimensions.get(i);
			pieceView.setLayoutParams(new android.view.ViewGroup.LayoutParams((int)dim.x, (int)dim.y));			
			layoutView.addView(pieceView);				
		}
		
		puzzleState = PUZZLE_STATE_STARTED;
		mPlacedPieceCount = 0;
		
		mLeftButton.setVisibility(ImageView.INVISIBLE);
		mRightButton.setVisibility(ImageView.INVISIBLE);
					    	
		try {
			InputStream imageStreamBack;
			imageStreamBack = getAssets().open("ui/item"+MainManager.getInstance().getPuzzleNumber(mPuzzleIndex)+"/" + useDeviceString + "/"+backImage);
			Bitmap bitmap = BitmapFactory.decodeStream(imageStreamBack);
			
			//*
			 ImageView imageView = (ImageView) mAbsoluteLayout.findViewById(R.id.back);
			
			imageView.setLayoutParams(new AbsoluteLayout.LayoutParams((int)screenResolution.x, (int)screenResolution.y, 0 , 0));
			imageView.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					 
		
		MainManager.getInstance().playSound(R.raw.crashwoodsofter);
	}
    
    public void cleanAndStartPuzzle()
    {
    	imageViews.clear();
    	
    	for (int i = 0; i < imageViewsRetained.size(); i++) {
			ImageView imageView = imageViewsRetained.get(i);
			
			mAbsoluteLayout.removeView(imageView);
		}
    	
    	imageViewsRetained.clear();
    	
    	startPuzzle(Integer.parseInt(MainManager.getInstance().mPuzzleIndex.get(mPuzzleIndex)), mAbsoluteLayout);
    }
    
    public void LeftPressed()
    {
    	puzzleState = PUZZLE_STATE_NOT_STARTED;
    	
    	mPuzzleIndex = MainManager.getInstance().getPrevPuzzleIndex(mPuzzleIndex);
    	
    	cleanAndStartPuzzle();
    }
    
    public void rightPressed()
    {
    	puzzleState = PUZZLE_STATE_NOT_STARTED;
    	
    	mPuzzleIndex = MainManager.getInstance().getNextPuzzleIndex(mPuzzleIndex);
    	
    	cleanAndStartPuzzle();
    }
    
    public void puzzleSolved()
    {
    	puzzleState = PUZZLE_STATE_NOT_FINISHED;
    	mPlacedPieceCount = 0;
    	MainManager.getInstance().puzzleFinished();
    	mLeftButton.setVisibility(ImageView.VISIBLE);
    	mRightButton.setVisibility(ImageView.VISIBLE);
    	
    	imageViews.clear();
    	imageViews.addAll(imageViewsRetained);
    	
    	for (int i=0; i<mPiecesCount; ++i)
    	{
    		imageViews.get(i).setVisibility(View.INVISIBLE);
    	}
    	
    	InputStream imageStreamBack;
		try {
			imageStreamBack = getAssets().open("ui/item"+MainManager.getInstance().getPuzzleNumber(mPuzzleIndex)+"/" + useDeviceString + "/"+fullImage);
			Bitmap bitmap = BitmapFactory.decodeStream(imageStreamBack);
			
			//*
			 ImageView imageView = (ImageView) mAbsoluteLayout.findViewById(R.id.back);
			
			imageView.setLayoutParams(new AbsoluteLayout.LayoutParams((int)screenResolution.x, (int)screenResolution.y, 0 , 0));
			imageView.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    }
    
    public void startPuzzle(int puzzleNumber,
    						AbsoluteLayout absoluteLayout)
    {
    	piecesFileName = new ArrayList<String>();
    	piecesFinalLocation = new ArrayList<PointF>();
    	piecesFinalDimensions = new ArrayList<PointF>();
    	piecesLocation = new ArrayList<PointF>();
    	imageViews = new ArrayList<ImageView>();
    	imageViewsRetained = new ArrayList<ImageView>();
    	mPiecesCount = 0;
    	mPlacedPieceCount = 0;
    	fullImageLocation = new PointF();
    	currentPiece = -1;
    	
    	DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		screenResolution = new PointF(dm.widthPixels, dm.heightPixels);
		
		float currentAspectRatio = screenResolution.x / screenResolution.y;
		
		if(currentAspectRatio > MIDDLE_ASPECT_RATIO)
		{
			useiPad = true;
		}
		
		if(useiPad)
		{
			useDeviceString = "ipad";
			baseScreenSize = new PointF(768, 1024);
		}				
		
		Point settingOrigSize = new Point(80, 58);
		Point sizeSettingIcon = translateCoordinates(settingOrigSize);
		Point locSettingIcon = translateCoordinates(new Point(26, 26));		
			
		if(useiPad)
		{
			settingOrigSize = new Point(90, 65);
			sizeSettingIcon = translateCoordinates(settingOrigSize);
			locSettingIcon = translateCoordinates(new Point(30, 30));
		}
		
		ImageView settingIcon = (ImageView) findViewById(R.id.settingsIcon);
		
		settingIcon.setScaleType(ScaleType.FIT_XY);
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(sizeSettingIcon.x, (int)((sizeSettingIcon.x * settingOrigSize.y) / settingOrigSize.x), locSettingIcon.x, locSettingIcon.y);
		//params.setMargins(locSettingIcon.x, locSettingIcon.y , 0, 0);
		settingIcon.setLayoutParams(params);
		
		
		Point homeOrigSize = new Point(80, 58);
		Point sizeHomeIcon = translateCoordinates(settingOrigSize);
		Point locHomeIcon = translateCoordinates(new Point(534, 22));		
			
		if(useiPad)
		{
			homeOrigSize = new Point(90, 65);
			sizeHomeIcon = translateCoordinates(homeOrigSize);
			locHomeIcon = translateCoordinates(new Point(648, 28));
		}
		
		ImageView homeIcon = (ImageView) findViewById(R.id.homeicon);
		
		homeIcon.setScaleType(ScaleType.FIT_XY);
		params = new AbsoluteLayout.LayoutParams(sizeHomeIcon.x, (int)((sizeHomeIcon.x * homeOrigSize.y) / homeOrigSize.x), locHomeIcon.x, locHomeIcon.y);
		//params.setMargins(locSettingIcon.x, locSettingIcon.y , 0, 0);
		homeIcon.setLayoutParams(params);
		
		ImageView imageViewBack = (ImageView) absoluteLayout.findViewById(R.id.back);
		
		imageViewBack.setLayoutParams(new AbsoluteLayout.LayoutParams((int)screenResolution.x, (int)screenResolution.y, 0 , 0));
		imageViewBack.setImageBitmap(null); 
    	
    	try {
			InputStream stream = getAssets().open("ui/item"+puzzleNumber+"/" + useDeviceString + "/item"+puzzleNumber+"-" + useDeviceString + ".pzl");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			
			while (reader.ready()) {
				String str = reader.readLine();
				
				if(str.length() != 0)
				{										
					if(mPiecesCount == 0)
					{
						String [] arr = str.split(",");
						
						backImage = arr[0];
						fullImage = arr[1];
					}
					else if(mPiecesCount == 1)
					{
						fullImageLocation.x = Float.parseFloat(str.trim());
					}
					else
					{
						String [] arr = str.split(",");
						
						piecesFileName.add(arr[0]);
						piecesLocation.add(new PointF(Float.parseFloat(arr[2]), baseScreenSize.y - Float.parseFloat(arr[3])));
					}
					
					mPiecesCount++;
				}
				
			}
			mPiecesCount = mPiecesCount - 2;	
			
			
			
			if(maintainAspectRation)
				screenResolution.y = (baseScreenSize.y / baseScreenSize.x) * screenResolution.x;
			
			maximumScrollSize = -1;
			
			int local_puzzleIndex = MainManager.getInstance().getPuzzleIndex(puzzleNumber + "");
			
			ArrayList<Integer> puzzleScrollSize = MainManager.getInstance().mPuzzleScrollSize;
			
			if (puzzleScrollSize.get(local_puzzleIndex) != null
					&& puzzleScrollSize.get(local_puzzleIndex).intValue() != -1)
			{
				maximumScrollSize = puzzleScrollSize.get(local_puzzleIndex).intValue();
			}
			
			int local_maximumScrollSize = maximumScrollSize;
			
			ImageView view = null;
			for (int i = 0; i < mPiecesCount; i++) {
				view = new ImageView(this);
				
				InputStream imageStream = getAssets().open("ui/item"+puzzleNumber+"/" + useDeviceString + "/"+piecesFileName.get(i));
				Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
				view.setScaleType(ScaleType.FIT_XY);
				view.setImageBitmap(bitmap);				
				
				int w = convertLocation(bitmap.getWidth() , screenResolution.x , baseScreenSize.x);
				int h = convertLocation(bitmap.getHeight() , screenResolution.y , baseScreenSize.y);
				
				int x = (int) (convertLocation(piecesLocation.get(i).x, screenResolution.x, baseScreenSize.x) - w/2.0f);				
				int y = (int) (convertLocation(piecesLocation.get(i).y, screenResolution.y,  baseScreenSize.y) - h/2.0f);								
				
				if (maximumScrollSize == -1 )
				{
					int calcH = -1;
					
					int orig_h = bitmap.getHeight();
					int orig_w = bitmap.getWidth();
					
					int j =0;
					for (j = orig_h - 1; j >= 0 ; j--) {
					//for (j = 0; j <orig_h ; j++) {
						for (int j2 = 0; j2 < orig_w; j2++) {
							
							int color = bitmap.getPixel(j2, j);
							float alphacolor = Color.alpha(color) / 255.0f;
							if (alphacolor >= 0.04)
							{
								calcH = convertLocation(j + 1 , screenResolution.y , baseScreenSize.y);
								//break from loop
								j = -1;
								j2 = orig_w;
							}
						}
					}
					
					if (calcH > local_maximumScrollSize)
					{
						local_maximumScrollSize = calcH;
					}					
				}			
				
				piecesFinalLocation.add(new PointF(x, y));
				piecesFinalDimensions.add(new PointF(w, h));
				
				view.setLayoutParams(new AbsoluteLayout.LayoutParams(w, h, x, y));
				
				final int index = i;
				
				/*view.setLongClickable(true);
				view.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View arg0) {

						if (puzzleState != PUZZLE_STATE_STARTED)
							return false;
												
						{
							currentPiece = index;
							ImageView view = imageViews.get(currentPiece);
							
							if(view == null)
							{
								currentPiece = -1;
								return false;
							}
							else
							{
								PointF dimension = piecesFinalDimensions.get(currentPiece);
								
								int cx = (int)(mLastTouch.x - dimension.x/2.0f);
								int cy = (int)(mLastTouch.y - dimension.y/2.0f);
								
								view.setLayoutParams(new AbsoluteLayout.LayoutParams((int)dimension.x, (int) dimension.y, cx, cy));
								
								mPiecesView.removeView(view);
								mAbsoluteLayout.addView(view);
								
								return false;
							}
						}
					}
				});*/
				
				view.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						
						mLastTouch = new Point((int)arg1.getRawX(), (int)arg1.getRawY());						

						if (puzzleState != PUZZLE_STATE_STARTED)
							return false;
						
						if(arg1.getAction() == MotionEvent.ACTION_DOWN)
						{
							currentPiece = index;
							ImageView view = imageViews.get(currentPiece);
							
							if(view == null)
							{
								currentPiece = -1;
								return false;
							}
							else
							{
								PointF dimension = piecesFinalDimensions.get(currentPiece);
								
								int cx = (int)(mLastTouch.x - dimension.x/2.0f);
								int cy = (int)(mLastTouch.y - dimension.y/2.0f);																
								
								mPiecesView.removeView(view);
								view.setLayoutParams(new AbsoluteLayout.LayoutParams((int)dimension.x, (int) dimension.y, cx, cy));
								mAbsoluteLayout.addView(view);
								
								return false;
							}
						}
						else
						{
							currentPiece = -1;
						}
						
						return false;
					}
				});
				
				absoluteLayout.addView(view);		
				imageViews.add(view);
				imageViewsRetained.add(view);
				
				view.setVisibility(View.INVISIBLE);
				
			}		
			
			if (maximumScrollSize == -1)
			{
				maximumScrollSize = local_maximumScrollSize;
				MainManager.getInstance().mPuzzleScrollSize.set(local_puzzleIndex, new Integer(maximumScrollSize));
			}
			
			InputStream imageStreamBack = getAssets().open("ui/item"+puzzleNumber+"/" + useDeviceString + "/"+fullImage);
			Bitmap bitmap = BitmapFactory.decodeStream(imageStreamBack);
			
			//*
			 ImageView imageView = (ImageView) absoluteLayout.findViewById(R.id.back);
			
			imageView.setLayoutParams(new AbsoluteLayout.LayoutParams((int)screenResolution.x, (int)screenResolution.y, 0 , 0));
			imageView.setImageBitmap(bitmap); 						
			
			// */
			
			/*
			
			absoluteLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));
			
			// */
			
			mPlacedPieceCount = 0;												
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WoozleActivity.this.finish();
		}
    	
    	
    }
    
    public void homeIconClicked()
    {
    	finish();
    }
    
    public boolean isNear(int a, int b)
    {
    	int diff = 10;
    	
    	if(a <= (b + diff)
    			&& a >= (b - diff))
    	{
    		return true;
    	}
    	return false;
    }        

	public int convertLocation(float value, float current, float base)
    {
    	int val = (int)((value * current) / base); 
    	return val;
    }
	
	public Point translateCoordinates(Point loc)
	{
		Point output = new Point();
		
		output.x = (int) ((screenResolution.x * 1.0f / baseScreenSize.x) * loc.x);
		output.y = (int) ((screenResolution.y * 1.0f / baseScreenSize.y) * loc.y);
		
		return output;
	}
	
	public Point translateCoordinates(PointF loc)
	{
		Point output = new Point();
		
		output.x = (int) ((screenResolution.x * 1.0f / baseScreenSize.x) * loc.x);
		output.y = (int) ((screenResolution.y * 1.0f / baseScreenSize.y) * loc.y);
		
		return output;
	}
	
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MainManager.getInstance().activityClosed(this);
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

	public int dontConvert(float value, float current, float base)
    {
    	return (int)value;
    }
}
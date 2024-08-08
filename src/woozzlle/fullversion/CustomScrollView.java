package woozzlle.fullversion;


import android.content.Context;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class CustomScrollView extends HorizontalScrollView {
	
	public WoozleActivity woozleActivity;

	public CustomScrollView(Context context, WoozleActivity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		
		woozleActivity = activity;
	}	

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(woozleActivity.puzzleState == woozleActivity.PUZZLE_STATE_STARTED 
				&& woozleActivity.currentPiece != -1)
			return false;
		
		return super.onTouchEvent(ev);
	}
	
}

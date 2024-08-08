package woozzlle.fullversion;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash_screen);
		
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				finish();
				Intent intent = new Intent(SplashActivity.this, MainScreenActivity.class);																	
				SplashActivity.this.startActivity(intent);
			}
		};
		
		Timer timer = new Timer();
		
		timer.schedule(timerTask, 1000);
	}
}

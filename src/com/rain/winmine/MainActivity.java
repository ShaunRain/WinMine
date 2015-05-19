package com.rain.winmine;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static MainActivity mainActivity = null;
	
	public static Vibrator vibrator;

	public static TextView highScore;
	public static ImageButton emoji;
	public static Chronometer time;
	private GameView gameView;
	static SharedPreferences sp;

	public MainActivity() {
		mainActivity = this;
	}

	public static MainActivity getMainInstance() {
		return mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		emoji = (ImageButton) findViewById(R.id.emoji);
		emoji.setScaleType(ScaleType.CENTER);
		highScore = (TextView) findViewById(R.id.highscore);
		sp = getPreferences(MODE_PRIVATE);
		highScore.setText(sp.getInt("highscore", 0) + "");
		time = (Chronometer) findViewById(R.id.time);
		time.setFormat("%s");
		gameView = (GameView) findViewById(R.id.gameView);
		emoji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gameView.startGame();
				Log.d("cornerup",
						GameView.blocksMap[Config.ROWS - 1][Config.ROWS - 2]
								.getNumber() + "");
				Log.d("cornerleft",
						GameView.blocksMap[Config.ROWS - 2][Config.ROWS - 1]
								.getNumber() + "");
				Log.d("corner",
						GameView.blocksMap[Config.ROWS - 1][Config.ROWS - 1]
								.getNumber() + "");
			}
		});
	}
}

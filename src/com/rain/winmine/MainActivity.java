package com.rain.winmine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	private static MainActivity mainActivity = null;

	//private int score = 0;
	public static ImageButton emoji;
	private GameView gameView;

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

		emoji = (ImageButton) findViewById(R.id.emoji);
		emoji.setScaleType(ScaleType.CENTER);
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

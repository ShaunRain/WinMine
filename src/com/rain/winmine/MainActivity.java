package com.rain.winmine;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	static MainActivity mainActivity = null;

	static MediaPlayer playerBGM;
	static MediaPlayer playerOpen;
	static MediaPlayer playerDie;
	static MediaPlayer playerClear;
	static MediaPlayer playerFlag;
	/*
	 * public static int SOUND_DIE = R.raw.mario_die; public static int
	 * SOUND_OPEN = R.raw.mario_coin; public static int SOUND_WIN =
	 * R.raw.mario_clear;
	 */

	public static Vibrator vibrator;

	public static RadioGroup group;
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

		playerBGM = MediaPlayer.create(this, R.raw.mario_bgm);
		playerBGM.setLooping(true);

		playerClear = MediaPlayer.create(this, R.raw.mario_clear);
		playerOpen = MediaPlayer.create(this, R.raw.mario_coin);
		playerDie = MediaPlayer.create(this, R.raw.mario_die);
		playerFlag = MediaPlayer.create(this, R.raw.mario_jump);

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
			}
		});
		emoji.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Block.win();
				return true;
			}
		});
		group = (RadioGroup) findViewById(R.id.group);
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio_easy:
					GameView.MINEMOUNT = 10;
					GameView.getGameView().startGame();
					break;
				case R.id.radio_normal:
					GameView.MINEMOUNT = 16;
					GameView.getGameView().startGame();
					break;
				case R.id.radio_hard:
					GameView.MINEMOUNT = 21;
					GameView.getGameView().startGame();
					break;
				case R.id.radio_hell:
					GameView.MINEMOUNT = 27;
					GameView.getGameView().startGame();
					break;
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (playerBGM.isPlaying())
			playerBGM.stop();
		
		playerBGM.release();
		playerOpen.release();
		playerDie.release();
		playerClear.release();
		playerFlag.release();
	}
}

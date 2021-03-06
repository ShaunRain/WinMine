package com.rain.winmine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Block extends FrameLayout {

	private final class MyLongListener implements OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if (statusNow == STATUS_COVERED) {
				coverBack.setBackgroundResource(R.drawable.square_flag);
				if (MainActivity.playerFlag.isPlaying()) {
					MainActivity.playerFlag.pause();
					MainActivity.playerFlag.seekTo(0);
				}
				MainActivity.playerFlag.start();
				setFlagged();
				if (GameView.checkGame())
					win();
			} else if (statusNow == STATUS_OPENED) {
				if (numberOfSurround != 0)// 长按不为空的数字block，自动打开没插旗的block
				{
					MainActivity.vibrator.vibrate(300);
					GameView.autoOpen(x, y);
				}

			}
			return true;
		}
	}

	private final class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (statusNow == STATUS_COVERED) {
				if (MainActivity.playerOpen.isPlaying()) {
					MainActivity.playerOpen.pause();
					MainActivity.playerOpen.seekTo(0);
				}
				MainActivity.playerOpen.start();
				setOpened();
				coverBack.setAlpha(0);
				if (isMine) {
					MainActivity.playerDie.start();
					MainActivity.playerBGM.pause();
					MainActivity.vibrator.vibrate(1500);
					MainActivity.time.stop();
					GameView.clearAll();
					MainActivity.emoji.setImageResource(R.drawable.sad);
					new AlertDialog.Builder(getContext())
							.setCancelable(false)
							.setTitle("Game Over")
							.setMessage("Sorry,you step on a mine.")
							.setPositiveButton("Restart",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method
											// stub
											GameView.getGameView().startGame();
										}
									})
							.setNegativeButton("Death Replay",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
				} else { // not mine

					if (numberOfSurround == 0) {// 如果是空，周围全打开
						//GameView.recBlank(x, y, x, y);
						GameView.clearAround(x, y);
					}

					if (GameView.checkGame())
						win();
				}

			} else if (statusNow == STATUS_FLAGGED) {
				setCovered();
				coverBack.setBackgroundResource(R.drawable.square_blue);
			}
		}
	}

	// 三种单元状态
	final static int STATUS_COVERED = 0;// 覆盖
	final static int STATUS_OPENED = 1;// 打开
	final static int STATUS_FLAGGED = 2;// 插旗

	private int statusNow;// 单元状态

	final static int OH_MINE = 0x11;
	private boolean isMine;// 是否是雷
	private boolean isFlag;// 是否为旗
	private boolean isClickable = true;// 是否可点击
	private int numberOfSurround;// 周围雷数目，即显示数字

	public TextView number;// 显示数字 。。或者地雷
	public View uncoverBack;// open背景
	public View coverBack;// cover背景

	public int x;// 在gameview中的坐标
	public int y;

	private ObjectAnimator animator;

	private MyOnClickListener listener;

	private int[] numberColors = { 0xffffffff, 0xffEEAD0E, 0xff90EE90,
			0xff836FFF, 0xffFFAEB9, 0xffFF00FF, 0xff8B5742, 0x121212 };

	public Block(Context context) {
		super(context);

		setDefault();

		LayoutParams lp = null; // 布局参数

		// 底层背景，白色就好
		uncoverBack = new View(getContext());
		lp = new LayoutParams(-1, -1);// -1,-1填充parent
		// lp.setMargins(1, 1, 0, 0);// 与边界间隔10像素
		uncoverBack.setBackgroundResource(R.drawable.square_grey);
		addView(uncoverBack, lp);

		// 显示数字用的TextView
		number = new TextView(getContext());
		number.setTextSize(20);
		number.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		number.setGravity(Gravity.CENTER);
		lp = new LayoutParams(-1, -1);
		// lp.setMargins(1, 1, 0, 0);
		// number.setVisibility(INVISIBLE);// 覆盖时不显示数字
		addView(number, lp);

		// 外层背景，暂时显示
		coverBack = new View(getContext());
		listener = new MyOnClickListener();
		coverBack.setOnClickListener(listener);

		coverBack.setOnLongClickListener(new MyLongListener());
		lp = new LayoutParams(-1, -1);// -1,-1填充parent
		// lp.setMargins(1, 1, 0, 0);// 与边界间隔10像素
		coverBack.setBackgroundResource(R.drawable.square_blue);
		addView(coverBack, lp);

	}

	public int getStatus() {
		return statusNow;
	}

	// 设置状态的公开方法，只能设置一种状态
	public void setCovered() {
		this.statusNow = STATUS_COVERED;
		isFlag = false;
		isClickable = true;
	}

	public void setOpened() {
		this.statusNow = STATUS_OPENED;
		isFlag = false;
		isClickable = false;
	}

	public void setFlagged() {
		this.statusNow = STATUS_FLAGGED;
		isFlag = true;
		isClickable = true;
		MainActivity.vibrator.vibrate(300);
	}

	public boolean isClickable() {
		return isClickable;
	}

	public void setMine(boolean flag) {
		isMine = flag;
	}

	public boolean isMine() {
		return isMine;
	}

	public boolean isFlag() {
		return isFlag;
	}

	public boolean isCover() {
		return isClickable && !isFlag;
	}

	/*
	 * public void open() { coverBack.setAlpha(0); }
	 */

	/*
	 * 根据数字设置颜色和背景
	 */
	public void setNumber(int num) {
		this.numberOfSurround = num;

		if (num == 0) {
			number.setAlpha(0);
		} else if (num == OH_MINE) {
			number.setText("M");
			number.setTextColor(Color.RED);
		} else {
			number.setText(num + "");
			number.setTextColor(numberColors[num]);
		}

	}

	public int getNumber() {
		return numberOfSurround;
	}

	/*
	 * 初始化状态
	 */
	public void setDefault() {

		statusNow = STATUS_COVERED;
		isMine = false;
		isClickable = true;
		numberOfSurround = 0;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static void win() {
		MainActivity.playerBGM.pause();
		MainActivity.playerClear.start();
		MainActivity.emoji.setImageResource(R.drawable.cool);
		new AlertDialog.Builder(MainActivity.getMainInstance())
				.setTitle("Congratulations!")
				.setMessage(
						"You have finished the game. With final Score:"
								+ GameView.score)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						GameView.getGameView().startGame();

					}
				}).show();
	}

	public void openClick(View v) {
		listener.onClick(v);
	}

	// 隐身技
	public void fade() {
		coverBack.setAlpha(0);
		uncoverBack.setAlpha(0);
		number.setAlpha(0);
	}

	// 现身
	public void appear() {
		animator = ObjectAnimator.ofFloat(this.coverBack, "alpha", 0f, 1f);
		animator.setStartDelay(x * 100 + y * 100);
		animator.setDuration(500);
		animator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				uncoverBack.setAlpha(1f);
				coverBack.setClickable(true);
				if (numberOfSurround != 0) {
					number.setAlpha(1f);
				}
			}
		});
		animator.start();
	}

	// mine动画
	public void blink() {
		animator = ObjectAnimator.ofFloat(this.number, "alpha", 1f, 0f, 1f);

		ObjectAnimator animator2 = new ObjectAnimator();
		animator2 = ObjectAnimator
				.ofFloat(this.number, "rotation", 360, 0, 360);

		AnimatorSet set = new AnimatorSet();
		set.setDuration(2000);
		set.setStartDelay(x * 100 + y * 100);
		set.play(animator).with(animator2);
		set.start();
	}

}

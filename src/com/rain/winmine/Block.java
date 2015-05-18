package com.rain.winmine;

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
				setFlagged();
				if (GameView.checkGame())
					win();
			} else if (statusNow == STATUS_OPENED) {
				if (numberOfSurround != 0)// ������Ϊ�յ�����block���Զ���û�����block
					GameView.autoOpen(x, y);
			}
			return true;
		}
	}

	private final class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (statusNow == STATUS_COVERED) {
				setOpened();
				coverBack.setAlpha(0);
				if (isMine) {
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

					if (numberOfSurround == 0) {// �õݹ��ҵ��ڽӵĿ�block��open it
						GameView.recBlank(x, y, x, y);
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

	// ���ֵ�Ԫ״̬
	final static int STATUS_COVERED = 0;// ����
	final static int STATUS_OPENED = 1;// ��
	final static int STATUS_FLAGGED = 2;// ����

	private int statusNow;// ��Ԫ״̬

	final static int OH_MINE = 0x11;
	private boolean isMine;// �Ƿ�����
	private boolean isFlag;// �Ƿ�Ϊ��
	private boolean isClickable = true;// �Ƿ�ɵ��
	private int numberOfSurround;// ��Χ����Ŀ������ʾ����

	private TextView number;// ��ʾ���� �������ߵ���
	private View uncoverBack;// open����
	public View coverBack;// cover����

	public int x;// ��gameview�е�����
	public int y;

	private MyOnClickListener listener;

	private int[] numberColors = { 0xffffffff, 0xffEEAD0E, 0xff90EE90,
			0xff836FFF, 0xffFFAEB9, 0xffFF00FF, 0xff8B5742, 0x121212 };

	public Block(Context context) {
		super(context);

		setDefault();

		LayoutParams lp = null; // ���ֲ���

		// �ײ㱳������ɫ�ͺ�
		uncoverBack = new View(getContext());
		lp = new LayoutParams(-1, -1);// -1,-1���parent
		// lp.setMargins(1, 1, 0, 0);// ��߽���10����
		uncoverBack.setBackgroundResource(R.drawable.square_grey);
		addView(uncoverBack, lp);

		// ��ʾ�����õ�TextView
		number = new TextView(getContext());
		number.setTextSize(20);
		number.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		number.setGravity(Gravity.CENTER);
		lp = new LayoutParams(-1, -1);
		// lp.setMargins(1, 1, 0, 0);
		// number.setVisibility(INVISIBLE);// ����ʱ����ʾ����
		addView(number, lp);

		// ��㱳������ʱ��ʾ
		coverBack = new View(getContext());
		listener = new MyOnClickListener();
		coverBack.setOnClickListener(listener);

		coverBack.setOnLongClickListener(new MyLongListener());
		lp = new LayoutParams(-1, -1);// -1,-1���parent
		// lp.setMargins(1, 1, 0, 0);// ��߽���10����
		coverBack.setBackgroundResource(R.drawable.square_blue);
		addView(coverBack, lp);

	}

	public int getStatus() {
		return statusNow;
	}

	// ����״̬�Ĺ���������ֻ������һ��״̬
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

	/*
	 * public void open() { coverBack.setAlpha(0); }
	 */

	/*
	 * ��������������ɫ�ͱ���
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
	 * ��ʼ��״̬
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

	public void win() {
		new AlertDialog.Builder(MainActivity.getMainInstance())
				.setTitle("Congratulation!")
				.setMessage("You have finished the game.")
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

}

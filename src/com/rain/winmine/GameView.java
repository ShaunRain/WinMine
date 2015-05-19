package com.rain.winmine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class GameView extends LinearLayout {
	final static int MINEMOUNT = 20;
	private static GameView gameView = null;
	final static int OH_MINE = 0x11;
	private boolean HAVE_BLOCKS = false;
	
	public static int score;

	public static Block[][] blocksMap = new Block[Config.ROWS][Config.ROWS]; // ����ÿ��block�ľ���

	// private static Block emptyBlock = null;

	// ���Ա�����ֵΪ�յ�block

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		gameView = this;
		initGameView();
	}

	public GameView(Context context, AttributeSet attr) {
		// TODO Auto-generated method stub
		super(context, attr);
		gameView = this;
		initGameView();
	}

	public static GameView getGameView() {
		return gameView;
	}

	private void initGameView() { // ��Ϸ���ֳ�ʼ������

		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(Color.WHITE);
		// setBackgroundResource(R.drawable.back);

	}

	/*
	 * ������Ļ�ߴ�ı�ı�block�ߴ磬ǿ������Ļ��ֱ������ֻ��GameView��һ���������
	 * 
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Config.BLOCK_WIDTH = (Math.min(w, h) - 5) / Config.ROWS;// ����card�ߴ�

		startGame();
	}

	public void startGame() {
		// TODO Auto-generated method stub
		MainActivity.emoji.setImageResource(R.drawable.smile);
		MainActivity.time.setBase(SystemClock.elapsedRealtime());
		MainActivity.time.start();
		if (!HAVE_BLOCKS) {
			addBlocks(Config.BLOCK_WIDTH, Config.BLOCK_WIDTH);// ��ʼ������block
		} else {
			this.removeAllViewsInLayout();
			addBlocks(Config.BLOCK_WIDTH, Config.BLOCK_WIDTH);
		}
		setTheNumber();
	}

	public void addBlocks(int cardWidth, int cardHeight) { // �򲼾ֿ�������blocks

		Block b;
		LinearLayout ll;
		LinearLayout.LayoutParams lp;

		for (int y = 0; y < Config.ROWS; y++) {
			ll = new LinearLayout(getContext());
			lp = new LinearLayout.LayoutParams(-1, cardHeight);// ÿһ�еĲ��֣�������䣬����Block����
			addView(ll, lp);
			for (int x = 0; x < Config.ROWS; x++) {
				b = new Block(getContext());
				ll.addView(b, cardWidth, cardHeight);
				// ��ʼ��Block���飬���׻�����
				blocksMap[x][y] = b;
				blocksMap[x][y].setLocation(x, y);
				blocksMap[x][y].setClickable(true);
				// blocksMap[x][y].setMine(Math.random() < 0.25 ? true : false);
			}
		}

		setMines();

		HAVE_BLOCKS = true;

	}

	private void setMines() {
		Random rand = new Random();
		for (int i = 0; i < MINEMOUNT; i++)
			blocksMap[rand.nextInt(Config.ROWS)][rand.nextInt(Config.ROWS)]
					.setMine(true);
	}

	private void setTheNumber() {// ����ÿ��block������

		for (int y = 0; y < Config.ROWS; y++) {
			for (int x = 0; x < Config.ROWS; x++) {
				if (!blocksMap[x][y].isMine())
					blocksMap[x][y].setNumber(getTheNumber(x, y));
				else
					blocksMap[x][y].setNumber(OH_MINE);

			}
		}
		/*
		 * StringBuffer sb = new StringBuffer(); for (int y = 0; y <
		 * Config.ROWS; y++) { for (int x = 0; x < Config.ROWS; x++) {
		 * sb.append(x + "," + y + ":" + blocksMap[x][y].getNumber() + "  "); }
		 * }
		 * 
		 * Log.d("allblock", sb.toString());
		 */
	}

	// �жϷ�������Ŀ����Χ��8�������ж��Ƿ����block�����������Ƿ�Ϊ�ס�
	private int getTheNumber(int x, int y) {
		int count = 0;

		for (int i = x - 1; i < x + 2; i++) {
			for (int j = y - 1; j < y + 2; j++) {
				if (i == x && j == y)
					continue;
				else {
					if (i >= 0 && i < Config.ROWS && j >= 0 && j < Config.ROWS) {// û�����߽�
						if (blocksMap[i][j].isMine())
							count++;
					}
				}
			}
		}

		return count;

	}

	/*
	 * �����Ϸ�Ƿ����
	 */
	public static boolean checkGame() {
		for (int i = 0; i < Config.ROWS; i++) {
			for (int j = 0; j < Config.ROWS; j++) {
				if (blocksMap[i][j].isMine() && !blocksMap[i][j].isFlag())
					return false;
			}
		}
		MainActivity.time.stop();
		String[] fin = MainActivity.time.getText().toString().split(":");
		score = getScore(Integer.parseInt(fin[0]), Integer.parseInt(fin[1]));
		MainActivity.sp.edit().putInt("highscore", score);
		return true;
	}

	/*
	 * getScore ͨ�����ʱ�����÷֣�ʱ��Խ�̷���Խ��
	 */
	public static int getScore(int min, int sec) {
		return (60 - min) * 5 + (60 - sec) * 50;
	}

	/*
	 * open������հ�block���ӵĿհ�block
	 */
	public static void recBlank(int x, int y, int px, int py) {
		int i, j;
		int unBlank = 0;
		/*
		 * i=x-1,j=y;//1 i=x,j=y-1;//2 i=x+1,j=y;//3 i=x,j=y+1;//4
		 */
		List<Point> list = new ArrayList<Point>();// ���ĸ��ڽӷ����block����list
		list.add(new Point(x - 1, y));
		list.add(new Point(x, y - 1));
		list.add(new Point(x + 1, y));
		list.add(new Point(x, y + 1));

		for (int n = 0; n < list.size(); n++) {// ���ĸ���������

			Point p = list.get(n);
			i = p.x;
			j = p.y;

			if (i < 0 || i > Config.ROWS - 1 || j < 0 || j > Config.ROWS - 1) {// �����߽�
				if (++unBlank == 4)
					return;
				else
					continue;
			} else {
				if (i == px && j == py) {// wtf..���ų���block
					if (++unBlank == 4)
						return;
					else
						continue;
				} else {
					if (blocksMap[i][j].getNumber() == 0) {
						blocksMap[i][j].openClick(blocksMap[i][j].coverBack);
						// recBlank(i, j, x, y);
					} else if (++unBlank == 4)
						return;
					else
						continue;
				}

			}

		}

	}

	/*
	 * AoutoOpen : ��������block�������Χ��������������ȣ�open others
	 */

	public static void autoOpen(int x, int y) {
		List<Block> openList = new ArrayList<>();

		int num = blocksMap[x][y].getNumber();

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && j >= 0 && i < Config.ROWS && j < Config.ROWS) {
					if (blocksMap[i][j].getStatus() == Block.STATUS_FLAGGED)
						num--;
					else
						openList.add(blocksMap[i][j]);
				}
			}
		}

		if (num == 0) {
			for (Block b : openList)
				b.openClick(b.coverBack);
		}
	}

	/*
	 * clear all covers
	 */
	public static void clearAll() {
		for (int x = 0; x < Config.ROWS; x++) {
			for (int y = 0; y < Config.ROWS; y++)
				blocksMap[x][y].coverBack.setAlpha(0);
		}
	}

	/*
	 * make all unclickable
	 */
	public static void unclickAll() {
		for (int x = 0; x < Config.ROWS; x++) {
			for (int y = 0; y < Config.ROWS; y++)
				blocksMap[x][y].setClickable(false);
		}
	}
}

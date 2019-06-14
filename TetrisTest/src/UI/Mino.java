package UI;

import java.util.Random;

public class Mino {
	enum Tetrominoes {// ミノの形と壁
		NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape, Wall
	};

	private Tetrominoes Mino;
	private int coords[][];// coordsは座標
	private int[][][] coordsTable;

	public Mino() {
		coords = new int[4][2];
		setMino(Tetrominoes.NoShape);
	}

	public void setMino(Tetrominoes s) {
		//Defines the Pixal of 7 shape of pieces + 1 NoShape + 1 Wall.
		coordsTable = new int[][][] {
				{ { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
				{ { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },
				{ { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
				{ { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
				{ { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },
				{ { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
				{ { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
				{ { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
				{ { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }
		};

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; ++j) {
				coords[i][j] = coordsTable[s.ordinal()][i][j];
			}
		}
		Mino = s;
	}

	public Tetrominoes getShape() {
		return Mino;
	}

	private void setX(int index, int x) {
		coords[index][0] = x;
	}

	private void setY(int index, int y) {
		coords[index][1] = y;
	}

	public int getX(int index) {
		return coords[index][0];
	}

	public int getY(int index) {
		return coords[index][1];
	}

	//ミノの形をランダムに生成
	public void setRandomShape() {
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % 7 + 1;
		Tetrominoes[] values = Tetrominoes.values();
		setMino(values[x]);
	}

	public int minX() {
		int m = coords[0][0];
		for (int i = 0; i < 4; i++) {
			m = Math.min(m, coords[i][0]);
		}
		return m;
	}

	public int minY() {
		int m = coords[0][1];
		for (int i = 0; i < 4; i++) {
			m = Math.min(m, coords[i][1]);
		}
		return m;
	}

	// ミノ回転メソッド
	public Mino rotate() {
		if (Mino == Tetrominoes.SquareShape)
			return this;

		Mino result = new Mino();
		result.Mino = Mino;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, -getY(i));
			result.setY(i, getX(i));
		}
		return result;
	}

}
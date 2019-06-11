package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import UI.Shape.Tetrominoes;

public class Panel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	static final int BoardWidth = 16;
	static final int BoardHeight = 21;

	Timer timer;
	int[] timeset = { 400, 350, 300, 200, 100, 50, 30, 20 };
	boolean isFallingFinished = false;
	boolean isStarted = false;
	boolean isPaused = false;
	int score = 0;
	int level = 0;
	int curX = 0;
	int curY = 0;
	Shape curPiece;
	Shape nextPiece;
	Tetrominoes[] panel;
	JLabel statusbar;
	JButton retry;

	public Panel(TFrame p) {
		setFocusable(true);
		curPiece = new Shape();
		nextPiece = new Shape();
		nextPiece.setRandomShape();
		timer = new Timer(400, this);
		timer.start();
		statusbar = p.getStatusBar();
		retry = p.getRetryButton();
		/*
		retry.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
				SetEmptyBoard();
				newPiece();
				statusbar.setText("Score: " + String.valueOf(score));
		    }
		});
		*/
		panel = new Tetrominoes[BoardWidth * BoardHeight];
		this.addKeyListener(new myAdapter());
		SetEmptyBoard();
	}

	// Game Start
	public void start() {
		if (isPaused)
			return;
		//始めたとき
		isStarted = true;
		//落下しなくなったとき
		isFallingFinished = false;
		score = 0;
		SetEmptyBoard();
		newPiece();
		timer.start();
	}

	// Pause Button.
	private void pause() {
		if (!isStarted)
			return;
		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			statusbar.setText("paused");
		} else {
			timer.start();
			statusbar.setText("Score: " + String.valueOf(score));
		}
		repaint();
	}

	public void actionPerformed(ActionEvent e) {

		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}

	// creating a new piece in the middle.
	private void newPiece() {
		curPiece = nextPiece;
		Shape temp = new Shape();
		temp.setRandomShape();
		nextPiece = temp;
		curX = (BoardWidth - 4) / 2;
		curY = BoardHeight - 1 + curPiece.minY();
		if (!tryMove(curPiece, curX, curY)) {
			curPiece.setShape(Tetrominoes.NoShape);
			timer.stop();
			isStarted = false;
			statusbar.setText("Oooops, game over.");
		}
	}

	// Calculating the Square Size.
	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;
	}

	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}

	// Return the Shape at Position(x,y) int the panel.
	Tetrominoes shapeAt(int x, int y) {
		return panel[(y * BoardWidth) + x];
	}

	// Initialize the Panel to NoShape.
	private void SetEmptyBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			panel[i] = Tetrominoes.NoShape;
	}

	// Check if there is enough space to move.
	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.getX(i);
			int y = newY - newPiece.getY(i);
			if (x < 1 || x >= BoardWidth - 5 || y < 1 || y >= BoardHeight)
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)
				return false;
		}

		curPiece = newPiece;
		curX = newX;
		curY = newY;
		repaint();
		return true;
	}

	private void removeFullLines() {
		int numFullLines = 0;

		for (int i = BoardHeight - 2; i >= 0; i--) {
			boolean lineIsFull = true;

			for (int j = 1; j < BoardWidth - 5; j++) {
				if (shapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {
				++numFullLines;
				for (int k = i; k < BoardHeight - 1; ++k) {
					for (int j = 0; j < BoardWidth; ++j)
						panel[(k * BoardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}

		if (numFullLines > 0) {
			int temps = 0;
			if (numFullLines == 1)
				temps = 1;
			else if (numFullLines == 2)
				temps = 3;
			else if (numFullLines == 3)
				temps = 5;
			else if (numFullLines == 4)
				temps = 10;
			score += temps;
			//Now we have current Score, try to set the level.
			level = score / 50;
			if (level > 7)
				level = 7;
			timer.setDelay(timeset[level]);
			timer.start();

			statusbar.setText("Score: " + String.valueOf(score));
			isFallingFinished = true;
			curPiece.setShape(Tetrominoes.NoShape);
			repaint();
		}
	}

	private void pieceDropped() {
		for (int i = 0; i < 4; ++i) {
			int x = curX + curPiece.getX(i);
			int y = curY - curPiece.getY(i);
			panel[(y * BoardWidth) + x] = curPiece.getShape();
		}

		removeFullLines();

		if (!isFallingFinished)
			newPiece();
	}

	private void oneLineDown() {
		if (!tryMove(curPiece, curX, curY - 1))
			pieceDropped();
	}

	private void dropDown() {
		int newY = curY;
		while (newY > 0) {
			if (!tryMove(curPiece, curX, newY - 1))
				break;
			--newY;
		}
		pieceDropped();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
				if ((i == BoardHeight - 1 && j <= BoardWidth - 5) || j == 0 || j == BoardWidth - 5)
					shape = Tetrominoes.Wall;
				if (shape != Tetrominoes.NoShape)
					drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
			}
		}

		if (nextPiece.getShape() != Tetrominoes.NoShape) {
			int nextX = BoardWidth - 2;
			int nextY = BoardHeight - 5 + nextPiece.minY();
			if (nextPiece.getShape() == Tetrominoes.SquareShape || nextPiece.getShape() == Tetrominoes.SShape
					|| nextPiece.getShape() == Tetrominoes.MirroredLShape)
				nextX--;
			for (int i = 0; i < 4; i++) {
				int x = nextX + nextPiece.getX(i);
				int y = nextY - nextPiece.getY(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						nextPiece.getShape());
			}

		}

		if (curPiece.getShape() != Tetrominoes.NoShape) {
			for (int i = 0; i < 4; i++) {
				int x = curX + curPiece.getX(i);
				int y = curY - curPiece.getY(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						curPiece.getShape());
			}
		}
	}

	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color colors[] = { //Color(引数)はint型の値で、R,G,Bの三原色を0～255の値で指定する。
				new Color(0, 0, 0),
				new Color(204, 102, 102),
				new Color(102, 204, 102),
				new Color(102, 102, 204),
				new Color(204, 204, 102),
				new Color(204, 102, 204),
				new Color(102, 204, 204),
				new Color(218, 170, 0),
				new Color(119, 136, 153)
		};

		Color color = colors[shape.ordinal()];

		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}

	//キーの入力設定
	class myAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				return;
			}

			int keycode = e.getKeyCode();

			if (keycode == 'p' || keycode == 'P') {
				pause();
				return;
			}

			if (isPaused)
				return;

			switch (keycode) {
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;
			case KeyEvent.VK_UP:
				tryMove(curPiece.rotate(), curX, curY);
				break;
			case KeyEvent.VK_DOWN:
				dropDown();
				break;
			case 'd':
				oneLineDown();
				break;
			case 'D':
				oneLineDown();
				break;
			}

		}
	}

}

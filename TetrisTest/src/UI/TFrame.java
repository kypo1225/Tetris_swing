package UI;

import javax.swing.JFrame;;

public class TFrame extends JFrame {

	//TFrameのコンストラクター
	public TFrame() {
		//WindowSize
		setSize(640, 740);
		//Title
		setTitle("Tetris");
		//終了処理(これがないと×で閉じてもプロセスに残る)
		setDefaultCloseOperation(TFrame.EXIT_ON_CLOSE);
		//Panelクラスをインスタンス化（this?）
		Panel panel = new Panel(this);
		//TFrameインスタンスににPanelインスタンスを追加
		add(panel);
		//PanelClassのgame startメソッド
		panel.start();
	}
}
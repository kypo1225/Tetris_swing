package Main;

import UI.TFrame;

public class Main {
	public static void main(String[] args) {

		// TFrameクラスをインスタンス化
		TFrame tetris = new TFrame();
		// JFrameクラスの親クラスであるWindowクラスのメソッド。nullを引数にすると画面中央に表示される
		tetris.setLocationRelativeTo(null);
		// フレームを表示するメソッド。引数にtrueを指定すると対象のフレームが表示される
		tetris.setVisible(true);

	}
}
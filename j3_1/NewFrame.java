import javax.swing.*;
import java.awt.*;
 
public class NewFrame extends JFrame {
  public NewFrame() {
    this.setTitle("NewFrame");
    this.setSize(300, 200);
    this.setVisible(true);
  }
 
  public static void main(String[] args) {
    new NewFrame(); // NewFrameオブジェクトを生成します．生成すると，
                    // コンストラクタ内の処理が自動的に実行されます．
  }
}
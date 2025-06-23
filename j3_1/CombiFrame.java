import java.awt.*;
import javax.swing.*;
 
public class CombiFrame extends JFrame {
  public CombiFrame() {
    // JPanelを2つ生成
    JPanel  p1 = new JPanel(), p2 = new JPanel();
    // JPanelに貼り付けるためのJButtonを5つ生成
    JButton b1 = new JButton("button 1");
    JButton b2 = new JButton("button 2");
    JButton b3 = new JButton("button 3");
    JButton b4 = new JButton("button 4");
    JButton b5 = new JButton("button 5");
    // JFrameのCENTERに貼り付けるための複数行の文字入力の部品の
    // JTextAreaを生成
    JTextArea t = new JTextArea(10, 20);
 
    this.setTitle("Panel Combination");
    // 2つのJPanelをそれぞれ，3x1, 2x1のGridLayoutに設定
    p1.setLayout(new GridLayout(3, 1));
    p2.setLayout(new GridLayout(2, 1));
    // ボタンをそれぞれのJPanelに貼付け
    p1.add(b1); p1.add(b2); p1.add(b3);
    p2.add(b4); p2.add(b5);
     
    // JButtonが3つ張り付いたJPanelを左，
    // JButtonが2つ張り付いたJPanelを右に貼り付ける
    this.add(p1, BorderLayout.WEST);
    this.add(p2, BorderLayout.EAST);
    // 真ん中はJTextArea
    this.add(t, BorderLayout.CENTER);
 
    // packはJFrameのサイズを自動設定するメソッド．
    // this.setSize(300, 200); などの代わり
    this.pack(); 
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  public static void main(String[] args) {
    new CombiFrame();
  }
}
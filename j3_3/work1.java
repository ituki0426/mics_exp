import java.awt.*;
import javax.swing.*;
 
class CombiFrame extends JFrame {
  public CombiFrame() {
    // JPanelを2つ生成
    JPanel  p1 = new JPanel(), p2 = new JPanel();
    // JPanelに貼り付けるためのJButtonを5つ生成
    JButton b1 = new JButton("button 1");
    JButton b2 = new JButton("button 2");
    JButton b3 = new JButton("button 3");
    JButton b4 = new JButton("button 4");
    JButton b5 = new JButton("button 5");
    JButton b6 = new JButton("button 6");
    JButton b7 = new JButton("button 7");
    JButton b8 = new JButton("button 8");
    JButton b9 = new JButton("button 9");       
    JButton b10 = new JButton("button 10");
    JButton b11 = new JButton("button 11");
    JButton b12 = new JButton("button 12");
    JButton b13 = new JButton("button 13");
    JButton b14 = new JButton("button 14");
    JButton b15 = new JButton("button 15");

    this.setTitle("Panel Combination");
    // 2つのJPanelをそれぞれ，3x1, 2x1のGridLayoutに設定
    p1.setLayout(new GridLayout(1, 10));
    p2.setLayout(new GridLayout(5, 1));
    // ボタンをそれぞれのJPanelに貼付け
    p1.add(b1);
    p1.add(b2);
    p1.add(b3);
    p1.add(b4);
    p1.add(b5);
    p1.add(b6);
    p1.add(b7);
    p1.add(b8);
    p1.add(b9);
    p1.add(b10);
    p2.add(b11);
    p2.add(b12);
    p2.add(b13);
    p2.add(b14);
    p2.add(b15); 
    // JButtonが3つ張り付いたJPanelを左，
    // JButtonが2つ張り付いたJPanelを右に貼り付ける
    this.add(p1, BorderLayout.NORTH);
    this.add(p2, BorderLayout.WEST);
 
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
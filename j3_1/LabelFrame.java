import javax.swing.*;
import java.awt.*;
 
public class LabelFrame extends JFrame {
  public LabelFrame() {
    this.setSize(300, 200);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton b = new JButton("OK");
    this.add(b, BorderLayout.SOUTH);
    JLabel l = new JLabel("JLabel");
    this.add(l, BorderLayout.CENTER);
    this.setVisible(true);
  }
 
  public static void main(String[] args) {
    new LabelFrame();
  }
}
import javax.swing.*;
import java.awt.*;
 
class ButtonFrame extends JFrame {
  public ButtonFrame() {
    this.setSize(300, 200);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton b_NORTH = new JButton("NORTH");
    this.add(b_NORTH, BorderLayout.NORTH);
    JButton b_WEST = new JButton("WEST");
    this.add(b_WEST, BorderLayout.WEST);
    JButton b_EAST = new JButton("EAST");
    this.add(b_EAST, BorderLayout.EAST);
    JButton b_CENTER = new JButton("CENTER");
    this.add(b_CENTER, BorderLayout.CENTER);
    JButton b_SOUTH = new JButton("SOUTH");
    this.add(b_SOUTH, BorderLayout.SOUTH);
    this.setVisible(true);
  }
 
  public static void main(String[] args) {
    new ButtonFrame();
  }
}
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class ViewPanel extends JPanel implements Observer {
    protected DrawModel model;
    JComboBox<String> comboBox;
    public ViewPanel(DrawModel m, DrawController c,ComboBoxController cb) {
      comboBox = new JComboBox<>();
      comboBox.addActionListener(cb);
      comboBox.addItem("red");
      comboBox.addItem("green");
      comboBox.addItem("blue");
      this.add(comboBox);
      this.setBackground(Color.white);
      this.addMouseListener(c);
      this.addMouseMotionListener(c);
      model = m;
      model.addObserver(this);
    }
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      ArrayList<Figure> fig = model.getFigures();
      for(int i = 0; i < fig.size(); i++) {
        Figure f = fig.get(i);
        f.draw(g);
      }
    }
    public void update(Observable o, Object arg) {
      repaint();
    }
  }
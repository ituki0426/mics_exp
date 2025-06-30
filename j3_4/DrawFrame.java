import java.awt.*;
import javax.swing.*;

public class DrawFrame extends JFrame {
    DrawModel model;
    ViewPanel view;
    DrawController cont;
    ComboBoxController cb;
    public DrawFrame() {
      model = new DrawModel();
      cb =  new ComboBoxController(model);
      cont = new DrawController(model);
      view = new ViewPanel(model, cont, cb);
      this.setBackground(Color.black);
      this.setTitle("Draw Editor");
      this.setSize(500, 500);
      this.add(view);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);
    }
    public static void main(String[] args) {
      new DrawFrame();
    }
  }
  
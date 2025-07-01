import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;


class ComboBoxController implements ActionListener{
   private DrawModel model;
   public ComboBoxController(DrawModel a) {
       model = a;
   }
   @Override
   public void actionPerformed(ActionEvent e) {
       JComboBox cb = (JComboBox) e.getSource();
       String selected = (String) cb.getSelectedItem();
       Color color = Color.RED;
       String mode = "draw";
       switch (selected) {
           case "draw":
               mode = "draw";
               break;
           case "choose":
               mode = "choose";
               break;
           case "red":
               color = Color.RED;
               break;
           case "green":
               color = Color.GREEN;
               break;
           case "blue":
               color = Color.BLUE;
               break;
       }
       model.setColor(color);
       model.setMode(mode);
   }
}


class DrawController implements MouseListener, MouseMotionListener {
   protected DrawModel model;
   protected int dragStartX,dragStartY;
   public DrawController(DrawModel a) {
     model = a;
   }
   public void mouseClicked(MouseEvent e) {}
   public void mousePressed(MouseEvent e) {
       int x = e.getX();
       int y = e.getY();
       if (model.getMode().equals("draw")) {
           dragStartX = x;
           dragStartY = y;
           model.createFigure(x, y);
       } else if (model.getMode().equals("choose")) {
           model.chooseFigure(x, y);
       }
   }
   public void mouseDragged(MouseEvent e) {
    if (model.getMode().equals("draw")) {
        model.reshapeFigure(dragStartX, dragStartY, e.getX(), e.getY());
    } else if (model.getMode().equals("choose")) {
        int dx = e.getX() - dragStartX;
        int dy = e.getY() - dragStartY;
        model.moveSelectedFigures(dx, dy);
        // dragStartX/Y を更新
        dragStartX = e.getX();
        dragStartY = e.getY();
    }
  }

   public void mouseReleased(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseMoved(MouseEvent e) {}
}

class ButtonController implements ActionListener {
  private DrawModel model;
  private int dx, dy;
  private String type; // "move" or "resize"

  public ButtonController(DrawModel model, int dx, int dy, String type) {
      this.model = model;
      this.dx = dx;
      this.dy = dy;
      this.type = type;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
      if (type.equals("move")) {
          model.moveSelectedFigures(dx, dy);
      } else if (type.equals("resize")) {
          model.resizeSelectedFigures(dx, dy);
      }
  }
}



class DrawModel extends Observable {
   protected ArrayList<Figure> fig;
   protected Figure drawingFigure;
   protected Color currentColor;
   protected String mode;
 
   public DrawModel() {
     fig = new ArrayList<Figure>();
     drawingFigure = null;
     mode = "draw";
     currentColor = Color.red;  // 色はとりあえず赤で固定
   }

   public void resizeSelectedFigures(int dw, int dh) {
    for (Figure f : fig) {
        if (f.isSelected()) {
            f.resize(dw, dh);
        }
    }
    setChanged();
    notifyObservers();
  }

    public void moveSelectedFigures(int dx, int dy) {
    for (Figure f : fig) {
        if (f.isSelected()) {
            f.translate(dx, dy);
        }
    }
    setChanged();
    notifyObservers();
  }

   public void setMode(String m){
       mode = m;
   }
   public String getMode() {
       return mode;
   }
   public void setColor(Color color){
       currentColor = color;
   }
   public ArrayList<Figure> getFigures() {
       return fig;
     }
   public Figure getFigure(int idx) {
       setChanged();
       notifyObservers();
       return fig.get(idx);
   }
   public void chooseFigure(int x, int y) {
       for (Figure f : fig) {
           boolean selected = f.contains(x, y);
           f.setSelected(selected);
       }
       setChanged();
       notifyObservers();
   }
   public void createFigure(int x,int y) {
       Figure f = new RectangleFigure(x, y, 0, 0, currentColor);
       fig.add(f);
       drawingFigure = f;
       setChanged();
       notifyObservers();
     }
     
     public void reshapeFigure(int x1, int y1, int x2, int y2) {
       if (drawingFigure != null) {
         drawingFigure.reshape(x1, y1, x2, y2);
         setChanged();
         notifyObservers();
       }
     }
}


class Figure {
   public int x, y, width, height;
   protected Color color;
   protected boolean selected = false;
   public Figure(int x, int y, int w, int h, Color c) {
       this.x = x; this.y = y;
       width = w; height = h;
       color = c;
   }
   public void translate(int dx, int dy) {
    this.x += dx;
    this.y += dy;
  }

   public void setSelected(boolean sel) {
       selected = sel;
   }
   public boolean isSelected() {
       return selected;
   }
   public boolean contains(int px, int py) {
       return (px >= x && px <= x + width &&
               py >= y && py <= y + height);
   }
   public void setColor(Color color){
       this.color = color;
   }
   public void setSize(int w, int h) {
       width = w; height = h;
   }
   public void resize(int dw, int dh) {
    width = Math.max(1, width + dw);
    height = Math.max(1, height + dh);
}

   public void setLocation(int x, int y) {
       this.x = x; this.y = y;
   }
   public void reshape(int x1, int y1, int x2, int y2) {
       int newx = Math.min(x1, x2);
       int newy = Math.min(y1, y2);
       int neww = Math.abs(x1 - x2);
       int newh = Math.abs(y1 - y2);
       setLocation(newx, newy);
       setSize(neww, newh);
   }
   public void draw(Graphics g) {}  // 実装はサブクラスに任せる
}
class RectangleFigure extends Figure {
   public RectangleFigure(int x, int y, int w, int h, Color c) {
       super(x, y, w, h, c);
   }
   public void draw(Graphics g) {
       g.setColor(color);
       g.drawRect(x, y, width, height);
       if (selected) {
           Graphics2D g2 = (Graphics2D) g;
           Stroke oldStroke = g2.getStroke();
           g2.setColor(Color.YELLOW);
           g2.setStroke(new BasicStroke(4));
           g2.drawRect(x - 4, y - 4, width + 8, height + 8);
           g2.setStroke(oldStroke);
       }
   }
}




class ViewPanel extends JPanel implements Observer {
   protected DrawModel model;
   JComboBox<String> comboBox_col;
   JComboBox<String> comboBox_mode;
   public ViewPanel(DrawModel m,DrawController c,ComboBoxController cb) {
     comboBox_col = new JComboBox<>();
     comboBox_mode = new JComboBox<>();
     comboBox_mode.addActionListener(cb);
     comboBox_col.addActionListener(cb);
     comboBox_mode.addItem("draw");
     comboBox_mode.addItem("choose");
     comboBox_col.addItem("red");
     comboBox_col.addItem("green");
     comboBox_col.addItem("blue");

     JButton upBtn = new JButton("↑");
     JButton downBtn = new JButton("↓");
     JButton leftBtn = new JButton("←");
     JButton rightBtn = new JButton("→");
     JButton zoomInBtn = new JButton("+");
     JButton zoomOutBtn = new JButton("-");
     upBtn.addActionListener(new ButtonController(m, 0, -10, "move"));
     downBtn.addActionListener(new ButtonController(m, 0, 10,"move"));
     leftBtn.addActionListener(new ButtonController(m, -10, 0,"move"));
     rightBtn.addActionListener(new ButtonController(m, 10, 0,"move"));
     zoomInBtn.addActionListener(new ButtonController(m, 10, 10, "resize"));
     zoomOutBtn.addActionListener(new ButtonController(m, -10, -10, "resize"));
     JPanel buttonPanel = new JPanel(new GridLayout(2, 3));
     JPanel resizePanel = new JPanel();
     resizePanel.add(zoomInBtn);
     resizePanel.add(zoomOutBtn);
     buttonPanel.add(new JLabel());  // 空白
     buttonPanel.add(upBtn);
     buttonPanel.add(new JLabel());  // 空白
     buttonPanel.add(leftBtn);
     buttonPanel.add(downBtn);
     buttonPanel.add(rightBtn);
     this.add(resizePanel, BorderLayout.NORTH);
     this.add(buttonPanel, BorderLayout.SOUTH);
     this.add(comboBox_col);
     this.add(comboBox_mode);
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


 public class DrawFrame extends JFrame {
   DrawModel model;
   ViewPanel view_panel;
   DrawController cont;
   ComboBoxController cb;


   public DrawFrame() {
       model = new DrawModel();
       cb = new ComboBoxController(model);
       cont = new DrawController(model);
       view_panel = new ViewPanel(model, cont, cb);
       this.setBackground(Color.black);
       this.setTitle("Draw Editor");
       this.setSize(500, 500);
       this.add(view_panel);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setVisible(true);
   }
   public static void main(String[] args) {
       new DrawFrame();
   }
}

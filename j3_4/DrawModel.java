import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DrawModel extends Observable {
    protected ArrayList<Figure> fig;
    protected Figure drawingFigure;
    protected Color currentColor;
   
    public DrawModel() {
      fig = new ArrayList<Figure>();
      drawingFigure = null;
      currentColor = Color.red;  // 色はとりあえず赤で固定
    }
    public void setColor(Color color){
        currentColor = color;
    }
    public ArrayList<Figure> getFigures() {
        return fig;
      }
    public Figure getFigure(int idx) {
        return fig.get(idx);
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
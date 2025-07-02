
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class OperationPanel extends JPanel {

    public OperationPanel(DrawModel model, ComboBoxController cb) {
        this.setLayout(new BorderLayout());
        //CombBoxの描画（画面上部）
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // ← ここを修正（行:3, 列:1, 間隔:5px）
        JComboBox<String> comboBox_shape = new JComboBox<>(
                new String[]{"rectangle", "filled_rectangle", "circle", "filled_circle", "triangle", "filled_triangle", "line"});
        JComboBox<String> comboBox_col = new JComboBox<>(new String[]{"red", "green", "blue"});
        JComboBox<String> comboBox_mode = new JComboBox<>(new String[]{"draw", "choose"});

        comboBox_col.addActionListener(cb);
        comboBox_mode.addActionListener(cb);
        comboBox_shape.addActionListener(cb);

        controlPanel.add(comboBox_shape);
        controlPanel.add(comboBox_col);
        controlPanel.add(comboBox_mode);

        // 移動ボタンパネル（中央）
        JPanel movePanel = new JPanel(new GridLayout(2, 3, 5, 5));
        JButton upBtn = new JButton("↑");
        JButton downBtn = new JButton("↓");
        JButton leftBtn = new JButton("←");
        JButton rightBtn = new JButton("→");
        movePanel.add(new JLabel(""));
        movePanel.add(upBtn);
        movePanel.add(new JLabel(""));
        movePanel.add(leftBtn);
        movePanel.add(downBtn);
        movePanel.add(rightBtn);

        upBtn.addActionListener(new ButtonController(model, 0, -10, "move"));
        downBtn.addActionListener(new ButtonController(model, 0, 10, "move"));
        leftBtn.addActionListener(new ButtonController(model, -10, 0, "move"));
        rightBtn.addActionListener(new ButtonController(model, 10, 0, "move"));

        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // 3行1列で縦に並べる
        JButton deleteBtn = new JButton("Delete");
        JButton zoomInBtn = new JButton("+");
        JButton zoomOutBtn = new JButton("-");

        deleteBtn.addActionListener(new ButtonController(model, 0, 0, "delete"));
        zoomInBtn.addActionListener(new ButtonController(model, 10, 10, "resize"));
        zoomOutBtn.addActionListener(new ButtonController(model, -10, -10, "resize"));
        actionPanel.add(deleteBtn);
        actionPanel.add(zoomInBtn);
        actionPanel.add(zoomOutBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(actionPanel, BorderLayout.NORTH);
        centerPanel.add(movePanel, BorderLayout.SOUTH);
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }
}

class DrawPanel extends JPanel implements Observer {

    protected DrawModel model;
    protected DrawController controller;
    private BufferedImage backgroundImage = null;

    public DrawPanel(DrawModel m, DrawController c) {
        this.model = m;
        this.controller = c;
        this.setBackground(Color.white);
        this.addMouseListener(c);
        this.addMouseMotionListener(c);
        model.addObserver(this);
    }

    public void setBackgroundImage(BufferedImage img) {
        this.backgroundImage = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        List<Figure> fig = model.getFigures();
        for (Figure f : fig) {
            f.draw(g);
        }
    }

    public BufferedImage exportImage() {
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        this.paint(g2d); // 描画内容を BufferedImage に描く
        g2d.dispose();
        return image;
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}

class ComboBoxController implements ActionListener {

    private DrawModel model;

    public ComboBoxController(DrawModel a) {
        model = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String selected = (String) cb.getSelectedItem();
// ComboBoxController.java の actionPerformed 内
        switch (selected) {
            case "draw":
            case "choose":
                model.setMode(selected);
                break;
            case "red":
                model.setColor(Color.RED);
                if (model.getMode().equals("choose")) {
                    model.setColorToSelectedFigures(Color.RED);
                }
                break;
            case "green":
                model.setColor(Color.GREEN);
                if (model.getMode().equals("choose")) {
                    model.setColorToSelectedFigures(Color.GREEN);
                }
                break;
            case "blue":
                model.setColor(Color.BLUE);
                if (model.getMode().equals("choose")) {
                    model.setColorToSelectedFigures(Color.BLUE);
                }
                break;
            case "filled_rectangle":
            case "filled_circle":
            case "rectangle":
            case "circle":
            case "triangle":
            case "filled_triangle":
            case "line":
                model.setShapeType(selected);
                break;
        }

    }

}

class DrawController implements MouseListener, MouseMotionListener {

    protected DrawModel model;
    protected int dragStartX, dragStartY;

    public DrawController(DrawModel a) {
        model = a;
    }

    public void mouseClicked(MouseEvent e) {
        if (!model.getMode().equals("choose")) {
            return;
        }
        int x = e.getX();
        int y = e.getY();

        boolean found = false;
        for (Figure f : model.getFigures()) {
            if (f.contains(x, y)) {
                f.setSelected(!f.isSelected()); // トグル
                found = true;
                break;
            }
        }
        if (!found) {
            model.clearSelection(); // 内部で notifyObservers 呼ばれる
        } else {
            model.notifyView();
        }
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (model.getMode().equals("draw")) {
            dragStartX = x;
            dragStartY = y;
            model.createFigure(x, y);
        } else if (model.getMode().equals("choose")) {
            dragStartX = x;
            dragStartY = y;
            // ここでは選択処理はしない（mouseClickedで行う）
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

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}

class ButtonController implements ActionListener {

    private DrawModel model;
    private int dx, dy;
    private String type; // "move"または"resize"

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
        } else if (type.equals("delete")) {
            model.deleteSelectedFigures();
        }
    }

}

class DrawModel extends Observable {

    protected ArrayList<Figure> fig;
    protected Figure drawingFigure;
    protected Color currentColor;
    protected String mode;
    private String shapeType = "rectangle";

    public DrawModel() {
        fig = new ArrayList<Figure>();
        drawingFigure = null;
        mode = "draw";
        currentColor = Color.red; // 色はとりあえず赤で固定
    }

    public void notifyView() {
        setChanged();
        notifyObservers();
    }

    public void setColorToSelectedFigures(Color c) {
        for (Figure f : fig) {
            if (f.isSelected()) {
                f.setColor(c);
            }
        }
        setChanged();
        notifyObservers();
    }

    public void clearSelection() {
        for (Figure f : fig) {
            f.setSelected(false);
        }
        setChanged();
        notifyObservers();
    }

    public void setShapeType(String type) {
        this.shapeType = type;
    }

    public String getShapeType() {
        return this.shapeType;
    }

    public void deleteSelectedFigures() {
        fig.removeIf(Figure::isSelected);
        setChanged();
        notifyObservers();
    }

    public void createFigure(int x, int y) {
        Figure f;
        switch (shapeType) {
            case "circle":
                f = new CircleFigure(x, y, 0, 0, currentColor, false);
                break;
            case "filled_circle":
                f = new CircleFigure(x, y, 0, 0, currentColor, true);
                break;
            case "filled_rectangle":
                f = new RectangleFigure(x, y, 0, 0, currentColor, true);
                break;
            case "triangle":
                f = new TriangleFigure(x, y, 0, 0, currentColor, false);
                break;
            case "filled_triangle":
                f = new TriangleFigure(x, y, 0, 0, currentColor, true);
                break;
            case "line":
                f = new LineFigure(x, y, 0, 0, currentColor, false);
                break;
            default:
                f = new RectangleFigure(x, y, 0, 0, currentColor, false);
        }
        fig.add(f);
        drawingFigure = f;
        setChanged();
        notifyObservers();
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

    public void setMode(String m) {
        mode = m;
    }

    public String getMode() {
        return mode;
    }

    public void setColor(Color color) {
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

    public void reshapeFigure(int x1, int y1, int x2, int y2) {
        if (drawingFigure != null) {
            drawingFigure.reshape(x1, y1, x2, y2);
            setChanged();
            notifyObservers();
        }
    }
}

class Figure implements Serializable {

    private static final long serialVersionUID = 1L;
    protected boolean filled;
    public int x, y, width, height;
    protected Color color;
    protected boolean selected = false;

    public Figure(int x, int y, int w, int h, Color c, boolean filled) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.filled = filled;
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
        return (px >= x && px <= x + width
                && py >= y && py <= y + height);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }

    public void resize(int dw, int dh) {
        width = Math.max(1, width + dw);
        height = Math.max(1, height + dh);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void reshape(int x1, int y1, int x2, int y2) {
        int newx = Math.min(x1, x2);
        int newy = Math.min(y1, y2);
        int neww = Math.abs(x1 - x2);
        int newh = Math.abs(y1 - y2);
        setLocation(newx, newy);
        setSize(neww, newh);
    }

    public void draw(Graphics g) {
    } // 実装はサブクラスに任せる
}

class CircleFigure extends Figure {

    private static final long serialVersionUID = 1L;

    public CircleFigure(int x, int y, int w, int h, Color c, boolean filled) {
        super(x, y, w, h, c, filled);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        if (filled) {
            g.fillOval(x, y, width, height);
        } else {
            g.drawOval(x, y, width, height);
        }
        if (selected) {
            Graphics2D g2 = (Graphics2D) g;
            Stroke oldStroke = g2.getStroke();
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(x - 4, y - 4, width + 8, height + 8);
            g2.setStroke(oldStroke);
        }
    }

    @Override
    public boolean contains(int px, int py) {
        double cx = x + width / 2.0;
        double cy = y + height / 2.0;
        double rx = width / 2.0;
        double ry = height / 2.0;
        if (rx <= 0 || ry <= 0) {
            return false;
        }
        double dx = px - cx;
        double dy = py - cy;
        return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1.0;
    }
}

class TriangleFigure extends Figure {

    private static final long serialVersionUID = 1L;

    public TriangleFigure(int x, int y, int w, int h, Color c, boolean filled) {
        super(x, y, w, h, c, filled);
    }

    @Override
    public void draw(Graphics g) {
        int[] xs = {x + width / 2, x, x + width};
        int[] ys = {y, y + height, y + height};
        g.setColor(color);
        if (filled) {
            g.fillPolygon(xs, ys, 3);
        } else {
            g.drawPolygon(xs, ys, 3);
        }
        if (selected) {
            Graphics2D g2 = (Graphics2D) g;
            Stroke oldStroke = g2.getStroke();
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(4));
            g2.drawPolygon(xs, ys, 3);
            g2.setStroke(oldStroke);
        }
    }

    @Override
    public boolean contains(int px, int py) {
        Polygon triangle = new Polygon(
                new int[]{x + width / 2, x, x + width},
                new int[]{y, y + height, y + height},
                3
        );
        return triangle.contains(px, py);
    }
}

class LineFigure extends Figure {

    private static final long serialVersionUID = 1L;

    public LineFigure(int x, int y, int w, int h, Color c, boolean filled) {
        super(x, y, w, h, c, false); // filled は無視
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(x, y, x + width, y + height);
        if (selected) {
            Graphics2D g2 = (Graphics2D) g;
            Stroke oldStroke = g2.getStroke();
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(x, y, x + width, y + height);
            g2.setStroke(oldStroke);
        }
    }

    @Override
    public boolean contains(int px, int py) {
        double x1 = x, y1 = y;
        double x2 = x + width, y2 = y + height;
        double distance = ptLineDist(x1, y1, x2, y2, px, py);
        return distance <= 5.0; // 選択判定：5ピクセル以内
    }

    private double ptLineDist(double x1, double y1, double x2, double y2, double px, double py) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lenSq = dx * dx + dy * dy;
        if (lenSq == 0) {
            return Math.hypot(px - x1, py - y1);
        }
        double t = ((px - x1) * dx + (py - y1) * dy) / lenSq;
        t = Math.max(0, Math.min(1, t));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        return Math.hypot(px - projX, py - projY);
    }
}

class RectangleFigure extends Figure {

    private static final long serialVersionUID = 1L;

    public RectangleFigure(int x, int y, int w, int h, Color c, boolean filled) {
        super(x, y, w, h, c, filled);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        if (filled) {
            g.fillRect(x, y, width, height);
        } else {
            g.drawRect(x, y, width, height);
        }
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

public class DrawFrame extends JFrame {

    DrawPanel draw_panel;
    OperationPanel operation_panel;
    DrawModel model;
    DrawController cont;
    ComboBoxController cb;
    JFileChooser chooser = new JFileChooser();
    JMenuBar mb = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem exitItem = new JMenuItem("Exit");

    public DrawFrame() {
        super("Simple File Chooser Application");
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image (*.jpg, *.jpeg)", "jpg", "jpeg"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (*.png)", "png"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap Image (*.bmp)", "bmp"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Serialized Data (*.ser)", "ser"));
        chooser.setAcceptAllFileFilterUsed(false);
        model = new DrawModel();
        cb = new ComboBoxController(model);
        cont = new DrawController(model);
        operation_panel = new OperationPanel(model, cb);
        draw_panel = new DrawPanel(model, cont);
        this.setLayout(new BorderLayout()); // ★レイアウトを設定（必須）
        this.add(draw_panel, BorderLayout.CENTER);
        this.add(operation_panel, BorderLayout.EAST);
        this.setBackground(Color.black);
        this.setTitle("Draw Editor");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        mb.add(fileMenu);
        setJMenuBar(mb);
        setSize(300, 300);
        openItem.addActionListener(e -> {
            int state = chooser.showOpenDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        draw_panel.setBackgroundImage(img);
                        System.out.println("Loaded image: " + file.getPath());
                    } else {
                        JOptionPane.showMessageDialog(null, "画像の読み込みに失敗しました。");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "画像の読み込み中にエラーが発生しました。");
                }
            } else if (state == JFileChooser.CANCEL_OPTION) {
                System.out.println("canceled");
            } else if (state == JFileChooser.ERROR_OPTION) {
                System.out.println("error");
            }
        });

        saveItem.addActionListener(e -> {
            int state = chooser.showSaveDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String filename = file.getName().toLowerCase();
                String selectedExt = "png"; // デフォルト
                javax.swing.filechooser.FileFilter selectedFilter = chooser.getFileFilter();

                if (selectedFilter instanceof FileNameExtensionFilter) {
                    String[] exts = ((FileNameExtensionFilter) selectedFilter).getExtensions();
                    if (exts.length > 0) {
                        selectedExt = exts[0].toLowerCase(); // ex: "ser", "jpg"
                    }
                }

                // ファイル名に拡張子が含まれていない場合、自動で追加
                if (!filename.endsWith("." + selectedExt)) {
                    file = new File(file.getAbsolutePath() + "." + selectedExt);
                    filename = file.getName().toLowerCase(); // 更新
                }

                try {
                    if (filename.endsWith(".png") || filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg") || filename.endsWith(".bmp")) {
                        BufferedImage img = draw_panel.exportImage();
                        String format = filename.substring(filename.lastIndexOf('.') + 1);
                        ImageIO.write(img, format, file);
                        System.out.println("画像を保存しました: " + file.getPath());
                    } else if (filename.endsWith(".ser")) {
                        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                            oos.writeObject(model.getFigures());
                            System.out.println("図形データを保存しました: " + file.getPath());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "未対応のファイル形式です（.png, .jpg, .ser など）");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "保存中にエラーが発生しました。");
                }
            }
        });

        openItem.addActionListener(e -> {
            int state = chooser.showOpenDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String filename = file.getName().toLowerCase();

                try {
                    if (filename.endsWith(".png") || filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg") || filename.endsWith(".bmp")) {
                        BufferedImage img = ImageIO.read(file);
                        if (img != null) {
                            draw_panel.setBackgroundImage(img);
                            System.out.println("画像を読み込みました: " + file.getPath());
                        } else {
                            JOptionPane.showMessageDialog(null, "画像の読み込みに失敗しました。");
                        }
                    } else if (filename.endsWith(".ser")) {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                            ArrayList<Figure> loaded = (ArrayList<Figure>) ois.readObject();
                            model.getFigures().clear();
                            model.getFigures().addAll(loaded);
                            model.notifyView();
                            System.out.println("図形データを読み込みました: " + file.getPath());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "未対応のファイル形式です（.png, .jpg, .ser など）");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "読み込み中にエラーが発生しました。");
                }
            }
        });

        exitItem.addActionListener(e -> System.exit(0));
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK);
        exitItem.setAccelerator(ks);
        // Menuが開いていなくても，ALT+X で exit が選択される．
        fileMenu.setMnemonic('F'); // ALT+F で fileMenuが選択出来る．
        exitItem.setMnemonic(KeyEvent.VK_X);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawFrame f = new DrawFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }

}

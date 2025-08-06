
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class OperationPanel extends JPanel {

    public OperationPanel(DrawModel model, ComboBoxController cb, ButtonController bb) {
        this.setLayout(new BorderLayout());
        //CombBoxの描画（画面上部）
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // ← ここを修正（行:3, 列:1, 間隔:5px）
        // コンボボックスの作成
        // 形状選択用
        JComboBox<String> comboBox_shape = new JComboBox<>(
                new String[]{"rectangle", "filled_rectangle", "circle", "filled_circle", "triangle", "filled_triangle", "line"});
        // 色選択用
        JComboBox<String> comboBox_col = new JComboBox<>(new String[]{"red", "green", "blue"});
        // モード選択用
        JComboBox<String> comboBox_mode = new JComboBox<>(new String[]{"draw", "choose"});
        // コンボボックスの初期選択値
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
        // ボタンのアクションリスナーを設定
        upBtn.addActionListener(bb);
        downBtn.addActionListener(bb);
        leftBtn.addActionListener(bb);
        rightBtn.addActionListener(bb);
        // ボタンをパネルに追加
        movePanel.add(new JLabel(""));
        movePanel.add(upBtn);
        movePanel.add(new JLabel(""));
        movePanel.add(leftBtn);
        movePanel.add(downBtn);
        movePanel.add(rightBtn);
        // ボタンの配置を調整
        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // 3行1列で縦に並べる
        JButton deleteBtn = new JButton("Delete");
        JButton zoomInBtn = new JButton("+");
        JButton zoomOutBtn = new JButton("-");
        // ボタンのアクションリスナーを設定
        deleteBtn.addActionListener(bb);
        zoomInBtn.addActionListener(bb);
        zoomOutBtn.addActionListener(bb);
        actionPanel.add(deleteBtn);
        actionPanel.add(zoomInBtn);
        actionPanel.add(zoomOutBtn);
        // 中央パネルにアクションパネルと移動パネルを配置
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(actionPanel, BorderLayout.NORTH);
        centerPanel.add(movePanel, BorderLayout.SOUTH);
        // 全体のパネルにコントロールパネルと中央パネルを配置
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }
}

class DrawPanel extends JPanel implements Observer {

    // DrawPanelクラスはJPanelを継承し、図形の描画を行う
    // Observerインターフェースを実装し、DrawModelの変更を監視する
    protected DrawModel model; // DrawModelのインスタンスを保持
    protected DrawController controller; // DrawControllerのインスタンスを保持
    private BufferedImage backgroundImage = null; // 背景画像を保持

    public DrawPanel(DrawModel m, DrawController c) {
        // コンストラクタでDrawModelとDrawControllerのインスタンスを受け取る
        this.model = m;
        // DrawControllerのインスタンスを設定
        this.controller = c;
        // パネルの設定
        this.setBackground(Color.white);
        // パネルのサイズを設定
        this.addMouseListener(c);
        // マウスドラッグイベントを処理するためにMouseMotionListenerを追加
        this.addMouseMotionListener(c);
        // DrawModelの変更を監視するためにObserverを追加
        model.addObserver(this);
    }

    public void setBackgroundImage(BufferedImage img) {
        // 背景画像を設定するメソッド
        // 引数として渡されたBufferedImageを背景画像として設定
        this.backgroundImage = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // paintComponentメソッドはJPanelの描画を行う
        // 背景をクリア
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 背景画像が設定されている場合、背景画像を描画
            // 画像をパネルのサイズに合わせて描画
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        // DrawModelから図形のリストを取得し、各図形を描画
        List<Figure> fig = model.getFigures();
        for (Figure f : fig) {
            // 各図形のdrawメソッドを呼び出して描画
            f.draw(g);
        }
    }

    public BufferedImage exportImage() {
        // 現在の描画内容をBufferedImageとしてエクスポートするメソッド
        // パネルのサイズに合わせたBufferedImageを作成
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Graphics2Dオブジェクトを取得
        Graphics2D g2d = image.createGraphics();
        this.paint(g2d); // 描画内容を BufferedImage に描く
        // 描画後のGraphics2Dオブジェクトを解放
        g2d.dispose();
        return image;// エクスポートされた画像を返す
    }

    @Override
    public void update(Observable o, Object arg) {
        // DrawModelの変更を受け取ったときに呼び出されるメソッド
        // ここでは再描画を行う
        repaint();
    }
}

class ComboBoxController implements ActionListener {

    private DrawModel model; // DrawModelのインスタンスを保持

    public ComboBoxController(DrawModel a) {
        // コンストラクタでDrawModelのインスタンスを受け取る
        model = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // コンボボックスの選択イベントを処理するメソッド
        JComboBox cb = (JComboBox) e.getSource();
        String selected = (String) cb.getSelectedItem(); // 選択されたアイテムを取得
        switch (selected) {
            // モードの変更
            case "draw":
            case "choose":
                model.setMode(selected);
                break;
            // 色の変更
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
            // 図形の種類の変更
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

    // DrawModelのインスタンスを保持
    protected DrawModel model;
    // ドラッグ開始位置を記録するための変数
    protected int dragStartX, dragStartY;

    public DrawController(DrawModel a) {
        // コンストラクタでDrawModelのインスタンスを受け取る
        model = a;
    }

    // マウスイベントのハンドラ
    public void mouseClicked(MouseEvent e) {
        // モードが "choose" の場合、クリックした位置にある図形を選択する
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
        // マウスが押されたときの処理
        // モードが "draw" または "choose" の場合、ドラッグ開始位置を記録
        int x = e.getX(); // マウスのX座標を取得
        int y = e.getY(); // マウスのY座標を取得

        if (model.getMode().equals("draw")) {
            // ドラッグ開始位置を記録
            // ドラッグ開始位置を記録し、図形を作成
            dragStartX = x;
            dragStartY = y;
            model.createFigure(x, y);
        } else if (model.getMode().equals("choose")) {
            // ドラッグ開始位置を記録
            // ドラッグ開始位置を記録し、選択処理は mouseClicked で行う
            dragStartX = x;
            dragStartY = y;
            // ここでは選択処理はしない（mouseClickedで行う）
        }
    }

    // ドラッグ中の処理
    public void mouseDragged(MouseEvent e) {
        if (model.getMode().equals("draw")) {
            // ドラッグ中の図形を更新
            // ドラッグ開始位置と現在の位置を使って図形を再描画
            model.reshapeFigure(dragStartX, dragStartY, e.getX(), e.getY());
        } else if (model.getMode().equals("choose")) {
            // ドラッグ中の選択された図形を移動
            // ドラッグ開始位置からの相対移動量を計算
            int dx = e.getX() - dragStartX;
            int dy = e.getY() - dragStartY;
            // 選択された図形を移動
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

    private DrawModel model; // DrawModelのインスタンスを保持

    public ButtonController(DrawModel a) {
        //  コンストラクタでDrawModelのインスタンスを受け取る
        model = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ボタンのクリックイベントを処理するメソッド
        // クリックされたボタンを取得
        JButton clickedButton = (JButton) e.getSource();
        // ボタンのテキストを取得
        String buttonText = clickedButton.getText();
        if (buttonText == "+") {
            // "+" ボタンがクリックされた場合、選択された図形を拡大
            model.resizeSelectedFigures(10, 10);
        } else if (buttonText == "-") {
            // "-" ボタンがクリックされた場合、選択された図形を縮小
            model.resizeSelectedFigures(-10, -10);
        } else if (buttonText == "Delete") {
            // "Delete" ボタンがクリックされた場合、選択された図形を削除
            model.deleteSelectedFigures();
        } else if (buttonText == "↑") {
            // "↑" ボタンがクリックされた場合、選択された図形を上に移動
            model.moveSelectedFigures(0, -10);
        } else if (buttonText == "↓") {
            // "↓" ボタンがクリックされた場合、選択された図形を下に移動
            model.moveSelectedFigures(0, 10);
        } else if (buttonText == "←") {
            // "←" ボタンがクリックされた場合、選択された図形を左に移動
            model.moveSelectedFigures(-10, 0);
        } else if (buttonText == "→") {
            // "→" ボタンがクリックされた場合、選択された図形を右に移動
            model.moveSelectedFigures(10, 0);
        }
    }

}

class DrawModel extends Observable {

    protected ArrayList<Figure> fig; // 図形のリストを保持
    protected Figure drawingFigure; // 現在描画中の図形を保持
    protected Color currentColor; //    現在の色を保持
    protected String mode; // 現在のモード（"draw" または "choose"）を保持
    private String shapeType = "rectangle"; // 現在の図形の種類を保持

    public DrawModel() {
        // DrawModelのコンストラクタ
        fig = new ArrayList<Figure>(); // 図形のリストを初期化
        drawingFigure = null; // 現在描画中の図形は初期状態ではなし
        mode = "draw"; //   現在のモードを "draw" に設定
        currentColor = Color.red; // 色はとりあえず赤で固定
    }

    public void notifyView() {
        // DrawModelの状態が変更されたことを通知するメソッド
        // 変更があったことを通知するために setChanged() を呼び出す 
        setChanged();
        notifyObservers();
    }

    public void setColorToSelectedFigures(Color c) {
        // 選択された図形の色を変更するメソッド
        for (Figure f : fig) {
            // 選択されている図形に対して色を設定
            if (f.isSelected()) {
                // 図形の色を変更
                f.setColor(c);
            }
        }
        // 変更があったことを通知するために setChanged() を呼び出す
        setChanged();
        // 変更を通知
        notifyObservers();
    }

    public void clearSelection() {
        // 選択されている図形の選択状態を解除するメソッド
        for (Figure f : fig) {
            // 選択されている図形の選択状態を解除
            f.setSelected(false);
        }
        // 変更があったことを通知するために setChanged() を呼び出す
        setChanged();
        notifyObservers();
    }

    public void setShapeType(String type) {
        // 現在の図形の種類を設定するメソッド
        this.shapeType = type;
    }

    public String getShapeType() {
        // 現在の図形の種類を取得するメソッド
        return this.shapeType;
    }

    public void deleteSelectedFigures() {
        // 選択されている図形を削除するメソッド
        fig.removeIf(Figure::isSelected);
        // 選択されている図形を削除
        setChanged();
        notifyObservers();
    }

    public void createFigure(int x, int y) {
        Figure f; // 新しい図形を作成するメソッド
        switch (shapeType) {
            // 現在の図形の種類に応じて図形を作成
            case "circle":
                f = new CircleFigure(x, y, 0, 0, currentColor, false);
                break;
            // 円
            case "filled_circle":
                f = new CircleFigure(x, y, 0, 0, currentColor, true);
                break;
            // 塗りつぶしの円
            case "filled_rectangle":
                f = new RectangleFigure(x, y, 0, 0, currentColor, true);
                break;
            // 塗りつぶしの四角形
            case "triangle":
                f = new TriangleFigure(x, y, 0, 0, currentColor, false);
                break;
            // 塗りつぶしの三角形
            case "filled_triangle":
                f = new TriangleFigure(x, y, 0, 0, currentColor, true);
                break;
            case "line":
                // 線分は幅と高さを0にして描画
                f = new LineFigure(x, y, 0, 0, currentColor, false);
                break;
            default:
                // デフォルトは四角形
                f = new RectangleFigure(x, y, 0, 0, currentColor, false);
        }
        // 新しい図形を作成し、リストに追加
        fig.add(f);
        drawingFigure = f;
        // 図形をリストに追加
        setChanged();
        // 変更があったことを通知するために setChanged() を呼び出す
        notifyObservers();
    }

    public void resizeSelectedFigures(int dw, int dh) {
        //  選択されている図形のサイズを変更するメソッド
        for (Figure f : fig) {
            // 選択されている図形に対してサイズを変更
            if (f.isSelected()) {
                // 図形のサイズを変更
                f.resize(dw, dh);
            }
        }
        // 変更があったことを通知するために setChanged() を呼び出す
        setChanged();
        // 変更を通知
        notifyObservers();
    }

    public void moveSelectedFigures(int dx, int dy) {
        // 選択されている図形を移動するメソッド
        for (Figure f : fig) {
            if (f.isSelected()) {
                // 選択されている図形に対して移動を行う
                f.translate(dx, dy);
            }
        }
        // 変更があったことを通知するために setChanged() を呼び出す
        setChanged();
        // 変更を通知
        notifyObservers();
    }

    public void setMode(String m) {
        // 現在のモードを設定するメソッド
        mode = m;
    }

    public String getMode() {
        // 現在のモードを取得するメソッド
        return mode;
    }

    public void setColor(Color color) {
        // 現在の色を設定するメソッド
        currentColor = color;
    }

    public ArrayList<Figure> getFigures() {
        // 現在の図形のリストを取得するメソッド
        return fig;
    }

    public Figure getFigure(int idx) {
        // 指定されたインデックスの図形を取得するメソッド
        setChanged();
        // 変更があったことを通知するために setChanged() を呼び出す
        notifyObservers();
        // インデックスが範囲外の場合はnullを返す
        return fig.get(idx);
    }

    public void chooseFigure(int x, int y) {
        // マウスクリック位置にある図形を選択するメソッド
        for (Figure f : fig) {
            // 各図形に対してクリック位置が含まれているかをチェック
            boolean selected = f.contains(x, y);
            // 図形がクリック位置を含む場合、選択状態を更新
            f.setSelected(selected);
        }
        // 変更があったことを通知するために setChanged() を呼び出す
        setChanged();
        // 変更を通知
        notifyObservers();
    }

    public void reshapeFigure(int x1, int y1, int x2, int y2) {
        // 現在描画中の図形のサイズと位置を更新するメソッド
        if (drawingFigure != null) {
            // 現在描画中の図形が存在する場合、その図形の位置とサイズを更新
            drawingFigure.reshape(x1, y1, x2, y2);
            // 変更があったことを通知するために setChanged() を呼び出す
            setChanged();
            // 変更を通知
            notifyObservers();
        }
    }
}

class Figure implements Serializable {

    private static final long serialVersionUID = 1L; // シリアライズ用のID
    protected boolean filled; // 塗りつぶしの有無
    public int x, y, width, height; // 図形の位置とサイズ
    protected Color color; // 図形の色
    protected boolean selected = false; // 図形が選択されているかどうか

    public Figure(int x, int y, int w, int h, Color c, boolean filled) {
        // Figureクラスのコンストラクタ
        // x, y は左上の座標、w は幅、h は高さ、c は色、filled は塗りつぶしの有無
        // 引数で位置、サイズ、色、塗りつぶしの有無を設定
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.filled = filled;
    }

    public void translate(int dx, int dy) {
        // 図形を指定された量だけ移動するメソッド
        this.x += dx; // x座標を移動
        this.y += dy; //    y座標を移動
    }

    public void setSelected(boolean sel) {
        // 図形の選択状態を設定するメソッド
        selected = sel; // 選択状態を設定
    }

    public boolean isSelected() {
        // 図形が選択されているかどうかを返すメソッド
        return selected; // 選択状態を返す
    }

    public boolean contains(int px, int py) {
        // 図形が指定された座標を含んでいるかどうかを判定するメソッド
        return (px >= x && px <= x + width
                && py >= y && py <= y + height); // 指定された座標が図形の範囲内にあるかをチェック
    }

    public void setColor(Color color) {
        // 図形の色を設定するメソッド
        this.color = color; // 色を設定
    }

    public void setSize(int w, int h) {
        // 図形のサイズを設定するメソッド
        width = w; // 幅を設定
        height = h; // 高さを設定
    }

    public void resize(int dw, int dh) {
        // 図形のサイズを変更するメソッド
        width = Math.max(1, width + dw);
        // 高さを変更
        height = Math.max(1, height + dh);
    }

    public void setLocation(int x, int y) {
        // 図形の位置を設定するメソッド
        this.x = x; // x座標を設定
        this.y = y; // y座標を設定
    }

    public void reshape(int x1, int y1, int x2, int y2) {
        // 図形の位置とサイズを更新するメソッド
        int newx = Math.min(x1, x2); // 左上のx座標を計算
        int newy = Math.min(y1, y2); // 左上のy座標を計算
        int neww = Math.abs(x1 - x2); // 幅を計算
        int newh = Math.abs(y1 - y2); // 高さを計算
        setLocation(newx, newy); // 新しい位置を設定
        setSize(neww, newh); // 新しいサイズを設定
    }

    public void draw(Graphics g) {
    } // 実装はサブクラスに任せる

}

class CircleFigure extends Figure {

    private static final long serialVersionUID = 1L; // CircleFigureクラスはFigureを継承し、円の描画を行う

    public CircleFigure(int x, int y, int w, int h, Color c, boolean filled) {
        // コンストラクタで位置、サイズ、色、塗りつぶしの有無を設定
        super(x, y, w, h, c, filled);
    }

    @Override
    public void draw(Graphics g) {
        // Graphicsオブジェクトを使用して円を描画
        g.setColor(color);
        // x, y は左上の座標、width は幅、height は高さ
        if (filled) {
            // 塗りつぶしの場合、fillOvalを使用
            g.fillOval(x, y, width, height);
        } else {
            // 塗りつぶしでない場合、drawOvalを使用
            g.drawOval(x, y, width, height);
        }
        if (selected) {
            // 選択されている場合、黄色の枠線を描画
            Graphics2D g2 = (Graphics2D) g;
            // Graphics2Dを使用して、太い枠線を描画
            Stroke oldStroke = g2.getStroke();
            // まず、現在のストロークを保存
            g2.setColor(Color.YELLOW);
            // 黄色の色を設定
            g2.setStroke(new BasicStroke(4));
            // 太いストロークを設定
            g2.drawOval(x - 4, y - 4, width + 8, height + 8);
            // 四角形の外側に枠線を描画
            g2.setStroke(oldStroke);
        }
    }

    @Override
    public boolean contains(int px, int py) {
        //  円の中心と半径を計算
        double cx = x + width / 2.0;
        double cy = y + height / 2.0;
        double rx = width / 2.0;
        double ry = height / 2.0;
        // px, py はクリック位置の座標
        if (rx <= 0 || ry <= 0) {
            // 半径が0以下の場合、円は存在しない
            return false;
        }
        // 円の方程式を使用して、クリック位置が円の内部にあるかを判定
        double dx = px - cx;
        double dy = py - cy;
        // dx, dy はクリック位置と円の中心の差分
        return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1.0;
    }
}

class TriangleFigure extends Figure {

    private static final long serialVersionUID = 1L; // TriangleFigureクラスはFigureを継承し、三角形の描画を行う

    public TriangleFigure(int x, int y, int w, int h, Color c, boolean filled) {
        // コンストラクタで位置、サイズ、色、塗りつぶしの有無を設定
        super(x, y, w, h, c, filled); // x, y は左上の座標、w は幅、h は高さ
    }

    @Override
    public void draw(Graphics g) {
        // 三角形の頂点を計算
        // x, y は左上の座標、width は底辺の長さ
        int[] xs = {x + width / 2, x, x + width};
        int[] ys = {y, y + height, y + height};
        // Graphicsオブジェクトを使用して三角形を描画
        g.setColor(color);
        if (filled) {
            // 塗りつぶしの場合、fillPolygonを使用
            // xs, ys は三角形の頂点のx座標とy座標の配列
            g.fillPolygon(xs, ys, 3);
        } else {
            // 塗りつぶしでない場合、drawPolygonを使用
            // xs, ys は三角形の頂点のx座標とy座標の配列
            g.drawPolygon(xs, ys, 3);
        }
        if (selected) {
            // 選択されている場合、黄色の枠線を描画
            // Graphics2Dを使用して、太い枠線を描画
            Graphics2D g2 = (Graphics2D) g;
            //  まず、現在のストロークを保存
            Stroke oldStroke = g2.getStroke();
            // 黄色の色を設定
            g2.setColor(Color.YELLOW);
            // 太いストロークを設定
            // BasicStrokeは線の太さを指定するクラス
            g2.setStroke(new BasicStroke(4));
            // 三角形の外側に枠線を描画
            // xs, ys は三角形の頂点のx座標とy座標の配列
            g2.drawPolygon(xs, ys, 3);
            // 元のストロークに戻す
            // これにより、他の図形の描画に影響を与えないようにする
            g2.setStroke(oldStroke);
        }
    }

    @Override
    public boolean contains(int px, int py) {
        // 三角形の頂点を計算
        // x, y は左上の座標、width は底辺の長さ    
        Polygon triangle = new Polygon(
                // 三角形の頂点のx座標
                new int[]{x + width / 2, x, x + width},
                new int[]{y, y + height, y + height},
                3
        );
        // px, py はクリック位置の座標
        return triangle.contains(px, py);
    }
}

class LineFigure extends Figure {

    private static final long serialVersionUID = 1L; // LineFigureクラスはFigureを継承し、線分の描画を行う

    public LineFigure(int x, int y, int w, int h, Color c, boolean filled) {
        // コンストラクタで位置、サイズ、色、塗りつぶしの有無を設定
        super(x, y, w, h, c, false); // filled は無視
    }

    @Override
    public void draw(Graphics g) {
        // Graphicsオブジェクトを使用して線分を描画
        g.setColor(color);// 色を設定
        g.drawLine(x, y, x + width, y + height); // x, y は左上の座標、width は幅、height は高さ
        if (selected) {
            // 選択されている場合、黄色の枠線を描画
            Graphics2D g2 = (Graphics2D) g; // Graphics2Dを使用して、太い枠線を描画
            Stroke oldStroke = g2.getStroke(); // まず、現在のストロークを保存
            g2.setColor(Color.YELLOW);// 黄色の色を設定
            g2.setStroke(new BasicStroke(4));// 太いストロークを設定
            g2.drawLine(x, y, x + width, y + height); // 線分の外側に枠線を描画
            g2.setStroke(oldStroke); // 元のストロークに戻す
        }
    }

    @Override
    public boolean contains(int px, int py) {
        // 線分の端点を計算
        double x1 = x, y1 = y;
        // x, y は左上の座標
        // width, height は線分の幅と高さ
        double x2 = x + width, y2 = y + height;
        double distance = ptLineDist(x1, y1, x2, y2, px, py);
        return distance <= 5.0; // 選択判定：5ピクセル以内
    }

    private double ptLineDist(double x1, double y1, double x2, double y2, double px, double py) {
        // 点(px, py)から線分(x1, y1) - (x2, y2)までの距離を計算するメソッド
        double dx = x2 - x1;
        double dy = y2 - y1;
        // dx, dy は線分のベクトル
        double lenSq = dx * dx + dy * dy;
        if (lenSq == 0) {
            // 線分の長さが0の場合、線分は点(x1, y1)と同じ
            return Math.hypot(px - x1, py - y1);
        }
        // 線分の長さの二乗を計算
        double t = ((px - x1) * dx + (py - y1) * dy) / lenSq;
        // 点(px, py)から線分の始点(x1, y1)へのベクトルと線分のベクトルの内積を計算
        t = Math.max(0, Math.min(1, t));
        // tを0から1の範囲に制限
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        // プロジェクション点の座標を計算
        return Math.hypot(px - projX, py - projY);
    }
}

class RectangleFigure extends Figure {

    // RectangleFigureクラスはFigureを継承し、四角形の描画を行う
    private static final long serialVersionUID = 1L;
    // コンストラクタで位置、サイズ、色、塗りつぶしの有無を設定
    // x, y は左上の座標、w は幅、h は高さ  

    public RectangleFigure(int x, int y, int w, int h, Color c, boolean filled) {
        // RectangleFigureクラスのコンストラクタ
        super(x, y, w, h, c, filled); // x, y は左上の座標、w は幅、h は高さ
    }

    public void draw(Graphics g) {
        g.setColor(color);
        if (filled) {
            // つぶしの場合、fillRectを使用
            // x, y は左上の座標、width, height は幅と高さ
            g.fillRect(x, y, width, height);
            // 塗りつぶしでない場合、drawRectを使用
        } else {
            g.drawRect(x, y, width, height);
        }
        if (selected) {
            // 選択されている場合、黄色の枠線を描画
            Graphics2D g2 = (Graphics2D) g;
            // Graphics2Dを使用して、太い枠線を描画
            // まず、現在のストロークを保存
            Stroke oldStroke = g2.getStroke();
            // 黄色の色を設定
            g2.setColor(Color.YELLOW);
            // 太いストロークを設定
            g2.setStroke(new BasicStroke(4));
            // 四角形の外側に枠線を描画
            // x, y は左上の座標、width, height は幅と高さ
            g2.drawRect(x - 4, y - 4, width + 8, height + 8);
            // 元のストロークに戻す
            // これにより、他の図形の描画に影響を与えないようにする
            g2.setStroke(oldStroke);
        }
    }

}

public class DrawFrame extends JFrame {

    // DrawFrameクラスはJFrameを継承し、描画エリアと操作パネルを持つ
    DrawPanel draw_panel;
    // DrawPanelは描画エリアを表し、DrawModelとDrawControllerを使用して描画を管理
    OperationPanel operation_panel;
    // OperationPanelは操作パネルを表し、ComboBoxControllerとButtonControllerを使用して操作を管理
    DrawModel model;
    // DrawModelは描画のデータモデルを表し、図形のリストや現在のモード、色などを管理
    DrawController cont;
    // DrawControllerはマウスイベントを処理し、図形の描画や選択を管理
    ComboBoxController cb;
    // ComboBoxControllerはコンボボックスの選択イベントを処理し、図形の種類や色を変更
    ButtonController bb;
    // ButtonControllerはボタンのクリックイベントを処理し、図形の移動や削除、拡大縮小を管理
    JFileChooser chooser = new JFileChooser();
    // ファイル選択ダイアログを表示するためのJFileChooser
    JMenuBar mb = new JMenuBar();
    // メニューバーを作成
    JMenu fileMenu = new JMenu("File");
    // ファイルメニューを作成
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem exitItem = new JMenuItem("Exit");

    public DrawFrame() {
        // DrawFrameのコンストラクタ
        // JFrameのタイトルを設定
        super("Simple File Chooser Application");
        // ファイル選択ダイアログの初期設定
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image (*.jpg, *.jpeg)", "jpg", "jpeg"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (*.png)", "png"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap Image (*.bmp)", "bmp"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Serialized Data (*.ser)", "ser"));
        chooser.setAcceptAllFileFilterUsed(false);
        model = new DrawModel(); // DrawModelのインスタンスを作成
        cb = new ComboBoxController(model); // ComboBoxControllerのインスタンスを作成
        bb = new ButtonController(model); // ButtonControllerのインスタンスを作成
        cont = new DrawController(model); // DrawControllerのインスタンスを作成
        operation_panel = new OperationPanel(model, cb, bb); // OperationPanelのインスタンスを作成
        draw_panel = new DrawPanel(model, cont); // DrawPanelのインスタンスを作成
        this.setLayout(new BorderLayout()); // ★レイアウトを設定（必須）
        this.add(draw_panel, BorderLayout.CENTER); // 描画エリアを中央に配置
        this.add(operation_panel, BorderLayout.EAST); //    操作パネルを右側に配置
        this.setBackground(Color.black); // フレームの背景色を黒に設定
        this.setTitle("Draw Editor"); // フレームのタイトルを設定
        this.setSize(500, 500); // フレームのサイズを設定
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // フレームを閉じたときの動作を設定
        this.setVisible(true); // フレームを表示
        // メニューバーの設定
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        // ファイルメニューにアイテムを追加
        mb.add(fileMenu);
        // ファイルメニューにアイテムを追加
        setJMenuBar(mb);
        // メニューバーをフレームに設定
        setSize(300, 300);

        saveItem.addActionListener(e -> {
            // 保存ダイアログを表示
            int state = chooser.showSaveDialog(null); // ファイル選択ダイアログを表示
            if (state == JFileChooser.APPROVE_OPTION) {
                // 選択されたファイルを取得
                File file = chooser.getSelectedFile(); // 選択されたファイルを取得
                String filename = file.getName().toLowerCase(); // ファイル名を小文字に変換
                String selectedExt = "png"; // デフォルト
                javax.swing.filechooser.FileFilter selectedFilter = chooser.getFileFilter();
                // 選択されたフィルターから拡張子を取得
                if (selectedFilter instanceof FileNameExtensionFilter) {
                    // FileNameExtensionFilterの場合、拡張子を取得
                    // exts は拡張子の配列
                    String[] exts = ((FileNameExtensionFilter) selectedFilter).getExtensions();
                    if (exts.length > 0) {
                        // 選択された拡張子を小文字に変換
                        selectedExt = exts[0].toLowerCase(); // ex: "ser", "jpg"
                    }
                }
                // ファイル名に拡張子が含まれていない場合、自動で追加
                if (!filename.endsWith("." + selectedExt)) {
                    // ファイル名に拡張子を追加
                    // 例: "image" -> "image.png"
                    file = new File(file.getAbsolutePath() + "." + selectedExt);
                    filename = file.getName().toLowerCase(); // 更新
                }

                try {
                    // 拡張子に応じて処理を分岐
                    // 画像ファイルの場合、BufferedImageを使用して保存
                    if (filename.endsWith(".png") || filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg") || filename.endsWith(".bmp")) {
                        // 画像を保存
                        // draw_panel.exportImage() メソッドを使用して画像を取得
                        BufferedImage img = draw_panel.exportImage(); // 描画パネルから画像を取得
                        String format = filename.substring(filename.lastIndexOf('.') + 1);
                        // 拡張子からフォーマットを取得
                        ImageIO.write(img, format, file);
                        // 画像を指定されたフォーマットで保存
                        System.out.println("画像を保存しました: " + file.getPath());
                    } else if (filename.endsWith(".ser")) {
                        // シリアライズされた図形データを保存
                        // ObjectOutputStreamを使用して図形データを保存
                        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                            // model.getFigures() から図形のリストを取得
                            oos.writeObject(model.getFigures());
                            // 図形データをシリアライズして保存
                            System.out.println("図形データを保存しました: " + file.getPath());
                        }
                    } else {
                        // 未対応のファイル形式の場合、エラーメッセージを表示
                        JOptionPane.showMessageDialog(null, "未対応のファイル形式です（.png, .jpg, .ser など）");
                    }
                } catch (Exception ex) {
                    // エラーが発生した場合、スタックトレースを表示し、エラーメッセージをダイアログで表示
                    ex.printStackTrace();
                    // エラーメッセージをダイアログで表示
                    JOptionPane.showMessageDialog(null, "保存中にエラーが発生しました。");
                }
            }
        });

        openItem.addActionListener(e -> {
            // 開くダイアログを表示
            int state = chooser.showOpenDialog(null); // ファイル選択ダイアログを表示
            if (state == JFileChooser.APPROVE_OPTION) {
                // 選択されたファイルを取得
                File file = chooser.getSelectedFile();
                // ファイル名を小文字に変換して拡張子をチェック
                String filename = file.getName().toLowerCase();

                try {
                    // 拡張子に応じて処理を分岐 
                    if (filename.endsWith(".png") || filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg") || filename.endsWith(".bmp")) {
                        // 画像ファイルを読み込む
                        BufferedImage img = ImageIO.read(file);
                        if (img != null) {
                            // 画像を背景として設定
                            draw_panel.setBackgroundImage(img);
                            System.out.println("画像を読み込みました: " + file.getPath());
                        } else {
                            JOptionPane.showMessageDialog(null, "画像の読み込みに失敗しました。");
                        }
                    } else if (filename.endsWith(".ser")) {
                        // シリアライズされた図形データを読み込む
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                            // 読み込んだ図形データをモデルに設定
                            ArrayList<Figure> loaded = (ArrayList<Figure>) ois.readObject();
                            // 既存の図形をクリアして新しい図形を追加
                            model.getFigures().clear();
                            // 既存の図形をクリアして新しい図形を追加
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

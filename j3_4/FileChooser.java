import java.io.*;
import java.awt.event.*;
import javax.swing.*;

class FileChooser extends JFrame {
    private JFileChooser chooser = new JFileChooser();
    private JButton button = new JButton("Show File Chooser");
    private JTextArea textArea = new JTextArea(20, 40); // ファイル内容表示用
    public FileChooser() {
        super("Simple File Chooser Application");
        // ボタンとテキストエリアを追加
        JScrollPane scrollPane = new JScrollPane(textArea);
        JPanel panel = new JPanel();
        panel.add(button);
        this.add(panel, "North");
        this.add(scrollPane, "Center");
        this.pack();
        this.setLocationRelativeTo(null); // 中央に表示
        // ボタンにアクションを設定
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int state = chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                if (file != null && state == JFileChooser.APPROVE_OPTION) {
                    System.out.println("file name: " + file.getPath());
                    displayFileContents(file);
                } else if (state == JFileChooser.CANCEL_OPTION) {
                    System.out.println("canceled");
                } else if (state == JFileChooser.ERROR_OPTION) {
                    System.out.println("error");
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    // ファイルの内容をテキストエリアに表示するメソッド
    private void displayFileContents(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            textArea.setText(""); // テキストエリアをクリア
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "ファイルの読み込みに失敗しました:\n" + ex.getMessage(),
                "読み込みエラー",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FileChooser();
    }
}

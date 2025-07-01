import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FileChooser2 extends JFrame {
  JFileChooser chooser = new JFileChooser();
  JMenuBar mb = new JMenuBar();
  JMenu fileMenu = new JMenu("File"); 
  JMenuItem openItem = new JMenuItem("Open");
  JMenuItem saveItem = new JMenuItem("Save");
  JMenuItem exitItem = new JMenuItem("Exit");

  public FileChooser2() {
    super("Simple File Chooser Application");
    fileMenu.add(openItem);
    fileMenu.add(saveItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);
    mb.add(fileMenu);
    setJMenuBar(mb);
    setSize(300,300);

    ActionListener al = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int state=0;
        if (e.getSource()==openItem)
          state = chooser.showOpenDialog(null);
        else if (e.getSource()==saveItem)
          state = chooser.showSaveDialog(null);
        else
          System.exit(0);
        File file = chooser.getSelectedFile();
        if(file != null && state == JFileChooser.APPROVE_OPTION)
          System.out.println("file name:"+file.getPath());
        else if(state == JFileChooser.CANCEL_OPTION)
          System.out.println("canceled");
        else if(state == JFileChooser.ERROR_OPTION) 
          System.out.println("error");
      }
    };
    openItem.addActionListener(al);
    saveItem.addActionListener(al);
    exitItem.addActionListener(al);

    KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK);
    exitItem.setAccelerator(ks); 
    // Menuが開いていなくても，ALT+X で exit が選択される．
 
    fileMenu.setMnemonic('F');  // ALT+F で fileMenuが選択出来る．
    exitItem.setMnemonic(KeyEvent.VK_X); 
    // fileMenuが開いている状態で X を押すと exit が選択できる
  }
  
  public static void main(String args[]) {
    JFrame f = new FileChooser2();
    f.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        System.exit(0);
      }
    });
    f.show();
  }
}
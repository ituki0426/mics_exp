import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ComboBoxController implements ActionListener{
    private DrawModel model;
    public ComboBoxController(DrawModel a) {
        model = a;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String selected = (String) cb.getSelectedItem();
        Color color;
        switch (selected) {
            case "red":
                color = Color.RED;
                break;
            case "green":
                color = Color.GREEN;
                break;
            case "blue":
                color = Color.BLUE;
                break;
            default:
                color = Color.RED;
        }
        model.setColor(color);
    }
}

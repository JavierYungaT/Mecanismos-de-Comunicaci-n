
package vistas;

import java.awt.HeadlessException;
import javax.swing.JFrame;


public class Ventana extends JFrame {

    public Ventana() throws HeadlessException {
        setTitle("CLIENTE");
        setBounds(600,300,280,350);
        add(new Panel());        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

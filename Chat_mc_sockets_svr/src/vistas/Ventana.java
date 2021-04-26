
package vistas;

import controladores.Servidor;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Ventana extends JFrame {

    private final JTextArea areatexto;

    public Ventana() {
        setTitle("SERVIDOR");
        setBounds(600,300, 280,350);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        areatexto = new JTextArea();
        panel.add(areatexto, BorderLayout.CENTER);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void iniciarServidor() {
        Servidor servidor = new Servidor(areatexto);
        servidor.start();   
    }
}

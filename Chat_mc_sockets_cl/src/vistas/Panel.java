
package vistas;

import controladores.EnviadorTexto;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Panel extends JPanel {

    private final JTextField txtCampo;
    private final JButton btnEnviar;

    public Panel() {
        add(new JLabel("CLIENTE"));
        txtCampo = new JTextField(20);
        add(txtCampo);
        btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new EnviadorTexto(txtCampo));
        add(btnEnviar);
    }

}

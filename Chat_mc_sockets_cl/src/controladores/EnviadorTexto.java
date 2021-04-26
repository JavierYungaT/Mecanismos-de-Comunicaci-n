

package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;


public class EnviadorTexto implements ActionListener {

    public final static int PUERTO_SERVIDOR = 9000;
    public final static String IP_SERVIDOR = "127.0.0.1";
    
    private final JTextField txtCampo;

    public EnviadorTexto(JTextField txtCampo) {
        this.txtCampo = txtCampo;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            Socket socket = new Socket(IP_SERVIDOR, PUERTO_SERVIDOR);
            try (DataOutputStream flujoSalida = 
                    new DataOutputStream(socket.getOutputStream())) {
                flujoSalida.writeUTF(txtCampo.getText() + "\n");
                txtCampo.setText("");
            }            
        } catch (IOException ex) {
            Logger.getLogger(EnviadorTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

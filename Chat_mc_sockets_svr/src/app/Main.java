
package app;

import vistas.Ventana;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
            Ventana gui = new Ventana();
            gui.setVisible(true);
            gui.iniciarServidor();
        
    }

}

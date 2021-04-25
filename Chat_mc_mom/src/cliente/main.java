package cliente;

import javax.swing.JOptionPane;

public class main {
	public static void main(String[] args) {

		Cliente cl = new Cliente();

		while (true) {
			String mensaje = JOptionPane.showInputDialog("Escriba Su mensaje");
			JOptionPane.showMessageDialog(null, "Enviado Middleware: " + mensaje);
			cl.conexion(mensaje);
		}
	}
}
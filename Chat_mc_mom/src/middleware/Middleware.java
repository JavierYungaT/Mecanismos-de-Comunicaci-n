package middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Middleware implements Runnable {

	@Override
	public void run() {

		System.out.println("Middleware activo");
		try {

			ServerSocket cola = new ServerSocket(9991);
			Hashtable<String, Integer> datos = new Hashtable<>();
			while (true) {
				Socket misocket = cola.accept();
				DataInputStream flujoentrada = new DataInputStream(misocket.getInputStream());
				String mensaje = flujoentrada.readUTF();

				datos.put(mensaje, 0);
				System.out.println(datos.toString());
				envioserver(datos);
				misocket.close();

			}
		} catch (IOException ex) {
			Logger.getLogger(Middleware.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void envioserver(Hashtable<String, Integer> cola) {
		try {
			try (Socket socket = new Socket("127.0.0.1", 9990)) {
				DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());

				flujoSalida.writeUTF(cola.toString());
				flujoSalida.close();
			}
		} catch (Exception e) {
			envioserver(cola);
		}
	}

}

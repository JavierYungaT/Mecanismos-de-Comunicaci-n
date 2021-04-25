package cliente;

import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {

	public void conexion(String mensaje) {
		try {
			//
			try (Socket socket = new Socket("127.0.0.1", 9991)) {
				DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());

				flujoSalida.writeUTF(mensaje);
				System.out.println("envio exitoso");
				flujoSalida.close();

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}

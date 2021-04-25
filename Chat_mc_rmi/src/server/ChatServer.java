package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;
import cliente.ChatCliente2;

public class ChatServer extends UnicastRemoteObject implements ChatServerIF {
	String line = "---------------------------------------------\n";
	private Vector<Chatter> chatters;
	private static final long serialVersionUID = 1L;

	public ChatServer() throws RemoteException {
		super();
		chatters = new Vector<Chatter>(10, 1);
	}

	/**
	 * MÉTODOS LOCALES
	 */
	public static void main(String[] args) {
		startRMIRegistry();
		String hostName = "localhost";
		String serviceName = "GroupChatService";

		if (args.length == 2) {
			hostName = args[0];
			serviceName = args[1];
		}

		try {
			ChatServerIF hello = new ChatServer();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, hello);
			System.out.println("El servidor RMI de chat en grupo se está ejecutando ...");
		} catch (Exception e) {
			System.out.println("El servidor tuvo problemas al comenzar");
		}
	}

	/**
	 * Inicio del registro RMI
	 */
	public static void startRMIRegistry() {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("Servidor RMI listo");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/*
	 * MÉTODOS REMOTOS
	 */

	/**
	 * Devolver un mensaje al cliente
	 */
	public String sayHello(String ClientName) throws RemoteException {
		System.out.println(ClientName + "envió un mensaje");
		return "Hola" + ClientName + "desde el servidor de chat grupal";
	}

	/**
	 * Enviar una cadena (la última publicación, en su mayoría) a todos los clientes
	 * conectados
	 */
	public void updateChat(String name, String nextPost) throws RemoteException {
		String message = name + " : " + nextPost + "\n";
		sendToAll(message);
	}

	/**
	 * Reciba una nueva referencia remota de cliente
	 */
	@Override
	public void passIDentity(RemoteRef ref) throws RemoteException {
		try {
			System.out.println(line + ref.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reciba un nuevo cliente y muestre los detalles en el envío de la consola para
	 * registrar el método.
	 */
	@Override
	public void registerListener(String[] details) throws RemoteException {
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println(details[0] + " se ha unido a la sesión de chat");
		System.out.println(details[0] + "'s hostname: " + details[1]);
		System.out.println(details[0] + "'sRMI service : " + details[2]);
		registerChatter(details);
	}

	/**
	 * registrar la interfaz del cliente y almacenarla en una referencia para
	 * futuros mensajes que se enviarán, es decir, mensajes de otros miembros de la
	 * sesión de chat. enviar un mensaje de prueba para confirmación/conexión de
	 * prueba
	 * 
	 * @param details
	 */
	private void registerChatter(String[] details) {
		try {
			ChatCliente2 nextClient = (ChatCliente2) Naming.lookup("rmi://" + details[1] + "/" + details[2]);

			chatters.addElement(new Chatter(details[0], nextClient));

			nextClient.messageFromServer("[Server] : Hola " + details[0] + " ahora eres libre de chatear.\n");

			sendToAll("[Server] : " + details[0] + " se ha unido al grupo.\n");

			updateUserList();
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Actualice todos los clientes invocando de forma remota su método
	 * updateUserList RMI
	 */
	private void updateUserList() {
		String[] currentUsers = getUserList();
		for (Chatter c : chatters) {
			try {
				c.getCliente().updateUserList(currentUsers);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * generar una matriz de cadenas de usuarios actuales
	 * 
	 * @return
	 */
	private String[] getUserList() {
		String[] allUsers = new String[chatters.size()];
		for (int i = 0; i < allUsers.length; i++) {
			allUsers[i] = chatters.elementAt(i).getNombre();
		}
		return allUsers;
	}

	/**
	 * Enviar un mensaje a todos los usuarios.
	 * 
	 * @param newMessage
	 */
	public void sendToAll(String newMessage) {
		for (Chatter c : chatters) {
			try {
				c.getCliente().messageFromServer(newMessage);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * remove a client from the list, notify everyone
	 */
	@Override
	public void leaveChat(String userNombre) throws RemoteException {

		for (Chatter c : chatters) {
			if (c.getNombre().equals(userNombre)) {
				System.out.println(line + userNombre + " left the chat session");
				System.out.println(new Date(System.currentTimeMillis()));
				chatters.remove(c);
				break;
			}
		}
		if (!chatters.isEmpty()) {
			updateUserList();
		}
	}

	/**
	 * A method to send a private message to selected clients The integer array
	 * holds the indexes (from the chatters vector) of the clients to send the
	 * message to
	 */
	@Override
	public void sendPM(int[] privateGroup, String privateMessage) throws RemoteException {
		Chatter pc;
		for (int i : privateGroup) {
			pc = chatters.elementAt(i);
			pc.getCliente().messageFromServer(privateMessage);
		}
	}

}
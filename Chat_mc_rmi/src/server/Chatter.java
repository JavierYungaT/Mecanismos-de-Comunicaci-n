package server;

import cliente.ChatCliente2;

public class Chatter {

	public String nombre;
	public ChatCliente2 cliente;

	public Chatter(String nombre, ChatCliente2 cliente) {
		this.nombre = nombre;
		this.cliente = cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public ChatCliente2 getCliente() {
		return cliente;
	}

}

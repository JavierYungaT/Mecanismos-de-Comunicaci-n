package middleware;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread hilo = new Thread( new Middleware(),"Cola a la escucha " );
	    hilo.start();
	}

}

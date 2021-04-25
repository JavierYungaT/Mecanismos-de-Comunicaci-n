package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Server implements Runnable  
{

    public Server() 
    {
       Thread hilo = new Thread(this);
       
       hilo.start();
    }
    

    @Override
    public void run()
    {
    	
    	System.out.println("Server Activo");
      servidor();
    
    }
    
    public void  servidor()
    {
        try {
            
            ServerSocket server = new ServerSocket(9990);
             while(true)
            {
            Socket misocket = server.accept();
            DataInputStream flujoentrada = new DataInputStream(misocket.getInputStream());
            
            String mensaje= flujoentrada.readUTF();
            
            System.out.println("Mensaje cliente: "+mensaje.toString());
            
            misocket.close();
            }
        } catch (IOException ex) 
        {
            
           Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

package Pt2_Sockets_ClientServidor;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;

public class Server {

    private static final List<ServerHilo> clientesConectados = Collections.synchronizedList(new ArrayList<>());
    private static boolean servidorActivo = true; 
    private static ServerSocket serverSocket;
    
    public static void main(String[] args) throws IOException {
    	
    	int maxClientes = Integer.parseInt(args[0]);
    	
        if (args.length < 1) {
            System.out.println("Uso: java ServidorChat <maxClientes>");
            return;
        }
        
        
        
        //variables para el puerto, palabraClave del Server y numero del Cliente
        int idClient = 1; 
        String keywordServidor = "cleopatra";
        int puerto = 1234;
        
        
        serverSocket = new ServerSocket(puerto);
        System.out.println(" ____________________________________________  \n"
				  		  +"| PORT_SERVIDOR: "+puerto+"                        | \n"
				  		  +"| PARAULA_CLAU_SERVIDOR: "+keywordServidor+"           | \n"
				  		  +"|____________________________________________| \n");

		//prints de inicio de Sesion
		System.out.println("> Server chat at port " + puerto );
		System.out.println("\n> Inicializing server... OK\n");
        
		//bucle qhile para crear los 
        while (servidorActivo) {
        	if (clientesConectados.size() < maxClientes) {
        		
        		try{ 
        			/** Este try catch es principalmente para poder capturar las Excepcione que dara
        			    el codigo al quere cerrar el socket del ServerSocket ya que simempre esta esperando
        			    a que algun cliente se una **/
        			
        			Socket clienteSocket = serverSocket.accept();

                    System.out.println("\n> Connection from client... OK");
                    System.out.println("\n> Inicializing chat... OK\n");

                    ServerHilo hilo = new ServerHilo(idClient, clienteSocket, keywordServidor, clientesConectados);
                    clientesConectados.add(hilo);
                    hilo.start();

                    idClient++;
        			
        		}catch(SocketException se){
        			if(se.getMessage().contains("Socket closed")){
        				//servidorActivo = false;
        			}else{
        				se.printStackTrace();
        			}
        		}catch(IOException e){
        			e.printStackTrace();
        		}
                
            } else {
                try {
                    Thread.sleep(500); // Espera per no saturar CPU
                } catch (InterruptedException e) {
                	cerrarServidor();
                }
            }
        }
        
        serverSocket.close();
    }

    // Método para cerrar el servidor desde un hilo cliente
    // para el bucle while 
    public static void cerrarServidor() {
        servidorActivo = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();  // <- Esto desbloquea el accept()
            }
        } catch (IOException e) {
        		e.printStackTrace();
        }
    }
    
    // Metodo para cerrar todos los clientes cuando el Server escriba
    // su palabra Clave "cleopatra"
    public static void cerrarTodosLosClientes() {
    	servidorActivo = false;
        synchronized (clientesConectados) {
            for (ServerHilo hilo : clientesConectados) {
                hilo.cerrarDesdeServidor();  // llama un metodo de ServerHilo para cerrar este hilo
            }
            clientesConectados.clear(); // limpia/vacia la lista de Clientes Conectados.
        }
    }
    
}

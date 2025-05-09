package Pt2_Sockets_ClientServidor;

import java.io.*;
import java.net.*;
import java.util.List;

class ServerHilo extends Thread {
	// Declaracion de las Variables que se utilizaran en el Codigo: 
	// Constructor / Metodo Run / Metodo para Cerrar Conexion
    private Socket socket;
    int idClient;
    private String keywordServidor;
    private String keywordCliente;
    private BufferedReader entrada;
    private PrintWriter salida;
    private List<ServerHilo> listaClientes;
    public static int count = 0;//contador que se aplica a un metodo
    
    //Constructor el Hilo Servidor
    public ServerHilo(int idClient, Socket socket, String keywordServidor, List<ServerHilo> listaClientes) {
    	this.idClient = idClient;
        this.socket = socket;
        this.keywordServidor = keywordServidor;
        this.listaClientes = listaClientes;
    }
    
    //Metodo Run que ejecutara cada Hilo
    public void run() {
        try {
        	//Variables de Lectura, Envio, Escritura
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            // Intercambio de claves
            salida.println(keywordServidor);
            keywordCliente = entrada.readLine();

            String mensaje;
            boolean activo = true;

            while (activo) {
            	
                mensaje = entrada.readLine();//almacenar el mensaje del cliente en "mensaje" 
                
                //Comprobar que el mensaje no sea null "posible desconexion del Cliente".
                if (mensaje == null)
                {
                	activo = false;
                	continue;
                }
                
                System.out.println("\n# Rebut del Cliente "+idClient+": " + mensaje); // Muestra el mensaje
                
                //Comprobar PalabraClave Cliente
                if (mensaje.toLowerCase().contains(keywordCliente.toLowerCase()))
                {
                    System.out.println("\n> Client keyword detected!");
                    cerrarConexion();
                    activo = false;
                    continue;
                }
                
                //Logica para Escribir al cliente
                System.out.print("\n# Enviar al Cliente "+idClient+": ");
                String respuesta = teclado.readLine();
                salida.println(respuesta);
                
                //Comprobar PalabraClave Client
                if (respuesta.toLowerCase().contains(keywordCliente.toLowerCase()))
                {
                	System.out.println("\n> Client keyword detected!");
                	cerrarConexion();
                    activo = false;
                    continue;
                }
                //Comprobar PalabraClave Servidor para cerra los chats
                if (respuesta.toLowerCase().contains(keywordServidor.toLowerCase()))
                {
                	System.out.println("\n> Server keyword detected!");
                    Server.cerrarTodosLosClientes();
                    activo = false;
                    continue;
                }
            }
                        
        } catch (IOException e) {
        	if(e.getMessage().contains("Socket closed")){
        		
        	}
        	else{
                System.out.println("\nError en hilo cliente: " + e.getMessage());
        	}
        	
        }
    }
    
    // Metodo que se ejecuta cuando el hilo se cierra para comprobar 
    // si todos los hilos/Clientes se han desconectado.
    private void cerrarConexion() {
        try {
            socket.close();
            listaClientes.remove(this);
            System.out.println("\n> Client"+idClient+" desconectado.");
            
            // Comprobacion de si todos los clientes se han ido del server
            // Con la listaClientes.isEmpty
            if (listaClientes.isEmpty()) {
                System.out.println("\n> No hay mas clientes.");
                System.out.println("\n> Cerrando servidor...");
                System.out.println("\n> Servidor cerrado.");
                Server.cerrarServidor();
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Metodo para cerra el hilo actual desde la Clase Server "desde donde se llama el metodo actual".
    public void cerrarDesdeServidor(){
    	try{
    		while(count < 1){
    			System.out.println("\n> Cerrando servidor...");
                System.out.println("\n> Servidor cerrado.");
                count ++;
    		}
    		
    		socket.close(); //cierra la conexion con el cliente "el Socket"
            this.interrupt(); //para el hilo si eta esperando
            
    	}catch(Exception e){
    		System.out.println("Error cerrando Client"+idClient);
    	}
    }
    
}

package Pt2_Sockets_ClientServidor;

import java.net.*;
import java.io.*;

public class Client {


public static void main(String[] args) throws IOException{
		
		int port = 1234;
		String keywordClient = "";
		String keywordServer = "";
		
		try {
			
			Socket s = new Socket("localhost", port); //crear el soquet con el puerto de acceso y el localhost
			Socket sc = null; 
			
			//logica para leer texto de el Servidor
			BufferedReader entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
			//logica para enviar el texto al servidor
			PrintWriter salida = new PrintWriter(s.getOutputStream());
			//logica para obtener los inputs del teclado y obtener el texto que escribes
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.print("\n> Escriba su palabra clave deseada: ");
			keywordClient = teclado.readLine();
			
			//prints de inicio de Sesion
			System.out.println("\n PORT_SERVIDOR: "+port+"                        \n"
							  +"\n PARAULA_CLAU_CLIENT: "+keywordClient+"         \n\n");
			
			//prints de inicio de Sesion
			System.out.println("> Client chat to port " + port );
			System.out.println("\n> Inicializing client... OK");
			
			System.out.println("\n \n> Inicializing chat... OK\n");
			
			
			
			//obtener constraseña y enviar contraseña cliente
			salida.println(keywordClient);
			salida.flush();
			
			//obtener la palabra clave del Servidor
			keywordServer = entrada.readLine();
			
			//String para almacenar mensajes
			String mensaje; 
			boolean activo = true;
			
			//while para no cerar el Socker asta que salga 
			//la palabra clave sea mencionada en el chat
			while( activo ){
				
				System.out.print("\n# Enviar al Servidor: ");
				mensaje = teclado.readLine();
				salida.println(mensaje.trim());
				salida.flush();
				
				//Condicion para cerrar el servidor si el cliente dice la palabra clave
				if(mensaje.toLowerCase().contains(keywordClient)){
					System.out.println("\n> Client keyword detected!");
					activo = false;
					continue;
				}
				
				//mostrar la informacio que envia el servidor
				mensaje = entrada.readLine();
				
				if(mensaje == null) {
					System.out.println("\n> Server keyword detected!");
					activo = false;
					continue;
				}
				
				System.out.println("\n# Rebut del servidor: " + mensaje);
				
				//Condicion para cerrar el cliente si el Servidor se cierra
				if(mensaje.toLowerCase().contains(keywordServer)){
					System.out.println("\n> Server keyword detected!");
					activo = false;
				}
				else if(mensaje.toLowerCase().contains(keywordClient)){
					System.out.println("\n> Client keyword detected!");
					activo = false;
				}
				
			}
			
			//prints de cierre de Sesion
			System.out.println("\n> Closing chat... OK");
			System.out.println("\n> Closing client... OK");
			System.out.println("\n> Bye! \n");
			
			//cerra socker, outPuts y inPuts lectura/ecritura
			entrada.close();
			salida.close();
			teclado.close();
			s.close();
			
		}catch(Exception e){
			//en caso de que la Excepcion sea esta se enviara el siguiente texto.
			if (e.getMessage().contains("Se ha anulado una conexión")){	
				//prints de cierre de Sesion
				System.out.println("\n> Server keyword detected!");
				System.out.println("\n> Closing chat... OK");
				System.out.println("\n> Closing client... OK");
				System.out.println("\n> Bye! \n");
			}
			else if(e.getMessage().contains("Software caused connection abort:")){
				//prints de cierre de Sesion
				System.out.println("\n> Server keyword detected!");
				System.out.println("\n> Closing chat... OK");
				System.out.println("\n> Closing client... OK");
				System.out.println("\n> Bye! \n");
			}
			else{
				System.out.println("Error inesperado : " + e);
			}
			
		}
	}
}
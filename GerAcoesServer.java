import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GerAcoesServer{

	public static void main(String[] args) {
		try {
			GerenciadorDeAcoes obj = new GerenciadorDeAcoesImpl();
			GerenciadorDeAcoes stub = (GerenciadorDeAcoes) UnicastRemoteObject
					.exportObject(obj, 0);
			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry("localhost");
			registry.bind("GerAcoesServer", stub);

			System.err.println("Server is running...");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}	
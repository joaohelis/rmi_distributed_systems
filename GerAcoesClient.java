import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GerAcoesClient extends Remote{
		
	void receiveMsg(String msg) throws RemoteException;
		
}

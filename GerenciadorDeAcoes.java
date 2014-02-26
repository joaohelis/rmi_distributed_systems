/* Interface para Ações */

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GerenciadorDeAcoes extends Remote, Serializable{   
    
    boolean login(GerAcoesClient client, String userName) throws RemoteException;
    
    boolean sendMsg(String to, String msg) throws RemoteException;
    
    String listUsers() throws RemoteException;
    
    String listAcoes() throws RemoteException;
    
    /* Ler preço da ação */
    public double lerPrecoAcao(String clientName, String codigoAcao)
        throws RemoteException;

    /* Escrever preço da ação */
    public void escreverPrecoAcao(String clientName, String codigoAcao, double NovoPreco)
        throws RemoteException;

    /* Ler nome da ação */
    public String lerNomeAcao(String clientName, String codigoAcao) 
    throws RemoteException;

    /* Ler quantidade de ações */
    public int lerQuantidadeAcoes(String clientName, String codigoAcao) 
    throws RemoteException;

    /* Adicionar nova ação */
    public void adicionarNovaAcao(String clientName, String nomeAcao, String codigoAcao, double precoInicial, int quantidade)
        throws RemoteException;

    /* Apagar ação */
    public void apagarAcao(String clientName, String codigoAcao)
        throws RemoteException;

    /* Comprar ações */
    public double comprarAcoes(String clientName, String codigoAcao, int quantidade)
        throws RemoteException;

    /* Vender ações */
    public double venderAcoes(String clientName, String codigoAcao, int quantidade)
        throws RemoteException;
}
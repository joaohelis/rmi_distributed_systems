import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;

public class GerenciadorDeAcoesImpl implements GerenciadorDeAcoes{
	
	private static final long serialVersionUID = 1L;
	public static int ATRASO = 15;	//Tempo de atraso na atualização
    public long ABERTURA;		//Tempo de abertura do pregão    

    // Cria estrutura de dados (Hashtable) para armazenar ações, com capacidade inicial de 100
    
    Hashtable<String, ElementoAcao> BOVESPA;
    private HashMap<String, GerAcoesClient> gerAcoesClient;
    
    // Construtor
    public GerenciadorDeAcoesImpl()throws RemoteException {		        
        super();
        // Armazena tempo de abertura do pregão
        ABERTURA = java.lang.System.currentTimeMillis();
        
        this.gerAcoesClient = new HashMap<String, GerAcoesClient>();
        this.BOVESPA  = new Hashtable<String,ElementoAcao>(100);

        //Adiciona ações exemplo
        BOVESPA.put("PET", new ElementoAcao(500, "Petrobras",17.00,"PET"));
        BOVESPA.put("OGX", new ElementoAcao(750, "OGX Petroleo",0.50,"OGX"));
        BOVESPA.put("GOL", new ElementoAcao(900, "GOL Linhas Aereas",10.00,"GOL"));
        BOVESPA.put("TAM", new ElementoAcao(900, "TAM Linhas Aereas",52.00,"TAM"));
    } 	

	@Override
	public boolean login(GerAcoesClient client, String clientName)
			throws RemoteException {
		System.out.println("Login: " + clientName);
		if(gerAcoesClient.containsKey(clientName)){
			client.receiveMsg("Um Gerenciador de acoes Cliente ja esta logado com esse nome. Por favor, modifique o nome do seu cliente e tente novamente.");
			return false;
		}
		gerAcoesClient.put(clientName, client);
		return true;
	}

	@Override
	public boolean sendMsg(String to, String msg)
			throws RemoteException {
		System.out.println("Enviando mensagem para: " + to);
		GerAcoesClient toClient = gerAcoesClient.get(to);
		if (toClient == null) {
			return false;
		}
		toClient.receiveMsg(msg);
		return true;
	}
	
	@Override
	public String listAcoes() throws RemoteException {
		System.out.println("Listando acoes.");
		return BOVESPA.keySet().toString();
	}

	@Override
	public String listUsers() throws RemoteException {
		System.out.println("Listando usuarios logados.");
		return gerAcoesClient.keySet().toString();
	}

    @Override
    public synchronized double lerPrecoAcao(String clientName, String codigoAcao) throws RemoteException{
    	System.out.println("<"+clientName+"> Ler preco da acao "+codigoAcao);
		if (!BOVESPA.containsKey(codigoAcao)){
			//throw new RemoteException("Acao nao existe!");
			gerAcoesClient.get(clientName).receiveMsg("Acao nao existe!");
			return Double.MIN_VALUE;
		}			
        return BOVESPA.get(codigoAcao).precoAcao;
    }
    
    @Override
    public synchronized String lerNomeAcao(String clientName, String codigoAcao)    
    	throws RemoteException {		
    	System.out.println("<"+clientName+"> Ler nome da acao "+codigoAcao);
    	if (!BOVESPA.containsKey(codigoAcao)){
    		//throw new RemoteException("Acao nao existe!");
			gerAcoesClient.get(clientName).receiveMsg("Acao nao existe!");
			return null;
		}        
        return BOVESPA.get(codigoAcao).nomeAcao;
    }
	
    @Override
    public synchronized int lerQuantidadeAcoes(String clientName, String codigoAcao) throws RemoteException {
    	System.out.println("<"+clientName+"> Ler quantidade de acoes "+codigoAcao);
    	if (!BOVESPA.containsKey(codigoAcao)){
    		//throw new RemoteException("Acao nao existe!");
			gerAcoesClient.get(clientName).receiveMsg("Acao nao existe!");
			return Integer.MIN_VALUE;
		}	    		    	
    	return BOVESPA.get(codigoAcao).quantidadeAcoes;
    }

    @Override
    public synchronized void escreverPrecoAcao(String clientName, String codigoAcao, double novoPreco)
		throws java.rmi.RemoteException {
    	System.out.println("<"+clientName+"> Escrever preco da acao "+codigoAcao);
    	if (!BOVESPA.containsKey(codigoAcao)){
    		//throw new RemoteException("Acao nao existe!");
			gerAcoesClient.get(clientName).receiveMsg("Acao nao existe!");
			return;
		}	
		/****************************************************************************************
		* A SEGUINTE IMPLEMENTACAO DE UMA ESCRITA É APENAS PARA DEMONSTRAÇÃO DO MÉTODO, NÃO	*
		* SENDO A MELHOR MANEIRA DE ESCREVER EM UMA TABELA HASH. VOCE DEVE GARANTIR QUE A	*
		* ESCRITA É THREAD-SAFE, OU SEJA, CADA ESCRITA DEVE SER CONSISTENTE NA PRESENÇA DE	*
		* UMA OUTRA ESCRITA CONCORRENTE, FATO ESTE QUE PODE ACONTECER SE DOIS CLIENTES ESTÃO	*
		* TENTANDO MUDAR O PREÇO DA MESMA AÇÃO NO MESMO INSTANTE DE TEMPO			*
		****************************************************************************************/
	
		long TimeOne = java.lang.System.currentTimeMillis();
	
		// Atualiza o preço removendo a cópia do objeto ElementoAcao e reinserindo na tabela
		ElementoAcao Temp = (ElementoAcao) BOVESPA.remove(codigoAcao);
		ElementoAcao Temp2 =  new ElementoAcao(Temp.quantidadeAcoes, Temp.nomeAcao, Temp.precoAcao, codigoAcao);
		BOVESPA.put(codigoAcao, Temp2);
		Temp.precoAcao = novoPreco;
		
		// Atraso apenas como exemplo
		do{	
		}while ((TimeOne+(ATRASO*1000))>java.lang.System.currentTimeMillis());
			
		BOVESPA.put(codigoAcao, Temp);
		gerAcoesClient.get(clientName).receiveMsg("Preco da acao alterado com sucesso!");
    }

    @Override
    public synchronized void adicionarNovaAcao(String clientName, String nomeAcao, String codigoAcao, double precoInicial, int quantidade)
		throws RemoteException{
	
		//Verifica se CodigoAcao não está em uso
		
    	System.out.println("<"+clientName+"> Adicionar nova acao "+codigoAcao);
    	if (BOVESPA.containsKey(codigoAcao)){
			gerAcoesClient.get(clientName).receiveMsg("O codigo da acao ja esta em uso. Por favor, insira um novo codigo e tente novamente.");
			return;
		}
	
		if (!((codigoAcao.length()>=1)&&(codigoAcao.length()<=3))){
			gerAcoesClient.get(clientName).receiveMsg("O codigo da acao nao eh valido!");
			return;
		}			
			
		BOVESPA.put(codigoAcao, new ElementoAcao(quantidade, nomeAcao, precoInicial, codigoAcao));		
		gerAcoesClient.get(clientName).receiveMsg("Acao(oes) adicionada(s) com sucesso!");
    }

    @Override
    public synchronized void apagarAcao(String clientName, String codigoAcao)
		throws RemoteException{
    	System.out.println("<"+clientName+"> Apagar acao "+codigoAcao);
    	if (!BOVESPA.containsKey(codigoAcao)){
    		//throw new RemoteException("Acao nao existe!");
			gerAcoesClient.get(clientName).receiveMsg("Acao nao existe!");
			return;
		}		
		if(BOVESPA.remove(codigoAcao)!=null)
			gerAcoesClient.get(clientName).receiveMsg("Acao "+codigoAcao+" removida com sucesso!");
    }

    @Override
    public synchronized double comprarAcoes(String clientName, String codigoAcao, int quantidade)
		throws RemoteException{	    	
    	System.out.println("<"+clientName+"> Comprar acoes "+codigoAcao);
    	if (!BOVESPA.containsKey(codigoAcao)){
    		//throw new RemoteException("Acao nao existe!");
			gerAcoesClient.get(clientName).receiveMsg("Acao nao existe!");
			return Double.MIN_VALUE;
		}    	
    	ElementoAcao temp = BOVESPA.get(codigoAcao);
    	if (temp.quantidadeAcoes < quantidade || quantidade < 1){
    		gerAcoesClient.get(clientName).receiveMsg("Quantidade de acoes insuficientes para a compra ou a quatidade requisitada é inválida");
			return Double.MIN_VALUE;
    	}
    	gerAcoesClient.get(clientName).receiveMsg("Acao(oes) comprada(s) com sucesso!");
    	temp.quantidadeAcoes -= quantidade;    		
		return quantidade * temp.precoAcao;
    }

    @Override
    public synchronized double venderAcoes(String clientName, String codigoAcao, int quantidade)
		throws RemoteException{	
    	System.out.println("<"+clientName+"> Vender acoes "+codigoAcao);
    	if (!BOVESPA.containsKey(codigoAcao)){
    		//throw new RemoteException("Acao nao existe!");
			gerAcoesClient.get(clientName).receiveMsg("Acao nao existe!");
			return Double.MIN_VALUE;
		}    	
    	ElementoAcao temp = BOVESPA.get(codigoAcao);
    	if (temp.quantidadeAcoes < quantidade || quantidade < 1){
    		gerAcoesClient.get(clientName).receiveMsg("Quantidade de acoes insuficientes para a venda ou a quatidade requisitada é inválida");
			return Double.MIN_VALUE;
    	}
    	gerAcoesClient.get(clientName).receiveMsg("Acao(oes) vendida(s) com sucesso!");
    	temp.quantidadeAcoes += quantidade;    		
		return quantidade * temp.precoAcao;
    }	
}
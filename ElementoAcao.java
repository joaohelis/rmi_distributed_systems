import java.io.Serializable;

class ElementoAcao implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	public String nomeAcao;	//Nome da ação
	public double precoAcao;	//Preço atual da ação
	public double precoInicial;	//Preço inicial da ação
	public String codigoAcao;	//Referência única para uma ação 
	public int    quantidadeAcoes; //Quantidade de ações disponíveis

	// Constructor
	public ElementoAcao(int quantidade, String nome, double preco, String codigo){
		this.nomeAcao = nome;
		this.precoAcao = preco;
		this.precoInicial = preco;
		this.codigoAcao = codigo;	
		this.quantidadeAcoes = quantidade;
	}
}

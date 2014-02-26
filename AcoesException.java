import java.io.Serializable;

public class AcoesException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public AcoesException(){
		super();
	}

	public AcoesException(String arg0){
		super(arg0);
	}		
}

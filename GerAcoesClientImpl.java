import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GerAcoesClientImpl implements GerAcoesClient{		
	
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("Número de argumentos inválidos. O formato deve ser: GerAcoesClientImpl <clinetName> <serverIP>");
			System.exit(0);
		}
		try{						
			String clientName = args[0];
			GerAcoesClient client = new GerAcoesClientImpl();
			GerAcoesClient stub = (GerAcoesClient) UnicastRemoteObject.exportObject(client, 0);
			Registry registry = LocateRegistry.getRegistry(args[1]);
			GerenciadorDeAcoes server = (GerenciadorDeAcoes) registry.lookup("GerAcoesServer");			

			if(!server.login(client, clientName))
				System.exit(0);

			System.out.println("|-----------------------------------------------------------");
			System.out.println("|INFO. Digite o comando 'help'");
			System.out.println("|-----------------------------------------------------------");

			Scanner input = new Scanner(System.in);
			String line = "";
			System.out.print("> ");
			
			// regex -> "(\\w)+"; // <comando>
			// regex -> "(\\w)+(\\s)(\\w){3}(\\s)(\\d)+";// <comando> <cod_acao> <inteiro>		
			// regex -> "(\\w)+(\\s)(\\w){3}"; //<comando> <cod_acao>		
			// regex -> "(\\w)+(\\s)(\\w){3}(\\s)(((\\d)+)|(\\d+[.]\\d+))";// <comando> <cod_acao> <double>				
			// regex -> "(\\w)+(\\s)(\\w){3}(\\s)(\\w)+(\\s)(((\\d)+)|(\\d+[.]\\d+))(\\s)(\\d)+"; // <comando> <cod_acao> <nome_acao> <double> <integer>
			
			while (!(line = input.nextLine().trim().toLowerCase()).equals("exit")) {				
				StringTokenizer lineTokenizer = new StringTokenizer(line, " ");
				if (lineTokenizer.hasMoreTokens()) {
					String command = lineTokenizer.nextToken();
									
					String[] entradaTokens = line.split(" ");
					
					switch(entradaTokens[0]){
					case "lerprecoacao":
						if(line.matches("(\\w)+(\\s)(\\w){3}"))
							System.out.println(server.lerPrecoAcao(clientName, entradaTokens[1].toUpperCase()));
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > lerprecoacao <cod_acao>");
						break;
					case "escreverpreco":
						if(line.matches("(\\w)+(\\s)(\\w){3}(\\s)(((\\d)+)|(\\d+[.]\\d+))"))
							server.escreverPrecoAcao(clientName, entradaTokens[1].toUpperCase(), Double.parseDouble(entradaTokens[2]));
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > escreverpreco <cod_acao> <valor>");
						break;												
					case "lernome":
						if(line.matches("(\\w)+(\\s)(\\w){3}"))
							System.out.println(server.lerNomeAcao(clientName, entradaTokens[1].toUpperCase()));
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > lernome <cod_acao>");
						break;
					case "lerquantidade":
						if(line.matches("(\\w)+(\\s)(\\w){3}"))
							System.out.println(server.lerQuantidadeAcoes(clientName, entradaTokens[1].toUpperCase()));
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > lerquantidade <cod_acao>");
						break;
					case "adicionar":
						if(line.matches("(\\w)+(\\s)(\\w){3}(\\s)(\\w)+(\\s)(((\\d)+)|(\\d+[.]\\d+))(\\s)(\\d)+"))
							server.adicionarNovaAcao(clientName, entradaTokens[2], entradaTokens[1].toUpperCase(), Double.parseDouble(entradaTokens[3]), Integer.parseInt(entradaTokens[4]));
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > adicionar <cod_acao> <nome_acao> <valor> <quantidade>");
						break;
					case "remover":
						if(line.matches("(\\w)+(\\s)(\\w){3}"))
							server.apagarAcao(clientName, entradaTokens[1].toUpperCase());
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > remover <cod_acao>");
						break;
					case "comprar":
						if(line.matches("(\\w)+(\\s)(\\w){3}(\\s)(\\d)+"))
							server.comprarAcoes(clientName, entradaTokens[1].toUpperCase(), Integer.parseInt(entradaTokens[2]));
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > comprar <cod_acao> <quantidade>");
						break;
					case "vender":
						if(line.matches("(\\w)+(\\s)(\\w){3}(\\s)(\\d)+"))
							server.venderAcoes(clientName, entradaTokens[1].toUpperCase(), Integer.parseInt(entradaTokens[2]));
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > vender <cod_acao> <quantidade>");
						break;
					case "listarusuarios":
						if(line.matches("(\\w)+"))
							System.out.println(server.listUsers());
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > listarusuarios");
						break;
					case "listaracoes":
						if(line.matches("(\\w)+"))
							System.out.println(server.listAcoes());
						else
							System.out.println("ERRO. Parametros invalidos! Modelo: > listaracoes");
						break;
					case "help":
						if(line.matches("(\\w)+")){
							System.out.println("|-----------------------------------------------------------");
							System.out.println("|  COMANDOS VALIDOS");
							System.out.println("|-----------------------------------------------------------");
							System.out.println("| > lerprecoacao <cod_acao>");
							System.out.println("| > escreverpreco <cod_acao> <valor>");
							System.out.println("| > lernome <cod_acao>");
							System.out.println("| > lerquantidade <cod_acao>");
							System.out.println("| > adicionar <cod_acao> <nome_acao> <valor> <quantidade>");
							System.out.println("| > remover <cod_acao>");
							System.out.println("| > comprar <cod_acao> <quantidade>");
							System.out.println("| > vender <cod_acao> <quantidade>");
							System.out.println("| > listarusuarios");
							System.out.println("| > listaracoes");
							System.out.println("| > exit");
							System.out.println("|-----------------------------------------------------------");
							System.out.println("| OBS: O codigo da acao deve ter exatamente 3 caracteres");
							System.out.println("|-----------------------------------------------------------");
						}else{
							System.out.println("ERRO. Parametros invalidos! Modelo: > help");
						}						
						break;
					default:						
						System.err.println("Comando não encontrado: " + command);
						break;
					}										
				}
				System.out.print("> ");
			}
			System.exit(0);
		}catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void receiveMsg(String msg) throws RemoteException {
		System.out.println();
		System.out.println(msg);
		System.out.println();
	}
}
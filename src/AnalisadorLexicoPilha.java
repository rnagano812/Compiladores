
import java.util.ArrayList;
import java.util.LinkedList;


public class AnalisadorLexicoPilha {
	private TabelaPilha automato ;
	///
	private LinkedList<event> entrada2;
	private token tokenDevolvido = null;
	private String est_atual;
	private int modo = 0;  // Modo 0 eh a usar a especificacao do automato, modo 1 eh ler caracter por caracter
	
	static final String[] ALFABETO = {"letra", "simbolo", "digito"};
	static final String [] ESTADOS = {"1","2","3","4","*5"};
	static final String[] LETRAS = {"a" , "b" , "c" ,  "d" , "e" , "f" , "g" , "h" , "i" , "j" , "k" , "l" , 
		"m" , "n" , "o" , "p" , "q" , "r" , "s" , "t" , "u" , "v" , "w" , "x" , "y" , "z"};
	static final String[] DIGITOS = {"0" , "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9"};
	static final String[] RESERVADAS = {"void","int", "bool", "return","func", "if", "else", "while","TRUE","FALSE"};
	
	ArrayList<String> variaveis;
	
	/**
	 * 
	 * @param entrada String de entrada
	 */
	public AnalisadorLexicoPilha (int modo,LinkedList<event> cadeia2, ArrayList<String> var){
		String [] nulo = new String [1] ;
		nulo[0] = null;
		automato = new TabelaPilha (ESTADOS.length, ALFABETO.length, ESTADOS, ALFABETO, "AnalisadorLexico", nulo);
		this.modo = modo;
		this.est_atual = automato.getNomePrimeiro();
		///
		entrada2 = cadeia2;
		variaveis = var;
	}
	
	/**
	 *   Insere as transicoes
	 */
	void inicializa() {
		
		// Transicoes para leitura de letras (que formam uma palavra) - Pode haver digitos apos a primeira letra
		automato.adicionaTransicao("1", "letra", "2");
		automato.adicionaTransicao("2", "letra", "2");
		automato.adicionaTransicao("2", "digito", "2");
		automato.adicionaTransicao("2", "simbolo", "5");
		
		
		// Transicoes para leitura de digitos (numeros)
		automato.adicionaTransicao("1", "digito", "3");
		automato.adicionaTransicao("3", "digito", "3");
		automato.adicionaTransicao("3", "simbolo", "5");
		automato.adicionaTransicao("3", "letra", "5");
		
		// Transicoes para leitura de simbolo
		automato.adicionaTransicao("1", "simbolo", "4");
		automato.adicionaTransicao("4", "digito", "5");
		automato.adicionaTransicao("4", "simbolo", "5");
		automato.adicionaTransicao("4", "letra", "5");
 		
	}
	
	/**
	 *  
	 * @return devolve o proximo token. Caso nao tenha mais, devolve null
	 */
	token getToken () {
		String caracter = null; // Caracter lido;
		String token = "";
		String [] nulo = new String [1];
		String saida = "";
		nulo[0] = null;
		boolean inicio = true;
		boolean[] transicaoVazia = {false};
	
		token out;
	
		
		if (entrada2.size() == 0) {
			out = new token ("eof","");
			return out ;
		}
		if (tokenDevolvido != null) {
			// Caso haja algum token que tenha sido devolvido, retorna ele e o zera
			token aux = tokenDevolvido;
			tokenDevolvido = null;
			return aux;
		}
		if (modo == 0) { 
			// Deve-se buscar o proximo token
			// Inicia o automato
			est_atual = automato.getNomePrimeiro();
			
			while (entrada2.size()!=0) {
				
				//caracter = String.valueOf(entrada.charAt(posEntrada));
				event entevent = entrada2.poll();
				caracter = String.valueOf(entevent.eventChar());
				
				if (ehLetra(caracter)) {
					// O caracter que esta sendo analisado eh letra
					est_atual = automato.proximoEstado(est_atual, ALFABETO[0], nulo, transicaoVazia);
					saida += caracter;
					if (inicio){
						token = "p";
						inicio = false;
					}
				}
				
				else if (ehDigito(caracter)) {
					// O caracter que esta sendo analisado eh digito
					est_atual = automato.proximoEstado(est_atual, ALFABETO[2], nulo, transicaoVazia);
					saida += caracter;
					if (inicio){
						token = "n";
						inicio = false;
					}
				}
				else {
					// O caracter que esta sendo analisado eh um simbolo
					est_atual = automato.proximoEstado(est_atual, ALFABETO[1], nulo, transicaoVazia);		
					saida += caracter;
					if (inicio){
						token = "s";
						inicio = false;
					}
				}
				
				// Verifica se eh estado final
				if (automato.ehEstadoFinal(est_atual)) {
					if (entevent.eventChar() != ' ' && entevent.eventChar() != '	'){
						entrada2.addFirst(entevent);
					}
					saida = saida.substring(0, saida.length()-1);
					
					if(token == "p"){
						for (int i = 0 ; i < RESERVADAS.length ; i ++) {
							if (RESERVADAS[i].equalsIgnoreCase(saida)) {
								out = new token(saida,"");
								return out;
							}
						}
						for (int i = 0 ; i < variaveis.size() ; i ++) {
							if (variaveis.get(i).equalsIgnoreCase(saida)) {
								out = new token("iden",String.valueOf(i));
								return out;
							}
							
						}
						
						variaveis.add(saida);
						out = new token("iden",String.valueOf(variaveis.size()-1));
						return out;
						
					}
					else if(token == "s"){
						out = new token (saida,saida);
						return out;
					}
					else if(token == "n"){
						out = new token ("num",saida);
						return out;
					}
				}
				
			}
			// Terminou de ler a entrada
			out = new token ("eof","");
			return out;
		}
		else if (modo == 1) {
			// ler caracter por caracter
			/*if (posEntrada < entrada.length()) {
				caracter = String.valueOf(entrada.charAt(posEntrada));
				posEntrada++;
				return caracter;
			}
			else {
				return token;
			}*/
		}
		out = new token ("eof","");
		return out;
	}
	
	/**
	 *  Volta o ultimo token
	 */
	void devolveUltimoToken(token token) {
		tokenDevolvido = token;
	}
	
	
	
	/**
	 *  Le o arquivo de entrada para saber que tokens usar
	 * @param nome_arquivo
	 */

	/**
	 * 
	 * @param entrada
	 * @return true caso entrada seja letra
	 */
	boolean ehLetra(String entrada) {
		for (int i = 0 ; i < LETRAS.length ; i ++) {
			if (LETRAS[i].equalsIgnoreCase(entrada)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param entrada
	 * @return true caso entrada seja digito
	 */
	boolean ehDigito(String entrada) {
		for (int i = 0 ; i < DIGITOS.length ; i ++) {
			if (DIGITOS[i].equalsIgnoreCase(entrada)) {
				return true;
			}
		}
		return false;
	}
	
	
}




import java.util.LinkedList;

public class AnalisadorLexicoWirth {
	private TabelaPilha automato ;
	///
	private LinkedList<event> entrada2;
	private token tokenDevolvido = null;
	private String est_atual;
	private int modo = 0;  // Modo 0 eh a usar a especificacao do automato, modo 1 eh ler caracter por caracter
	
	static final String[] ALFABETO = {"letra", "simbolo", "digito","aspas"};
	static final String [] ESTADOS = {"1","2","3","4","*5","6"};
	static final String[] LETRAS = {"a" , "b" , "c" ,  "d" , "e" , "f" , "g" , "h" , "i" , "j" , "k" , "l" , 
		"m" , "n" , "o" , "p" , "q" , "r" , "s" , "t" , "u" , "v" , "w" , "x" , "y" , "z"};
	static final String[] DIGITOS = {"0" , "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9"};
	static final String[] RESERVADAS = {"void","int", "bool", "return","func", "if", "else", "while","TRUE","FALSE"};
	
	
	/**
	 * 
	 * @param entrada String de entrada
	 */
	public AnalisadorLexicoWirth (LinkedList<event> cadeia2){
		String [] nulo = new String [1] ;
		nulo[0] = null;
		automato = new TabelaPilha (ESTADOS.length, ALFABETO.length, ESTADOS, ALFABETO, "AnalisadorLexico", nulo);
		this.est_atual = automato.getNomePrimeiro();
		///
		entrada2 = cadeia2;
		
		for(int i = 0 ; i<entrada2.size();i++){
			if(entrada2.get(i).eventChar() == ' ' || entrada2.get(i).eventChar() == '	'){
				entrada2.remove(i);
				i--;
			}
		}
	}
	
	/**
	 *   Insere as transicoes
	 */
	void inicializaWirth() {
		
		// Transicoes para leitura de letras (que formam uma palavra) - Pode haver digitos apos a primeira letra
		automato.adicionaTransicao("1", "letra", "2");
		automato.adicionaTransicao("2", "letra", "2");
		automato.adicionaTransicao("2", "digito", "2");
		automato.adicionaTransicao("2", "simbolo", "5");
		automato.adicionaTransicao("2", "aspas", "5");
		
		// Transicoes para leitura de digitos (numeros)
		automato.adicionaTransicao("1", "aspas", "3");
		automato.adicionaTransicao("3", "digito", "3");
		automato.adicionaTransicao("3", "simbolo", "3");
		automato.adicionaTransicao("3", "letra", "3");
		automato.adicionaTransicao("3", "aspas", "6");
		
		automato.adicionaTransicao("6", "aspas", "5");
		automato.adicionaTransicao("6", "digito", "5");
		automato.adicionaTransicao("6", "simbolo", "5");
		automato.adicionaTransicao("6", "letra", "5");
		
		// Transicoes para leitura de simbolo
		automato.adicionaTransicao("1", "simbolo", "4");
		automato.adicionaTransicao("4", "digito", "5");
		automato.adicionaTransicao("4", "simbolo", "5");
		automato.adicionaTransicao("4", "letra", "5");
		automato.adicionaTransicao("4", "aspas", "5");
	}
	
	/**
	 *  
	 * @return devolve o proximo token. Caso nao tenha mais, devolve null
	 */
	token getTokenWirth() {
		String caracter = null; // Caracter lido;
		String token = "";
		String [] nulo = new String [1];
		String saida = "";
		nulo[0] = null;
		boolean inicio = true;
		boolean[] transicaoVazia = {false};
	
		token out;
	
		
		if (tokenDevolvido != null) {
			// Caso haja algum token que tenha sido devolvido, retorna ele e o zera
			token aux = tokenDevolvido;
			tokenDevolvido = null;
			return aux;
		}
		if (entrada2.size() == 0) {
			out = new token ("eof","");
			return out ;
		}
		
		if (modo == 0) { 
			// Deve-se buscar o proximo token
			// Inicia o automato
			est_atual = automato.getNomePrimeiro();
			
			while (entrada2.size()!=0) {
				
				
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
					else if (caracter.startsWith("\"")) {
						// O caracter que esta sendo analisado aspas
						est_atual = automato.proximoEstado(est_atual, ALFABETO[3], nulo, transicaoVazia);
						saida += caracter;
						if (inicio){
							token = "t";
							inicio = false;
						}
					}
					else if (ehDigito(caracter)) {
						// O caracter que esta sendo analisado aspas
						est_atual = automato.proximoEstado(est_atual, ALFABETO[2], nulo, transicaoVazia);
						saida += caracter;
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
								
					entrada2.addFirst(entevent);
					
					saida = saida.substring(0, saida.length()-1);
					
					if(token == "p"){
						out = new token("N",saida);
						return out;
						
					}
					else if(token == "t"){
						if (entevent.eventChar() == '\"'&& saida.equals("\"\"")){
							entevent=entrada2.poll();
							saida = saida + entevent.eventChar();
						}
						out = new token ("T",saida);
						return out;
					}
					else if(token == "s"){
						
							out = new token (saida,saida);
						
						return out;
					}
				}
				
			}
			// Terminou de ler a entrada
			if(token == "p"){
				out = new token("N","");
				return out;
				
			}
			else if(token == "t"){
				out = new token ("T","");
				return out;
			}
			else if(token == "s"){
				
					out = new token (saida,saida);
				
				return out;
			}
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
	void devolveUltimoTokenWirth(token token) {
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

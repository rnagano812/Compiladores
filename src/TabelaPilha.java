
public class TabelaPilha {
	private String[][] transicoes;
	private int tamAlfabeto;
	private boolean[] indicesFinais;
	private String nome;
	private int linhas;
	private int colunas;
	
	/**
	 * 
	 * @param quantEstados
	 * @param tamAlfabeto 
	 * @param estados	Nome dos estados desta SubMaquina
	 * @param alfabeto	Alfabeto do automato
	 * @param nome		Nome desta subMaquina
	 * @param nomesSubM  As subMaquinas do automato
	 */
	TabelaPilha (int quantEstados, int tamAlfabeto, String[] estados, String[] alfabeto, String nome, String[] nomesSubM) {
		if (nomesSubM[0] != null)
			colunas = tamAlfabeto + nomesSubM.length + 2;
		else
			colunas = tamAlfabeto + 2;
		
		linhas = quantEstados + 1;
		indicesFinais = new boolean [linhas];
		transicoes = new String[linhas][colunas];
		this.nome = nome;
		this.tamAlfabeto = tamAlfabeto;
		
		// Zerando a matriz
		transicoes [0][0] = null;
		for (int i = 0; i < linhas ; i++) {
			for (int j = 0; j < colunas ; j++) {
				transicoes[i][j] = null;
			}
		}

		// Colocando os estados
		for (int i = 1 ; i < linhas ; i ++) {
			if (estados[i-1].startsWith("*")) {
				estados[i-1] = estados[i-1].substring(1);
				indicesFinais[i] = true;
				
			}
			else {
				indicesFinais[i] = false;
				
			}
			transicoes[i][0] = estados[i-1];
		}
		
		// Colocando o alfabeto, e outras subMaquinas
		int i;
		for (i = 1; i <= tamAlfabeto ; i++) {
			transicoes [0][i] = alfabeto[i-1];
		}

		if (nomesSubM[0] != null)
		for (int j = 0; j < nomesSubM.length; j++) {
			transicoes [0][i] = "<" + nomesSubM[j] + ">";
			i++;
		}
		
		
	}
	
	void adicionaTransicao (String nome_estado, String entrada, String prox_estado) {
		int indEstado = achaEstado (nome_estado);
		int indEntrada = achaElemento (entrada);
		int indProxEstado = achaEstado (prox_estado);
		
		if (indEstado != -1 && indEntrada != -1 && indProxEstado != -1) {
			transicoes[indEstado][indEntrada] = prox_estado;
			
		}
		else
			System.out.println("Foi passada uma transicao invalida");
		
		
	}

	/* devolve o indice do estado */
	int achaEstado (String nome_estado) {
		for (int i = 1; i <= linhas ; i++) {
			if (transicoes[i][0].equals(nome_estado)) {
				return i;
			}
		}
		return -1;
	}
	
	/* devolve o indice do elemento do alfabeto */
	int achaElemento (String elemento) {
		int i;
		for ( i = 1; i < colunas-1 ; i++) {
			if (transicoes[0][i].equals(elemento)) {
				return i;
			}
			else if (transicoes[0][i].contains(elemento)) {
				return i;
			}
		}
			
		// Entra na categoria outros
		return tamAlfabeto + 1;
	}
	
	String getNomePrimeiro() {
		return transicoes[1][0];
	}
	
	String proximoEstado( String estado_atual, String entrada, String[] abreSubMaquina, boolean[] transicaoVazia ){
		int indEstado = achaEstado(estado_atual);
		int indEntrada = achaElemento(entrada);
		abreSubMaquina[0] = null;

		// Caso seja uma transicao interna
		if (indEntrada <= tamAlfabeto && transicoes[indEstado][indEntrada] != null) {			
			if (indEstado > 0) {
				if (transicoes[indEstado][indEntrada] == null) {
					transicaoVazia[0] = true;
					return transicoes[indEstado][colunas-1];
				}
				else {
					transicaoVazia[0] = false;
					return transicoes[indEstado][indEntrada];
				}
			}
			else {
				return null;
			}
		}
		else {
			for (int i = tamAlfabeto + 1; i < colunas - 1; i++){
				if (transicoes[indEstado][i] != null) {
					// Transicao para subMaquina
					abreSubMaquina[0] = transicoes[0][i];
					abreSubMaquina[0] = abreSubMaquina[0].substring(1, abreSubMaquina[0].length()-1);
					
					return transicoes[indEstado][i];
				}
			}
			// Transicao Outros
			return transicoes[indEstado][colunas-1];
		}
	}
	

	boolean ehEstadoFinal (String nome_estado) {
		int indice = achaEstado (nome_estado);
		if (indice > 0)
			return indicesFinais[indice];
		else
			return false;
	}
	
	/**
	 *   
	 * @return Retorna o nome da tabela
	 */
	String getNomeTabela () {
		return this.nome;
	}
	
	/**
	 * 
	 * @param coluna
	 * @return
	 */
	boolean colunaVazia(int coluna) {
		boolean vazia = true;
		for (int i = 1; i < linhas && vazia; i++) {
			if (transicoes[i][coluna] != null)
				vazia = false;
		}
		return vazia;
	}
	
	void imprimetabela(){
		for (int i = 0;i<linhas ;i++){
			for(int j = 0; j<colunas;j++){
				if (transicoes[i][j]!= null)
				System.out.print(transicoes[i][j]+"|");
				else
					System.out.print(" |");
			}
			System.out.println();
		}
	}
}

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReconhecedorWirth {

	private String[] alfabeto;
	private TabelaPilha[] tabelas;
	private int quantSubM;
	private String[] nomeSubM;
	
	AnalisadorLexicoWirth analisador;
	
	public ReconhecedorWirth(AnalisadorLexicoWirth ALP){
		analisador = ALP;
	}
	/**
	 *  Faz a leitura da especificacao
	 * @param nome_arquivo
	 * @throws IOException 
	 */
	void lerEspecificacao (String nome_arquivo) throws IOException {
		
			// Abrir arquivo
			FileInputStream fstream = new FileInputStream(nome_arquivo);
			
			// Obter objeto DataInputStream
			DataInputStream in = new DataInputStream (fstream);
			BufferedReader br = new BufferedReader (new InputStreamReader(in));
			String strLine;
			
			// Ler uma linha
			strLine = br.readLine();
			
			
			// Ler o numero de SubMaquinas
			// Le do inicio ateh o aparecimento do primeiro espaco
			quantSubM = Integer.parseInt(strLine.substring(0, strLine.indexOf(" ")));
			
			tabelas = new TabelaPilha[quantSubM];
			nomeSubM = new String [quantSubM];
			
			
			
			int primeiraAspas = 0;
			int segundaAspas = 0;
			
			// Le o nome das subMaquinas
			for (int j = 0 ; j < quantSubM ; j++) {
				primeiraAspas = strLine.indexOf("\"", segundaAspas);

				segundaAspas = strLine.indexOf("\"", primeiraAspas+1); // Comeca a ler apos a aspas encontrada no primeira Aspas

				nomeSubM[j] = strLine.substring(primeiraAspas+1, segundaAspas);
				
				segundaAspas ++;
				
			}
			
			
			// Ler a segunda linha do arquivo
			strLine = br.readLine();
			
			// Ler a cardinalidade do alfabeto
			// Le do inicio ateh o aparecimento do primeiro espaco
			int tamAlf = Integer.parseInt(strLine.substring(0, strLine.indexOf(" ")));
			alfabeto = new String[tamAlf];
			
			primeiraAspas = 0;
			segundaAspas = 0;

			for (int j = 0 ; j < tamAlf ; j++) {
				primeiraAspas = strLine.indexOf("\"", segundaAspas);

				segundaAspas = strLine.indexOf("\"", primeiraAspas+1); // Comeca a ler apos a aspas encontrada no primeira Aspas

				String caracter = strLine.substring(primeiraAspas+1, segundaAspas);
				
				// Adiciona ao alfabeto
				alfabeto[j] = caracter;
				segundaAspas ++;
			}
			
			for (int i = 0; i < quantSubM; i ++) {
				strLine = br.readLine();
				
				// Ler o numero de estados
				// Le do inicio ateh o aparecimento do primeiro espaco
				int qEstados = Integer.parseInt(strLine.substring(0, strLine.indexOf(" ")));
				
				String [] estados = new String [qEstados];
				
				
				primeiraAspas = 0;
				segundaAspas = 0;
				
				for (int j = 0 ; j < qEstados ; j++) {
					primeiraAspas = strLine.indexOf("\"", segundaAspas);

					segundaAspas = strLine.indexOf("\"", primeiraAspas+1); // Comeca a ler apos a aspas encontrada no primeira Aspas

					estados[j] = strLine.substring(primeiraAspas+1, segundaAspas);
					
					segundaAspas ++;
					
				}
				

				tabelas[i] = new TabelaPilha(qEstados, tamAlf, estados, alfabeto, nomeSubM[i], nomeSubM);
				
				// Ler as proximas linhas que contem as transicoes
				// O caracter '/' indica o final das transicoes
				strLine = br.readLine();
				
				
				while (!strLine.isEmpty()) {
					int virgula1 = strLine.indexOf(",");
					int virgula2 = strLine.lastIndexOf(",");
					tabelas[i].adicionaTransicao(strLine.substring(0, virgula1), strLine.substring(virgula1+1, virgula2), strLine.substring(virgula2+1));

					strLine = br.readLine();
					
				}
				System.out.println();
			}
		
			
			//Close the input stream
			in.close();
			fstream.close();
	}

	/**
	 * Le a cadeia de entrada
	 * @param nome_arquivo
	 */
	void lerCadeia (boolean imprime) {
	
			Boolean erro = false;
			String est_atual = tabelas[0].getNomePrimeiro();
			String subMaquina_atual = tabelas[0].getNomeTabela();
			Pilha pilha = new Pilha();
			boolean[] transicaoVazia = {false};
			token token;
			
			if (imprime) {
				System.out.print("SubMaq	|estado		|final		|entrada(token)	|valorSem	|chamada	|pilha		|aceita	");
			System.out.println();
			
			}
			
			token = analisador.getTokenWirth();
			
			while(token.retornaTipo() != "eof" && !erro) {
				int indTabelaAtual = achaTabela(subMaquina_atual);
				String[] abreSubMaquina = new String[1];   // Se seu valor for diferente de null quer dizer que houve chamada para alguma submaquina
				abreSubMaquina[0] = null;
				String salva_estado = est_atual;
				
				
				if (imprime)
					System.out.print("" + subMaquina_atual + "	|	" + est_atual + "	|	" + tabelas[indTabelaAtual].ehEstadoFinal(salva_estado) +
										"	|	" + token.retornaTipo() +"	|	"+token.retornaSemantico());
				
				est_atual = tabelas[indTabelaAtual].proximoEstado(est_atual, token.retornaTipo(), abreSubMaquina, transicaoVazia);
				
				if (abreSubMaquina[0] != null) {
					// houve chamada para uma SubMaquina
					// devolver token
					analisador.devolveUltimoTokenWirth(token);
					
					// empilhar o estado de retorno e a informacao sobre a maquina corrente
					pilha.adiciona(subMaquina_atual, est_atual);
					
					// desviar para o estado inicial da subMaquina chamada
					subMaquina_atual = abreSubMaquina[0];
					est_atual = tabelas[achaTabela(subMaquina_atual)].getNomePrimeiro();					
					
					if (imprime)
						System.out.print("	|	" + subMaquina_atual);
					
				}
				else if (est_atual != null) {
					// Houve uma transicao interna
					if (transicaoVazia[0] == false) {
						if (imprime)
						System.out.print("	|	");
					}	
					else {
						// Foi uma transicao em vazio, devolver o token
						if (imprime)
							System.out.print("	|	");
						analisador.devolveUltimoTokenWirth(token);
					}
					
				}
				else {
					// Nao ha uma transicao a realizar
					// Verificar se eh um estado final
					if (tabelas[indTabelaAtual].ehEstadoFinal(salva_estado)) {
						
						// devolve o token
						analisador.devolveUltimoTokenWirth(token);
						
						if (imprime)
							System.out.print("	|	");
						
						// Desempilhar
						subMaquina_atual = pilha.nomeSubMaquina();
						est_atual = pilha.nomeEstado();
						pilha.retira();
						
					}
					else {
						if (imprime)
							System.out.println("ERRO: Transicao invalida");
						erro = true;
					}
				}
				if (imprime && !erro) {
					System.out.print("	|	");
					pilha.imprimir();
					System.out.print("	|	");

					System.out.print("não");
				
					System.out.println("");
				}
				
				
				token = analisador.getTokenWirth();		
				
				// fim do loop
			}
			
		
			
			
			
			while (!pilha.vazia() && !erro) {

				
				if (imprime) {
					System.out.print(subMaquina_atual + "	|	" + est_atual + "	|	");
				}
				
				if (tabelas[achaTabela(subMaquina_atual)].ehEstadoFinal(est_atual)) {
					
					if (imprime) {
						System.out.print("true	|	eof	|		|		|	");
						pilha.imprimir();
						if (pilha.vazia()) {
							System.out.print("	|	sim\n");
						}
						else
							System.out.print("	|	não\n");
					}
					subMaquina_atual = pilha.nomeSubMaquina();
					est_atual = pilha.nomeEstado();
					pilha.retira();
					analisador.devolveUltimoTokenWirth(new token("eof",""));
					
					analisador.getTokenWirth();
				}
				else {
					if (imprime){
						System.out.print("false	|	eof	|		|		|		|	");
						pilha.imprimir();
						if (pilha.vazia()) {
							System.out.print("	|	sim\n");
						}
						else
							System.out.print("	|	não\n");
						System.out.print("\nNao terminou com a pilha vazia");
					}
					erro = true;
				}


			}
			
			if (token.retornaTipo()=="eof" && pilha.vazia() && imprime && !erro) {
				System.out.print(subMaquina_atual + "	|	" + est_atual + "	|	");
				if (tabelas[achaTabela(subMaquina_atual)].ehEstadoFinal(est_atual)) {
					
						System.out.print("true	|	eof	|		|		|	");
						pilha.imprimir();
						System.out.print("	|	sim\n");
				}
				else {
					
						System.out.print("false	|	eof	|		|		|	");
						pilha.imprimir();
						System.out.print("	|	sim\n");
				}
			}
			
			
			System.out.println("");
			if (erro == false) {
				
				if (tabelas[achaTabela(subMaquina_atual)].ehEstadoFinal(est_atual)) 
					System.out.println("A cadeia é aceita.");
				else
					System.out.println("A cadeia não é aceita.");
			}
			else
				System.out.println("A cadeia não é aceita.");
			
		
		
	}
	
	
	/**
	 * 
	 * @param nome
	 * @return Devolve o indice da tabela cujo nome foi passado
	 */
	int achaTabela (String nome) {
		for (int i = 0; i < tabelas.length ; i++) {
			if (tabelas[i].getNomeTabela().equals(nome)) {
				return i;
			}
		}
		return -1;
	}
	

}

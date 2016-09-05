
public class Pilha {
	static final int MAX = 50;
	private String[] subMaquinas = new String [MAX];
	private String [] estados = new String[MAX];
	private int pos = -1;
	

	/**
	 *  Adiciona o elemento na pilha
	 * @param nome_subMaquina
	 * @param nome_estado
	 * @return true se os valores foram adicionas
	 * 			false se a pilha esta cheia e nao foi possivel adiciona-los
	 */
	boolean adiciona (String nome_subMaquina, String nome_estado) {
		
		if (pos < MAX-1) {
			pos++;
			subMaquinas[pos] = nome_subMaquina;
			estados[pos] = nome_estado;
			return true;
		}
		else {
			// Deu overflow
			System.out.println("ERRO: Pilha cheia");
			return false;
		}
	}
	
	/**
	 * 
	 * @return o nome da SubMaquina no topo da pilha
	 */
	String nomeSubMaquina () {
		return subMaquinas[pos];
	}
	/**
	 * 
	 * @return o nome do estado no topo da pilha
	 */
	String nomeEstado () {
		return estados[pos];
	}
	
	/**
	 *  Decrementa a pos da pilha
	 */
	void retira() {
		if (pos >= 0 )
			pos--;
	}
	
	/**
	 *  Verifica se a pilha esta vazia
	 * @return true se pilha vazia e false caso contrario
	 */
	boolean vazia() {
		if (pos == -1) {
			return true;
		}
		else
			return false;
	}
	
	int getPos () {
		return this.pos;
	}
	
	
	/**
	 * Imprime os elementos da pilha
	 */
	void imprimir() {
		if(this.vazia()) {
			System.out.print(" vazia ");
		}
		else {
			for (int i = 0; i <= pos ; i++) {
				System.out.print(subMaquinas[i] + estados[i] + ",");
			}
		}
	}
}
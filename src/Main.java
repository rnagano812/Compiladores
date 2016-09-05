
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException{
			
			Scanner reader = new Scanner(System.in);
			LinkedList<event> ASCII;
			boolean rastreamento = true;
			
			System.out.println("Escreva o nome do arquivo a ser lido:");
			String file = reader.next();
			System.out.println("Ligar rastreamento da pilha? (1-sim 0-Não)");
			int rast = reader.nextInt();
			
			if (rast == 1)
				rastreamento = true;
			else if (rast == 0)
				rastreamento = false;
			
		    Leitor leitor = new Leitor(file);
		    ASCII = leitor.lerArquivo();
		    
		    reader.close();
		  	
		    //de variaveis a ser criada
		    //ArrayList<String> variaveis = new ArrayList<String>();
		    
		    
		    //Analisador lexico deve receber a lista dos caracteres
		    AnalisadorLexicoWirth analisador = new AnalisadorLexicoWirth(0,ASCII);
			analisador.inicializaWirth();
			
			ReconhecedorWirth wirth = new ReconhecedorWirth(analisador);
			wirth.lerEspecificacao("especificacaoWirth.txt");
			wirth.lerCadeia(rastreamento);
			/*
			token test;
			test = analisador.getTokenWirth();
			while(test.retornaTipo() != "eof") {
				if(true){
					test.print();
				}
				test = analisador.getTokenWirth();
			}
			if(true){
				test.print();
			}*/
			
	}
}
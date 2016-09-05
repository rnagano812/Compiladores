import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Leitor {

	File arquivo;
    FileReader fr;
    BufferedReader br;
    String line;
    int lineNumber = 0;
    
	public Leitor(String file) throws FileNotFoundException{
		arquivo = new File(file);
		fr = new FileReader(arquivo);
		br = new BufferedReader(fr);
	}
	
	public void fecharLeitor() throws IOException{
	    br.close();
	    fr.close();
	}
	
	public LinkedList<event> lerArquivo() throws IOException{
		//criacao da fila com seu motor associado
	    eventLine Fila = new eventLine();
	    
	    //Leitura do arquivo por linhas
	    while((line = br.readLine()) != null){
	       
	        //Envio de cara caractere para a fila do motor
	        for(int i = 0; i < line.length(); i++){
	        	event charac = new event(line.charAt(i));
	        	Fila.addElement(charac);
	        }
	    
	    }
	    
	    return Fila.retornaASCII();
	}
}

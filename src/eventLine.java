import java.util.LinkedList;


public class eventLine {
	
	private int lastLine = 0;
	private LinkedList<event> eventA;
	motorEventos Engine;
	private LinkedList<event> ASCII;
	
	//contrutor da fila com seu motor associado
	public eventLine(){
		Engine = new motorEventos(this);
		eventA = new LinkedList<event>();
		ASCII = new LinkedList<event>();
		
	}
	
	
	//adiciona elemento na fila
	public void addElement(event add){
		eventA.add(add);
		lastLine++;
		
		if(lastLine-1 == 0){
			Engine.tratarFila(ASCII);
		}
	}
	
	//retira elemento da fila, retorna null se fila estiver vazia
	public event pullElement(){
		
		event temp;
		
		if(lastLine > 0){
			temp = eventA.remove();
			lastLine--;
		}
		else temp = null;
		return temp;
	}
	
	//retorna lista com os caracteres lidos e classificados
	public LinkedList<event> retornaASCII(){
		
		return ASCII;
	}
	
	
	
	
}

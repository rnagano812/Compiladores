import java.util.LinkedList;


public class motorEventos {
	
	private eventLine line;
	
	public motorEventos(eventLine lin){
		line = lin;
	}

	public void tratarFila(LinkedList<event> ASCII) {
		
			event currentEvent;
			while((currentEvent = line.pullElement())!= null){
				
				char currentChar = currentEvent.eventChar();
				
				if ((int)currentChar>=48 && (int)currentChar <=57){
					currentEvent.setCharType("digito");
				}
				else if (((int)currentChar>=65 && (int)currentChar <=90)||((int)currentChar>=97 && (int)currentChar <=122)){
					currentEvent.setCharType("letra");
				}
				else{
					currentEvent.setCharType("other");
				}
				
				ASCII.add(currentEvent);
			}
		}
	}


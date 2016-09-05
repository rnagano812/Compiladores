
public class event {
	
	private String eventType;
	
	//tipo char
	private char charac;
	private String charType = "";
	//tipo token
	
	
//CHAR EVENTS
	//contructor for char events
	public event(char carac){
		charac = carac;
		eventType =  "char";
	}
	
	public char eventChar(){
		return charac;
	}
	
	public void setCharType(String type){
		charType = type;
	}
	
	public void printEvent(){
		if(eventType == "char"){
			System.out.println(charac + " - ASCII:"+ (int)charac+ " Hex:" + Integer.toHexString((int)charac)+ " " +charType);
		}
	}
}

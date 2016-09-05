
public class token {

	String tipo;
	String valorSemantico;
	
	public token(String ti, String v_Seman){
		tipo = ti;
		valorSemantico = v_Seman;
	}
	
	public void print(){
		System.out.println("("+tipo+", "+valorSemantico+")");
	}
	
	public String retornaTipo(){
		return tipo;
	}
	
	public String retornaSemantico(){
		return valorSemantico;
	}
}

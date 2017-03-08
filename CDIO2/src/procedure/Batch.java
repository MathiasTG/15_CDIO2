package procedure;

public class Batch {
	private int id;
	private String name;
	public Batch(int id, String name){
		this.id=id;
		this.name=name;
	}
	public int getID(){
		return id;
	}
	public String getName(){
		return name;
	}
}

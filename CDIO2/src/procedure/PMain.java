package procedure;

import java.io.IOException;

public class PMain {
	public static void main(String[] args) throws IOException {
		// ConnectionLogic l = new ConnectionLogic("169.254.2.X");//for the physical wheight. Replace x with value
		ConnectionLogic l = new ConnectionLogic("127.0.0.1");//for simulator. 
					
				//Remember to run the main in controller.Main.java
		l.weighingProcedure();
	}
}

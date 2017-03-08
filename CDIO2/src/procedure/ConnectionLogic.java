package procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionLogic {
	String sentence = null;
	String answerFromServer = null;
	List<Operator> operatorArray= new ArrayList<Operator>();
	List<Batch> batchArray= new ArrayList<Batch>();
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	Socket clientSocket;
	PrintWriter outToServer;
	BufferedReader inFromServer;

	public ConnectionLogic() {
		try {
			clientSocket = new Socket("169.254.2.3", 8000);
			outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			operatorArray.add(new Operator(12,"Anders And"));
			batchArray.add(new Batch(1234,"Salt"));
		} catch (IOException e) {
			System.out.println("Konstruktør fejl");
			e.printStackTrace();
		}
	}
	public static void main (String[] args){
		ConnectionLogic l = new ConnectionLogic();
		l.weighingProcedure();
		
	}
	
	public void weighingProcedure() {
		try {
			inFromServer.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String answer =outputToServer("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
		answer=answer.split("\"")[1];
		boolean existed=false;
		for(int i =0;i<operatorArray.size();i++){
			existed=true;
			if(answer.equals(String.valueOf(operatorArray.get(i).getID()))){
				answer =outputToServer("RM20 8 \""+operatorArray.get(i).getName()+"?"+"\" \"\" \"&3\"");
				break;
			}
			existed=false;
		}
		
			
	}
	public String outputToServer(String outputToServer) {
		try {
			while (true) {
				outToServer.println(outputToServer);
				answerFromServer = inFromServer.readLine();
				if (answerFromServer.equals("RM20 B")) {
					while (!inFromServer.ready()) {
						Thread.sleep(200);
					}
					answerFromServer = inFromServer.readLine();
					return answerFromServer;
				} else {
					return answerFromServer;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Fejl i inputOutput metoden";
		}
	}
}

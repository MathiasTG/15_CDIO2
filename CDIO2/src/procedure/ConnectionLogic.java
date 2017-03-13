package procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionLogic {

	String answerFromServer = null;
	List<Operator> operatorArray;
	List<Batch> batchArray;
	String answer;
	boolean existed;

	BufferedReader inFromUser;

	Socket clientSocket;
	PrintWriter outToServer;
	BufferedReader inFromServer;

	public ConnectionLogic() {
		// initializing Reader and operator array, batch array.
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		operatorArray = new ArrayList<Operator>();
		batchArray = new ArrayList<Batch>();
		// Adds an object to operatorArray and batchArray
		operatorArray.add(new Operator(12, "Anders And"));
		batchArray.add(new Batch(1234, "Salt"));
		// User enters the IP-address of the weight
		System.out.println("Enter the IP-address of the weight:");
		try {
			String ip = inFromUser.readLine();
			clientSocket = new Socket(ip, 8000);
			outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to the specified IP");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in constructor");
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// ConnectionLogic l = new ConnectionLogic();
	// l.weighingProcedure();
	// }

	public void weighingProcedure() {
		try {
			inFromServer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		answer = outputToServer("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
		answer = answer.split("\"")[1];
		existed = false;
		while (true) {
			for (int i = 0; i < operatorArray.size(); i++) {
				existed = true;
				if (answer.equals(String.valueOf(operatorArray.get(i).getID()))) {
					answer = outputToServer("RM20 8 \"" + operatorArray.get(i).getName() + "?" + "\" \"\" \"&3\"");
					if(answer.startsWith("RM20 A"))
						break;
					else
						answer=outputToServer("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
						answer=answer.split("\"")[1];
						break;
				}
				existed = false;
			}
			if (existed == true)
				break;
			else {
				answer = outputToServer("RM20 8 \"No operator found. Enter new ID.\" \"\" \"&3\"");
				answer = answer.split("\"")[1];
			}

		}

		answer = outputToServer("RM20 8 \"Enter Batch-ID\" \"\" \"&3\"");
		answer = answer.split("\"")[1];
		existed = false;
		while (true) {
			for (int i = 0; i < batchArray.size(); i++) {
				existed = true;
				if (answer.equals(String.valueOf(batchArray.get(i).getID()))) {
					answer = outputToServer("RM20 8 \"" + operatorArray.get(i).getName() + "?" + "\" \"\" \"&3\"");
					break;
				}
				existed = false;
			}
			if (existed == true)
				break;
			else {
				answer = outputToServer("RM20 8 \"Batch not found. Enter new Batch-ID.\" \"\" \"&3\"");
				answer = answer.split("\"")[1];
			}

		}

	}

	public String outputToServer(String outputToServer) {
		try {
			while (inFromServer.ready())
				inFromServer.skip(1);// clears the buffer.

			outToServer.println(outputToServer);
			answerFromServer = inFromServer.readLine();

			// IF the message is the (RM 20 8 "TEXT" "" "&3") type, the
			// following if statement is initiated.
			// this is done because the RM type of message is answered two
			// times, confirmation of message received,
			// and then the answer from the user.
			if (answerFromServer.startsWith("RM20 B")) {

				while (!inFromServer.ready()) {// while the buffer is empty, do
												// sleep.
					Thread.sleep(200);// This is to spare the processor some
										// workload
				}

				answerFromServer = inFromServer.readLine();

				return answerFromServer;

			} else {
				return answerFromServer;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in outputToServer method");
			return "";// this is only to fulfill compiler demands.
		}
	}
}

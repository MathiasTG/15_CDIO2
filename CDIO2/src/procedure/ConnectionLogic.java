package procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionLogic implements Runnable {
	String answerFromServer = null;
	List<Operator> operatorArray;
	List<Batch> batchArray;
	String answer;
	boolean existed;
	String ip;

	double taraWeight;
	double nettoWeight;
	double bruttoWeight;
	BufferedReader inFromUser;

	Socket clientSocket;
	PrintWriter outToServer;
	BufferedReader inFromServer;
	int i = 0;
	private Thread t;
	private String threadName="Klient";

	public void start() {
		System.out.println("Starting " + threadName);

		if(t==null){
			t= new Thread(this,threadName);
			t.start();
		}
		

	}

	@Override
	public void run() {
		System.out.println("Running Klient");
		
		
		// initializing Reader and operator array, batch array.
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		operatorArray = new ArrayList<Operator>();
		batchArray = new ArrayList<Batch>();
		// Adds an object to operatorArray and batchArray
		operatorArray.add(new Operator(12, "Anders And"));
		batchArray.add(new Batch(1234, "Salt"));
		// User enters the IP-address of the weight

		try {
			// String ip = inFromUser.readLine();
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
		try {
			weighingProcedure();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Klien exiting");
	}

	public ConnectionLogic(String ip) {
		this.ip=ip;
		System.out.println("Creating Thread Klient");

	}

	// public static void main(String[] args) throws IOException {
	// ConnectionLogic l = new ConnectionLogic("169.254.2.3");
	// l.weighingProcedure();
	// }

	public String outputToServer(String outputToServer) {
		try {
			outToServer.println(outputToServer);
			answerFromServer = inFromServer.readLine();
			if (answerFromServer.startsWith("I4")) {
				answerFromServer = inFromServer.readLine();
			} else if (answerFromServer.startsWith("RM20 I")) {
				answerFromServer = inFromServer.readLine();
			}

			// IF the message is the (RM 20 8 "TEXT" "" "&3") type, the
			// following if statement is initiated.
			// this is done because the RM type of message is answered two
			// times, confirmation of message received,
			// and then the answer from the user.
			if (answerFromServer.startsWith("RM20 B")) {
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

	public void weighingProcedure() throws IOException {

		//// outToServer.println("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
		// try {
		// inFromServer.readLine();
		// outToServer.println("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
		// inFromServer.readLine();
		// answer=inFromServer.readLine();
		//// answer = answer.split("\"")[1];
		// System.out.println(answer);
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		// }

		// try {
		// System.out.println(inFromServer.readLine());
		//
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		answer = outputToServer("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
		while (answer.equals("RM20 C")) {
			answer = outputToServer("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
		}
		answer = answer.split("\"")[1];
		existed = false;
		while (true) {
			for (i = 0; i < operatorArray.size(); i++) {
				existed = true;
				if (answer.equals(String.valueOf(operatorArray.get(i).getID()))) {
					answer = outputToServer("RM20 8 \"" + operatorArray.get(i).getName() + "?" + "\" \"\" \"&3\"");
					break;
				}
				existed = false;
			}
			if (existed == true) {
				if (answer.startsWith("RM20 A"))
					break;
				else {
					answer = outputToServer("RM20 8 \"Enter Operator-ID\" \"\" \"&3\"");
					answer = answer.split("\"")[1];
				}
			} else {
				answer = outputToServer("RM20 8 \"Try again\" \"\" \"&3\"");
				System.out.println(answer);
				answer = answer.split("\"")[1];
			}
		}

		answer = outputToServer("RM20 8 \"Enter Batch-ID\" \"\" \"&3\"");
		while (answer.equals("RM20 C")) {
			answer = outputToServer("RM20 8 \"Enter Batch-ID\" \"\" \"&3\"");
		}
		answer = answer.split("\"")[1];

		existed = false;
		while (true) {
			for (i = 0; i < batchArray.size(); i++) {
				existed = true;
				if (answer.equals(String.valueOf(batchArray.get(i).getID()))) {
					answer = outputToServer("RM20 8 \"" + batchArray.get(i).getName() + "?" + "\" \"\" \"&3\"");
					break;
				}
				existed = false;
			}
			if (existed == true) {
				if (answer.startsWith("RM20 A")) {
					outputToServer("P111 \"" + batchArray.get(i).getName() + "\"");
					break;
				} else {
					answer = outputToServer("RM20 8 \"Enter Batch-ID\" \"\" \"&3\"");
					answer = answer.split("\"")[1];
				}
			} else {
				answer = outputToServer("RM20 8 \"Try again\" \"\" \"&3\"");
				answer = answer.split("\"")[1];
			}
		}

		answer = outputToServer("RM20 8 \"Unload weight\" \"\" \"&3\"");
		// answer = answer.split("\"")[1];
		while (true) {
			if (answer.startsWith("RM20 A")) {
				outputToServer("T");
				break;
			} else {
				answer = outputToServer("RM20 8 \"UNLOAD WEIGHT!\" \"\" \"&3\"");
			}
		}

		answer = outputToServer("RM20 8 \"Place tara\" \"\" \"&3\"");
		while (true) {
			if (answer.startsWith("RM20 A")) {
				outputToServer("B 0.400");// This is for the virtual
											// weight-simulator
				taraWeight = Double.parseDouble(outputToServer("S").replaceAll("[^-\\d.]", ""));// check
																								// om
																								// det
																								// virker
				answer = outputToServer("RM20 8 \"Tara: " + taraWeight + " kg\" \"\" \"&3\"");
				if (answer.startsWith("RM20 A")) {
					outputToServer("T");
					break;
				}
			} else {
				answer = outputToServer("RM20 8 \"PLACE TARA!\" \"\" \"&3\"");
			}
		}

		answer = outputToServer("RM20 8 \"Place netto\" \"\" \"&3\"");
		// answer = answer.split("\"")[1];
		while (true) {
			if (answer.startsWith("RM20 A")) {
				outputToServer("B 1.000");// This is for the virtual
											// weight-simulator
				nettoWeight = Double.parseDouble(outputToServer("S").replaceAll("[^-\\d.]", ""));
				answer = outputToServer("RM20 8 \"Netto: " + nettoWeight + " kg\" \"\" \"&3\"");
				if (answer.startsWith("RM20 A")) {
					outputToServer("T");
					break;
				}
			} else {
				answer = outputToServer("RM20 8 \"PLACE NETTO!\" \"\" \"&3\"");
			}
		}
		answer = outputToServer("RM20 8 \"Remove brutto\" \"\" \"&3\"");
		// answer = answer.split("\"")[1];
		while (true) {
			if (answer.startsWith("RM20 A")) {
				outputToServer("F");// This is for the virtual weight-simulator
				bruttoWeight = Double.parseDouble(outputToServer("S").replaceAll("[^-\\d.]", ""));
				answer = outputToServer("RM20 8 \"Brutto: " + bruttoWeight + " kg\" \"\" \"&3\"");
				if (answer.startsWith("RM20 A")) {
					outputToServer("T");
					break;
				}
			} else {
				answer = outputToServer("RM20 8 \"REMOVE BRUTTO!\" \"\" \"&3\"");
			}
		}

		answer = outputToServer("RM20 8 \"OK or discard?\" \"\" \"&3\"");
		// answer = answer.split("\"")[1];
		while (true) {
			if (answer.startsWith("RM20 A")) {
				answer = outputToServer("RM20 8 \"Process done\" \"\" \"&3\"");
				System.out.println("Tara: " + taraWeight + "\nNetto: " + nettoWeight + "\nBrutto: " + bruttoWeight);
				outputToServer("P111 \"\"");
				// inFromServer.readLine();
				break;
			} else {
				answer = outputToServer("RM20 8 \"Data deleted\" \"\" \"&3\"");
				outputToServer("P111 \"\"");
				// inFromServer.readLine();
				break;
			}
		}
	}

}

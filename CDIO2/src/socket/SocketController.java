package socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import socket.SocketInMessage.SocketMessageType;
import weight.IWeightInterfaceController;

public class SocketController implements ISocketController {
	Set<ISocketObserver> observers = new HashSet<ISocketObserver>();
	//TODO Maybe add some way to keep track of multiple connections?
	private BufferedReader inStream;
	private DataOutputStream outStream;
	private IWeightInterfaceController weightControl;
	private SocketInMessage sockInMsg;
	
	


	@Override
	public void registerObserver(ISocketObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unRegisterObserver(ISocketObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void sendMessage(SocketOutMessage message) {
		if (outStream!=null){
			//TODO send something over the socket! 
			try {
				outStream.writeBytes(message.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//TODO maybe tell someone that connection is closed?
		}
	}

	@Override
	public void run() {
		//TODO some logic for listening to a socket //(Using try with resources for auto-close of socket)
		try (ServerSocket listeningSocket = new ServerSocket(Port)){ 
			while (true){
				waitForConnections(listeningSocket); 	
			}		
		} catch (IOException e1) {
			// TODO Maybe notify MainController?
			e1.printStackTrace();
		} 


	}

	private void waitForConnections(ServerSocket listeningSocket) {
		try {
			Socket activeSocket = listeningSocket.accept(); //Blocking call
			inStream = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
			outStream = new DataOutputStream(activeSocket.getOutputStream());
			String inLine;
			//.readLine is a blocking call 
			//TODO How do you handle simultaneous input and output on socket?
			//TODO this only allows for one open connection - how would you handle multiple connections?
			while (true){
				inLine = inStream.readLine();
				System.out.println(inLine);
				if (inLine==null) break;
				switch (inLine.split(" ")[0]) {
				case "RM20": // Display a message in the secondary display and wait for response
					//TODO implement logic for RM command
					switch (inLine.split(" ")[1]) {
					case "4":
						notifyObservers(new SocketInMessage(SocketMessageType.RM204, inLine.split(" ")[2]));
						break;
					case "8":
						notifyObservers(new SocketInMessage(SocketMessageType.RM208, inLine));
						break;
					}
					break;
				case "D":// Display a message in the primary display
					//TODO Refactor to make sure that faulty messages doesn't break the system
					notifyObservers(new SocketInMessage(SocketMessageType.D, inLine.split(" ")[1])); 			
					break;
				case "DW": //Clear primary display
					//TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.DW, inLine.split(" ")[0]));
					break;
				case "P111": //Show something in secondary display
					//TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.P111, inLine.split(" ")[1])); 
					break;
				case "T": // Tare the weight
					//TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.T, inLine.split(" ")[0]));
					break;
				case "S": // Request the current load
					//TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.S, inLine.split(" ")[0]));
					break;
				case "K":
					if (inLine.split(" ").length>1){
						notifyObservers(new SocketInMessage(SocketMessageType.K, inLine.split(" ")[1]));
					}
					break;
				case "B": // Set the load
					//TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.B, inLine.split(" ")[1]));
					break;
				case "Q": // Quit
					//TODO imp
					notifyObservers(new SocketInMessage(SocketMessageType.Q, inLine.split(" ")[0]));
					break;
				case "F":
					notifyObservers(new SocketInMessage(SocketMessageType.F, inLine.split(" ")[0]));
					break;
					
				default: //Something went wrong?
					//TODO implement
					break;
				}
			}
		} catch (IOException e) {
			//TODO maybe notify mainController?
			e.printStackTrace();
		}
	}

	private void notifyObservers(SocketInMessage message) {
		for (ISocketObserver socketObserver : observers) {
			socketObserver.notify(message);
		}
	}
}


package controller;

import java.util.ArrayList;

import com.sun.xml.internal.ws.api.message.Message;

import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
import socket.SocketOutMessage;
import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;

/**
 * MainController - integrating input from socket and ui. Implements ISocketObserver and IUIObserver to handle this.
 * @author Christian Budtz
 * @version 0.1 2017-01-24
 *
 */
public class MainController implements IMainController, ISocketObserver, IWeightInterfaceObserver {

	private ISocketController socketHandler;
	private IWeightInterfaceController weightController;
	private KeyState keyState = KeyState.K1;
	private double currentLoad;
	private double weight;
	private double tara;
	private String msg;
	ArrayList<String> keys = new ArrayList<String>();
	boolean wait = true;

	public MainController(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.init(socketHandler, weightInterfaceController);
	}

	@Override
	public void init(ISocketController socketHandler, IWeightInterfaceController weightInterfaceController) {
		this.socketHandler = socketHandler;
		this.weightController=weightInterfaceController;
	}

	@Override
	public void start() {
		if (socketHandler!=null && weightController!=null){
			//Makes this controller interested in messages from the socket
			socketHandler.registerObserver(this);
			//Starts socketHandler in own thread
			new Thread(socketHandler).start();
			weightController.registerObserver(this); // HUSK AT KOMMENTER PÅ DETTE!
			weightController.run();
			//TODO set up weightController - Look above for inspiration (Keep it simple ;))


		} else {
			System.err.println("No controllers injected!");
		}
	}

	//Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		switch (message.getType()) {
		case B:
			weight = Double.parseDouble(message.getMessage());
			weightController.showMessagePrimaryDisplay(String.format("%.2f", weight) + " kg");
			socketHandler.sendMessage(new SocketOutMessage("DB\n"));
			break;
		case D:
			weightController.showMessagePrimaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("D" + " A\n"));
			break;
		case Q:
			System.exit(0);
			break;
		case RM204:
			weightController.showMessagePrimaryDisplay(message.getMessage());
			socketHandler.sendMessage(new SocketOutMessage("RM20 B\n"));
			break;
		case RM208:
			msg = message.getMessage();
			weightController.showMessagePrimaryDisplay(msg.split(" ")[2]);
			socketHandler.sendMessage(new SocketOutMessage("RM20 B\n"));
			wait = true;
			while (wait = true) {
				
			}
			break;
		case S:
			notifyWeightChange(weight);
			msg = String.valueOf(currentLoad);
			socketHandler.sendMessage(new SocketOutMessage("S" + " S" + " " + msg + " kg\n"));
			break;
		case T:
			tara -= currentLoad;
			msg = String.valueOf(currentLoad);
			weight = 0;
			weightController.showMessagePrimaryDisplay(String.format("%.2f", weight) + " kg");
			socketHandler.sendMessage(new SocketOutMessage("T" + " S" + " " + msg + " kg\n"));
			weight = tara;
			break;
		case DW:
			weight = 0;
			weightController.showMessagePrimaryDisplay(String.format("%.2f", weight) + " kg");
			socketHandler.sendMessage(new SocketOutMessage("DW" + " A\n"));
			break;
		case K:
			handleKMessage(message);
			break;
		case P111:
			weightController.showMessageSecondaryDisplay(message.getMessage()); 
			break;
		case F:
			weightController.showMessagePrimaryDisplay(String.format("%.2f", tara) + " kg");
			currentLoad = tara;
			break;
		}

	}

	private void handleKMessage(SocketInMessage message) {
		switch (message.getMessage()) {
		case "1" :
			this.keyState = KeyState.K1;
			break;
		case "2" :
			this.keyState = KeyState.K2;
			break;
		case "3" :
			this.keyState = KeyState.K3;
			break;
		case "4" :
			this.keyState = KeyState.K4;
			break;
		default:
			socketHandler.sendMessage(new SocketOutMessage("ES"));
			break;
		}
	}
	//Listening for UI input
	@Override
	public void notifyKeyPress(KeyPress keyPress) {
		//TODO implement logic for handling input from ui
		switch (keyPress.getType()) {
		case SOFTBUTTON:
			socketHandler.sendMessage(new SocketOutMessage("RM20 A" + " \"" + msg + "\"\n"));
			break;
		case TARA://Nulstil og gem
			//double totalTara += 0;
			weight = 0;
			weightController.showMessagePrimaryDisplay(String.format("%.1f", weight) + " kg");
			break;
		case TEXT:
			if (msg.split(" ")[0].equalsIgnoreCase("RM20")) { 
			char key = keyPress.getCharacter();
			keys.add(Character.toString(key));
			
			String keyString = keys.toString().substring(1,keys.toString().length()-1).replace(1,"X");
			System.out.println(keys.toString().substring(1,keys.toString().length()-1).replaceAll(",$", ""));
			weightController.showMessageSecondaryDisplay(keyString);
			wait = false;
			}
			break;
		case ZERO: //Unimplemented button.
			break;
		case C: //
			break;
		case EXIT:
			System.exit(0);
			break;
		case SEND:
			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3) ){
				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
			}
			break;
		}

	}

	@Override
	public void notifyWeightChange(double newWeight) {
		
		currentLoad = newWeight;

	}

}

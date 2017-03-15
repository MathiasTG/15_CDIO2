package test;

import java.io.IOException;

import org.junit.Test;

import controller.IMainController;
import controller.MainController;
import procedure.ConnectionLogic;
import socket.ISocketController;
import socket.SocketController;
import weight.IWeightInterfaceController;
import weight.gui.WeightInterfaceControllerGUI;

public class startserver {

	@Test
	public void test() {
		ISocketController socketHandler = new SocketController();
		IWeightInterfaceController weightController = new WeightInterfaceControllerGUI();
		//Injecting socket and uiController into mainController - Replace with improved versions...
		IMainController mainCtrl = new MainController(socketHandler, weightController);
		//.init and .start could be merged
		mainCtrl.start();
		//fail("Not yet implemented");
		
		
	ConnectionLogic lo = new ConnectionLogic("127.0.0.1");
	try {
		lo.weighingProcedure();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}

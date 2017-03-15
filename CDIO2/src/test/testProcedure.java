package test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Test;

import controller.IMainController;
import controller.MainController;
import procedure.ConnectionLogic;
import procedure.Operator;
import socket.ISocketController;
import socket.SocketController;
import weight.IWeightInterfaceController;
import weight.gui.WeightInterfaceControllerGUI;

public class testProcedure {

	private ArrayList<Operator> operatorArray;
	
	
	
	private ISocketController socketHandler = new SocketController();
	private IWeightInterfaceController weightController = new WeightInterfaceControllerGUI();
	private IMainController mainCtrl = new MainController(socketHandler, weightController);
	
	private Socket clientSocket;
	private ConnectionLogic b = new ConnectionLogic("127.0.0.1");
	// @Before
	// public void setUp() throws Exception {
	//
	// //.init and .start could be merged
	//
	//
	//
	// }

	// @After
	// public void tearDown() throws Exception {
	//
	//// operatorArray = new ArrayList<Operator>();
	//// operatorArray.add(new Operator(12, "Hans"));
	//// mainCtrl.close();
	//// //Nulstil alt
	// }

	@Test
	public void command1() {
		
		
		mainCtrl.start();

		try {
			clientSocket = new Socket("127.0.0.1", 8000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		operatorArray = new ArrayList<Operator>();
		operatorArray.add(new Operator(12, "Hans"));

		b.outputToServer("B 1");// S
		String answer = b.outputToServer("S");
		assertTrue(answer.startsWith("S S"));
	}
	// @Test
	// public void command2() {
	// String answer = b.outputToServer("T");//T
	// assertTrue(answer.startsWith("T S"));
	// }
	// @Test
	// public void command3() {
	// String answer = b.outputToServer("D");//D
	// assertTrue(answer.startsWith("D A"));
	// }
	// @Test
	// public void command4() {
	// String answer = b.outputToServer("DW");//DW
	// assertTrue(answer.startsWith("DW A"));
	// }
	// @Test
	// public void command5() {
	// String answer = b.outputToServer("P111");//P111
	// assertTrue(answer.startsWith("P111 A"));
	// }
	// @Test
	// public void command6() {
	// String answer = b.outputToServer("RM20 8\" \" \"&3\"");//RM20 8
	// assertTrue(answer.startsWith("RM20 B"));
	// }
	// @Test
	// public void command7() {
	// String answer = b.outputToServer("RM20 8\" \" &3");//K
	// assertTrue(answer.startsWith("DB"));
	// }
	// @Test
	// public void command8() {
	// String answer = b.outputToServer("B");//B
	// assertTrue(answer.startsWith("DB"));
	// }
	// @Test
	// public void command9() {
	// String answer = b.outputToServer("RM20 8\" \" &3");//Q
	// assertTrue(answer.startsWith("Q"));
	// }

}

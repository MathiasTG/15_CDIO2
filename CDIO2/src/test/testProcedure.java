package test;

import controller.*;
import procedure.*;
import socket.*;
import weight.*;
import weight.gui.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testProcedure {

	private ArrayList<Operator> operatorArray;

	@Before
	public void setUp() throws Exception {
		operatorArray = new ArrayList<Operator>();
		operatorArray.add(new Operator(12, "Hans"));
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

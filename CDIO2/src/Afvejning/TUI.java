package Afvejning;

import java.util.Scanner;

public class TUI {

	Scanner input = new Scanner(System.in);
	int userID;
	int batchNum;
	
	public TUI() {
	
		

	}
	
	public void menu() {
		
		System.out.println("Welcome.");
		
		System.out.println("Enter user ID:");
		
		userID = input.nextInt();
		input.nextLine();
		
//		Lav noget kode der sender ID'et til verifikation.
		
//		Lav noget kode der sender ID'et til vægten.
		
		System.out.println("Enter batch number:");
		
		batchNum = input.nextInt();
		input.nextLine();
		
		System.out.println("The weight has to be unladen.");
		System.out.println("Tare the weight by placing an empty container on it.");
		
	}

}

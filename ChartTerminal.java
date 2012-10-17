package com.node.client;

import java.util.Scanner;

public class ChatTerminal extends Thread {

	private Client client;
	private ChatTerminal(){}
	public ChatTerminal(Client client){
		this.client = client;
	}
	
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String line = null;
		System.out.println(">>");
		while(!(line = scanner.nextLine()).equalsIgnoreCase("bye")){
			client.addMessageToPool(line);
			System.out.println(">>");
		}
	}
	
}


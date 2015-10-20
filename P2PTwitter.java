import java.net.*;
import java.io.*;
import java.util.Scanner;

public class P2PTwitter{

	// Server port
	static final int SERVER_PORT = 7014;
	// Client port
	static final int CLIENT_PORT = 7015;
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String unikey = args[0];

		// FileManager manages logging and emails
		Profile profile = new Profile(unikey);

		// Thread to run the Profile manager in
		(new Thread(profile)).start();

		P2PTServer server;
		P2PTClient client;

		server = new P2PTServer(SERVER_PORT, profile);
		client = new P2PTClient(CLIENT_PORT, profile, unikey, SERVER_PORT);
		
		(new Thread(server)).start();
		(new Thread(client)).start();

		while(true){
			System.out.print("Status: ");
			String status = sc.nextLine();
			if(status.length() == 0){
				System.err.println("Status is empty. Retry.");
				continue;
			}else if(status.length() > 140){
				System.err.println("Status is too long, 140 characters max. Retry.");
				continue;
			}
			client.send(status, true);
		}
	}
}
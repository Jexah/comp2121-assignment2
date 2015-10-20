import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.Timer;

class P2PTClient implements Runnable{

	private DatagramSocket clientSocket;

	private Profile profile;

	private java.util.Timer timer;

	private String currentStatus;
	private String unikey;

	private DatagramPacket sendPacket;
	
	private int SERVER_PORT;

	private Random rand;

	public P2PTClient(int port, Profile profile, String unikey, int serverPort){
		// Set up local variables and begin timeout counter.
		try{
			clientSocket = new DatagramSocket(port);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		rand = new Random();
		currentStatus = "";
		this.unikey = unikey;
		this.profile = profile;
		this.SERVER_PORT = serverPort;
		timer = new Timer();
	}

	private void writeToClient(String str){
		for(User user : profile.users){
			try{
				sendPacket = new DatagramPacket(str.getBytes("ISO-8859-1"), str.getBytes("ISO-8859-1").length, InetAddress.getByName(user.ip), SERVER_PORT);
			}catch (Exception e){
				
			}
			
			try{
				clientSocket.send(sendPacket);
			}catch (IOException e){

			}
		}
	}

	public void send(String str, boolean first){
		currentStatus = str;
		writeToClient(unikey + ":" + str.replace(":", "\\:"));
		timer.cancel();
		timer = new java.util.Timer();
		timer.schedule(new java.util.TimerTask(){
			public void run(){
				send(currentStatus, false);
			}
		}, 1000 + rand.nextInt(2000));
		if(first){
			print();
			first = !first;
		}
	}
	
	private void print(){
		System.out.println("### P2P tweets ###");
		profile.printStatus();
		System.out.println("### End tweets ###");
	}

	public void run(){
		try{
			while(true){
				Thread.sleep(1);
			}
		}catch(Exception e){
			
		}finally{
			if(clientSocket != null){
				clientSocket.close();
			}
		}
	}
}
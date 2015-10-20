import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Profile implements Runnable{

	// Define local variables
	public ArrayList<User> users;
	private String unikey;

	public Profile(String unikey){		
		users = new ArrayList<User>();
		this.unikey = unikey;
		readProperties();
	}

	private void readProperties(){
		Properties participants = new Properties();
		try{
			participants.load(new FileInputStream("participants.properties"));
			String[] userKeys = participants.getProperty("participants").split(",");
			for(int i = 0; i < userKeys.length; i++){
				User tmp = new User(
					participants.getProperty(userKeys[i] + ".pseudo"),
					participants.getProperty(userKeys[i] + ".ip"),
					participants.getProperty(userKeys[i] + ".unikey"),
					users
				);
				users.add(tmp);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void printStatus(){
		for(User user : users){
			if(user.unikey.equals(unikey)){
				System.out.println("# " + user.alias + " (myself): " + user.lastStatus);
			}
		}
		for(User user : users){
			if(!user.unikey.equals(unikey)){
				switch(user.mode){
					case UNINITIALIZED:
						System.out.println("# [" + user.alias + " (" + user.unikey + "): not yet initialized]");
						break;
					case IDLE:
						System.out.println("# [" + user.alias + " (" + user.unikey + "): idle]");
						break;
					case DEAD:
						break;
					default:
						System.out.println("# " + user.alias + " (" + user.unikey + "): " + user.lastStatus);
						break;
				}
			}
		}
	}

	public void run(){
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ex){
				
			}
		}
	}

	public void receiveStatus(String unikey, String status){
		for(User user : users){
			if(user.unikey.equals(unikey)){
				user.receiveStatus(status);
				user.refresh();
			}
		}
	}
}
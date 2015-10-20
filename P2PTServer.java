import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

class P2PTServer implements Runnable{

	private DatagramSocket listenSocket;
	
	private Profile profile;

	public P2PTServer(int port, Profile profile){
		// Set up local variables and begin timeout counter.
		try{
			listenSocket = new DatagramSocket(port);
			this.profile = profile;
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void run(){
		try{
			while(listenSocket != null){

				byte[] buffer = new byte[300];
				DatagramPacket sendPacket = new DatagramPacket(buffer, 300);
				listenSocket.receive(sendPacket);

				Charset charset = Charset.forName("ISO-8859-1");
				CharBuffer data = charset.decode(ByteBuffer.wrap(buffer));

				String request = data.toString().substring(0, sendPacket.getLength());

				if(request == null) break;

				String[] info = request.split("\\:");
				String unikey = info[0];

				String[] messageArr = Arrays.copyOfRange(info, 1, info.length);

				String message = String.join(":", messageArr);
				
				message = message.replace("\\:", ":");

				profile.receiveStatus(unikey, message);

			}
		}catch(IOException e){
			e.printStackTrace();//(e.getMessage());
		}
	}
}
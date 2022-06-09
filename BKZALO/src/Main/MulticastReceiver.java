package Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {

	public static final byte[] BUFFER = new byte[8192];
	public static String Receiver() {
		MulticastSocket socket = null;
		DatagramPacket inPacket = null;
		try {
			// Get the address that we are going to connect to.
			InetAddress address = InetAddress.getByName("239.255.255.252");

			// Create a new Multicast socket
			socket = new MulticastSocket(8888);

			// Joint the Multicast group
			socket.joinGroup(address );

			
				// Receive the information and print it.
				inPacket = new DatagramPacket(BUFFER, BUFFER.length);
				socket.receive(inPacket);
				 String msg = new String(BUFFER, 0, inPacket.getLength());
				 System.out.println(msg);
				 if(msg.contains("---imgstart---:")) {
//					 System.out.println("anh -----");
					 socket.receive(inPacket);
					 String msgtemp = new String(BUFFER, 0, inPacket.getLength());
					 do {
						 msg += msgtemp;
						 socket.receive(inPacket);
						 msgtemp = new String(BUFFER, 0, inPacket.getLength());
//						 System.out.println(msgtemp);
						 
					 }while(!msgtemp.contains("---imgend---:"));
				 }
				System.out.println(msg.length());
				
				
				
				return msg;
				
				
		} catch (IOException ex) {
			ex.printStackTrace();
		}		
		return null;
	}
}
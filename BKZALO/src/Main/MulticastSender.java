package Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastSender {

	public static final String GROUP_ADDRESS = "239.255.255.252";
	public static final int PORT = 8888;
	public static InetAddress address ;
	public static void Send(String msg)
	{
		DatagramSocket socket = null;
		try {
			// Get the address that we are going to connect to.
			address = InetAddress.getByName(GROUP_ADDRESS);
			System.out.println(address);

			// Create a new Multicast socket
			socket = new DatagramSocket();

			DatagramPacket outPacket = null;
				outPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, PORT);
				socket.send(outPacket);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
}
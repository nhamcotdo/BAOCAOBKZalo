package Main;
import java.net.*;
import java.io.*;
public class Client	 {
	public static void main(String[] args) throws IOException {
		
		
	}
	public static String Receiver() throws UnknownHostException, IOException {
		
		Socket s = new Socket("192.168.1.5",4999);
		DataInputStream diStream = new DataInputStream(s.getInputStream());
		String str =  (String)diStream.readUTF();
		diStream.close();
		s.close();
		return str;
	}
	
	
	public static void send(String str) throws UnknownHostException, Exception {
		Socket s = new Socket("192.168.1.5",4999);
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		dout.writeUTF(str);
		dout.flush();
		dout.close();
		s.close();
	}
}
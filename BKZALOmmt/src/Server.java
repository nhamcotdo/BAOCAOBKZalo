import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		try
		{
			ServerSocket ss= new ServerSocket(4999);
			while(true)
			{
				Socket s = ss.accept();
				System.out.println("Success");
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String str = (String)dis.readUTF();
				System.out.println("Client: "+str);
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(str);
//				s.close();
			}
		}
		catch(Exception exception)
		{
			System.out.println("Adsad");
		}
	}
}

package Model;
import java.io.*;
import java.net.*;
/**
 *
 * @author hunt
 */
public class Request {
    static String hostname;

    public Request() {
        hostname="localhost";
    }
    
    //public static void main(String args[]){
    public int establishConn(String str[]){
        Socket sock=null;
        PrintWriter out=null;
        BufferedReader in=null;
        try{
            sock=new Socket(hostname,4334);
            out=new PrintWriter(sock.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            
        }
        catch(UnknownHostException u){
            System.out.println(u);
        }
        catch(IOException e){
            System.out.println(e);
        }
        
//        BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));
        String re;
		int i=0;
        try {
            while(i<str.length){
//                re=str[i++];
                
                out.println(str[i++]);
                System.out.println("Recieved: "+ in.readLine());
            }
                    in.close();
        out.close();
        sock.close();

            
        } catch (IOException ex) {
            System.out.print(ex);
            return 0;
        }
        return 1;
    }
    
}

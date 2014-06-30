package Model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package establish.classfile;
import View.postLogin;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author hunt
 */
public class Client {
    static String hostname;

    public Client() {
        hostname="192.168.35.102";
    }
    
    //public static void main(String args[]){
    public String[] establishConn(String str[]){
        Socket sock=null;
        ObjectOutputStream out=null;
        ObjectInputStream in=null;
        try{
            sock=new Socket(hostname,4334);
            out=new ObjectOutputStream(sock.getOutputStream());
            in=new ObjectInputStream(sock.getInputStream());
            String receive[]=new String[10];
            int i=0;
            out.writeObject(str);
            receive=(String[])in.readObject();
            
            in.close();
            out.close();
            sock.close();
            return receive;
        }
        catch(UnknownHostException u){
            System.out.println(u);
            return null;
        }
        catch(IOException e){
            System.out.println(e);
            return null;
        }
        catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                return null;
        }
    }
    
}

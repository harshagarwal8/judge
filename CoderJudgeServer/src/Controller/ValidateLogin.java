/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * @author harsh
 */
public class ValidateLogin implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
    private Socket sClient;
    private DatabaseHandle db;
    public ValidateLogin(Socket s,DatabaseHandle db){
        sClient=s;
        this.db=db;
    }

    @Override
    public void run() {
        try{
        in=new BufferedReader(new InputStreamReader(sClient.getInputStream()));
        out=new PrintWriter(sClient.getOutputStream());
         String arr[]=new String[10];
         int i=0;
                while((arr[i++]=in.readLine())!=null && i<4){
                    System.out.println(arr[i-1]);
                }   
            switch (arr[0]) {
                case "loginMe":
                    System.out.println(arr[1]+arr[2]);
                    out.print(db.ValidateLogin(arr[1],arr[2]));
                    break;
                case "registerUser":
                    out.print(db.RegisterUser(arr));
                    break;
                case "submitProb":
                    IOClass io=new IOClass();
  //                  out.print(new Compile().compileProgram(io.upload_file(arr[2], arr[1])));
                    break;
                default:
                    System.out.print(arr[0]);
                    break;
            }
               out.close();
               in.close();
               sClient.close();
        }
        catch(IOException e){
            System.out.println(e);
        } catch (SQLException ex) {
            System.out.println(ValidateLogin.class.getName()+ex);
        }

        
    }
    
    
}

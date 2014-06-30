package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author harsh
 */
public class Server{
    DatabaseHandle db;
    String path;
    public Server() throws SQLException
    {
        path="/home/hunt/javaonj/contests";
        ServerSocket soc;
        ObjectInputStream in;
        PrintWriter out;
  
        try{
        soc=new ServerSocket(4334);
        db=new DatabaseHandle();
        while(true){
                Socket clientSoc=soc.accept();
                System.out.println(clientSoc.getInetAddress());
            int i=0;
          
                Runnable conn=new HandleRequest(clientSoc, db);
                new Thread(conn).start();
/*                BufferedReader in=new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
                PrintWriter out=new PrintWriter(clientSoc.getOutputStream());
                String arr[]=new String[10];
                int i=0;
                while((arr[i++]=in.readLine())!=null && i<3){
                  //  out.println(arr[i-1]);
                }   
                
                if(arr[0].equals("1"))
                {
                    System.out.println(arr[1]+arr[2]);
                    out.print(this.ValidateLogin(arr[1],arr[2]));
                }
               System.out.println(in.readLine());
               out.close();
               in.close();
               clientSoc.close();*/
               
        }
        }
        catch(IOException e){
            System.out.println(e+"main");
        }
            
            if(!db.CheckConnection())
            {
                System.out.println("Something Went Wrong..!!");
                System.exit(0);
            }
            /*String arr[]=new String[7];
            arr[0]="meaaaa";
            arr[1]="a";
            arr[2]="a";
            arr[3]="harshaagarwaal.08@gmail.com";
            arr[4]="1";
            arr[5]="i";
            arr[6]="l";
            int val=this.RegisterUser(arr);
            switch(val)
            {
                case 10:
                System.out.println("Some unexpected error occured..!!");
                    break;
                case 11:
                   System.out.println("Registered successfully..!!");
                    break;
                case 0:
                    System.out.println("Handle Error..!!");
                    break;
                case 1:
                    System.out.println("Password Error..!!");
                    break;
                case 2:
                   System.out.println("Name Error..!!");
                     break;
                case 3:
                    System.out.println("Email Error..!!");
                    break;
                case 4:
                    System.out.println("Mobile Error..!!");
                    break;
                case 5:
                    System.out.println("Institution Error Error..!!");
                    break;
                 case 6:
                     System.out.println("Language Error..!!");
                    break;
              
            }*/
            
            
    }
    
    public final int RegisterUser(String arr[]) throws SQLException
    {
         return db.RegisterUser(arr);
    }
    
    
}
class HandleRequest implements Runnable{
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket sClient;
    private DatabaseHandle db;
    String path;
    public HandleRequest(Socket s,DatabaseHandle db){
        sClient=s;
        path="/home/hunt/javaonj/contests/";
        this.db=db;
    }
    private String getExtension(String type){
        if(type.equals("C"))
            return ".c";
        else if(type.equals("CPP"))
               return ".cpp";
        return ".txt";
    }
    /*arr fields
     * 0-requesttype
     * 1-handle
     * for login
     * 2-password
     * for problem submit
     * 2-
     * 3-prob number
     * 4-language
     * 5-solution
     * for problem request
     * 2-prob no
     */
    public void run(){
        try{
       // in=new BufferedReader(new InputStreamReader(sClient.getInputStream()));
            in=new ObjectInputStream(sClient.getInputStream());
            out=new ObjectOutputStream(sClient.getOutputStream());
            String []arr=(String[])in.readObject();
            int i=0;
            System.out.println(arr[0]);
            switch (arr[0]) {
                case "loginMe":
                    //System.out.println(arr[1]+arr[2]);
                    out.writeObject(db.ValidateLogin(arr[1],arr[2]));
                    break;
                case "registerUser":
                    out.writeObject(db.RegisterUser(arr));
                    break;
                case "submitProb":
                    IOClass io=new IOClass();
                    System.out.println("insubmit");
                    String name=arr[1]+"-"+db.runningContestId+"-"+arr[3];
                    String file_name=io.upload_file(path,name+getExtension(arr[4]),arr[5]);
                    Compile com=new Compile();
                    String result=com.compileProgram(name);
                    db.submitProblem(arr[3],db.fetchCurrentEvent(),arr[1],result,db.getPoints(arr[3]));
                    out.writeObject(new String[]{"resultCode",result});
                    break;
                case "requestProb":
                    String rec[]=new String[10];
                    rec=db.fetchProblem(arr[3],Integer.parseInt(arr[2]));
                    out.writeObject(rec);
                    break;
                default:
                    System.out.print(arr[0]);
                    out.writeObject("Invalid Request\n");
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HandleRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HandleRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
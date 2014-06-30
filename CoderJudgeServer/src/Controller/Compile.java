/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author hunt
 */
public class Compile {
    String path;

    public Compile() {
        path="/home/hunt/javaonj/contests/";
    }
    
        
    public String compileProgram(String fName) throws InterruptedException{
        String cmd="g++ "+path+fName+".c -o "+path+fName;
        System.out.println(cmd);
        try {
            Runtime rt=Runtime.getRuntime();
            
            Process process=rt.exec(cmd);
            
            int exitStatus=process.waitFor();
            System.out.println("Exit Value: "+exitStatus);
            if(exitStatus!=0)
                  return "Compile Error";
//            rt.exec("chroot "+path+"runfile/");
//            rt.exec("cd "+path);
//            BufferedReader stdOut = new BufferedReader(new `(process.getOutputStream()));
/*            process=rt.exec("pwd");
            BufferedReader stdIn;
            stdIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String st;
            while((st=stdIn.readLine())!=null)
                  System.out.println(st);
*/            
            ProcessBuilder pb=new ProcessBuilder(path+fName);
            pb.redirectOutput(new File(path+fName+".txt"));
            pb.redirectError(new File(path+"error.txt"));
            pb.redirectInput(new File(path+"input.txt"));
            process=pb.start();
//            process.wait(1);
//            Runnable kill=new TimeLimitExceed();
//            new Thread(kill);
            int status=process.waitFor();
            if(status!=0){
                switch(status){
                    case 136: return "RTE(SIGFPE)";
                    case 11: return "RTE(SIGSEGV)";
                    default:
                            return "Error";
                }
            }
            else{
                //compare output
                process=rt.exec("diff "+path+"ouput.txt "+path+fName+".txt");
                status=process.waitFor();
                System.out.println(status);
                if(status==0)
                    return "Accepted";
                else return "";
            }
//             BufferedReader stdOut = new BufferedReader(new (process.getOutputStream()));
//            process=rt.exec("pwd");
            /*
             * 
             * 
             * 
             run code
             */
        } catch (IOException ex) {
            System.out.print(ex);
        }
        catch(RuntimeException re){
            return re.toString();
        }
        return "t";
    }
}
/*    
class TimeLimitExceed implements Runnable{
    public void run(){
        System.out.println("kill");
    }
}
*/
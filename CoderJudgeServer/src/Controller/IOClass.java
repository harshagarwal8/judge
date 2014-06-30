/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.*;

/**
 *
 * @author hunt
 */
public class IOClass {
    private String contest_id;
    public String upload_file(String path,String id,String content){
        contest_id="van";
        try {
//            FileReader in=new FileReader(path);
            //BufferedReader buf =new BufferedReader(new FileReader(path));
            BufferedWriter out;
            out = new BufferedWriter(new FileWriter(path+id));
            String temp;
            out.write(content+"\n");
            out.close();
            return id;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
            return "false";
        } catch (IOException ex) {
            System.out.println(ex);
            
            return "false";

        }
    }
}

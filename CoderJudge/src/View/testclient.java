/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Client;

/**
 *
 * @author hunt
 */
public class testclient {
    public static void main(String args[]){
        String []str=new String[6];
        str[0]="submitProb";
        str[1]="vanjain";
        str[2]="";
        str[3]="1";
        str[4]="C";
        str[5]="#include<stdio.h>\n"
                + "main(){\n"
                + "printf(\"vandit\");\n"
                + "return 0;\n"
                + "}";
        Client cl=new Client();
        cl.establishConn(str);
    }
    
}

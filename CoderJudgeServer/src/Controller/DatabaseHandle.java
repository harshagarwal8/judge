/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import java.sql.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author harsh
 */
public class DatabaseHandle {
    boolean conn;
    
    Connection con;
    PreparedStatement stmt;
    ResultSet res;
    public int runningContestId;
    int t;
    int currprobs;
    long remtime;
    private SimpleDateFormat df;
    public DatabaseHandle()
    {
        conn=false;
        this.DatabaseConnect();
        df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z",Locale.ENGLISH);
    }
    
    private boolean DatabaseConnect()
    {
        try {
		if(Connect())
		return true;
                return false; 
                     
            }
        catch (Exception e) 
        {
                    
			System.out.println("NO CONNECTION =(\n"+ e);
                        return false;
	}
    }
    
    private boolean Connect()
    {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "coderjudge";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "Vandit";
		try {
		  Class.forName(driver).newInstance();
		  con = DriverManager.getConnection(url+dbName,userName,password);
		  System.out.println("con="+con);
                  conn=true;
                  return true;
                  
		}
		
                catch (Exception e) {
			System.out.println("NO CONNECTION =(\n"+ e);
                        conn=false;
                        return false;
                }
		
		
	}
    
    public boolean CheckConnection()
    {
        return conn;
    }
    
    private boolean CheckHandle(String str)
    {
        try
        {
            String s="SELECT COUNT(*) FROM `per_info` WHERE handle=?";
            PreparedStatement ps=con.prepareStatement(s);
            ps.setString(1, str);
            ResultSet rs=ps.executeQuery();
            rs.next();
            int row=rs.getInt(1);
            if(row==0) return true;
            else return false;
        }
        
        catch(SQLException e)
        {
            System.out.println(e);
            return false;
        }
      }
    public boolean checkAdmin(String handle, String pass)
    {
        try
        {
            String s="SELECT COUNT(*) FROM `admin` WHERE `userid`=? AND `password`=?";
            PreparedStatement ps=con.prepareStatement(s);
            ps.setString(1, handle);
            ps.setString(2, pass);
            ResultSet rs=ps.executeQuery();
            rs.next();
            int row=rs.getInt(1);
            if(row==0)
                return false;
            return true;
        }
        catch(Exception e)
        {
            System.out.println("Admin login exception:"+e);
            return false;
        }
    }
    
    public boolean checkEvent(String ss)
    {
        try{
            
        String s="SELECT COUNT(*) FROM `events` WHERE `code`=?";
        PreparedStatement ps=con.prepareStatement(s);
        ps.setString(1, ss);
        ResultSet rs=ps.executeQuery();
        rs.next();
        int row=rs.getInt(1);
        if(row!=0)
            return false;
        return true;
        }
        catch(SQLException e)
        {
            System.out.println("EVENT exception:"+e);
            return false;
        }
    }
    
    public boolean createEvent(String ename,String handle,String stime,String etime,int prob)
    {
        try{
        String s="INSERT INTO `events` VALUES(?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(s);
        ps.setString(1, ename);
        ps.setString(2, handle);
        ps.setString(3, stime);
        ps.setString(4, etime);
        ps.setInt(5, prob);
        int tt = ps.executeUpdate();
        if(tt==1)
            return true;
        return false;
        }
        catch(SQLException e)
        {
            System.out.println("Error in creating event:"+e);
            return false;
        }
        
        
    }
    public int getPoints(String str)
    {
        String sql;
        try
        {
            sql="SELECT * FROM `problems` WHERE `probid`=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            rs.next();
            return rs.getInt("points");
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    public void submitProblem(String probid,String eventid,String handle,String status,int points)
    {
        long time=System.currentTimeMillis();
        String sql;
        try
        {
            sql="INSERT INTO `submission` values(?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1, probid);
            ps.setString(2, eventid);
            ps.setString(3, handle);
            ps.setString(4, status);
            ps.setInt(5, points);
            int tt=ps.executeUpdate();
        }
        catch(Exception e)
        {
            
        }
    }
    private boolean CheckEmail(String str) throws SQLException
    {
            try
            {
            InternetAddress em=new InternetAddress(str);
            em.validate();
            String s="SELECT COUNT(*) FROM `per_info` WHERE email=?";
            PreparedStatement ps=con.prepareStatement(s);
            ps.setString(1, str);
            ResultSet rs=ps.executeQuery();
            rs.next();
            int row=rs.getInt(1);
            if(row==0) 
               return true;
            else return false;
        
            }
            catch(AddressException e)
            {
                return false;
            }
     }
    
    
    public int RegisterUser(String arr[]) throws SQLException
    {
        /*
         * String received by this contains:
         * arr[0]=handle
         * arr[1]=password
         * arr[2]=name
         * arr[3]=email
         * arr[4]=mobile
         * arr[5]=institution
         * arr[6]=language
         */
        try
        {
            
            for(int i=0;i<7;i++)
                    arr[i].trim();
            if(!this.CheckEmail(arr[3]))
                return 3;
            if(!this.CheckHandle(arr[0]))
              return 0;
            stmt=con.prepareStatement("Insert into `per_info` values(?,?,?,?,?,?,?)");
            for(int i=0;i<7;i++)
                stmt.setString(i+1, arr[i]);
            t=stmt.executeUpdate();
            if(t==1)
            {
                stmt.close();
                return 11;
            }
        }
        catch(Exception e)
        {
            System.out.println(e+"User cant be regsitered..!!\n");
            stmt.close();
            return 10;
        }
        return 10;
    }
    
    public String[] ValidateLogin(String handle, String pass)
    {
               String arr[]=new String[7];
     
        try{
            arr[0]="loginReply";
            arr[1]=handle;
            String sql="SELECT COUNT(*) FROM `per_info` WHERE `handle`=? AND `password`=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, handle);
        ps.setString(2, pass);
        ResultSet rs=ps.executeQuery();
        rs.next();
        if(rs.getInt(1)==0)
        {
            
            arr[2]="false";
            arr[3]="-1";
            arr[4]="-1";
            return arr;
        }
        else
        {
            String ss=this.fetchCurrentEvent();
            //this.checkDate("121214512");
            arr[2]="true";
            arr[3]=ss;
            
            System.out.println("Current event"+ss);
            arr[4]=new String(""+currprobs);
            arr[5]=new String(""+this.remtime);}
        
        return arr;
        }
        catch(SQLException e)
        {
            arr[3]="-1";
            System.out.println(e);
            return arr;
        }
    }
    public String fetchCurrentEvent()
    {
        try
        {
           // System.out.println("Hi");
            String sql="SELECT * FROM  `events`";
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                
                if(checkDate(rs.getString("starttime"),rs.getString("endtime"))==true)
                {
                    System.out.println("Event id="+rs.getString("code"));
                    currprobs=rs.getInt("probs");
                    return rs.getString("code");
                }
              //  if(rs.next()!=rs.last())
                //    break;
            }
            return "-1";
        }
        catch(Exception e)
        {
            System.out.println("Event fetch error:"+e);
            return "-1";
        }
    }
    private boolean checkDate(String start,String end)
    {
        try{
        java.util.Date dd=new java.util.Date();
        long st=Long.parseLong(start);
        long en=Long.parseLong(end);
        long current=System.currentTimeMillis();
        System.out.println("Current:"+current);
        if(current>=st && current<en)
        {
            this.remtime=en-current;
            return true;
        }
        
        return false;
        }    
    catch(Exception e)
        {
           System.out.println("Date Exception"+e);
            return false;
        }
    }
    public boolean checkProblemId(String id)
    {
       try
       {
        String sql="SELECT COUNT(*) FROM `problems` WHERE `probid`=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs=ps.executeQuery();
        rs.next();
        int row=rs.getInt(1);
        if(row!=0)
            return true;
        return false; 
        }
    catch(SQLException e)
    {
        System.out.println("Problem ID Check Error."+e);
        return false;
    }
   }
  
    public boolean enterProblem(String event,String probid,String desc,int time,int points,int no)
    {
        try
        {
            String sql="INSERT INTO `problems` VALUES (?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,event);
            ps.setString(2, probid);
            ps.setInt(3,no);
            ps.setString(4, desc);
            ps.setInt(5, time);
            ps.setInt(6, points);
            int tt=ps.executeUpdate();
            if(tt==1)
                return true;
            return false;
                      
        }
        catch(SQLException e)
        {
            System.out.println("Some Error Occured in Submitting problem."+e);
            return false;
        }
    }
    
    public String[] fetchProblem(String eventid,int no)
    {
        try
        {
            String sql="SELECT * FROM `problems` where `eventid`=? AND `probno`=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,eventid);
            ps.setInt(2,no);
            ResultSet rs=ps.executeQuery();
         //   if(rs.first()==rs.last())
           //     return null;
            rs.next();
            String arr[]=new String[8];
            arr[0]="probReply";
            arr[1]=rs.getString("probid");
            arr[2]=rs.getString("description");
           // System.out.println(arr[2]);
            arr[3]=""+rs.getInt("time");
            arr[4]=""+rs.getInt("points");
            return arr;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
       
}

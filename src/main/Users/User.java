package main.Users;

import java.sql.ResultSet;

public class User {

    public String username;
    public String password;
    public String fname;
    public String lname;
    public String dob;
    public String gender;
    public String mobileno;
    public String emailid;
    public String aadharno;
    public String datetravel;

    public User(ResultSet res)
    {

        try
        {
            while(res.next())
            {
                username=res.getString("username");
                password=res.getString("password");
                fname=res.getString("firstname");
                lname=res.getString("lastname");
                dob=res.getDate("dob").toString();
                gender=res.getString("gender");
                mobileno=res.getString("mobileno");
                emailid=res.getString("emailid");
                aadharno=res.getString("aadharno");
            }
            datetravel="2017-12-30";
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public User()
    {
        datetravel="2017-12-30";
    }
}

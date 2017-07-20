package main.Users;

import main.Home;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Profile extends JFrame {

    JLabel[] details=new JLabel[10];
    JTextField[] det=new JTextField[10];
    JPasswordField pass=new JPasswordField();
    JPasswordField cpass=new JPasswordField();
    String[] s= {"USERNAME","FIRST NAME","LAST NAME","DOB(YYYY-MM-DD)","GENDER(M/F)","MOBILE NO","EMAIL-ID","AADHAR-NO"};
    String[] res=new String[10];
    JButton update=new JButton("UPDATE");
    JLabel oldpass=new JLabel("OLD PASSWORD");
    JLabel newpass=new JLabel("NEW PASSWORD");
    JLabel cnfpass=new JLabel("CONFIRM PASSWORD");
    JButton chpass=new JButton("CHANGE PASSWORD");
    JLabel inv=new JLabel("DETAILS ALREADY EXIST");
    JLabel inv2=new JLabel("KINDLY FILL ALL DETAILS");
    JLabel inv3=new JLabel("PASSWORD DOES NOT MATCH");
    JLabel inv4=new JLabel("OLD PASSWORD INCORRECT");
    JLabel vld1=new JLabel("PASSWORD UPDATED SUCCESSFULLY");
    JLabel vld2=new JLabel("RECORDS UPDATED SUCCESSFULLY");
    JPasswordField olps=new JPasswordField();
    JPasswordField nwps=new JPasswordField();
    JPasswordField cfps=new JPasswordField();
    JButton back=new JButton("BACK");
    Connection myconn;
    Statement mystat;
    User uh;
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();

    public Profile(User u)
    {
        super("RAILWAY BOOKING RESERVATION-PROFILE");
        setLayout(null);
        this.getContentPane().setBackground(Color.WHITE);

        res[0]=u.username;
        res[1]=u.fname;
        res[2]=u.lname;
        res[3]=u.dob;
        res[4]=u.gender;
        res[5]=u.mobileno;
        res[6]=u.emailid;
        res[7]=u.aadharno;
        res[8]=u.password;
        uh=u;
        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        for(int i=0;i<8;i++)
        {
            details[i]=new JLabel(s[i]);
            details[i].setBounds(200, 100+50*i, 200, 40);
            add(details[i]);
            det[i]=new JTextField(res[i]);
            det[i].setBounds(400, 100+50*i, 200, 40);
            add(det[i]);
        }
        det[0].setEditable(false);
        det[0].setBackground(Color.GRAY);

        update.setBounds(300,550,100,40);
        add(update);

        back.setBounds(10, 10, 100, 40);
        add(back);

        oldpass.setBounds(800, 200, 200, 40);
        add(oldpass);
        olps.setBounds(1000, 200, 200, 40);
        add(olps);
        newpass.setBounds(800, 250, 200, 40);
        add(newpass);
        nwps.setBounds(1000, 250, 200, 40);
        add(nwps);
        cnfpass.setBounds(800,300,200,40);
        add(cnfpass);
        cfps.setBounds(1000, 300, 200, 40);
        add(cfps);
        chpass.setBounds(900, 400, 200, 40);
        add(chpass);

        inv.setForeground(Color.red);
        inv.setBounds(800,100,300,40);
        inv.setVisible(false);
        add(inv);
        inv2.setForeground(Color.red);
        inv2.setBounds(800,100,300,40);
        inv2.setVisible(false);
        add(inv2);
        inv3.setForeground(Color.red);
        inv3.setBounds(800,100,300,40);
        inv3.setVisible(false);
        add(inv3);
        inv4.setForeground(Color.red);
        inv4.setBounds(800,100,300,40);
        inv4.setVisible(false);
        add(inv4);
        vld1.setForeground(Color.green);
        vld1.setBounds(800,100,300,40);
        vld1.setVisible(false);
        add(vld1);
        vld2.setForeground(Color.green);
        vld2.setBounds(800,100,300,40);
        vld2.setVisible(false);
        add(vld2);

        chpass.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        String op=new String(olps.getPassword());
                        String np=new String(nwps.getPassword());
                        String cp=new String(cfps.getPassword());
                        if(op.isEmpty()||np.isEmpty()||cp.isEmpty())
                        {
                            inv.setVisible(false);
                            inv4.setVisible(false);
                            inv3.setVisible(false);
                            vld1.setVisible(false);
                            vld2.setVisible(false);
                            inv2.setVisible(true);
                            return;
                        }
                        if(op.equals(res[8])==false)
                        {
                            inv.setVisible(false);
                            inv2.setVisible(false);
                            inv3.setVisible(false);
                            inv4.setVisible(true);
                            vld1.setVisible(false);
                            vld2.setVisible(false);
                            return;
                        }
                        if(np.equals(cp)==false)
                        {
                            inv.setVisible(false);
                            inv2.setVisible(false);
                            inv4.setVisible(false);
                            inv3.setVisible(true);
                            vld1.setVisible(false);
                            vld2.setVisible(false);
                            nwps.setText("");
                            cfps.setText("");
                            return;
                        }
                        try
                        {
                            int st=mystat.executeUpdate("update users set password='"+np+"' where username='"
                                    +res[0]+"'");
                        }
                        catch(Exception f)
                        {
                            inv4.setVisible(false);
                            inv2.setVisible(false);
                            inv3.setVisible(false);
                            inv.setVisible(true);
                            vld1.setVisible(false);
                            vld2.setVisible(false);
                            f.printStackTrace();return;

                        }
                        inv.setVisible(false);
                        inv2.setVisible(false);
                        inv3.setVisible(false);
                        inv4.setVisible(false);
                        vld2.setVisible(false);
                        vld1.setVisible(true);
                        uh.password=np;

                    }

                }
        );

        update.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        for(int i=0;i<8;i++)
                        {
                            if(det[i].getText().isEmpty())
                            {
                                inv.setVisible(false);
                                inv3.setVisible(false);
                                inv4.setVisible(false);
                                inv2.setVisible(true);
                                vld1.setVisible(false);
                                vld2.setVisible(false);
                                return;
                            }
                        }
                        String str="firstname='"+det[1].getText()+"',lastname='"+det[2].getText()+"',dob=DATE'"
                                +det[3].getText()+"',gender='"+det[4].getText()+"',mobileno='"+det[5].getText()
                                +"',emailid='"+det[6].getText()+"',aadharno='"+det[7].getText()+"'where username='"
                                +det[0].getText()+"'";
                        try
                        {
                            int st=mystat.executeUpdate("update users set "+str);
                        }
                        catch(Exception g)
                        {
                            inv2.setVisible(false);
                            inv3.setVisible(false);
                            inv4.setVisible(false);
                            vld1.setVisible(false);
                            vld2.setVisible(false);
                            inv.setVisible(true);
                            g.printStackTrace();return;

                        }
                        uh.fname=det[1].getText();
                        uh.lname=det[2].getText();
                        uh.dob=det[3].getText();
                        uh.gender=det[4].getText();
                        uh.mobileno=det[5].getText();
                        uh.emailid=det[6].getText();
                        uh.aadharno=det[7].getText();
                        inv.setVisible(false);
                        inv2.setVisible(false);
                        inv3.setVisible(false);
                        inv4.setVisible(false);
                        vld1.setVisible(false);
                        vld2.setVisible(true);


                    }
                }



        );
        back.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        Home h=new Home(uh);
                        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        h.setVisible(true);
                        h.setBounds(0, 0, d.width, d.height);
                        dispose();
                    }
                }

        );



    }
}

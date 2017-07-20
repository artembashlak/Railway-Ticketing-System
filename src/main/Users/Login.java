package main.Users;

import main.Home;
import main.Trains.Admin;
import main.Users.Signup;
import main.Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends JFrame {

    JLabel user=new JLabel("USERNAME :");
    JLabel pass=new JLabel("PASSWORD :");
    JLabel inv=new JLabel("INVALID USER PLEASE ENTER CORRECT DETAILS OR SIGNUP");
    JLabel inv2=new JLabel("PLEASE ENTER DETAILS");
    JTextField username=new JTextField();
    JPasswordField password=new JPasswordField();
    JButton log=new JButton("LOGIN");
    JButton signup=new JButton("SIGNUP");
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    Connection myconn;
    Statement mystat;
    JLabel gpnr=new JLabel("GET PNR STATUS-ENTER PNR");
    JTextField gpnrtf=new JTextField();
    JButton gpnrbut=new JButton("SUBMIT");
    JLabel labpnr=new JLabel("INVALID PNR");
    JLabel succpnr=new JLabel();
    int stat=0;

    public Login()
    {
        super("RAILWAY BOOOKING SYSTEM-LOGIN");
        setLayout(null);
        this.getContentPane().setBackground(Color.WHITE);

        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        inv.setForeground(Color.red);
        inv.setBounds(300,150,400,40);
        inv.setVisible(false);
        add(inv);

        inv2.setForeground(Color.red);
        inv2.setBounds(300,150,400,40);
        inv2.setVisible(false);
        add(inv2);

        user.setBounds(300,200,100,40);
        add(user);
        pass.setBounds(300, 250, 100, 40);
        add(pass);

        username.setBounds(400,212,100,20);
        add(username);
        password.setBounds(400,261,100,20);
        add(password);

        log.setBounds(300, 350, 100, 40);
        add(log);
        signup.setBounds(430, 350, 100, 40);
        add(signup);

        gpnr.setBounds(700,200,300,50);
        add(gpnr);

        gpnrtf.setBounds(900, 200, 200, 50);
        add(gpnrtf);

        labpnr.setBounds(800, 100, 200, 50);
        labpnr.setForeground(Color.red);
        labpnr.setVisible(false);
        add(labpnr);

        succpnr.setBounds(800, 100, 200, 50);
        succpnr.setForeground(Color.green);
        succpnr.setVisible(false);
        add(succpnr);

        gpnrbut.setBounds(800,300,100,50);
        add(gpnrbut);

        log.addActionListener(

                new ActionListener()
                {

                    public void actionPerformed(ActionEvent event) {

                        String us=username.getText();
                        String ps=new String(password.getPassword());
                        if(us.isEmpty()||ps.isEmpty())
                        {
                            inv.setVisible(false);
                            inv2.setVisible(true);
                            return;
                        }
                        if(us.equals("admin")&&ps.equals("admin"))
                        {
                            Admin a=new Admin();
                            a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            a.setVisible(true);
                            a.setBounds(0,0,d.width,d.height);
                            dispose();
                            return;
                        }
                        try
                        {
                            ResultSet res=mystat.executeQuery("Select * from users where username='"+us+"' and password='"+ps+"'");
                            if(!res.isBeforeFirst())
                            {
                                password.setText("");
                                inv2.setVisible(false);
                                inv.setVisible(true);
                            }
                            else
                            {
                                User u=new User(res);
                                myconn.close();
                                Home h=new Home(u);
                                h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                h.setVisible(true);
                                h.setBounds(0,0,d.width,d.height);
                                dispose();
                            }
                        }
                        catch(Exception e)
                        {
                            inv.setVisible(true);
                            e.printStackTrace();return;

                        }
                    }

                }

        );
        signup.addActionListener(

                new ActionListener()
                {

                    public void actionPerformed(ActionEvent event) {

                        Signup s=new Signup();
                        s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        s.setVisible(true);
                        s.setBounds(0,0,d.width,d.height);
                        dispose();
                    }

                }

        );

        gpnrbut.addActionListener(


                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        labpnr.setVisible(false);
                        succpnr.setVisible(false);
                        try
                        {
                            ResultSet res9=mystat.executeQuery("select status from ticket where pnr='"+gpnrtf.getText()+"'");
                            int cnt=0;
                            while(res9.next())
                            {
                                stat=res9.getInt("status");
                                //System.out.println("hello");
                                cnt++;
                            }
                            if(cnt==0)
                            {
                                labpnr.setVisible(true);
                                gpnrtf.setText("");
                                return;
                            }


                        }catch(Exception m)
                        {
                            labpnr.setVisible(true);
                            gpnrtf.setText("");
                            m.printStackTrace();
                            return;
                        }
                        String str="";
                        if(stat>0)
                        {
                            str="CNF";
                        }
                        else
                            str="WL"+Integer.toString(stat);
                        succpnr.setText("PNR-STATUS-> "+str);
                        succpnr.setVisible(true);
                    }

                }

        );


    }

}

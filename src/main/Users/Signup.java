package main.Users;

import main.Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Signup extends JFrame {

    JLabel[] details=new JLabel[10];
    JLabel inv=new JLabel("DETAILS ALREADY EXIST");
    JLabel inv2=new JLabel("KINDLY FILL ALL DETAILS");
    JLabel inv3=new JLabel("PASSWORD DOES NOT MATCH");
    JTextField[] det=new JTextField[10];
    JPasswordField pass=new JPasswordField();
    JPasswordField cpass=new JPasswordField();
    JButton submit=new JButton("CREATE ACCOUNT");
    JButton clear=new JButton("RESET");
    String[] s= {"USERNAME","PASSWORD","CONFIRM PASSWORD","FIRST NAME","LAST NAME","DOB(YYYY-MM-DD)","GENDER(M/F)","MOBILE NO","EMAIL-ID","AADHAR-NO(OPTIONAL)"};
    Connection myconn;
    Statement mystat;
    JButton back=new JButton("BACK");

    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    public Signup()
    {
        super("RAILWAY BOOKING SYSTEM-SIGNUP");
        setLayout(null);
        this.getContentPane().setBackground(Color.WHITE);
        for (int i=0;i<10;i++)
        {
            details[i]=new JLabel(s[i]+" :");
            details[i].setBounds(200,100+50*i,200,40);
            add(details[i]);
            det[i]=new JTextField();
            det[i].setBounds(400,100+50*i,200,40);
            add(det[i]);
        }

        det[9].setText("-");

        inv.setForeground(Color.red);
        inv.setBounds(700,250,300,40);
        inv.setVisible(false);
        add(inv);
        inv2.setForeground(Color.red);
        inv2.setBounds(700,250,300,40);
        inv2.setVisible(false);
        add(inv2);
        inv3.setForeground(Color.red);
        inv3.setBounds(700,250,300,40);
        inv3.setVisible(false);
        add(inv3);

        det[1].setVisible(false);
        det[1].setText("random");
        pass.setBounds(400,150,200,40);
        add(pass);
        det[2].setVisible(false);
        det[2].setText("random");
        cpass.setBounds(400,200,200,40);
        add(cpass);

        submit.setBounds(700, 300,200, 40);
        add(submit);
        clear.setBounds(900,300, 100,40);
        add(clear);

        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        submit.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        char p[]=pass.getPassword();
                        String ps=new String(p);
                        p=cpass.getPassword();
                        String cps=new String(p);
                        for(int i=0;i<10;i++)
                        {
                            if(det[i].getText().isEmpty()||ps.isEmpty()||cps.isEmpty())
                            {
                                inv.setVisible(false);
                                inv3.setVisible(false);
                                inv2.setVisible(true);
                                return;
                            }
                        }
                        if(ps.equals(cps)==false)
                        {
                            inv.setVisible(false);
                            inv2.setVisible(false);
                            inv3.setVisible(true);
                            return;
                        }
                        String adn=det[9].getText();
                        if(adn.equals("-"))
                            adn="NULL";

                        String str="'"+det[0].getText()+"','"+ps+"','"+det[3].getText()
                                +"','"+det[4].getText()+"',DATE'"+det[5].getText()+"','"+det[6].getText()
                                +"','"+det[7].getText()+"','"+det[8].getText()+"','"+adn+"')";
                        try
                        {
                            int f=mystat.executeUpdate("Insert into users values ("+str);
                            ResultSet res=mystat.executeQuery("Select * from users where username='"+det[0].getText()+"'");
                            User u=new User(res);
                            myconn.close();
                            Home h=new Home(u);
                            h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            h.setVisible(true);
                            h.setBounds(0,0,d.width,d.height);
                            dispose();

                        }
                        catch(Exception e)
                        {
                            inv3.setVisible(false);
                            inv2.setVisible(false);
                            inv.setVisible(true);
                            e.printStackTrace();return;

                        }


                    }


                }



        );

        clear.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        inv.setVisible(false);
                        inv2.setVisible(false);
                        for(int i=0;i<10;i++)
                        {
                            det[i].setText("");
                        }
                        pass.setText("");
                        cpass.setText("");
                    }


                }



        );

        back.setBounds(10, 10, 100, 40);
        add(back);

        back.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        Login h=new Login();
                        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        h.setVisible(true);
                        h.setBounds(0, 0, d.width, d.height);
                        dispose();
                    }
                }

        );

    }

}

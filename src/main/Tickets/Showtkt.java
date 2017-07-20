package main.Tickets;

import main.Home;
import main.Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Showtkt extends JFrame {

    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    String[] s={"PNR","DATE","TRAIN-ID","TRAIN-NAME","SOURCE STATION","DESTINATION STATION"
            ,"DEPARTURE TIME","ARRIVAL TIME","STATUS","FIRST NAME","LAST NAME","GENDER","AGE","MEAL","FARE"};
    JLabel[] det=new JLabel[100];
    ArrayList<String> finaldet=new ArrayList<String>();
    Connection myconn;
    Statement mystat;
    String pnr="",srcname="",destname="";
    JButton goback=new JButton("GO BACK TO HOME");
    User uh;
    public Showtkt(User u, ArrayList<String>info, String src, String dest, String date)
    {
        super("RAILWAY BOOKING SYSTEM-FINAL TICKET");
        setLayout(null);
        this.getContentPane().setBackground(Color.white);

        uh=u;

        JLabel congrats=new JLabel("CONGRATULATIONS "+u.fname+" "+u.lname+" you have successfully booked a ticket");
        congrats.setBounds(0, 0, d.width, 100);
        congrats.setHorizontalAlignment(SwingConstants.CENTER);
        congrats.setFont(new Font("Serif", Font.PLAIN, 24));
        add(congrats);



        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
            ResultSet res=mystat.executeQuery("select max(pnr) as mpnr from ticket");
            while(res.next())
            {
                pnr=res.getString("mpnr");
            }

            ResultSet res2=mystat.executeQuery("select stationname from station where stationid='"+src+"'");
            while(res2.next())
            {
                srcname=res2.getString("stationname");
            }

            ResultSet res3=mystat.executeQuery("select stationname from station where stationid='"+dest+"'");
            while(res3.next())
            {
                destname=res3.getString("stationname");
            }

            mystat.executeUpdate("insert into books values('"+pnr+"','"+u.username+"')");
            mystat.executeUpdate("insert into ticketstart values('"+pnr+"','"+src+"')");
            mystat.executeUpdate("insert into ticketdest values('"+pnr+"','"+dest+"')");
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }
        String status=info.get(5);
        if(info.get(5).charAt(0)=='A')
            status="CNF";

        finaldet.add(pnr);
        finaldet.add(date);
        finaldet.add(info.get(0));
        finaldet.add(info.get(1));
        finaldet.add(srcname);
        finaldet.add(destname);
        finaldet.add(info.get(2));
        finaldet.add(info.get(3));
        finaldet.add(status);
        finaldet.add(info.get(6));
        finaldet.add(info.get(7));
        finaldet.add(info.get(8));
        finaldet.add(info.get(9));
        finaldet.add(info.get(10));
        finaldet.add(info.get(4));

        for(int i=0;i<8;i++)
        {
            det[i]=new JLabel(s[i]+" : "+finaldet.get(i));
            det[i].setBounds(d.width/2-300,150+50*i,300,50);
            det[i].setHorizontalAlignment(SwingConstants.CENTER);
            det[i].setBorder(BorderFactory.createLineBorder(Color.black));
            add(det[i]);
        }
        for(int i=8;i<15;i++)
        {
            det[i]=new JLabel(s[i]+" : "+finaldet.get(i));
            det[i].setBounds(d.width/2,150+50*(i-8),300,50);
            det[i].setHorizontalAlignment(SwingConstants.CENTER);
            det[i].setBorder(BorderFactory.createLineBorder(Color.black));
            add(det[i]);
        }

        goback.setBounds(d.width/2-150, 600, 300, 50);
        add(goback);

        goback.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        Home h=new Home(uh);
                        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        h.setBounds(0, 0, d.width, d.height);
                        h.setVisible(true);
                        dispose();

                    }
                }

        );
    }
}

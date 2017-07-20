package main.Trains;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RouteMap extends JFrame {

    String tid;
    Connection myconn;
    Statement mystat;
    JLabel trainid=new JLabel();
    JLabel run=new JLabel();
    JLabel st=new JLabel("STATION NAME");
    JLabel tim=new JLabel("TIME");
    JLabel dy=new JLabel("DAY");
    public RouteMap(String ti)
    {
        super("RAILWAY BOOKING -ROUTE MAP");
        this.getContentPane().setBackground(Color.white);
        setLayout(null);

        //System.out.println(ti);
        tid=ti;
        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        trainid.setText("TRAIN ID- "+tid);
        trainid.setBounds(300,20,200,50);
        add(trainid);
        run.setText("RUNNING DAYS- ");
        run.setBounds(500,20,200,50);
        add(run);
        st.setBounds(400, 70, 200, 50);
        st.setBorder(BorderFactory.createLineBorder(Color.black));
        st.setHorizontalAlignment(SwingConstants.CENTER);
        add(st);
        tim.setBounds(600, 70, 200, 50);
        tim.setBorder(BorderFactory.createLineBorder(Color.black));
        tim.setHorizontalAlignment(SwingConstants.CENTER);
        add(tim);
        dy.setBounds(800,70, 200, 50);
        dy.setHorizontalAlignment(SwingConstants.CENTER);
        dy.setBorder(BorderFactory.createLineBorder(Color.black));
        add(dy);
        try
        {
            ResultSet res=mystat.executeQuery("select stationname,HOUR(time) as hour,HOUR(time)%24 as h, MINUTE(time) as m from route,station where tid='"+tid+"' and station.stationid = route.sid order by time asc");
            JLabel l;
            int id=0;
            while(res.next())
            {
                l=new JLabel(res.getString("stationname"));
                l.setBounds(400, 120+50*id, 200, 50);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(BorderFactory.createLineBorder(Color.black));
                add(l);

                l=new JLabel(String.format("%02d",Integer.parseInt(res.getString("h")))+":"+ String.format("%02d",Integer.parseInt(res.getString("m"))));
                l.setBounds(600, 120+50*id, 200, 50);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(BorderFactory.createLineBorder(Color.black));
                add(l);

                l=new JLabel(Integer.toString(((Integer.parseInt(res.getString("hour")))/24)+1));
                l.setBounds(800, 120+50*id, 200, 50);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(BorderFactory.createLineBorder(Color.black));
                add(l);
                id++;
            }
            ResultSet res2=mystat.executeQuery("select day from days where tid='"+tid+"'");
            id=0;
            while(res2.next())
            {
                l=new JLabel(mapping(res2.getInt("day"))+"  ");
                l.setBounds(600+id*50, 20, 50, 50);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                add(l);
                id++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public String mapping(int a)
    {
        if (a==1)
            return "Sun";
        if (a==2)
            return "Mon";
        if (a==3)
            return "Tue";
        if (a==4)
            return "Wed";
        if (a==5)
            return "Thurs";
        if (a==6)
            return "Fri";
        else
            return "Sat";

    }


}
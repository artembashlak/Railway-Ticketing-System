package main.Trains;

import main.Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class Timetrain extends JFrame {

    JLabel stat[]=new JLabel[100];
    JLabel what=new JLabel("SET TIME TAKEN FROM PREVIOUS STATION TO CURRENT STATION");
    Connection myconn;
    Statement mystat;
    CallableStatement mycstat;
    JLabel hh=new JLabel("HH");
    JLabel mm=new JLabel("MM");
    JLabel ss=new JLabel("SS");
    JTextField[] time=new JTextField[300];
    int nos;
    String tid;
    JButton done=new JButton("DONE");
    Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
    String sttime;
    JButton back=new JButton("BACK");
    JLabel issue=new JLabel("NUMBER OF TRAINS AT THIS STATION AND TIME HAS EXCEEDED THE NUMBER OF PLATFORMS AT STATION NUMBER- ");
    int mark[]=new int[1000];
    public Timetrain(String sttim,ArrayList<String>station,String tidd)
    {
        super("RAILWAY BOOKING SYSTEM-TIME");
        setLayout(null);
        this.getContentPane().setBackground(Color.white);

        tid=tidd;
        sttime=sttim;
        JLabel st=new JLabel("START TIME : "+sttime);
        st.setBounds(600, 100, 200, 40);
        add(st);
        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
            mycstat=myconn.prepareCall("{call insert_into_route(?,?,?,?,?)}");
            mycstat.registerOutParameter(5, Types.INTEGER);

        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }
        done.setBounds(400, 600, 100, 50);
        add(done);
        what.setBounds(10,50,400,40);
        add(what);
        hh.setBounds(400, 50, 40, 40);
        hh.setHorizontalAlignment(SwingConstants.CENTER);
        add(hh);
        mm.setBounds(450, 50, 40, 40);
        mm.setHorizontalAlignment(SwingConstants.CENTER);
        add(mm);
        ss.setBounds(500, 50, 40, 40);
        ss.setHorizontalAlignment(SwingConstants.CENTER);
        add(ss);
        nos=station.size();


        for(int i=0;i<nos;i++)
        {
            mark[i]=0;
            stat[i]=new JLabel(station.get(i));
            stat[i].setBounds(200,100+50*i,200,40);
            add(stat[i]);
            for(int j=3*i;j<3*i+3;j++)
            {
                time[j]=new JTextField();
                time[j].setBounds(400+50*(j%3),100+50*i,40,40);
                time[j].setText("00");
                add(time[j]);

            }
        }
        time[0].setBackground(Color.gray);
        time[0].setEditable(false);
        time[1].setBackground(Color.gray);
        time[1].setEditable(false);
        time[2].setBackground(Color.gray);
        time[2].setEditable(false);

        issue.setBounds(600,50,700,50);
        issue.setVisible(false);
        add(issue);


        done.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        issue.setVisible(false);

                        int h,m,s;
                        h=Integer.parseInt(sttime.substring(0,2));
                        m=Integer.parseInt(sttime.substring(3,5));
                        s=Integer.parseInt(sttime.substring(6,8));
                        String hst,mst,sst;

                        for(int i=0;i<nos;i++)
                        {
                            s+=Integer.parseInt(time[3*i+2].getText());
                            m+=Integer.parseInt(time[3*i+1].getText())+s/60;
                            h+=Integer.parseInt(time[3*i].getText())+m/60;
                            s%=60;
                            m%=60;
                            hst=Integer.toString(h);
                            mst=Integer.toString(m);
                            sst=Integer.toString(s);
                            if(hst.length()==1)
                                hst="0"+hst;
                            if(mst.length()==1)
                                mst="0"+mst;
                            if(sst.length()==1)
                                sst="0"+sst;
                            String sid="";
                            if(mark[i]==1)
                                continue;
                            //System.out.println(i+" " +hst +" " +mst+ " " +sst);
                            try
                            {
                                int id=0;
                                while(stat[i].getText().charAt(id)!=' ')
                                    id++;
                                id++;
                                ResultSet res=mystat.executeQuery("select stationid from station where stationname='"+
                                        stat[i].getText().substring(id, stat[i].getText().length())+"'");
                                while(res.next())
                                    sid=res.getString("stationid");

                            }
                            catch(Exception e2)
                            {
                                e2.printStackTrace();return;
                            }
                            int res=0;
                            try
                            {
                                mycstat.setString(1,sid);
                                mycstat.setString(2,tid);
                                mycstat.setString(3,hst+":"+mst+":"+sst);
                                mycstat.setString(4, sttime);
                                mycstat.execute();
                                res=mycstat.getInt(5);
                                if(res==0 && i!=0)
                                {
                                    for(int j=0;j<3*(i);j++)
                                        time[j].setEditable(false);
                                    issue.setText(issue.getText()+Integer.toString(i+1));
                                    issue.setVisible(true);
                                    back.setEnabled(false);
                                    return;
                                }
                                else if (res==1 || (res==0 && i==0) )
                                {
                                                                    /*if(res==0)
                                                                    {
                                                                        try
									{
										mystat.executeUpdate("delete from route where tid='"+tid+"'");

									}catch(Exception e1)
									{
										e1.printStackTrace();
										return;
									}
                                                                    }*/
                                    Object[] options1 =  {"OK"};
                                    int n=JOptionPane.showOptionDialog(null,"RUNNING TIME LIMIT EXCEEDED FOR THIS TRAIN\nPLEASE RE-ENTER DETAILS"
                                            + "","ERROR",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,options1,options1[0]);
                                    User u=new User();
                                    Trainstations Th=new Trainstations(sttime,u,Integer.toString(nos),tid,1);
                                    Th.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    Th.setVisible(true);
                                    Th.setBounds(0, 0, d.width, d.height);
                                    dispose();
                                    return;
                                }
                                else if(res==2)
                                    mark[i]=1;

                            }
                            catch(Exception e2)
                            {
                                e2.printStackTrace();return;
                            }
                        }
                        Admin a=new Admin();
                        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        a.setBounds(0, 0, d.width, d.height);
                        a.setVisible(true);
                        dispose();
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
                        User u=new User();
                        Trainstations h=new Trainstations(sttime,u,Integer.toString(nos),tid,1);
                        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        h.setVisible(true);
                        h.setBounds(0, 0, d.width, d.height);
                        dispose();
                    }
                }

        );

    }

}

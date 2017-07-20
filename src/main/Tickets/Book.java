package main.Tickets;

import main.Home;
import main.Trains.RouteMap;
import main.Trains.Trainstations;
import main.Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Book extends JFrame {

    User uh;
    Connection myconn;
    Statement mystat,mystat2;
    JLabel from=new JLabel("SOURCE STATION :");
    JLabel to=new JLabel("DESTINATION STATION :");
    JButton fromto=new JButton("ENTER SOURCE AND DESTINATION STATION");
    JLabel datelab=new JLabel("ENTER DATE(YYYY-MM-DD) :");
    JTextField date=new JTextField();
    JTextField ss=new JTextField();
    JTextField ds=new JTextField();
    JLabel inv=new JLabel("PAST DATE NOT ALLOWED");
    JLabel inv2=new JLabel("PLEASE FILL THE DATE");
    Dimension dm=Toolkit.getDefaultToolkit().getScreenSize();
    int todo,id;
    JRadioButton[] choose=new JRadioButton[100];
    ButtonGroup grp=new ButtonGroup();
    JLabel k1,k2,k3,k4,k5,k6;
    JLabel[][] k=new JLabel[200][10];
    JButton[] tn=new JButton[200];
    JButton done=new JButton("DONE");
    JButton back=new JButton("BACK");
    String trainid="";

    public Book(User u, String src, String dest, int tod)
    {
        super("RAILWAY BOOKING SYSTEM-BOOK TICKET");
        setLayout(null);
        this.getContentPane().setBackground(Color.white);

        uh=u;
        todo=tod;
        if(todo==2)
        {
            int i=0;
            while(src.charAt(i)!='(')
                i++;
            src=src.substring(i+1,src.length()-1);
            i=0;
            while(dest.charAt(i)!='(')
                i++;
            dest=dest.substring(i+1,dest.length()-1);
        }
        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
            mystat2=myconn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }
        from.setBounds(200, 70, 200, 40);
        add(from);
        to.setBounds(500,70, 200, 40);
        add(to);
        datelab.setBounds(800, 70, 200, 40);
        add(datelab);
        date.setBounds(1000, 70, 200, 40);
        if(todo==2)
            date.setEditable(false);
        add(date);
        ss.setBounds(350, 70, 100, 40);
        ss.setText(src);
        ss.setEditable(false);
        add(ss);
        ds.setBounds(650, 70, 100, 40);
        ds.setText(dest);
        ds.setEditable(false);
        add(ds);
        fromto.setBounds(dm.width/2-150, 130, 300, 40);
        if(todo==1)
            add(fromto);
        inv.setBounds(1000,30,200,40);
        inv.setVisible(false);
        inv.setForeground(Color.red);
        add(inv);
        inv2.setBounds(1000,30,200,40);
        inv2.setVisible(false);
        inv2.setForeground(Color.red);
        add(inv2);

        k1=new JLabel("TRAIN-ID");
        k1.setVisible(false);
        k1.setBounds(200,150,150,40);
        add(k1);
        k2=new JLabel("TRAIN-NAME");
        k2.setVisible(false);
        k2.setBounds(350,150,150,40);
        add(k2);
        k3=new JLabel("DEPARTURE TIME");
        k3.setVisible(false);
        k3.setBounds(500,150,150,40);
        add(k3);
        k4=new JLabel("ARRIVAL TIME");
        k4.setVisible(false);
        k4.setBounds(650,150,150,40);
        add(k4);
        k5=new JLabel("FARE");
        k5.setVisible(false);
        k5.setBounds(800,150,150,40);
        add(k5);
        k6=new JLabel("STATUS");
        k6.setVisible(false);
        k6.setBounds(950,150,150,40);
        add(k6);
        k1.setHorizontalAlignment(SwingConstants.CENTER);
        k2.setHorizontalAlignment(SwingConstants.CENTER);
        k3.setHorizontalAlignment(SwingConstants.CENTER);
        k4.setHorizontalAlignment(SwingConstants.CENTER);
        k5.setHorizontalAlignment(SwingConstants.CENTER);
        k6.setHorizontalAlignment(SwingConstants.CENTER);
        k1.setBorder(BorderFactory.createLineBorder(Color.black));
        k2.setBorder(BorderFactory.createLineBorder(Color.black));
        k3.setBorder(BorderFactory.createLineBorder(Color.black));
        k4.setBorder(BorderFactory.createLineBorder(Color.black));
        k5.setBorder(BorderFactory.createLineBorder(Color.black));
        k6.setBorder(BorderFactory.createLineBorder(Color.black));

        if(todo==2)
        {
            k1.setVisible(true);
            k2.setVisible(true);
            k3.setVisible(true);
            k4.setVisible(true);
            k5.setVisible(true);
            k6.setVisible(true);

            date.setText(uh.datetravel);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date d;
            int dayOfWeek=1;
            try
            {
                d=sdf.parse(date.getText());
                c.setTime(d);
                dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            }catch(ParseException e)
            {
                e.printStackTrace();return;
            }
            try
            {
                ResultSet res=mystat.executeQuery("select temp.tid as `tid`, HOUR(temp.time)%24 as `timeh`,"
                        + " HOUR(temp.time2)%24 as `time2h`,MINUTE(temp.time) as `timem`, "
                        +"MINUTE(temp.time2) as `time2m` "
                        +"FROM (Select p.tid as `tid`, p.time as `time`,q.time as `time2` "+
                        "From route AS p, route AS q "+
                        "Where p.sid like '" +ss.getText()+ "' and q.sid like '"
                        + ds.getText()+
                        "' and p.tid = q.tid and p.time<q.time ) as temp "+
                        "WHERE ( ('"+date.getText()+"' = CURRENT_DATE AND check_time(temp.time)) OR ( '"+date.getText()+"' > CURRENT_DATE) )"
                        + " AND (FLOOR( "+ Integer.toString(dayOfWeek-1) +
                        " + (HOUR(temp.time)/24))%7 + 1 in ( SELECT day "+
                        "from days "+
                        "where days.tid = temp.tid)) ");


                id=0;
                int yoff=190;
                while(res.next())
                {
                    choose[id]=new JRadioButton(Integer.toString(id+1));
                    grp.add(choose[id]);
                    choose[id].setBackground(Color.white);
                    choose[id].setBounds(150, yoff+id*40, 40, 40);
                    add(choose[id]);
                    trainid=res.getString("tid");
                    k[id][0]=new JLabel(trainid);
                    k[id][0].setBounds(200,yoff+id*40,150,40);
                    k[id][0].setHorizontalAlignment(SwingConstants.CENTER);
                    k[id][0].setBorder(BorderFactory.createLineBorder(Color.black));
                    add(k[id][0]);
                    k[id][2]=new JLabel(String.format("%02d", Integer.parseInt(res.getString("timeh")))+":"+String.format("%02d", Integer.parseInt(res.getString("timem"))));
                    k[id][2].setBorder(BorderFactory.createLineBorder(Color.black));
                    k[id][2].setHorizontalAlignment(SwingConstants.CENTER);
                    k[id][2].setBounds(500,yoff+id*40,150,40);
                    add(k[id][2]);
                    k[id][3]=new JLabel(String.format("%02d", Integer.parseInt(res.getString("time2h")))+":"+String.format("%02d", Integer.parseInt(res.getString("time2m"))));
                    k[id][3].setBorder(BorderFactory.createLineBorder(Color.black));
                    k[id][3].setHorizontalAlignment(SwingConstants.CENTER);
                    k[id][3].setBounds(650,yoff+id*40,150,40);
                    add(k[id][3]);


                    ResultSet res2=mystat2.executeQuery("Select trainname from train where trainid='"+trainid+"'");
                    while(res2.next())
                    {
                        tn[id]=new JButton(res2.getString("trainname"));
                        tn[id].setBorder(BorderFactory.createLineBorder(Color.black));
                        tn[id].setHorizontalAlignment(SwingConstants.CENTER);
                        tn[id].setBackground(Color.white);
                        tn[id].setForeground(new Color(128,0,128));
                        tn[id].setBounds(350,yoff+id*40,150,40);
                        tn[id].addActionListener(


                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        RouteMap rm=new RouteMap(trainid);
                                        rm.setVisible(true);
                                        rm.setBounds(0, 0, dm.width, dm.height);
                                    }
                                }


                        );

                        add(tn[id]);

                    }



                    ResultSet res3=mystat2.executeQuery("SELECT "+
                            "concat(train.basefare + (train.incrperstation*(price2.nos - price1.nos))) as `total fare` "
                            +"FROM	( SELECT COUNT(DISTINCT(q.sid)) AS `nos` FROM route AS p, route AS q "
                            +"WHERE p.tid = '"+trainid+"' AND p.sid = '"+ss.getText()+"' AND q.tid = '"+trainid
                            +"' AND q.time < p.time) AS price1, "
                            +"( SELECT COUNT(DISTINCT(q.sid)) AS `nos` "
                            +" FROM route AS p, route AS q "
                            +" WHERE p.tid = '"+trainid+"' AND p.sid = '"+ds.getText()+"' AND q.tid = '"+trainid
                            + "' AND q.time < p.time) AS price2, "
                            +"train WHERE train.trainid ='"+trainid+"' ");

                    while(res3.next())
                    {
                        k[id][4]=new JLabel(res3.getString("total fare"));
                        k[id][4].setBorder(BorderFactory.createLineBorder(Color.black));
                        k[id][4].setHorizontalAlignment(SwingConstants.CENTER);
                        k[id][4].setBounds(800,yoff+id*40,150,40);
                        add(k[id][4]);

                    }
                    ResultSet res4=mystat2.executeQuery("Select concat( train.totalseats -temp2.cnt  +  temp.cnticket) as `cnt` "
                            +" From (Select count(ticket.pnr) as `cnticket` "
                            +" From train,ticket,ticketdest,ticketstart,route as p, route as q, route as r "
                            +"Where ticket.trainid = '"+trainid+"' and r.tid = ticket.trainid and r.sid = '"+ss.getText()+"' "
                            +"and ticket.trainstartdate = DATE_ADD('"+date.getText()+"',INTERVAL -(HOUR(r.time)/24) DAY) "
                            +"and ((ticketdest.pnr = ticket.pnr and p.sid = ticketdest.sid "
                            +"and p.tid = ticket.trainid and q.sid = '"+ss.getText()+"' and q.tid = ticket.trainid "
                            +"and p.time < q.time ) "
                            +"OR (ticketstart.pnr = ticket.pnr and p.sid = ticketstart.sid and "
                            +"p.tid = ticket.trainid and q.sid = '"+ds.getText()+"' and q.tid = ticket.trainid "
                            +"and p.time > q.time))) as temp, "
                            +"(select count(ticket.pnr) as `cnt` "
                            +"from ticket, route as s "
                            +"where ticket.trainid = '"+trainid+"' and s.tid = ticket.trainid and "
                            + "s.sid = '"+ss.getText()+"' and ticket.trainstartdate = "
                            + "DATE_ADD('"+date.getText()+"',INTERVAL -FLOOR((HOUR(s.time)/24)) DAY) "
                            +") as temp2,train Where train.trainid = '"+trainid+"'");

                    while(res4.next())
                    {
                        int seats=Integer.parseInt(res4.getString("cnt"));
                        //System.out.println(seats);
                        k[id][5]=new JLabel(res4.getString("cnt"));
                        if(seats>0)
                            k[id][5].setText("AL-"+k[id][5].getText());
                        else
                            k[id][5].setText("WL"+Integer.toString(seats-1));

                        k[id][5].setBorder(BorderFactory.createLineBorder(Color.black));
                        k[id][5].setHorizontalAlignment(SwingConstants.CENTER);
                        k[id][5].setBounds(950,yoff+id*40,150,40);
                        add(k[id][5]);

                    }
                    id+=1;
                }
                if(id!=0)
                    choose[0].setSelected(true);
                done.setBounds(dm.width/2-150,650,300,40);
                add(done);

            }
            catch(Exception e3)
            {
                e3.printStackTrace();return;
            }
        }

        done.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        for(int i=0;i<id;i++)
                        {
                            if(choose[i].isSelected())
                            {
                                ArrayList<String>arr=new ArrayList<String>();
                                arr.add(k[i][0].getText());
                                arr.add(tn[i].getText());
                                for(int j=2;j<6;j++)
                                    arr.add(k[i][j].getText());
                                Ticketmake tm=new Ticketmake(uh,arr,ss.getText(),ds.getText(),date.getText());
                                tm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                tm.setVisible(true);
                                tm.setBounds(0, 0, dm.width, dm.height);
                                dispose();
                            }
                        }
                    }
                }

        );

        fromto.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if(date.getText().isEmpty())
                        {
                            inv.setVisible(false);
                            inv2.setVisible(true);
                            return;
                        }
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt,df,today;
                        Calendar c=Calendar.getInstance();
                        try {
                            dt = sdf.parse(date.getText());
                            today=sdf.parse(sdf.format(new Date()));
                            c.setTime(today);
                            c.add(Calendar.MONTH, 3);
                            df=c.getTime();
                            if(dt.compareTo(today)<0)
                            {
                                inv.setText("Past Date Not Allowed!");
                                inv.setVisible(true);
                                inv2.setVisible(false);
                                return;
                            }
                            if (dt.compareTo(df)>0)
                            {
                                inv.setText("Should be Less than 3 months");
                                inv.setVisible(true);
                                inv2.setVisible(false);
                                return;
                            }
                        } catch (ParseException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();return;
                        }

                        if(todo==1)
                        {
                            uh.datetravel=date.getText();
                            Trainstations ts=new Trainstations("00:00:00",uh,"2","12",2);
                            ts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            ts.setVisible(true);
                            ts.setBounds(0, 0, dm.width, dm.height);
                            dispose();
                        }

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
                        if(todo==1)
                        {
                            Home h=new Home(uh);
                            h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            h.setVisible(true);
                            h.setBounds(0, 0, dm.width, dm.height);
                            dispose();
                        }
                        else if(todo==2)
                        {
                            Book h = new Book(uh,"SOURCE","DESTINATION",1);
                            h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            h.setVisible(true);
                            h.setBounds(0, 0, dm.width, dm.height);
                            dispose();
                        }

                    }
                }

        );

    }

}

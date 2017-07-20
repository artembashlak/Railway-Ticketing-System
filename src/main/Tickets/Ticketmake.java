package main.Tickets;

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

public class Ticketmake extends JFrame {

    String[] jlab={"TRAIN-ID","TRAIN-NAME","SOURCE","DESTINATION","FIRST-NAME","LAST-NAME","GENDER","AGE","MEAL(V/NV)"};
    JLabel[] det=new JLabel[10];
    JTextField[] in=new JTextField[10];
    JButton done=new JButton("DONE");
    Connection myconn;
    Statement mystat;
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    String stdate,date,src,dest;
    ArrayList<String>info=new ArrayList<String>();
    User uh;
    JLabel inv=new JLabel("PLEASE FILL ALL RECORDS");
    JLabel inv2=new JLabel("INCORRECT PNR OR GUARDIAN NOT ABOVE 18 YEARS OF AGE");
    JButton back=new JButton("BACK");
    JLabel age=new JLabel("ENTER PNR OF THE GUARDIAN'S TICKET OR BOOK A TICKET FOR THE GUARDIAN FIRST(MAKE CHANGES ABOVE)");
    JTextField agetf=new JTextField();
    JButton done2=new JButton("DONE");

    public Ticketmake(User u, ArrayList<String>inf, String sr, String des, String dat)
    {
        super("RAILWAY BOOKING SYSTEM-TICKET DETAILS");
        setLayout(null);
        this.getContentPane().setBackground(Color.white);

        info=inf;
        src=sr;
        dest=des;
        date=dat;
        uh=u;
        for(int i=0;i<4;i++)
        {
            det[i]=new JLabel(jlab[i]+" -");
            det[i].setBorder(BorderFactory.createLineBorder(Color.black));
            det[i].setHorizontalAlignment(SwingConstants.CENTER);
            det[i].setBounds(300+200*i, 100, 200, 40);
            add(det[i]);
        }
        det[0].setText(det[0].getText()+info.get(0));
        det[1].setText(det[1].getText()+info.get(1));
        det[2].setText(det[2].getText()+src);
        det[3].setText(det[3].getText()+dest);

        for(int i=4;i<9;i++)
        {
            det[i]=new JLabel(jlab[i]);
            det[i].setBorder(BorderFactory.createLineBorder(Color.black));
            det[i].setHorizontalAlignment(SwingConstants.CENTER);
            det[i].setBounds(200+200*(i-4), 200, 200, 40);
            add(det[i]);
            in[i-4]=new JTextField();
            in[i-4].setHorizontalAlignment(SwingConstants.CENTER);
            in[i-4].setBorder(BorderFactory.createLineBorder(Color.black));
            in[i-4].setBounds(200+200*(i-4), 240, 200, 40);
            add(in[i-4]);
        }

        done.setBounds(d.width/2-150, 400, 300, 50);
        add(done);

        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }
        inv.setBounds(d.width/2-150, 360, 1000, 40);
        inv.setForeground(Color.red);
        inv.setVisible(false);
        add(inv);

        inv2.setBounds(d.width/2-150, 360, 1000, 40);
        inv.setForeground(Color.red);
        inv2.setVisible(false);
        add(inv2);

        age.setBounds(300, 500, 1000, 50);
        age.setVisible(false);
        add(age);

        agetf.setBounds(400, 600, 200, 50);
        agetf.setVisible(false);
        add(agetf);

        done2.setBounds(700, 600, 100, 50);
        done2.setVisible(false);
        add(done2);

        done2.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        String datea="",srca="",desta="";
                        int ag=0;
                        inv.setVisible(false);
                        inv2.setVisible(false);
                        if(agetf.getText().isEmpty())
                        {
                            inv.setVisible(true);
                            return;
                        }
                        try
                        {
                            ResultSet res=mystat.executeQuery("select ticket.age as age,ticket.date as date,ticketdest.sid as dest,ticketstart.sid"
                                    + " as src from ticket,ticketdest,ticketstart"
                                    + " where ticket.pnr='"+agetf.getText()+"' and ticketdest.pnr=ticket.pnr and ticketstart.pnr=ticket.pnr");
                            int cnt=0;
                            while(res.next())
                            {
                                datea=res.getString("date");
                                srca=res.getString("src");
                                desta=res.getString("dest");
                                ag=res.getInt("age");
                                cnt++;
                            }
                            if(cnt==0 || !date.equals(datea) || !src.equals(srca) || !dest.equals(desta) || ag<18 )
                            {
                                inv2.setVisible(true);
                                return;
                            }
                            done2.setEnabled(false);
                            agetf.setEditable(false);
                            age.setText("VERIFIED SUCCESFULLY");
                            age.setForeground(Color.GREEN);
                        }
                        catch(Exception ef)
                        {
                            inv2.setVisible(true);
                            ef.printStackTrace();
                        }
                    }
                }

        );



        done.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        for(int i=0;i<5;i++)
                        {
                            if(in[i].getText().isEmpty())
                            {
                                inv.setVisible(true);
                                return;
                            }
                        }
                        if(Integer.parseInt(in[3].getText())<=12 && done2.isEnabled())
                        {
                            age.setVisible(true);
                            agetf.setVisible(true);
                            done2.setVisible(true);
                            return;
                        }
                        for(int i=0;i<5;i++)
                            info.add(in[i].getText());
                        try
                        {
                            ResultSet res=mystat.executeQuery("select DATE_ADD('"+date+"',"
                                    + "INTERVAL -FLOOR(HOUR(r.time)/24) DAY) as `stdate` "
                                    +"from route as r "
                                    +"where r.tid = "+info.get(0)+" and r.sid = '"+src+"'");
                            while(res.next())
                            {
                                stdate=res.getString("stdate");
                            }
                        }catch(Exception ef)
                        {
                            ef.printStackTrace();return;
                        }

                        try
                        {
                            String str=info.get(5);
                            int stat=Integer.parseInt(str.substring(2, info.get(5).length()));
                            if(str.charAt(0)=='A')
                                stat=-1*stat;
                            mystat.executeUpdate("insert into ticket values(NULL,'"+date+"','"
                                    +in[0].getText()+"','"
                                    +in[1].getText()+"','"+in[2].getText()+"','"+in[3].getText()+"','"
                                    +Integer.toString(stat)+"','"+in[4].getText()+"','"+info.get(0)+"','"
                                    +stdate+"','"+info.get(4)+"')");


                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();return;
                        }
                        Showtkt st=new Showtkt(uh,info,src,dest,date);
                        st.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        st.setBounds(0, 0, d.width, d.height);
                        st.setVisible(true);
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
                        Book h=new Book(uh,"("+src+")","("+dest+")",2);
                        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        h.setVisible(true);
                        h.setBounds(0, 0, d.width, d.height);
                        dispose();
                    }
                }

        );


    }

}

package main.Trains;

import main.Tickets.Book;
import main.Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Trainstations extends JFrame {

    int nos,cnt;
    String tid;
    JLabel[] stlab=new JLabel[100];
    Connection myconn;
    Statement mystat;
    ArrayList<String>str=new ArrayList<String>();
    ArrayList<String>finalstationlist=new ArrayList<String>();
    JButton done=new JButton("DONE");
    JButton back=new JButton("BACK");
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    JLabel inv=new JLabel("KINDLY SELECT ALL THE REQUIRED STATIONS");
    Stack<Integer> st=new Stack<Integer>();
    int to;
    User uh;
    String sttime;
    public Trainstations(String sttim, User u, String s, String tidd, int todo)
    {
        super("RAILWAY BOOKING SYSTEM-STATIONS");
        setLayout(null);
        this.getContentPane().setBackground(Color.white);

        //addMouseListener(this);
        nos=Integer.parseInt(s);
        to=todo;
        tid=tidd;
        sttime=sttim;
        uh=u;
        for(int i=nos;i>=1;i--)
            st.push(new Integer(i));

        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        try
        {
            ResultSet res=mystat.executeQuery("Select stationname,stationid from station where status=0");
            cnt=0;
            while(res.next())
            {
                cnt++;
                str.add(res.getString("stationname")+"("+res.getString("stationid")+")");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        Collections.sort(str);

        for(int i=0;i<cnt;i++)
        {
            stlab[i]=new JLabel(str.get(i));
            stlab[i].setHorizontalAlignment(SwingConstants.CENTER);
            stlab[i].setOpaque(true);
            stlab[i].setBackground(Color.white);
            stlab[i].setBorder(BorderFactory.createLineBorder(Color.black));
            stlab[i].setBounds(10+130*((int)(i%10)),100+40*(i/10),130,40);
            add(stlab[i]);
            stlab[i].addMouseListener(

                    new MouseListener()
                    {

                        @Override
                        public void mouseClicked(MouseEvent e) {

                            JLabel src=(JLabel)e.getSource();
                            if(src.getBackground()==Color.green)
                            {
                                src.setBackground(Color.white);
                                String str=src.getText();
                                int i=0;
                                while(str.charAt(i)!='.')
                                    i++;
                                i++;
                                int a=Integer.parseInt(str.substring(0, i-1));
                                st.push(a);
                                src.setText(str.substring(i, str.length()));
                            }
                            else
                            {
                                if(!st.empty())
                                {
                                    src.setBackground(Color.green);
                                    int a=st.pop();
                                    src.setText(Integer.toString(a)+". "+src.getText());
                                }
                            }

                        }

                        @Override
                        public void mouseEntered(MouseEvent arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void mouseExited(MouseEvent arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void mousePressed(MouseEvent arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void mouseReleased(MouseEvent arg0) {
                            // TODO Auto-generated method stub

                        }

                    }

            );

        }
        inv.setBounds(d.width/2-100, 670, 200, 40);
        inv.setVisible(false);
        add(inv);

        done.setBounds(d.width/2-150, 620, 300, 50);
        add(done);
        done.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if(!st.isEmpty())
                        {
                            inv.setVisible(true);
                            return;

                        }
                        for(int i=0;i<cnt;i++)
                        {
                            if(stlab[i].getBackground()==Color.green)
                            {
                                int j=0;
                                while(stlab[i].getText().charAt(j)!='(')
                                    j++;
                                if(to==1)
                                    finalstationlist.add(stlab[i].getText().substring(0, j));
                                else if(to==2)
                                    finalstationlist.add(stlab[i].getText());
                            }
                        }
                        Collections.sort(finalstationlist);
                        if(to==1)
                        {
                            Timetrain t=new Timetrain(sttime,finalstationlist,tid);
                            t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            t.setVisible(true);
                            t.setBounds(0,0,d.width,d.height);
                            dispose();
                        }
                        else if(to==2)
                        {
                            Book t=new Book(uh,finalstationlist.get(0),finalstationlist.get(1),2);
                            t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            t.setVisible(true);
                            t.setBounds(0,0,d.width,d.height);
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
                        if(to==2)
                        {
                            Book h = new Book(uh,"SOURCE","DESTINATION",1);
                            h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            h.setVisible(true);
                            h.setBounds(0, 0, d.width, d.height);
                            dispose();
                        }
                        else if (to==1)
                        {
                            Admin h = new Admin();
                            h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            h.setVisible(true);
                            h.setBounds(0, 0, d.width, d.height);
                            dispose();
                        }

                    }
                }

        );

    }
}

package main;

import main.Tickets.Book;
import main.Users.Login;
import main.Users.Profile;
import main.Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class Home extends JFrame {

    JButton book=new JButton("BOOK A NEW TICKET");
    JButton cancel=new JButton("SELECT ANY ONE TO CANCEL TICKET");
    JButton profile=new JButton ("EDIT PROFILE");
    JLabel welc=new JLabel();
    String[] jlab={"PNR","DATE","FIRST NAME","LAST NAME","GENDER","AGE","STATUS","MEAL","TRAIN-ID"};
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    User uh;
    int id=0,yoff=0;
    Connection myconn;
    Statement mystat;
    CallableStatement mycstat;
    JRadioButton[] rad=new JRadioButton[100];
    ButtonGroup grp=new ButtonGroup();
    ArrayList<String>pnrlist=new ArrayList<String>();
    JButton logout=new JButton("LOGOUT");

    public Home(User u)
    {
        super("RAILWAY BOOKING SYSTEM-HOME");
        setLayout(null);
        this.getContentPane().setBackground(Color.WHITE);

        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();
            mycstat=myconn.prepareCall("{CALL delete_ticket(?)}");

        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        uh=u;
        welc.setText("WELCOME "+u.fname.toUpperCase()+" "+u.lname.toUpperCase()+"   "+"- BOOKING HISTORY");
        welc.setHorizontalAlignment(SwingConstants.CENTER);
        welc.setBounds(0, 0, d.width, 100);
        add(welc);


        book.setHorizontalAlignment(SwingConstants.CENTER);
        book.setBounds(d.width/2-300,600,200,50);
        add(book);

        profile.setHorizontalAlignment(SwingConstants.CENTER);
        profile.setBounds(d.width/2+100,600,200,50);
        add(profile);

        JLabel head;
        for(int i=0;i<9;i++)
        {
            head=new JLabel(jlab[i]);
            head.setHorizontalAlignment(SwingConstants.CENTER);
            head.setBorder(BorderFactory.createLineBorder(Color.black));
            head.setBounds(200+100*i,70,100,40);
            add(head);
        }
        try
        {
            ResultSet res=mystat.executeQuery("select * from ticket where pnr in (select pnr from books where username='"+u.username+"')");
            yoff=100;
            int xoff;
            JLabel l=new JLabel();
            while(res.next())
            {
                xoff=200;
                rad[id]=new JRadioButton(Integer.toString(id+1));
                rad[id].setBounds(xoff-50, yoff, 40, 40);
                rad[id].setBackground(Color.white);
                add(rad[id]);
                grp.add(rad[id]);
                id+=1;
                l=new JLabel(res.getString("pnr"));
                pnrlist.add(res.getString("pnr"));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setBounds(xoff, yoff, 100, 40);
                add(l);
                l=new JLabel(res.getString("date"));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setBounds(xoff+100, yoff, 100, 40);
                add(l);
                l=new JLabel(res.getString("firstname"));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setBounds(xoff+200, yoff, 100, 40);
                add(l);
                l=new JLabel(res.getString("lastname"));
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBounds(xoff+300, yoff, 100, 40);
                add(l);
                l=new JLabel(res.getString("gender"));
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBounds(xoff+400, yoff, 100, 40);
                add(l);
                l=new JLabel(res.getString("age"));
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBounds(xoff+500, yoff, 100, 40);
                add(l);
                int f=res.getInt("status");
                String str="";
                if(f>0)
                    str="CNF";
                else if(f<0)
                {
                    str="WL"+Integer.toString(f);
                }
                l=new JLabel(str);
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBounds(xoff+600, yoff, 100, 40);
                add(l);
                l=new JLabel(res.getString("meal"));
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBounds(xoff+700, yoff, 100, 40);
                add(l);
                l=new JLabel(res.getString("trainid"));
                l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBounds(xoff+800, yoff, 100, 40);
                add(l);
                yoff+=40;
            }
            if(yoff==100)
            {
                cancel.setVisible(false);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }
        cancel.setHorizontalAlignment(SwingConstants.CENTER);
        cancel.setBounds(d.width/2-150,yoff+10,300,50);
        add(cancel);

        profile.addActionListener(


                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        Profile p=new Profile(uh);
                        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        p.setVisible(true);
                        p.setBounds(0,0,d.width,d.height);
                        dispose();

                    }
                }


        );

        book.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        Book b = new Book(uh,"SOURCE","DESTINATION",1);
                        b.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        b.setVisible(true);
                        b.setBounds(0,0,d.width,d.height);
                        dispose();
                    }
                }
        );

        cancel.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        int i;
                        String pnrfinal="";
                        for(i=0;i<id;i++)
                        {
                            if(rad[i].isSelected())
                            {
                                pnrfinal=pnrlist.get(i);
                                break;
                            }
                        }
                        JLabel inv=new JLabel("PLEASE SELECT ONE TICKET TO DELETE");
                        inv.setBounds(d.width/2-150,yoff+50,300,50);
                        inv.setHorizontalAlignment(SwingConstants.CENTER);
                        inv.setForeground(Color.red);
                        inv.setVisible(false);
                        add(inv);
                        if(i==id)
                        {
                            inv.setVisible(true);
                            return;
                        }
                        else
                        {
                            inv.setVisible(false);
                        }
                        try
                        {
                            mycstat.setString(1, pnrfinal);
                            mycstat.execute();
                        }
                        catch(Exception e4)
                        {
                            e4.printStackTrace();
                            return;
                        }
                        Home h=new Home(uh);
                        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        h.setVisible(true);
                        h.setBounds(0,0,d.width,d.height);
                        dispose();
                    }
                }
        );
        logout.setBounds(10, 10, 100, 40);
        add(logout);
        logout.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        Login l=new Login();
                        l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        l.setVisible(true);
                        l.setBounds(0,0,d.width,d.height);
                        dispose();

                    }
                }


        );
    }
}

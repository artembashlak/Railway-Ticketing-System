package main.Trains;

import main.Users.Login;
import main.Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class Admin extends JFrame {

    JLabel welc=new JLabel("WELCOME ADMINISTRATOR");
    JLabel as=new JLabel("ADD STATION");
    JLabel at=new JLabel("ADD TRAIN");
    JButton uprec=new JButton("UPDATE TICKETS");
    JButton us=new JButton("ADD STATION");
    JButton ut=new JButton("SET STATIONS");
    JButton dt=new JButton("DELETE TRAIN");
    JButton ds=new JButton("DELETE STATION");
    String[] sinfo={"STATION ID","STATION NAME","NO OF PLATFORMS"};
    String[] tinfo={"TRAIN ID","TRAIN NAME","TOTAL SEATS","START TIME(HH:MM:SS)","RUNNING DAYS","CATEGORY","BASE FARE","INCREMENT","NO. OF STATIONS"};
    JLabel[] slab=new JLabel[3];
    JLabel[] tlab=new JLabel[9];
    JTextField[] stf=new JTextField[3];
    JTextField[] ttf=new JTextField[9];
    JTextField dttf=new JTextField();
    JTextField dstf=new JTextField();
    String[] days={"SUN","MON","TUE","WED","THURS","FRI","SAT"};
    JCheckBox[] runday=new JCheckBox[7];
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    Connection myconn;
    Statement mystat;
    CallableStatement mycstat,mycstat2;
    JLabel invs=new JLabel("STATION ALREADY EXISTS");
    JLabel invt=new JLabel("TRAIN ALREADY EXISTS");
    JLabel inv2s=new JLabel("KINDLY FILL ALL DETAILS");
    JLabel inv2t=new JLabel("KINDLY FILL ALL DETAILS");
    JLabel succs=new JLabel("STATION ADDED SUCCESSFULLY");
    JLabel succtk=new JLabel("TICKETS UPDATED SUCCESSFULLY");
    JLabel succt=new JLabel("TRAIN ADDED SUCCESSFULLY");
    JLabel succds=new JLabel("STATION DELETED SUCCESSFULLY");
    JLabel succdt=new JLabel("TRAIN DELETED SUCCESSFULLY");
    JLabel invdt=new JLabel("INVALID TRAIN ID");
    JLabel invds=new JLabel("INVALID STATION ID");
    JButton logout=new JButton("LOGOUT");
    JButton meal=new JButton("MEAL ORDER STATUS");
    JLabel mealtid=new JLabel("ENTER TRAIN-ID :");
    JLabel mealdate=new JLabel("ENTER JOURNEY DATE(YYYY-MM-DD) :");
    JTextField mealtidtf=new JTextField();
    JTextField mealdatetf=new JTextField();
    JLabel mealerror=new JLabel("INVALID DETAILS");
    JLabel succ=new JLabel();


    public Admin()
    {
        super("RAILWAY BOOKING SYSTEM-ADMIN");
        setLayout(null);
        this.getContentPane().setBackground(Color.white);



        try
        {
            myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAY","root","root");
            mystat=myconn.createStatement();

        }
        catch(Exception e)
        {
            e.printStackTrace();return;
        }

        welc.setBounds(0, 0, d.width,100);
        welc.setHorizontalAlignment(SwingConstants.CENTER);
        add(welc);

        as.setBounds(0,100,d.width/3,50);
        as.setHorizontalAlignment(SwingConstants.CENTER);
        add(as);
        at.setBounds(d.width/3,100,d.width/3,50);
        at.setHorizontalAlignment(SwingConstants.CENTER);
        add(at);

        succtk.setBounds(2*d.width/3+50,60,200,40);
        succtk.setForeground(Color.green);
        succtk.setVisible(false);
        add(succtk);
        uprec.setBounds(2*d.width/3+50,100,200,50);
        uprec.setHorizontalAlignment(SwingConstants.CENTER);
        add(uprec);

        for(int i=0;i<3;i++)
        {
            slab[i]=new JLabel(sinfo[i]+" :");
            slab[i].setBounds(100,200+50*i,200,40);
            add(slab[i]);
            stf[i]=new JTextField();
            stf[i].setBounds(250,200+50*i,100,40);
            add(stf[i]);

        }
        us.setBounds(125, 350, 200, 40);
        invs.setVisible(false);
        invs.setBounds(125,400,200,40);
        invs.setForeground(Color.RED);
        add(invs);
        add(us);
        inv2s.setVisible(false);
        inv2s.setBounds(125,400,200,40);
        inv2s.setForeground(Color.RED);
        add(inv2s);
        succs.setVisible(false);
        succs.setBounds(125,400,200,40);
        succs.setForeground(Color.GREEN);
        add(succs);
        for(int i=0;i<9;i++)
        {
            tlab[i]=new JLabel(tinfo[i]+" :");
            tlab[i].setBounds(d.width/3+100,200+50*i,200,40);
            add(tlab[i]);
            ttf[i]=new JTextField();
            ttf[i].setBounds(d.width/3+250,200+50*i,100,40);
            add(ttf[i]);

        }
        ut.setBounds(d.width/3+125, 650, 200, 40);
        add(ut);
        invt.setVisible(false);
        invt.setBounds(d.width/3+325,650,200,40);
        invt.setForeground(Color.RED);
        add(invt);
        inv2t.setVisible(false);
        inv2t.setBounds(d.width/3+325,650,200,40);
        inv2t.setForeground(Color.RED);
        add(inv2t);
        succt.setVisible(false);
        succt.setBounds(d.width/3+325,650,200,40);
        succt.setForeground(Color.GREEN);
        add(succt);

        ttf[4].setVisible(false);
        ttf[4].setText("random");
        for(int i=0;i<7;i++)
        {
            runday[i]=new JCheckBox(days[i]);
            runday[i].setBackground(Color.white);
            runday[i].setBounds(d.width/3+250+70*i,400,70,40);
            add(runday[i]);
        }

        us.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        for(int i=0;i<3;i++)
                        {
                            if(stf[i].getText().isEmpty())
                            {
                                invs.setVisible(false);
                                invt.setVisible(false);
                                inv2s.setVisible(true);
                                inv2t.setVisible(false);
                                succs.setVisible(false);
                                succt.setVisible(false);
                                return;
                            }
                        }
                        try
                        {
                            mystat.executeUpdate("Insert into station values('"+stf[0].getText()+"','"+stf[1].getText()+"','"+stf[2].getText()+"',0)");
                        }
                        catch(Exception ez)
                        {
                            invs.setVisible(true);
                            invt.setVisible(false);
                            inv2s.setVisible(false);
                            inv2t.setVisible(false);
                            succs.setVisible(false);
                            succt.setVisible(false);
                            ez.printStackTrace();
                            return;
                        }
                        invs.setVisible(false);
                        invt.setVisible(false);
                        inv2s.setVisible(false);
                        inv2t.setVisible(false);
                        succs.setVisible(true);
                        succt.setVisible(false);
                        for(int i=0;i<3;i++)
                        {
                            stf[i].setText("");
                        }
                    }
                }
        );
        ut.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        String str="";
                        for(int i=0;i<7;i++)
                        {
                            if(runday[i].isSelected())
                            {
                                str+=Integer.toString(i+1);

                            }
                        }
                        ttf[4].setText(str);
                        for(int i=0;i<9;i++)
                        {
                            if(ttf[i].getText().isEmpty())
                            {
                                invs.setVisible(false);
                                invt.setVisible(false);
                                inv2s.setVisible(false);
                                inv2t.setVisible(true);
                                succs.setVisible(false);
                                succt.setVisible(false);
                                return;
                            }
                        }
                        try
                        {
                            mystat.executeUpdate("Insert into train(trainid,trainname,totalseats,"
                                    + "type,basefare,incrperstation)"
                                    + " values('"+ttf[0].getText()+"','"+ttf[1].getText()+"','"+ttf[2].getText()+"','"
                                    +ttf[5].getText()+"','"+ttf[6].getText()
                                    +"','"+ttf[7].getText()+"')");

                        }
                        catch(Exception ez)
                        {
                            invs.setVisible(false);
                            invt.setVisible(true);
                            inv2s.setVisible(false);
                            inv2t.setVisible(false);
                            succs.setVisible(false);
                            succt.setVisible(false);
                            ez.printStackTrace();
                            return;
                        }
                        for(int i=0;i<str.length();i++)
                        {
                            try
                            {
                                mystat.executeUpdate("insert into days values('"+ttf[0].getText()+"','"+str.charAt(i)+"')");
                            }
                            catch(Exception e3)
                            {
                                e3.printStackTrace();return;
                            }
                        }
                        invs.setVisible(false);
                        invt.setVisible(false);
                        inv2s.setVisible(false);
                        inv2t.setVisible(false);
                        succs.setVisible(false);
                        succt.setVisible(true);

                        User u =new User();
                        Trainstations ts=new Trainstations(ttf[3].getText(),u,ttf[8].getText(),ttf[0].getText(),1);
                        ts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        ts.setVisible(true);
                        ts.setBounds(0, 0, d.width, d.height);
                        dispose();
                    }
                }
        );
        uprec.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            mystat.executeUpdate("delete from ticket where date<DATE_SUB(CURRENT_DATE,INTERVAL 2 MONTH)");
                        }catch(Exception ex)
                        {
                            ex.printStackTrace();return;

                        }
                        succtk.setVisible(true);
                    }
                }


        );

        dt.setBounds(125, 500, 200, 40);
        add(dt);
        ds.setBounds(125, 650, 200, 40);
        add(ds);
        dttf.setText("ENTER TRAIN-ID");
        dttf.setBounds(125, 450, 200, 40);
        add(dttf);
        dstf.setText("ENTER STATION-ID");
        dstf.setBounds(125, 600, 200, 40);
        add(dstf);

        succdt.setForeground(Color.green);
        succdt.setBounds(125, 410, 100, 40);
        succdt.setVisible(false);
        add(succdt);
        invdt.setForeground(Color.red);
        invdt.setBounds(125, 410, 100, 40);
        invdt.setVisible(false);
        add(invdt);

        succds.setForeground(Color.green);
        succds.setBounds(125, 560, 100, 40);
        succds.setVisible(false);
        add(succds);
        invds.setForeground(Color.red);
        invds.setBounds(125, 560, 100, 40);
        invds.setVisible(false);
        add(invds);

        dttf.addMouseListener(

                new MouseListener()
                {

                    @Override
                    public void mouseClicked(MouseEvent arg0) {
                        dttf.setText("");
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

        dstf.addMouseListener(

                new MouseListener()
                {

                    @Override
                    public void mouseClicked(MouseEvent arg0) {
                        dstf.setText("");
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
        dt.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            int f=mystat.executeUpdate("update train set status=1 where trainid='"+dttf.getText()+"'");
                            if(f==0)
                            {
                                succdt.setVisible(false);
                                invdt.setVisible(true);
                                return;
                            }
                        }
                        catch (SQLException e1) {

                            e1.printStackTrace();
                            return;
                        }
                        invdt.setVisible(false);
                        succdt.setVisible(true);
                        dttf.setText("ENTER TRAIN ID");
                    }
                }
        );

        ds.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            int f=mystat.executeUpdate("update train set status=1 where pnr='"+dstf.getText()+"'");
                            if(f==0)
                            {
                                succds.setVisible(false);
                                invds.setVisible(true);
                                return;
                            }
                        }
                        catch (SQLException e1) {

                            e1.printStackTrace();
                            return;
                        }
                        invds.setVisible(false);
                        succds.setVisible(true);
                        dstf.setText("ENTER STATION ID");

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

        mealtid.setBounds(900,200,200,40);
        add(mealtid);
        mealtidtf.setBounds(1100, 200, 100, 40);
        add(mealtidtf);
        mealdate.setBounds(850, 250, 250, 40);
        add(mealdate);
        mealdatetf.setBounds(1100, 250, 100, 40);
        add(mealdatetf);
        meal.setBounds(950, 300, 200, 50);
        add(meal);
        mealerror.setBounds(900, 350, 200, 50);
        mealerror.setVisible(false);
        add(mealerror);
        succ.setBounds(900,350,200,50);
        succ.setVisible(false);
        add(succ);

        meal.addActionListener(


                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        mealerror.setVisible(false);
                        succ.setVisible(false);
                        int cnt=0;
                        int veg=0,nonveg=0;
                        try
                        {
                            ResultSet res=mystat.executeQuery("Select veg,nonveg from meal_order where trainid='"+mealtidtf.getText()+"' and date='"+mealdatetf.getText()+"'");
                            while(res.next())
                            {
                                veg=res.getInt("veg");
                                nonveg=res.getInt("nonveg");
                                cnt++;
                            }
                            if(cnt==0)
                            {
                                mealerror.setVisible(true);
                                return;
                            }
                        }catch(Exception d)
                        {
                            mealerror.setVisible(true);
                            d.printStackTrace();
                            return;
                        }
                        succ.setText("VEG- "+Integer.toString(veg)+"   NON-VEG-"+Integer.toString(nonveg));
                        succ.setVisible(true);

                    }
                }


        );



    }
}

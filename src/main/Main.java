package main;

import main.Users.Login;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        //main.Users.Signup l=new main.Users.Signup();
        Login l=new Login();
        //main.Trains.Admin l=new main.Trains.Admin();
        //main.Trains.RouteMap l =new main.Trains.RouteMap("10050");
        //main.Trains.Trainstations l=new main.Trains.Trainstations("5","12");
        //main.UsersUser u=new main.Users.User.User();
        //main.Tickets.Book l=new main.Tickets.Book(u,"1. (AGC)","2. (DGR)",2);
        l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l.setBounds(0,0,d.width,d.height);
        l.setVisible(true);

        ////System.out.println(System.getProperty("java.version"));
        // TODO code application logic here
    }

}

package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//class BankingThread extends Thread implements Comparable<BankingThread> {
//    private Account account;
//    private String mode;
//    private int totalbalance;
//    private boolean loop = true;
//    public BankingThread(String n, Account ac, String m)
//    {
//        super(n);
//        account = ac;
//        mode = m;
//    }
//    public void run()
//    {
//    /* Add a loop to process either 5 consecutive deposits (mode = m) or 5
//    consecutive withdraws (mode = w). For each transaction processing
//    1. Random the amount of money to deposit/withdraw. You may set the range of
//    random values, e.g. 1-10
//    2. Call account.deposit(…) or account.withdraw(…) to deposit/withdraw money
//    */
//        int count_loop = 1;
//        while (loop)
//        {
//            Random rand = new Random();
//            int  money = rand.nextInt(10) + 1;
//            try
//            {
//                synchronized (this) {
//                    switch (mode) {
//                        case "d":
//                        case "D":
//                            account.deposit(money, this.getName(), count_loop);
//                            totalbalance += money;
//                            Thread.sleep(5);
//                            break;
//                        case "w":
//                        case "W":
//                            account.withdraw(money, this.getName(), count_loop);
//                            totalbalance -= money;
//                            Thread.sleep(5);
//                            break;
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//                System.err.println(e);
//            }
//            count_loop++;
//            if (count_loop == 6)
//            {
//                loop = false;
//            }
//        }
//    }
//
//    @Override
//    public int compareTo(BankingThread t) {
//        if (this.totalbalance < t.totalbalance) {
//            return -1;
//        } else if (this.totalbalance > t.totalbalance) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }
//
//} // end BankingThread

//class Account {
////    private int balance;
////    public Account(int b) {
////        balance = b;
////    }
////    synchronized public void deposit(int money, String name, int count)
////    {
////        // Update balance &amp; print thread name, transaction ID, money to deposit, balance
////        balance += money;
////        System.out.printf("%s  transaction %d  + %-2d  balance = %-3d\n", name, count, money, balance);
////    }
////    public synchronized boolean withdraw(int money, String name, int count)
////    {
////    /* If there is enough money to withdraw
////    - Update balance
////    - Print thread name, transaction ID, money to withdraw, balance
////    Otherwise
////    - Print thread name, transaction ID, money to withdraw, failure message
////    */
////        if (balance < money)
////        {
////            System.out.printf("%s  transaction %d  - %-2d  fails\n", name, count, money);
////            return false;
////        }
////        else if (balance == money)
////        {
////            balance -= money;
////            System.out.printf("%s  transaction %d  - %-2d  balance = %-3d\n", name, count, money, balance);
////            return true;
////        }
////        else
////        {
////            balance -= money;
////            System.out.printf("%s  transaction %d  - %-2d  balance = %-3d\n", name, count, money, balance);
////            return true;
////        }
////    }
////    public int print(){
////        System.out.printf("===== Final Balance = %d =====\n", balance);
////        System.out.println();
////        return balance;
////    }
////} // end Account

//public class StockSimulation {
//
//    public static void main(String[] args) {
//
//        boolean doTrans = true;
//        boolean flag = true;
//        int bal = 0;
//        Scanner scan = new Scanner(System.in);
//
//        System.out.printf("Initial Balance   = ");
//        int initial = scan.nextInt();
//
//        System.out.printf("Number of threads = ");
//        int numThread = scan.nextInt();
//        System.out.println();
//
//        while(doTrans){
//
//            System.out.printf("Enter D (deposit), W (withdraw), or other (quit) = ");
//            String md = scan.next().trim();
//
//            if(md.equals("d")||md.equals("D")||md.equals("w")||md.equals("W")) {
//                doTrans = true;
//            } else break;
//
//            if(!flag)
//                initial = bal;
//
//            ArrayList<BankingThread> list = new ArrayList<>();
//            Account ac = new Account(initial);
//
//            for (int i = 0; i < numThread; i++) {
//                list.add(new BankingThread("T"+(i+1),ac,md));
//            }
//
//            for (int i = 0; i < numThread; i++) {
//                list.get(i).start();
//            }
//
//            try {
//                for (int i = 0; i < numThread; i++) {
//                    list.get(i).join();
//                }
//            }
//
//            catch (InterruptedException e) {
//            }
//
//            bal = ac.print();
//            flag = false;
//        }
//    }
//}

class Product {
    private String name;
    private int balance;
    public Product (String n){
        name = n;
    }
    public void addToStock(){

    }
    public void removeFromStock (){

    }
}

class VendorThread extends Thread{

}

public class StockSimulation {

    public static void main(String[] args) {

        int SimTime = 1;

        ArrayList <Product> product = new ArrayList <> ();

        while(true){
            System.out.printf("%s > Enter product file = ", Thread.currentThread().getName());
            Scanner file = new Scanner(System.in);
            String proFile = file.next();
            try{
                Scanner scan = new Scanner(new File(proFile));
                while(scan.hasNextLine()){
                    String nm = scan.nextLine();
                    product.add(new Product(nm));
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }

        while(true){
            System.out.printf("%s > Enter transaction file for vendor 1 = ", Thread.currentThread().getName());
            Scanner trans1 = new Scanner(System.in);
            String file1 = trans1.next();
            try{
                Scanner scan = new Scanner(new File(file1));
                while(scan.hasNextLine()){
                    String nm = scan.nextLine();
                    product.add(new Product(nm));
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }

        while(true){
            System.out.printf("%s > Enter transaction file for vendor 2 = ", Thread.currentThread().getName());
            Scanner trans2 = new Scanner(System.in);
            String file2 = trans2.next();
            try{
                Scanner scan = new Scanner(new File(file2));
                while(scan.hasNextLine()){
                    String nm = scan.nextLine();
                    product.add(new Product(nm));
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }

        while(true){
            System.out.printf("%s > Enter transaction file for vendor 3 = ", Thread.currentThread().getName());
            Scanner trans3 = new Scanner(System.in);
            String file3 = trans3.next();
            try{
                Scanner scan = new Scanner(new File(file3));
                while(scan.hasNextLine()){
                    String nm = scan.nextLine();
                    product.add(new Product(nm));
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }

        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
        System.out.printf("%s >      %S (%d)\n", Thread.currentThread().getName(), "Stock Simulation", SimTime);
        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
    }
}
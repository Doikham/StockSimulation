package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class Product {
    private String name;
    private int balance;
    public Product (String n){
        name = n;
    }
    public Product (int g){
        balance = g;
    }
    synchronized public int addToStock(int value, int transaction_no){
        balance += value;
        System.out.printf("%s > trans   %-3d   +%d  %-20s  balance =  %-5d\n", Thread.currentThread().getName(), transaction_no, value,name, balance);
        return balance;
    }
    synchronized public void removeFromStock (int value, int transaction_no){
        balance -= value;
        System.out.printf("%s > trans   %-3d   %d  %-20s  balance =  %-5d\n", Thread.currentThread().getName(), transaction_no, value,name, balance);
    }
    public void show_stock(){
        System.out.printf("name: %-20s balance: %-8d\n",name,balance);
    }
    public String get_name(){
        return name;
    }
}

class Transaction{
    private int trans_id;
    private String product_name;
    private int product_amount;
    public Transaction(int id, String name, int amount){
        trans_id = id;
        product_name = name;
        product_amount = amount;
    }
    public boolean same_product(String name){
        if(product_name.equals(name)){
            return true;
        }
        else{
            return false;
        }
    }
    public void print_transaction(){
        System.out.println(Thread.currentThread().getName()+trans_id+" "+product_name+" "+product_amount);
    }
    public String getProduct_name(){
        return product_name;
    }
    public int getTrans_id(){
        return trans_id;
    }
    public int getProduct_amount(){
        return product_amount;
    }
}

class VendorThread extends Thread{
//    private Product product;
    Product allStocks[];
    ArrayList <Transaction> transactions = new ArrayList <> ();
    protected CyclicBarrier finish;
    //cyclic barrier from main (similar to other vendor)
    public VendorThread(String vendor_name, Product[] z, CyclicBarrier b){
        super(vendor_name);
//        product = z;
        allStocks = z;
        finish = b;
    }
    public void add_transaction(Transaction new_trans){
        transactions.add(new_trans);
    }
    public void show_transactions(){
        for(int i=0;i<transactions.size();i++){
            transactions.get(i).print_transaction();
        }
    }
    public void show_products(){
        for(int i=0; i<4; i++) allStocks[i].show_stock();
    }
    public void buy_summary(int b){
        int a = 0;
        System.out.printf("%s > Buying complete\n",Thread.currentThread().getName());
        for(int i=0; i<allStocks.length; i++){
            System.out.printf("%s > %-20s  buy = %-6d  sales = %-6d  balance = %-6d\n",Thread.currentThread().getName(),transactions.get(i).getProduct_name(),transactions.get(i).getProduct_amount(),a,b);
        }
    }
    public void run(){

        for(int i=0;i<transactions.size();i++) {
            try {
                if (transactions.get(i).getProduct_amount() > 0) {
                   for(int j=0;j<allStocks.length;j++){
                       //if same product as transaction
                       if(transactions.get(i).same_product(allStocks[j].get_name())){
                           allStocks[j].addToStock(transactions.get(i).getProduct_amount(),transactions.get(i).getTrans_id());
                       }
                   }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        try {
            finish.await();
            sleep(5);
        } // changed from Program 6.10
        catch (InterruptedException e) {
            System.out.println("***** " + getName() + " is interrupted");
        }
        catch (BrokenBarrierException e) {
            System.out.println("***** " + getName() + " is out of broken barrier");
        }
        System.out.println("***** " + getName() + " finishes");

        //wait fo others
        //for(int i=0; i<allStocks.length; i++)

        //check all tran that mount<0
//        for(int i=0;i<transactions.size();i++) {
//            try {
//                if (transactions.get(i).getProduct_amount() < 0) {
//                    for(int j=0;j<allStocks.length;j++){
//                        //if same product as transaction
//                        if(transactions.get(i).same_product(allStocks[j].get_name())){
//                            allStocks[j].removeFromStock(transactions.get(i).getProduct_amount(),transactions.get(i).getTrans_id());
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }
    }
}

public class StockSimulation {

    public static void main(String[] args) {

        int SimTime = 1;
        int initial = 0;

        Product products[] = new Product[4];
        VendorThread vendors[] = new VendorThread[3];
        CyclicBarrier br = new CyclicBarrier(4);
//        Product pr = new Product(initial);
        //read product file
        while(true){
            System.out.printf("%s > Enter product file = ", Thread.currentThread().getName());
            Scanner file = new Scanner(System.in);
            String proFile = file.next();
            try{
                Scanner scan = new Scanner(new File(proFile));
                int j=0;
                while(scan.hasNextLine()){
                    String nam = scan.nextLine();
                    products[j] = new Product(nam);
                    j++;
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }
        //read transaction file to each vendor
        for(int i=0;i<3;i++){
            //create vendor
            vendors[i] = new VendorThread("v"+(i+1),products,br);
            while(true){
                System.out.printf("%s > Enter transaction file for vendor %d = ", Thread.currentThread().getName(),i+1);
                Scanner in1 = new Scanner(System.in);
                String file1 = in1.next();
                try{
                    //read each transaction
                    Scanner scan = new Scanner(new File(file1));
                    while(scan.hasNextLine()){
                        String line = scan.nextLine();
                        String [] buffer = line.split(",");
                        try{
                            int numTrans = Integer.parseInt(buffer[0].trim());
                            String nm = buffer[1].trim();
                            int value = Integer.parseInt(buffer[2].trim());
                            vendors[i].add_transaction(new Transaction(numTrans,nm,value));
                        }
                        catch (RuntimeException e){
                            System.out.println(e);
                        }
                    }
                    break;
                } catch (FileNotFoundException e) {
                    System.out.println(e);
                }
            }
//           vendors[i].start();
        }
        System.out.println();
        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
        System.out.printf("%s >      %S (%d)\n", Thread.currentThread().getName(), "Stock Simulation", SimTime);
        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
        System.out.println();
        for (int i = 0; i < 3; i++) {
            vendors[i].start();
//            vendors[i].show_transactions();
//            vendors[i].show_products();
        }
        try{

            br.await();//for main thread

        }
        catch(InterruptedException | BrokenBarrierException e){

        }
        try {
            for (int i = 0; i < 3; i++) {
                vendors[i].join();
            }
        }

        catch (InterruptedException e) {

        }
    }
}
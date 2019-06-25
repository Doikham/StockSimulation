package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Product {
    private String name;
    private int balance;
    public Product (String n){
        name = n;
    }
    public Product (int g){
        balance = g;
    }
    synchronized public void addToStock(String nme, int value, int n){
        balance += value;
        System.out.printf("%s > trans   %-3d   +%d  %-20s  balance =  %-5d", Thread.currentThread().getName(), n, value, nme, balance);
    }
    synchronized public void removeFromStock (){

    }
}

class Transaction{
    int trans_id;
    String product_name;
    int product_amount;
    public Transaction(int id, String name, int amount){
        trans_id = id;
        product_name = name;
        product_amount = amount;
    }
    boolean same_product(String name){
        if(product_name==name)return true;
        else return false;
    }
    public void print_transaction(){
        System.out.println(Thread.currentThread().getName()+trans_id+" "+product_name+" "+product_amount);
    }
}

class VendorThread extends Thread{
    ArrayList <Product> allStocks = new ArrayList <> ();
    ArrayList <Transaction> transactions = new ArrayList <> ();

    public VendorThread(String vendor_name){
        super(vendor_name);
    }
    public void add_transaction(Transaction new_trans){
        transactions.add(new_trans);
    }
    public void show_transactions(){
        for(int i=0;i<transactions.size();i++){
            transactions.get(i).print_transaction();
        }
    }
    public void run(){
//        for(int i=0;i<transactions.size();i++) {
//            try {
//                if (transactions.get(i).product_amount > 0) {
//                    synchronized (this) {
//                        product.addToStock(this.getName(), productValue, numTransaction);
//                        //allStocks.add(this.productName,productValue);
//                        total += productValue;
//                        Thread.sleep(5);
//                    }
//                } else {
//
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

        ArrayList <Product> products = new ArrayList <> ();
        VendorThread vendors[] = new VendorThread[3];
        Product pr = new Product(initial);
        //read product file
        while(true){
            System.out.printf("%s > Enter product file = ", Thread.currentThread().getName());
            Scanner file = new Scanner(System.in);
            String proFile = file.next();
            try{
                Scanner scan = new Scanner(new File(proFile));
                while(scan.hasNextLine()){
                    String nam = scan.nextLine();
                    products.add(new Product(nam));
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }
        //read transaction file to each vendor
        for(int i=0;i<3;i++){
            //create vendor
            vendors[i] = new VendorThread("v"+(i+1));
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
        }

        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
        System.out.printf("%s >      %S (%d)\n", Thread.currentThread().getName(), "Stock Simulation", SimTime);
        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());

        for (int i = 0; i < 3; i++) {
            //trans.get(i).start();
            vendors[i].show_transactions();
        }

        try {
            for (int i = 0; i < 3; i++) {
//                trans.get(i).join();
                vendors[i].join();
            }
        }

        catch (InterruptedException e) {

        }
    }
}
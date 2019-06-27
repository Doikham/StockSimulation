package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Math.abs;

class Product implements Comparable<Product> {
    private int totalbuy = 0;
    private int totalsales = 0;
    private String name;
    private int balance;
    public Product (String n){
        name = n;
    }
    synchronized public void addToStock(int value, int transaction_no){
        balance += value;
        System.out.printf("%s > trans   %-3d   +%d  %-20s  balance =  %-5d\n", Thread.currentThread().getName(), transaction_no, value,name, balance);
    }
    synchronized public void removeFromStock (int value, int transaction_no){
        if(balance<abs(value)){
            int balance_before_sell = balance;
            balance = 0;
            System.out.printf("%s > trans   %-3d   -%-3d  %-20s  balance =  %-5d\n", Thread.currentThread().getName(), transaction_no, balance_before_sell ,name, balance);
        }
        else {
            balance += value;
            System.out.printf("%s > trans   %-3d   %-3d  %-20s  balance =  %-5d\n", Thread.currentThread().getName(), transaction_no, value,name, balance);
        }
    }
    public String get_name(){
        return name;
    }
    public void print_buy_summary(){
        totalbuy = balance;
        System.out.printf("%s > %-20s  buy = %5d  sales = %5d  balance = %5d\n",Thread.currentThread().getName(),name,totalbuy,totalsales,totalbuy - totalsales);
    }
    public void print_sell_summary(){
        totalsales = balance;
        System.out.printf("%s > %-20s  buy = %5d  sales = %5d  balance = %5d\n",Thread.currentThread().getName(),name,totalbuy,totalbuy - totalsales,balance);
        totalbuy = 0;
        totalsales = 0;
        balance = 0;
    }
    @Override
    public int compareTo(Product o) {
        if(this.balance<o.balance) return 1;
        else if(this.balance>o.balance) return -1;
        else return 0;
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
    public int getTrans_id(){
        return trans_id;
    }
    public int getProduct_amount(){
        return product_amount;
    }
}

class VendorThread extends Thread{
    Product allStocks[];
    ArrayList <Transaction> transactions = new ArrayList <> ();
    protected CyclicBarrier finish;
    public VendorThread(String vendor_name, Product[] z, CyclicBarrier b){
        super(vendor_name);
        allStocks = z;
        finish = b;
    }
    public void add_transaction(Transaction new_trans){
        transactions.add(new_trans);
    }
    public void run(){
        boolean doSum = true;
        while(doSum) {
            for (int i = 0; i < transactions.size(); i++) {
                try {
                    if (transactions.get(i).getProduct_amount() > 0) {
                        for (int j = 0; j < allStocks.length; j++) {
                            //if same product as transaction
                            if (transactions.get(i).same_product(allStocks[j].get_name())) {
                                allStocks[j].addToStock(transactions.get(i).getProduct_amount(), transactions.get(i).getTrans_id());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                finish.await();
                sleep(15);
            }
            catch (InterruptedException | BrokenBarrierException e) { }
            doSum = false;
        }
        while(!doSum) {
            for (int i = 0; i < transactions.size(); i++) {
                try {
                    if (transactions.get(i).getProduct_amount() < 0) {
                        for (int j = 0; j < allStocks.length; j++) {
                            //if same product as transaction
                            if (transactions.get(i).same_product(allStocks[j].get_name())) {
                                allStocks[j].removeFromStock(transactions.get(i).getProduct_amount(), transactions.get(i).getTrans_id());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                finish.await();
                sleep(15);
            }
            catch (InterruptedException | BrokenBarrierException e) { }
            doSum = true;
        }
    }
}

public class StockSimulation {

    public static void main(String[] args) {

        boolean next_round = true;
        int SimTime = 1;
        boolean another_run = true;
        Product products[] = new Product[4];
        VendorThread vendors[] = new VendorThread[3];
        String file_vendors[] = new String[3];
        CyclicBarrier br = new CyclicBarrier(4);
        Scanner scanRun = new Scanner(System.in);
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
        while(another_run){
            for(int i=0;i<3;i++){
                //create vendor
                vendors[i] = new VendorThread("v"+(i+1),products,br);
                while(true){
                    if(next_round) {
                        System.out.printf("%s > Enter transaction file for vendor %d = ", Thread.currentThread().getName(), i + 1);
                        Scanner in1 = new Scanner(System.in);
                        file_vendors[i] = in1.next();
                    }
                    try{
                        //read each transaction
                        Scanner scan = new Scanner(new File(file_vendors[i]));
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
            next_round = false;
            System.out.println();
            System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
            System.out.printf("%s >      %S (%d)\n", Thread.currentThread().getName(), "Stock Simulation", SimTime);
            System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
            //Start run()
            System.out.println();
            for (int i = 0; i < 3; i++) {
                vendors[i].start();
            }

            try {
                br.await();
            } catch (InterruptedException | BrokenBarrierException e) { }

            Arrays.sort(products);

            System.out.println();
            System.out.printf("%s > Buying completes\n", Thread.currentThread().getName());
            for (int i = 0; i < products.length; i++) {
                products[i].print_buy_summary();
            }
            System.out.println();

            try {
                br.await();
            } catch (InterruptedException | BrokenBarrierException e) { }

            Arrays.sort(products);

            System.out.println();
            System.out.printf("%s > Selling completes\n", Thread.currentThread().getName());
            for (int i = 0; i < products.length; i++) {
                products[i].print_sell_summary();
            }
            System.out.println();

            System.out.printf("Run another simulation (y/n) ? ");
            String run_again = scanRun.next().trim();
            if(run_again.equals("y")||run_again.equals("Y")) {
                SimTime++;
                another_run = true;
            } else if (run_again.equals("n")||run_again.equals("N")) {
                break;
            }
        }
    }
}
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
        System.out.printf("%s > trans   %-3d   +%d  %-20s  balance =     " +
                "%-5d", Thread.currentThread().getName(), n, value, nme, balance);
    }
    synchronized public void removeFromStock (){

    }
}

class VendorThread extends Thread{
    private String productName;
    private int numTransaction;
    private int productValue;
    private Product product;
    private int total;
    public VendorThread(int a, String b, int c, Product d){
        numTransaction = a;
        productName = b;
        productValue = c;
        product = d;
    }
    public void run(){
        ArrayList <Product> allStocks = new ArrayList <> ();
        try{
            if (productValue > 0){
                synchronized (this){
                    product.addToStock(this.getName(),productValue,numTransaction);
                    //allStocks.add(this.productName,productValue);
                    total += productValue;
                    Thread.sleep(5);
                }
            } else {

            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}

public class StockSimulation {

    public static void main(String[] args) {

        int SimTime = 1;
        int initial = 0;

        ArrayList <Product> product = new ArrayList <> ();
        ArrayList <VendorThread>  trans = new ArrayList <> ();
        Product pr = new Product(initial);

        while(true){
            System.out.printf("%s > Enter product file = ", Thread.currentThread().getName());
            Scanner file = new Scanner(System.in);
            String proFile = file.next();
            try{
                Scanner scan = new Scanner(new File(proFile));
                while(scan.hasNextLine()){
                    String nam = scan.nextLine();
                    product.add(new Product(nam));
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }

        while(true){
            System.out.printf("%s > Enter transaction file for vendor 1 = ", Thread.currentThread().getName());
            Scanner in1 = new Scanner(System.in);
            String file1 = in1.next();
            try{
                Scanner scan = new Scanner(new File(file1));
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    String [] buffer = line.split(",");
                    try{
                        int numTrans = Integer.parseInt(buffer[0].trim());
                        String nm = buffer[1].trim();
                        int value = Integer.parseInt(buffer[2].trim());
                        trans.add(new VendorThread(numTrans,nm,value,pr));
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

        while(true){
            System.out.printf("%s > Enter transaction file for vendor 2 = ", Thread.currentThread().getName());
            Scanner in2 = new Scanner(System.in);
            String file2 = in2.next();
            try{
                Scanner scan = new Scanner(new File(file2));
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    String [] buffer = line.split(",");
                    try{
                        int numTrans = Integer.parseInt(buffer[0].trim());
                        String nm = buffer[1].trim();
                        int value = Integer.parseInt(buffer[2].trim());
                        trans.add(new VendorThread(numTrans,nm,value,pr));
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

        while(true){
            System.out.printf("%s > Enter transaction file for vendor 3 = ", Thread.currentThread().getName());
            Scanner in3 = new Scanner(System.in);
            String file3 = in3.next();
            try{
                Scanner scan = new Scanner(new File(file3));
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    String [] buffer = line.split(",");
                    try{
                        int numTrans = Integer.parseInt(buffer[0].trim());
                        String nm = buffer[1].trim();
                        int value = Integer.parseInt(buffer[2].trim());
                        trans.add(new VendorThread(numTrans,nm,value,pr));
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

        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());
        System.out.printf("%s >      %S (%d)\n", Thread.currentThread().getName(), "Stock Simulation", SimTime);
        System.out.printf("%s > ------------------------------\n", Thread.currentThread().getName());

        for (int i = 0; i < 3; i++) {
            trans.get(i).start();
        }

        try {
            for (int i = 0; i < 3; i++) {
                trans.get(i).join();
            }
        }

        catch (InterruptedException e) {

        }
    }
}
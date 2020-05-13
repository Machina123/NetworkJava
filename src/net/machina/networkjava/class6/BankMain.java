package net.machina.networkjava.class6;

public class BankMain {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Runnable r = new BankOperationsRunnable(bank);
//        Thread thread = new Thread(r);
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for(int z=0; z<7; z++) {
            new Thread(r).start();
        }
    }
}

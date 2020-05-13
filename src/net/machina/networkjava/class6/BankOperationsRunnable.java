package net.machina.networkjava.class6;

public class BankOperationsRunnable implements Runnable {

    private Bank bank;

    public BankOperationsRunnable(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void run() {
        System.out.println("Hello from thread " + Thread.currentThread().getName());
        for(int i=0; i < 4; i++) {
            try {
                bank.performOperation();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted");
                e.printStackTrace();
            }
        }
    }
}

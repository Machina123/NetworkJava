package net.machina.networkjava.class6;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {

    private int[] accts;
    private Random random;
    private Lock lock;

    public Bank() {
        accts = new int[10];
        random = new Random();
        Arrays.fill(accts, 1000);
        lock = new ReentrantLock();
    }

    public synchronized void performOperation() {
        int acctFrom = random.nextInt(accts.length);
        int acctTo = random.nextInt(accts.length);
        int amount = random.nextInt(500);
//        lock.lock();
        try {
            if (accts[acctFrom] > amount) {
                accts[acctFrom] -= amount;
                accts[acctTo] += amount;
                System.out.println("Account[" + acctFrom + "] = " + accts[acctFrom] + " | Account[" + acctTo + "] = " + accts[acctTo]);
            } else {
                System.out.println("Cannot perform operation");
            }
        } finally {
//            lock.unlock();
        }
    }
}

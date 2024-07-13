package MTCollatz;


import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

/**
A named concrete MTCollatz class that accepts a range and number of 
threads and creates that number of threads to cooperatively calculate 
a histogram csv of stopping times.
@author Katharine Angelopoulos and Cody Delaney
@version 1.0

COP5518 Project 1
File Name: MTCollatz.java
*/ 
public class MTCollatz implements Runnable {
  /**
   * Reentrant lock for the counter that increments from 1 to the collatz range.
   */
  private final ReentrantLock counterLock = new ReentrantLock();

  /**
   * Reentrant lock for the collatz histogram array.
   */
  private final ReentrantLock histogramLock = new ReentrantLock();

  /**
   * Counter that increments by 1 across the collatz range.
   */
  private int counter = 0;

  /**
   * The max range for the collatz histogram calculation.
   */
  private final int collatzRange;

  /**
   * Array storing values for the collatz stopping time histogram.
   */
  private final int[] collatzStoppingTimeHistogram = new int[1001];

  /**
   * Array to hold the threads performing the calculations.
   */
  private final Thread[] threads;

  /**
   * Boolean flag to determine if locks should be used.
   */
  private final boolean useLocks;

  /**
   * Constructs an MTCollatz object
   * @param threadCount  the number of threads to generate
   * @param collatzRange  the max range for the collatz calculation
   * @param useLocks  boolean flag to determine if locks should be used
   * @throws InterruptedException  throws an interrupted exception
   */
  public MTCollatz(int threadCount, int collatzRange, boolean useLocks) throws InterruptedException {
    this.collatzRange = collatzRange;
    this.threads = new Thread[threadCount];
    this.useLocks = useLocks;
    Instant start = Instant.now();

    // Create and start all threads
    for (int i = 0; i < threadCount; i++) {
      threads[i] = new Thread(this);
      threads[i].start();
    }

    // Wait for all threads to finish
    for (int i = 0; i < threadCount; i++) {
      threads[i].join();
    }

    Instant end = Instant.now();

    System.err.println(this.collatzRange + "," + threadCount + "," + (double)(end.toEpochMilli() - start.toEpochMilli()) / 1000);

    // Print the collatz histogram as specified in requirements.
    for (int i = 1; i < this.collatzStoppingTimeHistogram.length; i++) {
      System.out.println((i) + "," + this.collatzStoppingTimeHistogram[i]);
    }
  }

  /**
   * Gets the counter with mutual exclusion if useLocks is true
   * @return  the current counter (incremented by one)
   */
  public int getCounter() {
    if (useLocks) {
      counterLock.lock();
    }
    try {
      return ++this.counter;
    } finally {
      if (useLocks) {
        counterLock.unlock();
      }
    }
  }

  /**
   * Gets the range for the collatz calculation
   * @return  the range for the collatz calculation
   */
  public int getCollatzRange() {
    return this.collatzRange;
  }

  /**
   * Updates the collatz stopping time histogram with mutual exclusion if useLocks is true
   * @param stoppingTime  the stopping time for a collatz calculation
   */
  public void updateCollatzStoppingTimeHistogram(int stoppingTime) {
    if (useLocks) {
      histogramLock.lock();
    }
    try {
      this.collatzStoppingTimeHistogram[stoppingTime]++;
    } finally {
      if (useLocks) {
        histogramLock.unlock();
      }
    }
  }

  /**
   * Runs the threads, performing collatz operations cooperatively
   */
  @Override
  public void run() {
    while (true) {
      // Get the current counter value and the max range
      int index = this.getCounter();
      int range = this.getCollatzRange();

      // If the index exceeds the range, stop working
      if (index > range) {
        break;
      }

      // Calculate the collatz stopping time and update the histogram array
      int stoppingTime = this.getCollatzStoppingTimeForNumber(index);
      this.updateCollatzStoppingTimeHistogram(stoppingTime);
    }
  }

  /**
   * Calculates the collatz stopping time for a number
   * @param number  the number to perform a collatz stopping time calculation on
   * @return  the calculated collatz stopping time
   */
  public int getCollatzStoppingTimeForNumber(int number) {
    int stoppingTime = 0; // stopping time is the least number of steps for number to reach 1
    long hold = number; // long is required because the algorithm exceeds the limits of int

    while (hold != 1) { // break when number reaches 1
      if (hold % 2 == 0) { // if even, divide number by 2
        hold = hold / 2;
      } else { // if odd, multiply number by 3 and add 1
        hold = (3 * hold) + 1;
      }
      stoppingTime++; // increment stopping time
    }

    return stoppingTime;
  }

  /**
   * The main method
   * @param args - 0: collatz range for calculation, 1: number of requested threads, 2: -nolocks (optional) 
   */
  public static void main(String[] args) {
    int collatzRange = Integer.parseInt(args[0]);
    int threadCount = Integer.parseInt(args[1]);
    boolean useLocks = true;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-nolock")) {
        useLocks = false;
      }
    }
    try {
      new MTCollatz(threadCount, collatzRange, useLocks);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
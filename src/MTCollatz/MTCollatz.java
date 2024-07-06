package MTCollatz;

public class MTCollatz implements Runnable {
  private int counter = 0;
  private int collatzRange;
  private int[] collatzStoppingTimeHistogram;
  private Thread[] threads;


  public static void main(String[] args) {
    int collatzRange = Integer.parseInt(args[0]);
    int threadCount = Integer.parseInt(args[1]);

    System.out.println("Collatz Range: " + collatzRange);
    System.out.println("Thread Count: " + threadCount);

    MTCollatz mtCollatz = new MTCollatz(4, 1000);
    System.out.println("done");
  }

  public MTCollatz(int threadCount, int collatzRange) {
    this.collatzRange = collatzRange;
    threads = new Thread[threadCount];

    for (int i = 0; i < threadCount; i++) {
      threads[i] = new Thread(this);
      threads[i].start();
      System.out.println("Thread Created: " + threads[i].getName());
    }
  }

  public void run() {
    // Loop through -
      // Get counter value (lock) 
      // Check if counter === array length 
      // if so, stop
      // else
        // Compute collatz stopping time for number
        // Mutate array[counter] with stopping time
        // increment counter (needs lock)
  }

  public static int getCollatzStoppingTimeForNumber(int number) {
    int stoppingTime = 0;

    while (number != 1) {
      if (number % 2 == 0) {
        number = number / 2;
      } else {
        number = ( 3 * number ) + 1;
      }
      stoppingTime++;
    }

    return stoppingTime;
  }

}

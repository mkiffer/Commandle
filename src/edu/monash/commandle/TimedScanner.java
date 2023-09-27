package edu.monash.commandle;



import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class TimedScanner {
    private long timeoutMillis = 10000; // Default timeout duration: 10 seconds
    private final Scanner scanner;
    private final Timer timer;
    private final ExecutorService executor;
    private boolean timeoutOccurred = false;

    public TimedScanner() {
        this.scanner = new Scanner(System.in);
        this.timer = new Timer();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public TimedScanner(long timeoutMillis) {
        this(); // Call the default constructor to initialize scanner and timer
        this.timeoutMillis = timeoutMillis;
    }

    public TimedScanner(long timeoutMillis, InputStream in) {
        this.scanner = new Scanner(in);
        this.timer = new Timer();
        this.timeoutMillis = timeoutMillis;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public String nextLineWithTimeout() throws TimeoutException {
        StringBuilder inputBuilder = new StringBuilder();

        Future<Void> inputFuture = (Future<Void>) executor.submit(() -> {
            InputStream inputStream = System.in;
            int nextChar;

            try {
                while ((nextChar = inputStream.read()) != -1) {
                    char c = (char) nextChar;
                    if (c == '\n') {
                        break; // Exit the loop when Enter key is pressed
                    }
                    inputBuilder.append(c);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            inputFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("An error occurred while waiting for input.", e);
        } catch (TimeoutException e) {
            timeoutOccurred = true;
        } finally {
            inputFuture.cancel(true);
        }

        if (timeoutOccurred) {
            throw new TimeoutException("Timeout! No input received for " + timeoutMillis / 1000 + " seconds.");
        }

        return inputBuilder.toString();
    }

//    public String nextLineWithTimeout() throws TimeoutException {
//        Future<String> inputFuture = executor.submit(() -> scanner.nextLine());
//
//        try {
//            return inputFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("An error occurred while waiting for input.", e);
//        } catch (TimeoutException e) {
//            throw new TimeoutException("Timeout! No input received for " + timeoutMillis / 1000 + " seconds.");
//        } finally {
//            inputFuture.cancel(true);
//        }
//    }

//    public String nextLineWithTimeout() throws TimeoutException {
//        final String[] inputHolder = new String[1];
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                synchronized (TimedScanner.this) {
//                    if (!timeoutOccurred) {
//                        timeoutOccurred = true;
//                        scanner.close();
//                    }
//                }
//            }
//        }, timeoutMillis);
//
//        try {
//            inputHolder[0] = scanner.nextLine();
//        } catch (Exception e) {
//            synchronized (TimedScanner.this) {
//                if (!timeoutOccurred) {
//                    timer.cancel(); // Cancel the timer only if it hasn't been canceled due to a timeout
//                }
//            }
//            throw e; // Rethrow the exception
//        } finally {
//            synchronized (TimedScanner.this) {
//                if (!timeoutOccurred) {
//                    timer.cancel(); // Cancel the timer since input was received before timeout or an exception occurred
//                }
//            }
//        }
//
//        if (timeoutOccurred) {
//            throw new TimeoutException("Timeout! No input received for " + timeoutMillis / 1000 + " seconds.");
//        }
//
//        return inputHolder[0];
//    }


    public void close() {
        scanner.close();
    }

}


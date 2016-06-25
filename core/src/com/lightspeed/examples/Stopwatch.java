package com.lightspeed.examples;

/**
 * Created by Ricky Jones Jr on 6/25/2016.
 */
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {
    static int interval;
    static Timer timer;
    String timerString;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Input seconds => : ");
        String secs = sc.nextLine();
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        interval = Integer.parseInt(secs);
        System.out.println(secs);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println(setInterval());

            }
        }, delay, period);
    }

    private static final int setInterval() {
        if (interval == 1)
            timer.cancel();
        --interval;
        return --interval;
    }
}

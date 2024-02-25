package com.ua.project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class Race {
    public static AtomicLong startRaceTime;

    public static void main(String[] args) {
        final double racingDistance = 5000.0;
        CountDownLatch count = new CountDownLatch(5);
        List<RaceCarRunnable> cars = new ArrayList<RaceCarRunnable>(List.of(
                new RaceCarRunnable("McLaren", 375.32, racingDistance, count),
                new RaceCarRunnable("Ferrari", 364.96, racingDistance, count),
                new RaceCarRunnable("Aston Martin", 381.4, racingDistance, count),
                new RaceCarRunnable("Chevrolet Corvette", 350.1, racingDistance, count),
                new RaceCarRunnable("Mini Copper", 370.24, racingDistance, count)
        ));
        List<Thread> threads = new ArrayList<Thread>();
        cars.forEach((car) -> threads.add(new Thread(car)));

        try {
            startRace(threads);
            count.await();

            cars.sort(Comparator.comparing(RaceCarRunnable::getFinishTime));
            System.out.println("\n");
            System.out.println("-".repeat(13));
            System.out.println(" LEADERBOARD:");
            System.out.println("-".repeat(13));
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(i + 1 + ". " + cars.get(i).getName());
            }
        }
        catch(InterruptedException e) {
            System.out.println(" Execution was interrupted!");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void startRace(final List<Thread> cars) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("3...");
                    Thread.sleep(500);
                    System.out.println("2...");
                    Thread.sleep(500);
                    System.out.println("1...");
                    Thread.sleep(500);
                    System.out.println("GO!!!");

                    startRaceTime = new AtomicLong(new Date().getTime());
                    cars.forEach(Thread::start);
                }
                catch(InterruptedException e) {
                    System.out.println(" Execution was interrupted!");
                }
                catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }
}

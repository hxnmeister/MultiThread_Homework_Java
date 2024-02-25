package com.ua.project;

import com.ua.project.models.Car;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class RaceCarRunnable extends Car implements Runnable {
    private long finishTime;
    private double passed;
    private double distance;
    private boolean isFinished;
    private CountDownLatch count;

    public RaceCarRunnable(String name, double maxSpeed, double distance, CountDownLatch count) {
        super(name, maxSpeed);
        this.distance = distance;
        this.count = count;
        this.isFinished = false;
        this.passed = 0.0;
    }

    public double getPassed() {
        return passed;
    }

    public double getDistance() {
        return distance;
    }

    private void toggleIsFinished() {
        isFinished = !isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public double getRandomSpeed() {
        setCurrentSpeed(new Random().nextDouble(getMaxSpeed() / 2, getMaxSpeed()));
        return getCurrentSpeed();
    }

    public static String convertToTime(final long time) {
        return new SimpleDateFormat("mm:ss").format(time);
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return " " + getName() + " => speed: " + decimalFormat.format(getCurrentSpeed()) + "km/h; progress: " + decimalFormat.format(this.passed) + "/" + decimalFormat.format(this.distance) + "km"
                + "\n" + "-".repeat(50);
    }

    @Override
    public void run() {
        while(!this.isFinished) {
            try {
                Thread.sleep(1000);
                this.passed += getRandomSpeed();
                System.out.println(this);

                if(this.passed >= this.distance){
                    toggleIsFinished();
                    this.count.countDown();
                    this.finishTime = new Date().getTime() - Race.startRaceTime.get();
                    System.out.println(" " + this.getName() + " finished!\n Time: " + /*this.finishTime / (1000 * 60) + "m. " + (this.finishTime / 1000) % 60*/ convertToTime(this.finishTime) + "\n" + "-".repeat(20));
                }
            }
            catch(InterruptedException e) {
                System.out.println(" Execution was interrupted!");
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

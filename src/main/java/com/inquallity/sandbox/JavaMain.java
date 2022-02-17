package com.inquallity.sandbox;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class JavaMain {
    public static String LEFT = "[L]";
    public static String RIGHT = "[R]";

    public static void main(String[] args) {
        new JavaMain().launch();
    }

    public void launch() {
        System.out.println(printSteps(4));
    }

    public String printSteps(int howMuch) {
        final StringBuffer sb = new StringBuffer();
        final Task task = new Task(sb, howMuch);
        final Thread left = new Thread(task);
        final Thread right = new Thread(task);
        left.setName(LEFT);
        right.setName(RIGHT);

        left.start();
        right.start();
        try {
            left.join();
            right.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String printSteps2(int howMuch) {
        final BehaviorSubject<String> valve = BehaviorSubject.create();
        final Observable<String> left = Observable.<String>create(emitter -> {
            int iterations = howMuch;
            while (iterations-- >= 0) {
                emitter.onNext(JavaMain.LEFT);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
        final Observable<String> right = Observable.<String>create(emitter -> {
            int iterations = howMuch;
            while (iterations-- >= 0) {
                emitter.onNext(JavaMain.RIGHT);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
        valve.publish();
        return "";
    }
}

class Task implements Runnable {

    private final StringBuffer sb;
    private final int howMuch;

    public Task(StringBuffer sb, int howMuch) {
        this.sb = sb;
        this.howMuch = howMuch;
    }

    @Override
    public void run() {
        int iterations = howMuch;
        while (iterations > 0) {
            synchronized (Thread.class) {
                try {
                    sb.append(Thread.currentThread().getName());
                    Thread.class.notifyAll();
                    iterations--;
                    if (iterations > 0) Thread.class.wait();
                } catch (Exception e) {
                    System.err.println("[" + Thread.currentThread().getName() + "] " + e.getMessage());
                }
            }
        }
    }
}

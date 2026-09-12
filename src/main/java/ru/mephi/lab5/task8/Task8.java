package ru.mephi.lab5.task8;

import java.util.concurrent.locks.ReentrantLock;

public class Task8 {

    @FunctionalInterface
    public interface LockGuard extends AutoCloseable {
        @Override
        void close();
    }

    public static LockGuard lock(ReentrantLock lock) {
        lock.lock();
        return lock::unlock;
    }
}
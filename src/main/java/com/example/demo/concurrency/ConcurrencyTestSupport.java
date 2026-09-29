package com.example.demo.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyTestSupport {

    public record Result(int success, int failure, long elapsedMs) {
        public int total() { return success + failure; }
    }

    /**
     * task를 threadCount개의 스레드에서 동시에 실행하고 성공/실패 수를 집계한다.
     */
    public static Result runConcurrently(int threadCount, Runnable task)
            throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(threadCount, 32));
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();// 모든 스레드가 동시에 출발
                    task.run();
                    success.incrementAndGet();
                } catch (Exception e) {
                    failure.incrementAndGet();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();// 전원 준비 대기
        long begin = System.currentTimeMillis();
        start.countDown();// 시작 신호
        done.await(30, TimeUnit.SECONDS);
        long elapsed = System.currentTimeMillis() - begin;

        executor.shutdown();
        return new Result(success.get(), failure.get(), elapsed);
    }
}
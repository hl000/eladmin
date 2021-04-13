package me.zhengjie.utils;

public abstract class RetryT<T> {

    // 重试次数
    private int retryTime = 3;

    // 重试的睡眠时间
    private int sleepTime = 1000;

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    protected abstract T doAction() throws Exception;

    public RetryT() {
    }

    public RetryT(int retryTime, int sleepTime) {
        this.retryTime = retryTime;
        this.sleepTime = sleepTime;
    }

    public T execute() throws Exception {
        for (int i = 0; i < retryTime; i++) {
            try {
                return doAction();
            } catch (Exception e) {
                Thread.sleep(sleepTime);
            }
        }
        return null;
    }
}

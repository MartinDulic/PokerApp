package hr.dulic.pokerapp.utils.viewUtils;

public class ThreadUtils {
    public static void pauseThread(long durationMilis) {
        try {
            Thread.sleep(durationMilis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

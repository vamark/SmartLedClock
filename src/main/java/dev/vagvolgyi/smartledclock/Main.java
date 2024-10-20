package dev.vagvolgyi.smartledclock;

import dev.vagvolgyi.smartledclock.display.DateDisplay;
import dev.vagvolgyi.smartledclock.display.DigitalTimeDisplay;
import dev.vagvolgyi.smartledclock.display.Display;
import dev.vagvolgyi.smartledclock.display.render.FullscreenRenderer;
import dev.vagvolgyi.smartledclock.display.render.LocalizedRenderer;

import java.awt.*;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Main {
    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));

        try(FullscreenRenderer renderer = new FullscreenRenderer();
            ScheduledExecutorService renderExecutorService = Executors.newSingleThreadScheduledExecutor()) {

            Runnable renderTask = createRenderTask(renderer);
            renderExecutorService.scheduleWithFixedDelay(renderTask, 0, 100, MILLISECONDS);

            latch.await();
        }
    }

    private static Runnable createRenderTask(FullscreenRenderer renderer) {
        List<Display> displays = List.of(new DigitalTimeDisplay(new LocalizedRenderer(renderer, new Point(22, 64))),
                                         new DateDisplay(new LocalizedRenderer(renderer, new Point(0, 64))));

        return () -> {
            renderer.setBrightness(getBrightness());
            displays.forEach(Display::renderContent);
            renderer.commit();
        };
    }

    private static int getBrightness() {
        int hour = LocalTime.now().getHour();

        if(hour < 8) {
            return 1;
        }
        else if(hour < 17) {
            return 60;
        }
        else {
            return 30;
        }
    }
}

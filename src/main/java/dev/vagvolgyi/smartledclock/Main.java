package dev.vagvolgyi.smartledclock;

import dev.vagvolgyi.smartledclock.display.DateDisplay;
import dev.vagvolgyi.smartledclock.display.DigitalTimeDisplay;
import dev.vagvolgyi.smartledclock.display.Display;
import dev.vagvolgyi.smartledclock.display.render.FullscreenRenderer;
import dev.vagvolgyi.smartledclock.display.render.LocalizedRenderer;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Main {
    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));

        try(ScheduledExecutorService renderExecutorService = Executors.newSingleThreadScheduledExecutor();
            FullscreenRenderer renderer = new FullscreenRenderer()) {
            Runnable renderTask = createRenderTask(renderer);

            renderExecutorService.scheduleWithFixedDelay(renderTask, 0, 100, MILLISECONDS);

            latch.await();
        }
    }

    private static Runnable createRenderTask(FullscreenRenderer renderer) {
        List<Display> displays = List.of(new DigitalTimeDisplay(new LocalizedRenderer(renderer, new Point(22, 64))),
                                         new DateDisplay(new LocalizedRenderer(renderer, new Point(0, 64))));

        return () -> {
            displays.forEach(Display::renderContent);
            renderer.commit();
        };
    }
}

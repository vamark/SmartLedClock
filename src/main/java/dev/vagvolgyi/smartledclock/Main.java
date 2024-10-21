package dev.vagvolgyi.smartledclock;

import dev.vagvolgyi.smartledclock.background.NetatmoWeather;
import dev.vagvolgyi.smartledclock.display.DateDisplay;
import dev.vagvolgyi.smartledclock.display.DigitalTimeDisplay;
import dev.vagvolgyi.smartledclock.display.Display;
import dev.vagvolgyi.smartledclock.display.TemperatureDisplay;
import dev.vagvolgyi.smartledclock.display.render.FullscreenRenderer;
import dev.vagvolgyi.smartledclock.display.render.LocalizedRenderer;

import java.awt.*;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class Main {
    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));

        try(FullscreenRenderer renderer = new FullscreenRenderer();
            ScheduledExecutorService renderExecutorService = Executors.newSingleThreadScheduledExecutor();
            ScheduledExecutorService backgroundExecutorService = Executors.newSingleThreadScheduledExecutor()) {

            NetatmoWeather netatmoWeather = new NetatmoWeather();
            backgroundExecutorService.scheduleWithFixedDelay(netatmoWeather, 0, 5, MINUTES);

            Runnable renderTask = createRenderTask(renderer, netatmoWeather);
            renderExecutorService.scheduleWithFixedDelay(renderTask, 0, 100, MILLISECONDS);

            latch.await();
        }
    }

    private static Runnable createRenderTask(FullscreenRenderer renderer, NetatmoWeather netatmoWeather) {
        List<Display> displays = List.of(
                new DigitalTimeDisplay(new LocalizedRenderer(renderer, new Point(22, 64))),
                new DateDisplay(new LocalizedRenderer(renderer, new Point(0, 64))),
                new TemperatureDisplay(new LocalizedRenderer(renderer, new Point(0, 10)), netatmoWeather::getOutdoorTemp),
                new TemperatureDisplay(new LocalizedRenderer(renderer, new Point(0, 23)), netatmoWeather::getIndoorTemp)
        );

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

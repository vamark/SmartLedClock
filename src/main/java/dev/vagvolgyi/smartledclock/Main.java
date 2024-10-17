package dev.vagvolgyi.smartledclock;

import dev.vagvolgyi.smartledclock.display.DateDisplay;
import dev.vagvolgyi.smartledclock.display.DigitalTimeDisplay;
import dev.vagvolgyi.smartledclock.display.Display;
import dev.vagvolgyi.smartledclock.display.render.FullscreenRenderer;
import dev.vagvolgyi.smartledclock.display.render.LocalizedRenderer;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws Exception {
        AtomicBoolean running = new AtomicBoolean(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> running.set(false)));

        try(FullscreenRenderer renderer = new FullscreenRenderer()) {
            List<Display> displays = List.of(
                new DigitalTimeDisplay(new LocalizedRenderer(renderer, new Point(22, 64))),
                new DateDisplay(new LocalizedRenderer(renderer, new Point(0, 64))
            ));

            while(running.get()) {
                displays.forEach(Display::renderContent);
                renderer.commit();
            }
        }
    }
}

package dev.vagvolgyi.smartledclock.display;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;
import dev.vagvolgyi.smartledclock.display.render.Renderer;
import dev.vagvolgyi.smartledclock.util.FontCache;

import java.awt.*;
import java.time.LocalTime;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;
import static java.lang.Math.*;
import static java.time.format.DateTimeFormatter.ofPattern;

public class DigitalTimeDisplay extends Display {
    private static final BdfFont largeFont = FontCache.getFont("8x13.bdf");
    private static final BdfFont smallFont = FontCache.getFont("4x6.bdf");

    public DigitalTimeDisplay(Renderer renderer) {
        super(renderer);
    }

    @Override
    public void renderContent() {
        LocalTime currentTime = LocalTime.now();

        drawTime(currentTime);
        drawSecondsMeter(currentTime);
        drawTimeSeparators(currentTime);
    }

    private void drawTime(LocalTime currentTime) {
        renderer.drawText(largeFont, new Point(0, 0), RED, currentTime.format(ofPattern("HH")));
        renderer.drawText(largeFont, new Point(17, 0), RED, currentTime.format(ofPattern("mm")));
        renderer.drawText(smallFont, new Point(35, 0), RED, currentTime.format(ofPattern("ss")));
    }

    private void drawSecondsMeter(LocalTime currentTime) {
        int lineLength = Math.round((float)40 / 59 * currentTime.getSecond());
        if(lineLength > 0) {
            Point meterStart = new Point(1, -12);
            Point meterEnd = new Point(meterStart);
            meterEnd.translate(lineLength - 1, 0);
            renderer.drawLine(meterStart, meterEnd, BLUE);
        }
    }

    private void drawTimeSeparators(LocalTime currentTime) {
        int centiSeconds = currentTime.getNano() / 100_000_000;
        int red = (int)round((double)255 / 2 * (1 + cos(2 * PI / 10 * centiSeconds)));
        Color separatorColor = new Color(red, 0, 0);

        renderer.drawPixel(new Point(16, -7), separatorColor);
        renderer.drawPixel(new Point(16, -3), separatorColor);

        renderer.drawPixel(new Point(33, -4), separatorColor);
        renderer.drawPixel(new Point(33, -2), separatorColor);
    }
}

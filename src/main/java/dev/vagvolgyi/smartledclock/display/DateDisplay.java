package dev.vagvolgyi.smartledclock.display;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;
import dev.vagvolgyi.smartledclock.display.render.Renderer;
import dev.vagvolgyi.smartledclock.util.FontCache;

import java.awt.*;
import java.time.LocalDate;

import static java.awt.Color.GREEN;
import static java.time.format.DateTimeFormatter.ofPattern;

public class DateDisplay implements Display {
    private static final BdfFont yearFont = FontCache.getFont("5x7.bdf");
    private static final BdfFont dateFont = FontCache.getFont("4x6.bdf");

    private final Renderer renderer;

    public DateDisplay(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void renderContent() {
        LocalDate currentDate = LocalDate.now();
        drawDate(currentDate);
    }

    private void drawDate(LocalDate currentDate) {
        renderer.drawText(yearFont, new Point(0, - 6), GREEN, currentDate.format(ofPattern("yyyy")));
        renderer.drawText(dateFont, new Point(0, 0), GREEN, currentDate.format(ofPattern("MM/dd")));
    }
}

package dev.vagvolgyi.smartledclock.display;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;
import dev.vagvolgyi.smartledclock.background.weather.Trend;
import dev.vagvolgyi.smartledclock.display.render.Renderer;
import dev.vagvolgyi.smartledclock.util.FontCache;

import java.awt.*;
import java.util.function.Supplier;

import static java.awt.Color.*;

public class TemperatureDisplay extends Display {
    private static final BdfFont largeFont = FontCache.getFont("8x13.bdf");
    private static final BdfFont smallFont = FontCache.getFont("4x6.bdf");

    private final Supplier<Float> tempSupplier;
    private final Supplier<Trend> tempTrendSupplier;

    public TemperatureDisplay(Renderer renderer, Supplier<Float> tempSupplier, Supplier<Trend> tempTrendSupplier) {
        super(renderer);
        this.tempSupplier = tempSupplier;
        this.tempTrendSupplier = tempTrendSupplier;
    }

    @Override
    public void renderContent() {
        Float temp = tempSupplier.get();

        if(temp != null) {
            renderValue(temp);
            renderUnit();
            renderTempTrend(tempTrendSupplier.get());
        }
        else {
            renderer.drawText(largeFont, new Point(0, 0), DARK_GRAY, "NA");
        }
    }

    private void renderValue(Float temp) {
        int integerPart = Math.abs(temp.intValue());
        int decimalPart = Math.round((temp - integerPart) * 10);

        if(temp < 0) {
            renderer.drawLine(new Point(0, -5), new Point(2, -5), WHITE);
        }

        renderer.drawText(largeFont, new Point(3, 0), WHITE, String.format("%2d", integerPart));
        renderer.drawPixel(new Point(19, -1), WHITE);
        renderer.drawText(smallFont, new Point(21, 0), WHITE, String.format("%d", decimalPart));
    }

    private void renderUnit() {
        renderer.drawPixel(new Point(25, -5), WHITE);
        renderer.drawText(smallFont, new Point(27, 0), WHITE, "C");
    }

    private void renderTempTrend(Trend trend) {
        if(trend != null) {
            switch(trend) {
                case UP -> renderUp();
                case DOWN -> renderDown();
                case STABLE -> renderStable();
            }
        }
    }

    private void renderUp() {
        renderer.drawPixel(new Point(25, -9), RED);
        renderer.drawLine(new Point(24, -8), new Point(26, -8), RED);
        renderer.drawLine(new Point(23, -7), new Point(27, -7), RED);
    }

    private void renderDown() {
        renderer.drawLine(new Point(23, -9), new Point(27, -9), BLUE);
        renderer.drawLine(new Point(24, -8), new Point(26, -8), BLUE);
        renderer.drawPixel(new Point(25, -7), BLUE);
    }

    private void renderStable() {
        renderer.drawLine(new Point(24, -8), new Point(26, -8), DARK_GRAY);
    }
}

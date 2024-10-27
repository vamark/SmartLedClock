package dev.vagvolgyi.smartledclock.display;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;
import dev.vagvolgyi.smartledclock.display.render.Renderer;
import dev.vagvolgyi.smartledclock.util.FontCache;

import java.awt.*;
import java.util.function.Supplier;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.WHITE;

public class HumidityDisplay extends Display {
    private static final BdfFont largeFont = FontCache.getFont("8x13.bdf");
    private static final BdfFont smallFont = FontCache.getFont("4x6.bdf");

    private final Supplier<Short> humiditySupplier;

    public HumidityDisplay(Renderer renderer, Supplier<Short> humiditySupplier) {
        super(renderer);
        this.humiditySupplier = humiditySupplier;
    }

    @Override
    public void renderContent() {
        Short humidity = humiditySupplier.get();

        if(humidity != null) {
            renderer.drawText(largeFont, new Point(0, 0), WHITE, String.format("%2d", humidity));
            renderer.drawText(smallFont, new Point(16, 0), WHITE, "%");
        }
        else {
            renderer.drawText(largeFont, new Point(0, 0), DARK_GRAY, "NA");
        }
    }
}

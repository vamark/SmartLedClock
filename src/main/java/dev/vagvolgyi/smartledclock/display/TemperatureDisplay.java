package dev.vagvolgyi.smartledclock.display;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;
import dev.vagvolgyi.smartledclock.display.render.Renderer;
import dev.vagvolgyi.smartledclock.util.FontCache;

import java.awt.*;
import java.util.function.Supplier;

import static java.awt.Color.WHITE;

public class TemperatureDisplay extends Display {
    private static final BdfFont largeFont = FontCache.getFont("8x13.bdf");
    private static final BdfFont smallFont = FontCache.getFont("4x6.bdf");

    private final Supplier<Float> tempSupplier;

    public TemperatureDisplay(Renderer renderer, Supplier<Float> tempSupplier) {
        super(renderer);
        this.tempSupplier = tempSupplier;
    }

    @Override
    public void renderContent() {
        Float temp = tempSupplier.get();
        String tempString = temp != null ? String.format("%4.1f", temp) : "N/A";

        renderer.drawText(largeFont, new Point(0, 0), WHITE, tempString);
        renderer.drawText(smallFont, new Point(32, 0), WHITE, "°C");
    }
}

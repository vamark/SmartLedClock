package dev.vagvolgyi.smartledclock.display.render;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class LocalizedRenderer implements Renderer {
    private final Renderer renderer;
    private final Point location;

    public LocalizedRenderer(Renderer renderer, Point location) {
        this.renderer = renderer;
        this.location = location;
    }

    @Override
    public void setBrightness(int brightness) {
        renderer.setBrightness(brightness);
    }

    @Override
    public void drawPixel(Point point, Color color) {
        point.translate(location.x, location.y);
        renderer.drawPixel(point, color);
    }

    @Override
    public void drawPixels(Point start, int width, int height, Collection<Color> colors) {
        start.translate(location.x, location.y);
        renderer.drawPixels(start, width, height, colors);
    }

    @Override
    public void fill(Color color) {
        renderer.fill(color);
    }

    @Override
    public void drawLine(Point start, Point end, Color color) {
        start.translate(location.x, location.y);
        end.translate(location.x, location.y);
        renderer.drawLine(start, end, color);
    }

    @Override
    public void drawCircle(Point center, int radius, Color color) {
        center.translate(location.x, location.y);
        renderer.drawCircle(center, radius, color);
    }

    @Override
    public int drawText(BdfFont font, Point start, Color color, String text) {
        start.translate(location.x, location.y);
        return renderer.drawText(font, start, color, text);
    }

    @Override
    public int drawVerticalText(BdfFont font, Point start, Color color, String text) {
        start.translate(location.x, location.y);
        return renderer.drawVerticalText(font, start, color, text);
    }

    @Override
    public boolean drawImage(Point start, BufferedImage image) {
        start.translate(location.x, location.y);
        return renderer.drawImage(start, image);
    }

    @Override
    public void clear() {
        renderer.clear();
    }
}

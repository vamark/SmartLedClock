package dev.vagvolgyi.smartledclock.display.render;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

public interface Renderer {
    void setBrightness(int brightness);

    void drawPixel(Point point, Color color);

    void drawPixels(Point start, int width, int height, Collection<Color> colors);

    void fill(Color color);

    void drawLine(Point start, Point end, Color color);

    void drawCircle(Point center, int radius, Color color);

    int drawText(BdfFont font, Point start, Color color, String text);

    int drawVerticalText(BdfFont font, Point start, Color color, String text);

    boolean drawImage(Point start, BufferedImage image);

    void clear();
}

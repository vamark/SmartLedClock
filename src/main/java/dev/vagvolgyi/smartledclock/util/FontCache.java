package dev.vagvolgyi.smartledclock.util;

import dev.vagvolgyi.rgbmatrix.wrapper.BdfFont;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.requireNonNull;

public class FontCache {
    private static final ConcurrentMap<String, BdfFont> cache = new ConcurrentHashMap<>();
    private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private FontCache() {}

    public static BdfFont getFont(String fontName) {
        String fontPath = "fonts/" + fontName;
        return cache.computeIfAbsent(fontPath, path -> new BdfFont(requireNonNull(classLoader.getResource(path))));
    }
}

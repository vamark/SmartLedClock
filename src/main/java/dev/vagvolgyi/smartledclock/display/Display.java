package dev.vagvolgyi.smartledclock.display;

import dev.vagvolgyi.smartledclock.display.render.Renderer;

public abstract class Display {
    protected final Renderer renderer;

    protected Display(Renderer renderer) {
        this.renderer = renderer;
    }

    public abstract void renderContent();
}

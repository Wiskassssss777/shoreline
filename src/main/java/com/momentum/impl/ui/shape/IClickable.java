package com.momentum.impl.ui.shape;

import java.io.IOException;

/**
 * Shape that can be interacted with via the LWJGL keyboard and mouse
 *
 * @author linus
 * @since 03/24/2023
 */
public interface IClickable
{
    /**
     * Called when the mouse is clicked.
     *
     * @param mouseX The mouse x
     * @param mouseY The mouse y
     * @param mouseButton The clicked button code
     */
    void onClick(int mouseX, int mouseY, int mouseButton);

    /**
     * Called when a key is typed (except F11 which toggles fullscreen).
     *
     * @param typedChar The keyboard character
     * @param keyCode The LWJGL keycode
     */
    void onType(char typedChar, int keyCode);
}
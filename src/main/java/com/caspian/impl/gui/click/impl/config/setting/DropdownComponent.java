package com.caspian.impl.gui.click.impl.config.setting;

import com.caspian.api.config.Config;
import com.caspian.impl.gui.click.impl.config.ConfigFrame;
import net.minecraft.client.util.math.MatrixStack;

/**
 *
 *
 * @author linus
 * @since 1.0
 *
 */
public class DropdownComponent extends ConfigComponent<Enum<?>>
{
    /**
     *
     *
     * @param frame
     * @param config
     */
    public DropdownComponent(ConfigFrame frame, Config<Enum<?>> config)
    {
        super(frame, config);
    }

    /**
     *
     *
     * @param matrices
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    @Override
    public void render(MatrixStack matrices, float mouseX, float mouseY,
                       float delta)
    {

    }

    /**
     *
     *
     * @param mouseX
     * @param mouseY
     * @param button
     */
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button)
    {

    }

    /**
     *
     *
     * @param mouseX
     * @param mouseY
     * @param button
     */
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button)
    {

    }

    /**
     *
     *
     * @param keyCode
     * @param scanCode
     * @param modifiers
     */
    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers)
    {

    }
}

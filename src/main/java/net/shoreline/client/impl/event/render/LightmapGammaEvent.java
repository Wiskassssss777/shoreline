package net.shoreline.client.impl.event.render;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;
import net.shoreline.client.mixin.render.MixinLightmapTextureManager;

/**
 *
 *
 * @author linus
 * @since 1.0
 *
 * @see MixinLightmapTextureManager
 */
@Cancelable
public class LightmapGammaEvent extends Event
{
    //
    private int gamma;

    /**
     *
     *
     * @param gamma
     */
    public LightmapGammaEvent(int gamma)
    {
        this.gamma = gamma;
    }

    public int getGamma()
    {
        return gamma;
    }

    public void setGamma(int gamma)
    {
        this.gamma = gamma;
    }
}

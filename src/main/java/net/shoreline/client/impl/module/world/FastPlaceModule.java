package net.shoreline.client.impl.module.world;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.ListConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorMinecraftClient;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.world.SneakBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class FastPlaceModule extends ToggleModule
{
    //
    Config<Selection> selectionConfig = new EnumConfig<>("Selection", "The " +
            "selection of items to apply fast placements", Selection.WHITELIST,
            Selection.values());
    Config<Integer> delayConfig = new NumberConfig<>("Delay", "Fast place " +
            "delay", 0, 1, 4);
    Config<Float> startDelayConfig = new NumberConfig<>("StartDelay", "Fast" +
            " place start delay", 0.0f, 0.0f, 1.0f);
    Config<Boolean> ghostFixConfig = new BooleanConfig("GhostFix", "Fixes " +
            "item ghosting issue on some servers", false);
    Config<List<Item>> whitelistConfig = new ListConfig<>("Whitelist",
            "Valid item whitelist", Items.EXPERIENCE_BOTTLE,
            Items.SNOWBALL, Items.EGG);
    Config<List<Item>> blacklistConfig = new ListConfig<>("Blacklist",
            "Valid item blacklist", Items.ENDER_PEARL, Items.ENDER_EYE);
    //
    private final CacheTimer startTimer = new CacheTimer();

    /**
     *
     */
    public FastPlaceModule()
    {
        super("FastPlace", "Place items and blocks faster",
                ModuleCategory.WORLD);
    }

    /**
     *
     *
     * @param event
     */
    @EventListener
    public void onTick(TickEvent event)
    {
        if (event.getStage() != EventStage.PRE)
        {
            return;
        }
        if (!mc.options.useKey.isPressed())
        {
            startTimer.reset();
        }
        else if (startTimer.passed(startDelayConfig.getValue(), TimeUnit.SECONDS)
                && ((AccessorMinecraftClient) mc).hookGetItemUseCooldown() > delayConfig.getValue()
                && placeCheck(mc.player.getMainHandStack()))
        {
            if (ghostFixConfig.getValue())
            {
                Managers.NETWORK.sendSequencedPacket(id ->
                        new PlayerInteractItemC2SPacket(mc.player.getActiveHand(), id));
            }
            ((AccessorMinecraftClient) mc).hookSetItemUseCooldown(delayConfig.getValue());
        }
    }

    /**
     *
     *
     * @param event
     */
    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event)
    {
        if (mc.player == null || mc.world == null)
        {
            return;
        }
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet
                && ghostFixConfig.getValue() && !event.isClientPacket()
                && placeCheck(mc.player.getStackInHand(packet.getHand())))
        {
            final BlockState state = mc.world.getBlockState(
                    packet.getBlockHitResult().getBlockPos());
            if (!SneakBlocks.isSneakBlock(state))
            {
                event.cancel();
            }
        }
    }

    /**
     *
     *
     * @param held
     * @return
     */
    private boolean placeCheck(ItemStack held)
    {
        return switch (selectionConfig.getValue())
                {
                    case WHITELIST -> ((ListConfig<?>) whitelistConfig)
                            .contains(held.getItem());
                    case BLACKLIST -> !((ListConfig<?>) blacklistConfig)
                            .contains(held.getItem());
                    case ALL -> true;
                };
    }

    public enum Selection
    {
        WHITELIST,
        BLACKLIST,
        ALL
    }
}

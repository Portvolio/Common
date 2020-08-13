package wtf.violet.common;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UtilPlayer
{

    private static final Random RANDOM = new Random();

    // Don't construct
    private UtilPlayer() {}

    /**
     * Find a random slot of the player's inventory with an item in it
     * @param player Player
     * @return The ItemStack, null if none are found
     */
    public static ItemStack findRandomFilledSlot(final Player player)
    {
        final List<ItemStack> stacks = new ArrayList<>();

        for (final ItemStack stack : player.getInventory().getContents())
        {
            if (stack != null)
            {
                stacks.add(stack);
            }
        }

        final int size = stacks.size();

        if (size == 0)
        {
            return null;
        }

        return stacks.get(RANDOM.nextInt(size));
    }

}

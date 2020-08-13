package wtf.violet.common.nms;

import org.bukkit.Bukkit;

public class NmsUtil
{

    private NmsUtil() {} // Do not construct

    public static String getNmsVersion()
    {
        return Bukkit.getServer()
            .getClass()
            .getPackageName()
            .substring("org.bukkit.craftbukkit.".length())
            .split("\\.")[0];
    }

}

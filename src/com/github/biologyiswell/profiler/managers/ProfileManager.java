/**
 * MIT License
 * <p>
 * Copyright (c) 2017 biologyiswell
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in * all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.biologyiswell.profiler.managers;

import com.github.biologyiswell.profiler.IsMyProfiler;
import com.github.biologyiswell.profiler.util.HttpUtils;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.*;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by biologyiswell on 03/10/2017.
 */
public class ProfileManager
{

    /**
     * Profiles map,
     * This map represents the registered profiles
     * UUID: Represents the UUID from player that GameProfile has been changed
     * GameProfile: Represents the old GameProfile from player
     */
    private static final Map<UUID, GameProfile> PROFILES = new HashMap<>();

    /**
     * Set Skin And Name,
     * This method set the skin to player from the other player,
     * And the set the name from the other player
     *
     * @param player the player that the skin will be updated
     * @param from   the player name skin
     * @param name   the name from player that will set to player
     */
    public static void setSkinAndName(final Player player, final String from, final String name)
    {
        Preconditions.checkNotNull(player, "player can't be null");
        Preconditions.checkNotNull(from, "from (skin name) can't be null");

        boolean contains = PROFILES.containsKey(player.getUniqueId());
        IsMyProfiler.instance.getLogger().info(String.format("Player %s has been %s skin for " + from + ".", player.getName(), contains ? "updating your" : "set"));

        // ## GET PROPERTIES FROM (FROM) THAT REPRESENTS THE PROPERTIES WITH THE (VALUE, SIGNATURE)
        final JsonObject properties = getProperty(from);
        final String value = properties.get("value").getAsString();
        final String signature = properties.get("signature").getAsString();

        // ## CHANGE THE GAME PROFILE
        final EntityPlayer ep = ((CraftPlayer) player).getHandle();
        final GameProfile gameProfile = ep.getProfile();

        if (!contains)
        {
            PROFILES.put(player.getUniqueId(), gameProfile);
        }

        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));

        try
        {
            final Field nameField = gameProfile.getClass().getDeclaredField("name");

            nameField.setAccessible(true);
            nameField.set(gameProfile, name);
            nameField.setAccessible(!nameField.isAccessible());
        } catch (Exception e)
        {
            throw new RuntimeException("Can't be change the name from the player " + player.getName() + ".");
        }

        // ## MAKE THE PACKETS
        // ## (TO MAKE MORE FAST THE UPDATE)

        final PacketPlayOutPlayerInfo prem = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        final PacketPlayOutPlayerInfo padd = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        final PacketPlayOutEntityDestroy pdes = new PacketPlayOutEntityDestroy(ep.getId());
        final PacketPlayOutNamedEntitySpawn pnes = new PacketPlayOutNamedEntitySpawn(ep);

        Bukkit.getOnlinePlayers().forEach(online ->
        {
            // ## TAKE CARE: NO REMOVE THE (ONLINE == PLAYER), IF NO THE PLAYER IS DUPLICATED
            if (online == player || online.canSee(player))
            {
                final PlayerConnection playerConnection = ((CraftPlayer) online).getHandle().playerConnection;
                playerConnection.sendPacket(prem);
                playerConnection.sendPacket(padd);
                playerConnection.sendPacket(pdes);
                playerConnection.sendPacket(pnes);
            }
        });
    }

    /**
     * Set Skin,
     * This method set the skin to player from the other player
     *
     * @param player the player that the skin will be updated
     * @param from   the player name skin
     */
    public static void setSkin(final Player player, final String from)
    {
        Preconditions.checkNotNull(player, "player can't be null");
        Preconditions.checkNotNull(from, "from (skin name) can't be null");

        boolean contains = PROFILES.containsKey(player.getUniqueId());
        IsMyProfiler.instance.getLogger().info(String.format("Player %s has been %s skin for " + from + ".", player.getName(), contains ? "updating your" : "set"));

        // ## GET PROPERTIES FROM (FROM) THAT REPRESENTS THE PROPERTIES WITH THE (VALUE, SIGNATURE)
        final JsonObject properties = getProperty(from);
        final String value = properties.get("value").getAsString();
        final String signature = properties.get("signature").getAsString();

        // ## CHANGE THE GAME PROFILE
        final EntityPlayer ep = ((CraftPlayer) player).getHandle();
        final GameProfile gameProfile = ep.getProfile();

        if (!contains)
        {
            PROFILES.put(player.getUniqueId(), gameProfile);
        }

        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));

        // ## MAKE THE PACKETS
        // ## (TO MAKE MORE FAST THE UPDATE)

        final PacketPlayOutPlayerInfo prem = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        final PacketPlayOutPlayerInfo padd = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        final PacketPlayOutEntityDestroy pdes = new PacketPlayOutEntityDestroy(ep.getId());
        final PacketPlayOutNamedEntitySpawn pnes = new PacketPlayOutNamedEntitySpawn(ep);

        Bukkit.getOnlinePlayers().forEach(online ->
        {
            // ## TAKE CARE: NO REMOVE THE (ONLINE == PLAYER), IF NO THE PLAYER IS DUPLICATED
            if (online == player || online.canSee(player))
            {
                final PlayerConnection playerConnection = ((CraftPlayer) online).getHandle().playerConnection;
                playerConnection.sendPacket(prem);
                playerConnection.sendPacket(padd);
                playerConnection.sendPacket(pdes);
                playerConnection.sendPacket(pnes);
            }
        });
    }

    /**
     * Get Property,
     * This method get the property from player
     *
     * @param fromWhom this represents the player name that will get the properties
     */
    private static JsonObject getProperty(final String fromWhom)
    {
        IsMyProfiler.SERVICE.submit(() ->
        {
            final String API_MOJANG_URL = "https://api.mojang.com/users/profiles/minecraft/" + fromWhom;
            final JsonObject api = IsMyProfiler.GSON.fromJson(HttpUtils.getResponse(API_MOJANG_URL), JsonObject.class);
            final String UUID = api.get("id").getAsString();

            final String SESSION_URL = "https://sessionserver.mojang.com/session/minecraft/profile/" + UUID + "?unsigned=false";
            final JsonObject session = IsMyProfiler.GSON.fromJson(HttpUtils.getResponse(SESSION_URL), JsonObject.class);

            final JsonObject properties = session.get("properties").getAsJsonArray().get(0).getAsJsonObject();
            return properties;
        });

        throw new RuntimeException("Can't be get the properties from " + fromWhom + ".");
    }

    /**
     * Unregister Profile,
     * This method set the default profile from player and unregister the player from map
     */
    public static void unregisterProfile(final Player player)
    {
        if (PROFILES.containsKey(player.getUniqueId()))
        {
            final GameProfile oldGameProfile = PROFILES.get(player.getUniqueId());
            final EntityHuman entityHuman = ((CraftPlayer) player).getHandle();

            // ## TRING TO REFLECTION
            // ## NEED BE REMOVE THE FINAL MODIFIER FROM GAME PROFILE FIELD (ENTITY HUMAN)
            try
            {
                final Field gameProfileField = entityHuman.getClass().getDeclaredField("bH");

                gameProfileField.setAccessible(true);
                gameProfileField.set(entityHuman, oldGameProfile);
                gameProfileField.setAccessible(!gameProfileField.isAccessible());
            } catch (Exception e)
            {
                throw new RuntimeException("Can't be change the game profile from player");
            }

            PROFILES.remove(player.getUniqueId());
        }
    }
}

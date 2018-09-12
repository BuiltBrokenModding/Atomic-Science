package com.builtbroken.atomic.lib;

import com.builtbroken.atomic.AtomicScience;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to handle working with Strings
 *
 * @author Darkguardsman
 * based on Calclavia version from Resonant AtomicScience but has been mostly rewritten so no longer marked as author
 */
public class LanguageUtility
{
    public static int toolTipLineLength = 120;

    /**
     * Grabs the localization for the string provided. Make sure the string
     * matches the exact key in a translation file.
     *
     * @param key - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     * @return translated key, or the same string provided if the key didn't match anything
     */
    public static String getLocal(String key)
    {
        //Check for empty or null keys
        if (key == null || key.isEmpty())
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("LanguageUtility.getLocal(" + key + ") - invalid key", new RuntimeException());
            }
            return "error.key.empty";
        }

        //Get translation
        String translation = I18n.format(key);
        if (translation == null || translation.isEmpty())
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("LanguageUtility.getLocal(" + key + ") - no translation", new RuntimeException());
            }
            return key;
        }
        return translation;
    }

    /**
     * Same as getLocal(String) but appends '.name' if it is missing
     *
     * @param key - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     * @return translated key, or the same string provided if the key didn't match anything
     */
    public static String getLocalName(String key)
    {
        //Check for empty or null keys
        if (key == null || key.isEmpty())
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("LanguageUtility.getLocalName(" + key + ")", new RuntimeException());
            }
            return "error.key.empty";
        }
        if (!key.endsWith(".name"))
        {
            key = key + ".name";
        }

        //Get translation
        String translation = I18n.format(key);
        if (translation == null || translation.isEmpty())
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("LanguageUtility.getLocal(" + key + ") - no translation", new RuntimeException());
            }
            return key;
        }
        return translation;
    }


    /**
     * Uses the language file as a place to store settings
     * for GUI components that use translations. In which
     * the same component may need to change sizes with
     * changes in words.
     *
     * @param key    - string to use to look up the result
     * @param backup - returned if key fails to be found or parsed
     * @return integer parsed from a lang file
     */
    public static Integer getLangSetting(String key, int backup)
    {
        String result = getLocal(key);
        if (result != null && !result.isEmpty())
        {
            try
            {
                return Integer.parseInt(key);
            }
            catch (NumberFormatException e)
            {
                if (AtomicScience.runningAsDev)
                {
                    AtomicScience.logger.error("LanguageUtility.getLangSetting(" + key + ")", e);
                }
            }
        }
        return backup;
    }

    /**
     * Helper version of getLocalName that places the translated string inside
     * minecraft's chat component system.
     *
     * @param key
     * @return
     */
    public static ITextComponent getLocalChat(String key)
    {
        String translation = getLocalName(key);
        if (translation == null || translation.isEmpty())
        {
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.error("LanguageUtility.getLocalChat(" + key + ")", new RuntimeException());
            }
            return new TextComponentTranslation("error.translation.empty", key);
        }

        return new TextComponentTranslation(translation);
    }

    /**
     * Helper method to translate, wrap, then send the msg to the player.
     * Designed to save line length when creating a lot of feed back for the player
     *
     * @param player - player who will receive the message
     * @param key    - - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     */
    public static void addChatToPlayer(EntityPlayer player, String key)
    {
        if (player != null)
        {
            player.sendMessage(getLocalChat(key));
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("LanguageUtility.addChatToPlayer(Null Player, " + key + ")", new RuntimeException());
        }
    }

    public static List<String> splitStringPerWord(String string) //TODO move to string utility in coding lib
    {
        return Arrays.asList(toWordArray(string));
    }

    public static String[] toWordArray(String string) //TODO move to string utility in coding lib
    {
        return string.trim().split("\\W+");
    }

    public static List<String> splitByLine(String string, int charsPerLine)
    {
        //Single line
        if (string.length() < charsPerLine)
        {
            return Lists.newArrayList(string);
        }

        //Break string into words
        String[] words = toWordArray(string);

        //Turn words back into lines
        List<String> lines = new ArrayList(); //TODO predict size for faster runtime
        String line = "";
        for (String word : words)
        {
            //Add each word to the line until we hit line max
            if (word.length() + line.length() <= charsPerLine)
            {
                line += word;
            }
            //Create new line, add old
            else
            {
                lines.add(line);
                line = word;
            }
        }

        //Fix if single line was entered with a lot of spaces
        if (lines.isEmpty())
        {
            lines.add(line);
        }
        return lines;
    }

    public static String capitalizeFirst(String str) //TODO move to string utility in coding lib
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
    }

    public static String decapitalizeFirst(String str) //TODO move to string utility in coding lib
    {
        return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
    }
}

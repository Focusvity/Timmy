package dev.deafkid.timmy;

import dev.deafkid.timmy.command.Command;
import dev.deafkid.timmy.command.SubCommand;
import dev.deafkid.timmy.util.ReflectionHelper;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import javax.security.auth.login.LoginException;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Copyright (C) 2022  Nathan Curran
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@Getter
public class TimmyBot {

    @Getter
    private static TimmyBot instance;
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(TimmyBot.class);
    private Settings settings;
    private JDA api;

    public static void main(String[] args) {
        if (System.getProperty("file.encoding").equalsIgnoreCase("UTF-8")) {
            (instance = new TimmyBot()).load();
        } else {
            logger.error("Unable to start the Timmy bot... The program is not running in UTF-8!");
            System.exit(-1);
        }
    }

    private void load() {
        try {
            settings = new Settings();
            settings.load();

            JDABuilder builder = JDABuilder.createDefault(settings.getToken());
            builder.enableIntents(Arrays.asList(GatewayIntent.values()));
            builder.setChunkingFilter(ChunkingFilter.ALL);
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);

            Set<Class<? extends ListenerAdapter>> listeners = ReflectionHelper.getClassesBySubType("dev.deafkid.timmy.listener",
                ListenerAdapter.class);
            listeners.forEach(c -> {
                try {
                    builder.addEventListeners(c.getConstructor().newInstance());
                } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                    logger.error(String.format("Something went wrong while trying to register %s as a listener", c.getSimpleName()), ex);
                }
            });

            // Subcommands must load first, so we can assign them to commands afterwards
            SubCommand.fetchSubCommands();
            Command.fetchCommands();

            builder.setBulkDeleteSplittingEnabled(false);
            builder.setActivity(Activity.watching("Married at First Sight"));

            api = builder.build();

            api.upsertCommand("ping", "Pong").queue();

            api.awaitReady();
        } catch (LoginException ex) {
            logger.error(
                "Unable to start the Timmy bot with the current token. Please check the token and try again. If the issue is persistent, please check the Discord status.",
                ex);
        } catch (InterruptedException ex) {
            logger.error("Something went wrong while starting the Timmy bot, please check the following error.", ex);
        }
    }
}

package dev.deafkid.timmy.command;

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

import dev.deafkid.timmy.TimmyBot;
import dev.deafkid.timmy.util.ReflectionHelper;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Getter
public abstract class SubCommand {

    @Getter
    private static final List<SubCommand> subcommands = new ArrayList<>();
    private final String name;
    private final String description;
    private final Class<? extends Command> parentCommand;
    private SlashCommandInteractionEvent event;
    private Member sender;
    private String[] args;

    public SubCommand() {
        SubCommandInfo info = getClass().getAnnotation(SubCommandInfo.class);
        if (info == null) {
            throw new NullPointerException("Missing SubCommandInfo class for " + getClass().getSimpleName());
        }

        this.name = getClass().getSimpleName().replace("SubCommand", "");
        this.description = info.description();
        this.parentCommand = info.command();
    }

    public static void fetchSubCommands() {
        Set<Class<? extends SubCommand>> classes = ReflectionHelper.getClassesBySubType("dev.deafkid.timmy.command", SubCommand.class);
        classes.forEach(c -> {
            try {
                subcommands.add(c.getConstructor().newInstance());
            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                TimmyBot.getLogger().error(String.format("Something went wrong while trying to register %s as a subcommand", c.getSimpleName()), ex);
            }
        });
    }

    public String getArg(int index) {
        return args[index+2];
    }

    public abstract void run(SlashCommandInteractionEvent event);

    public final void execute(SlashCommandInteractionEvent event) {
        this.event = event;
        this.sender = event.getMember();
        this.args = event.getCommandString().split(" "); // args starts from 2

        try {
            run(event);
        } catch (Exception ex) {
            TimmyBot.getLogger().error("Something went wrong while trying to execute subcommand " + name, ex);
        }
    }
}

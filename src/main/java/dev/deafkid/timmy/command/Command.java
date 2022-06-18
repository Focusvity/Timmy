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

@Getter
public abstract class Command {

    @Getter
    private static final List<Command> commands = new ArrayList<>();
    private final String name;
    private final String description;
    private final List<SubCommand> subCommands;

    public Command() {
        CommandInfo info = getClass().getAnnotation(CommandInfo.class);
        if (info == null) {
            throw new NullPointerException("Missing CommandInfo class for " + getClass().getSimpleName());
        }

        this.name = getClass().getSimpleName().replace("Command", "");
        this.description = info.description();
        this.subCommands = new ArrayList<>();
        assignSubCommands();
    }

    public static void fetchCommands() {
        Set<Class<? extends Command>> classes = ReflectionHelper.getClassesBySubType("dev.deafkid.timmy.command", Command.class);
        classes.forEach(c -> {
            try {
                commands.add(c.getConstructor().newInstance());
            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                TimmyBot.getLogger().error(String.format("Something went wrong while trying to register %s as a command", c.getSimpleName()), ex);
            }
        });
    }

    public void assignSubCommands() {
        SubCommand remove = null;
        for (SubCommand subCommand : SubCommand.getSubcommands()) {
            if (subCommand.getParentCommand() == getClass()) {
                subCommands.add(subCommand);
                remove = subCommand;
                break;
            }
        }
        SubCommand finalRemove = remove;
        SubCommand.getSubcommands().removeIf(p -> finalRemove != null && p == finalRemove);
    }

    public abstract void run();
}

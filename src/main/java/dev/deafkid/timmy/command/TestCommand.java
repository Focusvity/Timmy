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

import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@CommandInfo(description = "Test command")
public class TestCommand extends Command {

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if (event.getOption("test") != null) {
            event.reply(event.getOption("test").getAsString()).queue();
            return;
        }

        if (event.getOption("user") != null) {
            event.reply("User functional").queue();
            return;
        }

        event.reply("Yes, this is working").queue();
    }

    @Override
    public List<OptionData> getOptionData() {
        return Arrays.asList(new OptionData(OptionType.STRING, "test", "Test subcommand"),
            new OptionData(OptionType.USER, "user", "User subcommand"));
    }
}

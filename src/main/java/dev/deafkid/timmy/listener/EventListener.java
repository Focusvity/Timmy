package dev.deafkid.timmy.listener;

import dev.deafkid.timmy.TimmyBot;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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

public class EventListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        SelfUser self = event.getJDA().getSelfUser();
        String botInfo = self.getName() + "#" + self.getDiscriminator() + " (" + self.getId() + ")";
        TimmyBot.getLogger().info("Successfully connected to Discord as " + botInfo);
    }
}

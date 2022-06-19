package dev.deafkid.timmy;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import lombok.Getter;

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
public class Settings {

    private final File file = new File("settings.properties");
    private String token;
    private List<String> developers;

    public void load() {
        try {
            if (!file.exists()) { // TODO: Copy file from resources
                TimmyBot.getLogger().info("Cannot find the settings file, creating a new one!");
                file.createNewFile();
                TimmyBot.getLogger().info(
                    "The settings file has been created. Please edit the details accordingly and restart the program!");
            }

            // Code from Plex - START
            Properties properties;
            try (InputStream in = new FileInputStream(file)) {
                properties = new Properties();
                properties.load(in);
            }
            // Code from Plex - END

            token = properties.getProperty("botToken", "empty");
            developers = Arrays.asList(properties.getProperty("developers", "empty").split(","));
        } catch (Exception ignored) {
        }
    }
}

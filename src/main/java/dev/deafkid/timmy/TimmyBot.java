package dev.deafkid.timmy;

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
public class TimmyBot {

    private static TimmyBot instance;
    private static Logger logger = LoggerFactory.getLogger(TimmyBot.class);

    public static void main(String[] args) {
        if (System.getProperty("file.encoding").equalsIgnoreCase("UTF-8")) {
            (instance = new TimmyBot()).load();
        } else {
            logger.error("Unable to start TimmyBot... The program is not running in UTF-8!");
            System.exit(-1);
        }
    }

    private void load() {
        //
    }
}

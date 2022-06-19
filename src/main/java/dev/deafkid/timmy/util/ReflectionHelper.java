package dev.deafkid.timmy.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import dev.deafkid.timmy.TimmyBot;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

public class ReflectionHelper {

    public static Set<Class<?>> getClassesFrom(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            ClassPath path = ClassPath.from(TimmyBot.class.getClassLoader());
            ImmutableSet<ClassInfo> infoSet = path.getTopLevelClasses(packageName);
            infoSet.forEach(info -> {
                try {
                    Class<?> clazz = Class.forName(info.getName());
                    classes.add(clazz);
                } catch (ClassNotFoundException ex) {
                    TimmyBot.getLogger().error(
                        String.format("Unable to find %s in %s", info.getName(), packageName));
                }
            });
        } catch (IOException ex) {
            TimmyBot.getLogger()
                .error("Something went wrong while fetching classes from " + packageName);
            throw new RuntimeException(ex);
        }
        return Collections.unmodifiableSet(classes);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<Class<? extends T>> getClassesBySubType(String packageName,
        Class<T> subType) {
        Set<Class<?>> unfilteredClasses = getClassesFrom(packageName);
        Set<Class<? extends T>> filteredClasses = new HashSet<>();
        unfilteredClasses.forEach(clazz -> {
            if (clazz.getSuperclass() == subType || Arrays.asList(clazz.getInterfaces())
                .contains(subType)) {
                filteredClasses.add((Class<? extends T>) clazz);
            }
        });
        return Collections.unmodifiableSet(filteredClasses);
    }
}

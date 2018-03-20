/*
 *  openjavacard-tools: OpenJavaCard development tools
 *  Copyright (C) 2018  Ingo Albrecht (prom@berlin.ccc.de)
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *
 */

package org.openjavacard.tool;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Parameters(
        commandNames = "help",
        commandDescription = "Show help for available commands"
)
public class Help implements Runnable {

    @Parameter(
            description = "Command to show help for"
    )
    List<String> command = new ArrayList<>();

    JCommander mCommander;

    public Help(JCommander commander) {
        mCommander = commander;
    }

    @Override
    public void run() {
        if (command.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("  Commands:\n");
            Map<String, JCommander> commands = mCommander.getCommands();
            for (Map.Entry<String, JCommander> entry : commands.entrySet()) {
                String name = entry.getKey();
                JCommander command = entry.getValue();
                sb.append("    ");
                sb.append(name);
                sb.append("\t");
                sb.append(mCommander.getCommandDescription(name));
                sb.append("\n");
            }
            System.out.print(sb.toString());
        } else {
            mCommander.usage(command.get(0));
        }
    }

}

/*
 * openjavacard-tools: OpenJavaCard Development Tools
 * Copyright (C) 2015-2018 Ingo Albrecht, prom@berlin.ccc.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package org.openjavacard.tool.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.openjavacard.cap.file.CapFileApplet;
import org.openjavacard.cap.file.CapFileComponent;
import org.openjavacard.cap.component.CapComponentType;
import org.openjavacard.cap.file.CapFile;
import org.openjavacard.cap.file.CapFileImport;
import org.openjavacard.cap.file.CapFilePackage;
import org.openjavacard.cap.file.CapFileReader;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

@Parameters(
        commandNames = "cap-info",
        commandDescription = "CAP: Show information about a cap file"
)
public class CapInfo implements Runnable {

    @Parameter(
            description = "CAP files to show information about",
            required = true
    )
    List<File> capFiles;

    @Parameter(
            names = "--all",
            description = "Show all details"
    )
    boolean showAll = false;

    @Parameter(
            names = "--components",
            description = "Show details of all components"
    )
    boolean showComponents = false;

    @Override
    public void run() {
        PrintStream os = System.out;

        if(showAll) {
            showComponents = true;
        }

        for (File file : capFiles) {
            os.println();
            try {
                os.println("Reading CAP file " + file + "...");
                CapFile capFile = CapFileReader.readFile(file);
                os.println();

                os.println("CAP file " + file.getName());
                os.println("  Manifest version: " + capFile.getManifestVersion());
                os.println("  Created by: " + capFile.getCreatedBy());
                os.println();

                for (CapFilePackage capPkg : capFile.getPackages()) {
                    os.println("CAP package " + capPkg.getPackageAID());
                    os.println("  Creation time: " + capPkg.getCapCreationTime());
                    os.println("  Converter provider: " + capPkg.getConverterProvider());
                    os.println("  Converter version: " + capPkg.getConverterVersion());
                    os.println("  Format version: " + capPkg.getCapFileVersion());
                    os.println("  Integer required: " + capPkg.isIntSupportRequired());
                    os.println("  Package name: " + capPkg.getPackageName());
                    os.println("  Package version: " + capPkg.getPackageVersion());
                    os.println();

                    for (CapFileImport capImp : capPkg.getImports()) {
                        os.println("  Import " + capImp.getAID());
                        os.println("    Version: " + capImp.getVersion());
                    }
                    os.println();

                    for (CapFileApplet capApp : capPkg.getApplets()) {
                        String appName = capApp.getName();
                        String appVersion = capApp.getVersion();
                        os.println("  Applet " + capApp.getAID());
                        os.println("    Name: " + appName);
                        if(appVersion != null) {
                            os.println("    Version: " + appVersion);
                        }
                    }
                    os.println();

                    if(showComponents) {
                        for(CapComponentType type : CapComponentType.values()) {
                            CapFileComponent capCom = capPkg.getComponentByType(type);
                            if(capCom != null) {
                                os.println("  Component " + capCom.getFilename());
                                os.println("    Type: " + capCom.getName());
                                os.println("    Size: " + capCom.getSize());
                            }
                        }
                        os.println();
                    }
                }
            } catch (Exception ex) {
                throw new Error("Exception reading CAP file", ex);
            }
        }
    }

}

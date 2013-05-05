/* NativeCodeGeneration.java : Takes care of generating the final executable
 * Copyright (C) 2013  Paulo Pinto
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.progtools.l98.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.progtools.l98.CompilerError;
import org.progtools.l98.util.Pathnames;

/**
 * Takes care of generating the final executable.
 */
public class NativeCodeGeneration {
    /**
     * Compiles the generated code into native code.
     * @param fileName Name of the destination file.
     * @param asmFile File that contains the generated assembly code.
     * @param linkPath Extra link paths for the native code linker
     */
      public static void generateExecutable (String fileName, CompilerError error, String linkPath) {
        String exeName = getExecutableName(fileName);
        List<File> files = new ArrayList<>(3);
        files.add(new File(fileName));
    
        try {
            Runtime run = Runtime.getRuntime ();
            files.add(getRuntimeFile("/l98-rtl.s"));
            String osName = System.getProperty("os.name").toLowerCase().split(" ")[0];
            files.add(getRuntimeFile(String.format("/l98-%s.s", osName)));
            assembleFiles(run, files);
            linkFiles(run, exeName, files, linkPath);
        }
        catch (Exception ex) {
          error.message ("Error while generating executable", ex);
        }
      }
    
     private static File getRuntimeFile(String filename) throws IOException {
         Path runtimeFile = Files.createTempFile("l98", ".s");
         try (InputStream stream = Compiler.class.getResourceAsStream(filename)) {
             Files.copy(stream, runtimeFile, StandardCopyOption.REPLACE_EXISTING);
         }
         File runtime = runtimeFile.toFile();
         runtime.deleteOnExit();
         return runtime;
     }
      
      
    /**
     * Assembles the give files into their  object files.
     * @param run The runtime to execute the assembler from.
     * @param files The files to assemble.
     * @throws If any error happened while lauching the extern processes
     */
     private static void assembleFiles (Runtime run, List<File> files) throws Exception {
         for (File fd: files) {
            String asmFile = fd.toString();
            String objFile = asmFile.replace(".s", ".o");
            Process proc = run.exec (String.format("as --32 -o %s %s", objFile, asmFile));
            proc.waitFor ();
            if (proc.exitValue () != 0) {
                throw new Exception(String.format("Error assembling file %s", fd.toString()));
            }
         }
     }
     
    /**
     * Links the give file into an object file.
     * @param run The runtime to execute the assembler from.
     * @param files The files to assemble.
     * @param linkPath Extra link paths for the native code linker
     * @throws If any error happened while lauching the extern processes
     */
     private static void linkFiles (Runtime run, String executableName, List<File> files, String linkPath) throws Exception {
         StringBuilder cmd = new StringBuilder(1024); // 1kb should be enough
         
         cmd.append("ld  ");
         cmd.append(getLinkerOptions());
         cmd.append(getExecutableName(executableName));
         if (!linkPath.isEmpty()) {
             cmd.append(" -L");
             cmd.append(linkPath);
         }
         for (File fd: files) {
            cmd.append(" ");
            cmd.append(fd.toString().replace(".s", ".o"));
         }
         cmd.append(getSystemLibs());
         
        Process proc = run.exec (cmd.toString());
        proc.waitFor ();
        if (proc.exitValue () != 0) {
            throw new Exception("Error linking files");
        }
     }
     
     /**
      * @param filename The name of the file we are producing.
      * @return  The full pathname with the proper extension, depending on the OS.
      */
     private static String getExecutableName(String filename) {
         String osName = System.getProperty("os.name");
         return Pathnames.changeFileExtension(filename, (osName.startsWith("Windows") ? ".exe" : ""));
     }

     /**
      * @return  The set of linker options depending on the platform
      */
     private static String getLinkerOptions() {
         String osName = System.getProperty("os.name");
         return (osName.startsWith("Windows") ? " -o " : "-melf_i386 -o ");
     }

     /**
      * This method cannot be made part of getLinkerOptions() due to ordering issues on Windows.
      * @return  The set of libraries to link to.
      */
     private static String getSystemLibs() {
         String osName = System.getProperty("os.name");
         return (osName.startsWith("Windows") ? " -lkernel32 " : "");
     }
}

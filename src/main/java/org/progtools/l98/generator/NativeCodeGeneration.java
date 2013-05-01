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
     */
      public static void generateExecutable (String fileName, CompilerError error) {
        String exeName = getExecutableName(fileName);
        List<File> files = new ArrayList<>(3);
        files.add(new File(fileName));
    
        try {
            Runtime run = Runtime.getRuntime ();
            files.add(getRuntimeFile("/l98-rtl.s"));
            files.add(getRuntimeFile(String.format("/l98-%s.s", System.getProperty("os.name"))));
            assembleFiles(run, files);
            linkFiles(run, exeName, files);
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
     * @throws If any error happened while lauching the extern processes
     */
     private static void linkFiles (Runtime run, String executableName, List<File> files) throws Exception {
         StringBuilder cmd = new StringBuilder(1024); // 1kb should be enough
         
         cmd.append("ld -o ");
         cmd.append(executableName);
         cmd.append(" ");
         for (File fd: files) {
            cmd.append(fd.toString().replace(".s", ".o"));
         }
         
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
         return Pathnames.changeFileExtension(filename, (osName.equals("Windows") ? ".exe" : ""));
     }
}

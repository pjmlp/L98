/* Compiler.java : Represents the attributes associated with an identifier.
 * Copyright (C) 1999  Paulo Pinto, Pablo Tavares
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
package org.progtools.l98;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.progtools.l98.access.Access;
import org.progtools.l98.access.Builtin;
import org.progtools.l98.ast.ASTStat;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeArg;
import org.progtools.l98.type.TypeBool;
import org.progtools.l98.type.TypeFunc;
import org.progtools.l98.type.TypeInt;
import org.progtools.l98.type.TypeProc;
import org.progtools.l98.util.List;

import org.progtools.l98.generator.LVMCodeGenerator;
import org.progtools.l98.generator.X86CodeGenerator;


/**
 * Compiler's driver class.
 */
public class Compiler {
   
   /**
    * Command line version entry point.
    */
    public static void main(String args[]) {
      CompilerError error = new CompilerError ("L'98");
      if (args.length  < 1) {
        System.out.println ("\nUsage : Compiler [-e] [-s] <filename>");
	System.out.println ("\n -e -> Additionally to the bytecode, an executable is generated.");
        System.out.println ("\n -s -> When -e is given, the generated Assembly file is not deleted.");
        System.exit (1);
      }

      try {
        InputStream in = new FileInputStream (args [args.length - 1]);
	Parser parser = new Parser (in);
	parser.setCompilerError(error);
	ASTStat node = parser.start();
        
        if (node != null)
	  generateCode (node, error, args [args.length - 1], args [0].equals ("-e"));
      } 
      catch (ParseException | FileNotFoundException ex) {
	error.message (ex.getMessage ());
      }
    }

  /**
   * @return The compiler's initial environment with the pre-defined subroutines.
   */
  private static Environ initEnviron () {
    Environ env = new Environ (); 
    List args;
    Type subType;
    Attributes attr;
    Access subAccess;
    
    // Creates the readint function
    subType = new TypeFunc (new List (), new TypeInt (), 1);
    subAccess = new Builtin (Builtin.READINT);
    attr = new Attributes (subType, subAccess, false, false);
    env.update ("readint", attr);


    // Creates the readbool function
    subType = new TypeFunc (new List (), new TypeBool (), 1);
    subAccess = new Builtin (Builtin.READBOOL);
    attr = new Attributes (subType, subAccess, false, false);
    env.update ("readbool", attr);

    // Creates the printint procedure
    args = new List ();
    args.pushBack (new TypeArg (false, new TypeInt ()));
    
    subType = new TypeProc (args);
    subAccess = new Builtin (Builtin.PRINTINT);
    attr = new Attributes (subType, subAccess, false, false);
    env.update ("printint", attr);

    // Creates the printbool procedure
    args = new List ();
    args.pushBack (new TypeArg (false, new TypeBool ()));
    
    subType = new TypeProc (args);
    subAccess = new Builtin (Builtin.PRINTBOOL);
    attr = new Attributes (subType, subAccess, false, false);
    env.update ("printbool", attr);

    // Creates the println procedure
    subType = new TypeProc (new List ());
    subAccess = new Builtin (Builtin.PRINTLN);
    attr = new Attributes (subType, subAccess, false, false);
    env.update ("println", attr);
     
    return env;
  }
  
  /**
   * Generates the code from the AST.
   * The semantic analysis is integrated with the code generation.
   * @param tree  AST resulted fromt the syntax anaylis.
   * @param error Error reporting class.
   * @param sourceName File to compile.
   * @param asExe if true native code will be generated.
   * @param removeAsm if true and asExe was true as well, the temporary Assembly file will be kept.
   */ 
  private static void generateCode (ASTStat tree, CompilerError error, String sourceName,
				    boolean asExe, boolean removeAsm) {
    try {
      String fileName = sourceName.substring (sourceName.lastIndexOf (File.separatorChar) + 1, 
					sourceName.lastIndexOf ('.')) + ".s";
      FileOutputStream  out = new FileOutputStream (fileName);
        
      // Selects the type of code generation. 
      CodeGenerator gen;
      if (asExe) {
        gen = new X86CodeGenerator (out, 20);
        if (removeAsm)
          new File(fileName).deleteOnExit();
      }
      else {
        gen = new LVMCodeGenerator (out, 20);
      }
      
      generateEntryPoint (tree, gen);
      tree.transverse (initEnviron (), error, gen, 1, 1);
      generateExitPoint (gen);
      
      gen.close ();
      
      // If everything went well, time to generate the binary when requested.
      if (error.getErrorCount () == 0 && asExe) {
	  generateExecutable (sourceName, fileName, error);
      }
    }
    catch (IOException ex) {
      error.message(ex.getMessage());
      System.exit (3);
    }
  }
   
  /**
   * @param tree  Abstract sintax tree after the semantic processing was applied to it.
   * @param gen   Code generator to use as target.
   */
  private static void generateEntryPoint (ASTStat tree, CodeGenerator gen) throws IOException {
     gen.sectionBSS ();
     gen.globalMemSize (tree.alloc ());
     gen.sectionTEXT ();
     gen.insLabel ("P_START");
  }

  /**
   * @param gen   Code generator to use as target.
   */
  private static void generateExitPoint (CodeGenerator gen) throws IOException {
     gen.halt ();
  }

  
  /**
   * Compiles the generated code into native code.
   * @param fileName Name of the destination file.
   * @param asmFile File that contains the generated assembly code.
   */
  private static void generateExecutable (String fileName, String asmFile, CompilerError error) {
    String exeName = fileName.substring (fileName.lastIndexOf (File.separatorChar) + 1, fileName.lastIndexOf ('.'));

    try {
        Runtime run = Runtime.getRuntime ();
        Path runtimeLib = getL98Runtime();
        Process proc = run.exec (String.format("gcc -m32 -o %s %s %s", exeName, runtimeLib.toString(), asmFile));
        proc.waitFor ();
        if (proc.exitValue () != 0)
	 error.message ("Error while calling GCC");
    }
    catch (InterruptedException | IOException ex) {
      error.message (ex.getMessage ());
    }
  }
  
 private static Path getL98Runtime() throws IOException {
     Path runtimeFile = Files.createTempFile("l98", ".c");
     try (InputStream stream = Compiler.class.getResourceAsStream("/l98lib.c")) {
         Files.copy(stream, runtimeFile, StandardCopyOption.REPLACE_EXISTING);
     }
     //File runtime = runtimeFile.toFile();
     //runtime.deleteOnExit();
     return runtimeFile;
 }
  
}


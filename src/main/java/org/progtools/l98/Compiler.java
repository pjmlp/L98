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
import org.progtools.l98.ast.ASTStat;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.Environ;

import org.progtools.l98.generator.LVMCodeGenerator;
import org.progtools.l98.generator.NativeCodeGeneration;
import org.progtools.l98.generator.X86CodeGenerator;
import org.progtools.l98.table.EvironFactory;
import org.progtools.l98.util.Pathnames;


/**
 * Compiler's driver class.
 */
public final class Compiler {
   
   /**
    * Command line version entry point.
     * @param args Java command line arguments
    */
    public static void main(String args[]) {
      var error = new CompilerError ("L'98");
      if (args.length  < 1) {
        System.out.println ("\nUsage : Compiler [-e] [-k] [-L<path>] <filename>");
	System.out.println ("\n -e -> Additionally to the bytecode, an executable is generated.");
        System.out.println ("\n -k -> When -e is given, the generated Assembly file is not deleted.");
        System.out.println ("\n -L<path>] -> Provides extra paths for the linker to search for");
        System.exit (1);
      }

      try {
        InputStream in = new FileInputStream (args [args.length - 1]);
	Parser parser = new Parser (in);
	parser.setCompilerError(error);
	ASTStat node = Parser.start();
        
        if (node != null) {
          boolean asExe = args.length > 0 && args [0].equals ("-e");
          boolean keepAsm = args.length > 1 && args [1].equals ("-k");
          String linkPath = "";
          for (String arg: args) {
              if (arg.equals("-e")) {
                  asExe = true;
              } else if (arg.equals("-k")) {
                  keepAsm = true;
              } else if (arg.startsWith("-L")) {
                  linkPath = arg.substring(2);
              }
          }

          generateCode (node, error, args [args.length - 1], asExe, keepAsm, linkPath);
        }
      } 
      catch (ParseException | FileNotFoundException ex) {
	error.message (ex.getMessage ());
      }
    }

 
  /**
   * Generates the code from the AST.
   * The semantic analysis is integrated with the code generation.
   * @param tree  AST resulted fromt the syntax anaylis.
   * @param error Error reporting class.
   * @param sourceName File to compile.
   * @param asExe if true native code will be generated.
   * @param keepAsm if true and asExe was true as well, the temporary Assembly file will be kept.
   * @param linkPath Extra link paths for the native code linker
   */ 
  private static void generateCode (ASTStat tree, CompilerError error, String sourceName,
				    boolean asExe, boolean keepAsm, String linkPath) {
    try {
      var fileName = Pathnames.changeFileExtension(sourceName, ".s");
      var  out = new FileOutputStream (fileName); // The code generators will close the file.
      
      Environ env = EvironFactory.getEnvironment();
        
      // Selects the type of code generation. 
      if (asExe) {
          try(CodeGenerator gen = new X86CodeGenerator (out, 20, tree.alloc())) {
            if (!keepAsm)
              new File(fileName).deleteOnExit();
            tree.transverse (env, error, gen, 1, 1);
          }
          if (error.getErrorCount () == 0) {
        	     NativeCodeGeneration.generateExecutable (fileName, error, linkPath);
          }
      }
      else {
        try (CodeGenerator gen = new LVMCodeGenerator (out, 20, tree.alloc())) {
            tree.transverse (env, error, gen, 1, 1);
        }
      }
    }
    catch (Exception ex) {
      error.message(ex.getMessage());
      System.exit (3);
    }
  }
}

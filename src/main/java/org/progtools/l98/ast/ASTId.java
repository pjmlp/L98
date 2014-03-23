/* ASTId.java : AST node for identifiers.
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
package org.progtools.l98.ast;
import java.io.IOException;

import org.progtools.l98.table.Attributes;
import org.progtools.l98.CompilerError;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.BadVarException;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeFunc;
import org.progtools.l98.type.TypeProc;


/**
 * AST node for identifiers.
 */
public class ASTId extends ASTExp {
   
   /**
    * Identifier's name.
    */
  private final String m_name;

 /**
  * @param line where the expression was found.
  * @param name identifier's name.
  */
  public ASTId (int line, String name) {
   super (line);
   m_name = name;
  }  

  /**
   * Semantic analysis and code generation for general access.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @return the resulting expression type after the operation is applied
   */ 
  @Override
  public Type transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting) {
    Type result = null;
     
    try {
       Object temp = env.getVal (m_name);
       if (temp != null) {
         Attributes attr = (Attributes) temp;
         result = attr.getType ();
	 
	 if (result instanceof TypeProc || result instanceof TypeFunc) {
	   result = null;
	   err.message (getLine () + ": " + m_name + " is not reckognized as variable or constant.");
	 }
	 else // Generates the code for the variable's access
           attr.getAccess ().genLoadAccess (gen, nesting);
       }
    }
    catch (BadVarException e) {
       err.message (getLine() + ": The variable " + e.getMessage() + " is not declared");
    }
    catch (IOException e) {
       err.message ("Error while generating code");
    }

    return result; 
  }
  
  /**
   * Semantic analysis and code generation using the variable's address.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @return the resulting expression type after the operation is applied
   */ 
  public Type transverseAsArg (Environ env, CompilerError err, CodeGenerator gen, int nesting) {
    Type result;
     
    try {
       Attributes attr = (Attributes) env.getVal (m_name);
       result = attr.getType ();
       
       if (!attr.isVar ())
   	 err.message (getLine() + ": " + m_name + " is not a variable");
       else
         // Generates the code for the variable's access
         attr.getAccess ().genLoadAdrAccess (gen, nesting);
    }
    catch (BadVarException e) {
       err.message (getLine() + ": The variable " + e.getMessage() + " is not declared");
       result = null;
    }
    catch (IOException e) {
       err.message ("Error while generating code");
       result = null;
    }

    return result; 
  }  
}

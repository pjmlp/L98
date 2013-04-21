/* ASTNot.java : AST node for the boolean negation operator.
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

import org.progtools.l98.CompilerError;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeBool;


/**
 * AST node for the boolean negation operator.
 */
public class ASTNot extends ASTUnOp {

 /**
  * @param line where the expression was found.
  * @param node expression to invert
  */
  public ASTNot (int line, ASTExp node) { super (line, node);}
 
  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @return the resulting expression type after the operation is applied
   */
  public Type transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting) {
    Type node = getNode().transverse (env, err, gen, nesting);
    Type result;

    if (node != null)
      if (!(node instanceof TypeBool)) {
        err.message(getLine () + ": invalid type given to the not operator");
        result = null;
      }
      else {
        result = new TypeBool ();
	 
        // Code generation
	try {
	  gen.not ();
	}
	catch (IOException e) {
	  err.message ("Error while generating code");
	}	
      }
    else
      result = null;

    return result;
  }

}

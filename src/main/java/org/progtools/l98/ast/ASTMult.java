/* ASTMul.java : AST node for multiplication operations.
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
import org.progtools.l98.type.TypeInt;


/**
 * AST node for multiplication operations.
 */
public class ASTMult extends ASTBinOp {

 /**
  * @param line where the expression was found.
  * @param left left side expression of the operator
  * @param right right side expression of the operator
  */
  public ASTMult (int line, ASTExp left, ASTExp right) { super (line, left, right);}

  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @return the resulting expression type after the operation is applied
   */ 
  @Override
  public Type transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting) {
    Type left = getLeft().transverse (env, err, gen, nesting);
    Type right = getRight().transverse (env, err, gen, nesting);
    Type result;

    if (left != null && right != null) {
      if (!(left instanceof TypeInt) || !(right instanceof TypeInt)) {
        err.message(getLine () + ": multiplication applied to invalid types");
        result = null;
      }
      else {
        result = new TypeInt ();
	 
       // Code generation
	try {
	  gen.mul ();
	}
	catch (IOException e) {
	  err.message ("Error while generating code");
	}
      }
    }
    else
      result = null;

    return result;
  }

}

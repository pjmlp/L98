/* ASTMinus.java : AST node for the subtraction operation
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
 * AST node for the subtraction operation.
 */
public class ASTMinus extends ASTBinOp {

  /**
   * @param line where the expression was found.
   * @param left expression on the left side of the operator.
   * @param right expression on the right side of the operator.
   */
  public ASTMinus (int line, ASTExp left, ASTExp right) { super (line, left, right);}

  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err where to generate errors to.
   * @param gen interface with the compiler's backend.
   * @param nesting current static lexical level.
   * @return The L98 type of the expression.
   */
  @Override
   public Type transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting) {
    Type left = getLeft().transverse (env, err, gen, nesting);
    Type right = getRight().transverse (env, err, gen, nesting);
    Type result;

    if (left != null && right != null) {
      if (!(left instanceof TypeInt) || !(right instanceof TypeInt)) {
        err.message(getLine () + ": Invalid type used in the subtraction");
        result = null;
      }
      else {
        result = new TypeInt ();
	 
        // Code generation
	try {
	  gen.sub ();
	}
	catch (IOException e) {
	  err.message ("Error while generating the code");
	}
	
      }
    }
    else
      result = null;

    return result;
  }
}

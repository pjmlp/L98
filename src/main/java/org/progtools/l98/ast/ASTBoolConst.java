/* ASTBoolConst.java : AST node for boolean constants
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
 * AST node for boolean constants
 */
public class ASTBoolConst extends ASTExp {
 /**
  * Valor da constante
  */
  private boolean m_val;

  /**
   * @param line where the expression was found.
   * @param val  constant's value.
   */
  public ASTBoolConst (int line, boolean val) {
   super (line);
   m_val = val;
  }

   /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @return the type resulting from the expression. always an instance of TypeBool
   */  
  @Override
  public Type transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting) {
    try {
      gen.load (m_val ? 1 : 0);
    }
    catch (IOException e) {
      err.message ("Error while generating code");
    }

    return new TypeBool ();
  }

}


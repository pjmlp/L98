/* ASTStat.java : AST node for a statement
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
import org.progtools.l98.CompilerError;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.Environ;


/**
 * AST root node for a statement
 */
public abstract class ASTStat extends ASTNode {
  /**
   * @param line where the expression was found.
   */   
  protected ASTStat (int line) { super (line); }

  /**
   * @return Number of 32 bit units to allocate for the variables
   */
  public abstract int alloc ();


  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @param index location for the next variables.
   * @return true if the code block has a return statement
   */ 
  
  public abstract boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index);
}

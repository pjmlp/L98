/* ASTArg.java : AST node for function arguments
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
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;


/**
 * RAST node for function arguments
 */
public abstract class ASTArg extends ASTNode {
  /**
   * Argument's name
   */
  private String m_id;
   
  /**
   * L98 type
   */
  private Type   m_type;
  
  /**
   * @param line where the expression was found.
   * @param left expression on the left side of the operator.
   * @param right expression on the right side of the operator.
   */
  protected ASTArg (int line, String id, Type type) {
    super (line);
    m_id = id;
    m_type = type;
  }
  
  /**
   * @return Identifier used for the argument.
   */
  public String getId () { return m_id; }

   /**
    * @return L98 type used for the arugment.
    */
  public Type getType () { return m_type; }
   
  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err where to generate errors to.
   * @param gen interface with the compiler's backend.
   * @param nesting current static lexical level.
   * @return The L98 type of the expression.
   */  
  public abstract void transverse (Environ env, int nesting, int index);
   
}

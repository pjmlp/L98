/* ASTArgVal.java : AST node for arguments declared with val.
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
import org.progtools.l98.table.Attributes;
import org.progtools.l98.access.Frame;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;


/**
 * AST node for arguments declared with val.
 */
public class ASTArgVal extends ASTArg {
  /**
   * @param line where the expression was found.
   * @param id   identifier used for the argument.
   * @param type identifier's data type.
   */
  public ASTArgVal (int line, String id, Type type) { 
    super (line, id, type);
  }
   
  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param nesting current static lexical level.
   * @param index next slot available for variables.
   * @return The L98 type of the expression.
   */  
  @Override
  public void transverse (Environ env, int nesting, int index) {
     Frame frame = new Frame (nesting, index);
     Attributes attr = new Attributes (getType (), frame, false, true);
     
     env.update (getId (), attr);
  }
   
}


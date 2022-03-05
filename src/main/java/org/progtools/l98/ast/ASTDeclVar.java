/* ASTDeclVar.java : AST node for variable declarations
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
import org.progtools.l98.access.Access;
import org.progtools.l98.access.Frame;
import org.progtools.l98.access.Global;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;


/**
 * AST node for variable declarations
 */
public class ASTDeclVar extends ASTDecl {
  /**
   * Identifier's name.
   */
  private final String m_id;
   
  /**
   * Identifier's type.
   */
  private final Type m_type;
   
  /**
   *Initialization expression.
   */
  private final ASTExp  m_exp;

 /**
  * @param line where the expression was found.
  * @param id   variable's name.
  * @param type return type.
  * @param exp initialization expression
  */
  public ASTDeclVar (int line, String id, Type type, ASTExp exp) {
    super (line);
    m_id = id;
    m_type = type;
    m_exp = exp;
  }

  /**
   * @return Value's name.
   */
  public String getId () { return m_id; }

  /**
   * @return Value's type.
   */
  public Type getType () { return m_type; }

  /**
   * @return Initialization expression.
   */
  public ASTExp getExp () { return m_exp; }

   
  /**
   * @return Number of 32 units to allocate for the variable.
   */
  @Override
  public int alloc () { return 1; }

  
  /**
   * Validates if the type of the initialization expression is compatible with
   * the value's type.
   * @param expType Expression's type.
   * @param err used for error messages.
   */
  private boolean typeCheck (Type expType, CompilerError err) {
    boolean retValue = true;
     
    if (expType != null && !m_type.equals (expType)) {
       err.message (getLine() + ": Invalid expression type assigned to variable");
       retValue = false;
    }
     
    return retValue;
  }
   
  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @param index next slot available for variables.
   * @return the next available slot for the following instructions.
   */   
  @Override
  public int transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
    Attributes attr = null;
    try {
      Type expType = m_exp.transverse (env, err, gen, nesting);

     	
      if (typeCheck (expType, err)) {
        Access varAccess;
	
	if (nesting == 1)                // Global variables don't have nesting
	  varAccess = new Global (index);
	else
	  varAccess = new Frame (nesting, index);
	
        // Generates the code for storing the variable's value
        varAccess.genStoreAccess (gen, nesting);
	attr = new Attributes (m_type, varAccess, true, false);
      }
    }
    catch (IOException e) {
      err.message ("Error while generating code");
    }
 
    env.update (m_id, attr);
    return index + 1;
  }
}


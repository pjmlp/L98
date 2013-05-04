/* ASTAssign.java : AST node for assignments.
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
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;



/**
 * AST node for assignments
 */
public class ASTAssign extends ASTStat {
  /**
   * lvalue
   */
  private String m_id;
   
   /**
    * expression to be assigned.
    */
  private ASTExp m_exp;

  /**
   * @param line  where the expression was found.
   * @param id    variable's name.
   * @param exp   expression to assign to the variable.
   */ 
  public ASTAssign (int line, String id, ASTExp exp) {
    super (line); 
    m_id = id;
    m_exp = exp;
  }

  /**
   * @return number of 32bit units to allocate for the variable.
   */
  public int alloc () { return 0; }
   
   
  /**
   * Validates if the expression can be assigned to the given variable.
   * @param varType variable's type.
   * @param expType expression type.
   * @param err used for compiler error messages.
   */
  private boolean typeCheck (Type varType, Type expType, CompilerError err) {
    boolean retValue = true;
     
    if (varType != null && expType != null && !varType.equals (expType)) {
       err.message (getLine() + ": Different types being assigned");
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
   * @param index Pnext slot available for variables.
   * @return true in case the code contains a return instruction
   */
  @Override
  public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
     try {
       Object temp  = env.getVal (m_id);
       Type expType = m_exp.transverse (env, err, gen, nesting);

	if (temp != null) {
	   Attributes attr = (Attributes) temp;
	   
	   if (typeCheck (attr.getType (), expType, err))
	     if (!attr.getIsVar ())
	       err.message (getLine() + ": " + m_id + " is not a variable");
	     else
	       attr.getAccess ().genStoreAccess (gen, nesting);
	}
     }
     catch (org.progtools.l98.table.BadVarException e) {
	err.message (getLine() + ": " + e.m_id + " is not declared");
     }
     catch (IOException e) {
      err.message ("Error while generating code");
    }
    return false;
  }  
}


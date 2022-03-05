/* ASTReturn.java : AST node for the return statement.
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
import org.progtools.l98.table.BadVarException;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeFunc;


/**
 * AST node for the return statement.
 */
public class ASTReturn extends ASTStat {
   /**
    * Expression to return as value from the statement.
    */
  private final ASTExp m_exp;

  /**
   * @param line where the expression was found.
   * @param exp  expression to return
   */
  public ASTReturn (int line, ASTExp exp) {
   super (line);
   m_exp = exp;
  }


  /**
   * @return number of 32 bit slots required to store the variable.
   */
  @Override
  public int alloc () { return 0; }
  
  /**
   * Validates the return type.
   * @param funcType Type being returned by the function.
   * @param expType Type being returned by the return expression.
   * @param err used for error messages.
   * @return true if the return expression type is compatible with the function's
   */  
  private boolean typeCheck (Type funcType, Type expType, CompilerError err) {
    boolean retValue = true;
    
    if (!(funcType instanceof TypeFunc)) {
      err.message (getLine () + ": return used outside a function body");
      retValue = false;
    }
    else {
      TypeFunc fType = (TypeFunc) funcType;
      if (!fType.getReturnType ().equals (expType)) {
	err.message (getLine () + ": the type of the return expression is not compatible with the function's result");
	retValue = false;
      }
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
  public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
    boolean hasReturn = true;
     
    try {
      Type expType = m_exp.transverse (env, err, gen, nesting);
      Object temp = env.getVal ("$LastSub$");
      TypeFunc funcType = null;
      
      if (temp != null)
        if (temp instanceof TypeFunc)
	  funcType = (TypeFunc) temp;
        else {
          err.message (getLine () + ": return used outside a function body");
          hasReturn = false; // Behaves as if no return has been used.
	}
      
      if (funcType != null && expType != null && typeCheck (funcType, expType, err)) {
	// generates the code related to the return
	
        gen.storeVar (0, funcType.getReturnOffset ());
        gen.ret ();
      }
      
           
    }
    catch (BadVarException e) {
      err.message (getLine () + ": return used outside a function body");
      hasReturn = false; // Behaves as if no return has been used.
    }
    catch (IOException e) {
      err.message ("Error while generating code");
    }
    return hasReturn;
  }
}

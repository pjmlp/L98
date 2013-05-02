/* ASTProcCall.java : AST node for procedure calls
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
import java.util.Enumeration;

import org.progtools.l98.table.Attributes;
import org.progtools.l98.CompilerError;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.BadVarException;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeArg;
import org.progtools.l98.type.TypeProc;


/**
 * AST node for procedure calls.
 */
public class ASTProcCall extends ASTStat {
   /**
    * Argument list used in the call.
    */
  private ASTElementList m_elementList;
   
   /**
    * Procedure's name.
    */
  private String m_id;

 /**
  * @param line where the expression was found.
  * @param id   function's name.
  * @param elementList argument list used in the call
  */
  public ASTProcCall (int line, String id, ASTElementList elementList) {
    super (line);
    m_elementList = elementList;
    m_id = id;
  }

  /**
   * @return number of 32 bit slots required to store the variable.
   */
  public int alloc () { return 0; }
  
  
  /**
   * Validates the function arguments.
   * @param env current environment.
   * @param args arguments used in the call
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   */
  private void pushArgs (Environ env, Enumeration args, CompilerError err, CodeGenerator gen,
			 int nesting)  {
    int argCount = m_elementList.length ();
    ASTExp exp;
    TypeArg argType;
    Enumeration exps = m_elementList.elementsBackward ();
    Type expType = null;
     
    while (exps.hasMoreElements () && args.hasMoreElements ()) {
      exp = (ASTExp) exps.nextElement ();
      argType = (TypeArg) args.nextElement ();
      	  
      
      if (argType.getIsVar ())
	if (!(exp instanceof ASTId))
	  err.message (getLine () + ": The " + argCount + " parameter must be a variable");
        else {
	  ASTId id = (ASTId) exp;
	  expType = id.transverseAsArg (env, err, gen, nesting);
	}
      else
        expType = exp.transverse (env, err, gen, nesting);
      
      
	  
      if (expType != null && !argType.getType ().equals (expType))
	err.message (getLine () + ": The " + argCount + " parameter is invalid");
	  
      argCount--;
    }
	
    if (exps.hasMoreElements ())
      err.message (getLine () + ": Too many parameters");
    else if (args.hasMoreElements ())
      err.message (getLine () + ": Parameters missing");
  }
  
   /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @param index Pnext slot available for variables.
   * @return the next available slot for the following instructions.
   */   
  public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
    try {
      Object temp;
      Enumeration exps, args;
      ASTExp exp;
      Type ty, expType;
      
      temp = env.getVal (m_id);
      if (temp != null) {
        Attributes attr = (Attributes) temp;
	
        if (attr.getType () instanceof TypeProc) {
	  TypeProc procType = (TypeProc) attr.getType ();
	  pushArgs (env, procType.elementsBackward (), err, gen, nesting);
	  
	  // code generation
          attr.getAccess ().genCallAccess (gen, nesting, procType.length ());
	}
        else
	 err.message (getLine () + ": The identifier " + m_id + " is not a procedure");
      }
      
    }
    catch (BadVarException e) {
      err.message (getLine () + ": The identifier " + e.m_id + "is not declared");
    }
    catch (IOException e) {
      err.message ("Error while generating code");
    }
    return false;
  }
}

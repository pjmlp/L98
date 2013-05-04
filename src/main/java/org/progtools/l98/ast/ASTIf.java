/* ASTIf.java : AST node for if statements
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
import org.progtools.l98.generator.LabelGenerator;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeBool;


/**
 * AST node for if statements
 */
public class ASTIf extends ASTStat {
  /**
   * Statement for the then branch
   */
  private ASTStat m_statThen;
   
  /**
   * Statement for the else branch.
   * It is null if no else branch is present.
   */
  private ASTStat m_statElse;
   
  /**
   * conditional expression
   */
  private ASTExp m_exp;

  /**
   * @param line where the expression was found.
   * @param exp  conditional expression used for the if statement
   * @param statThen statments used in the then branch
   * @param statElse statments used in the else branch, it may be null
   */
  public ASTIf (int line, ASTExp exp, ASTStat statThen, ASTStat statElse) { 
    super (line);
    m_exp = exp;
    m_statThen = statThen;
    m_statElse = statElse;
  }

  /**
   * @return Number of 32 bit units to allocate for the variables
   */
  @Override
  public int alloc () {
    int retValue;
    
    retValue =  m_statThen.alloc ();
    if (m_statElse != null) {
      int temp = m_statElse.alloc ();
      if (temp > retValue)
	retValue = temp;
    }
    
    return retValue;
  }
    
  /**
   * Validates if the expression type is valid.
   * @param expType Type of the conditional expression
   * @param err used for error messages.
   * @return true if the expression is boolean
   */
  private boolean typeCheck (Type expType, CompilerError err) {
    boolean retValue = true;
    
    
    if (expType != null && !(expType instanceof TypeBool)) {
      err.message (getLine() + ": if statement requires a boolean expression");
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
   * @param index location for the next variables.
   * @return true if the code block has a return statement
   */ 
  @Override
  public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
    boolean hasReturn = false;
     
    try {
      String elseLabel = LabelGenerator.getLabel ();
      String endLabel = LabelGenerator.getLabel ();
    
      Type expType = m_exp.transverse (env, err, gen, nesting);
    
      // validates the codition and generates the corresponding code
      if (typeCheck (expType, err))
        if (m_statElse != null)
          gen.jpc (0, elseLabel);
        else
          gen.jpc (0, endLabel);
      
       hasReturn = m_statThen.transverse(env, err, gen, nesting, index);
    
       // tries to optimize the code generation for the cases where an else exists
       // but return might have been used
       if (m_statElse != null) {
	  // generates the code taking into regard the code already generated by the then branch
         if (!hasReturn)
	   gen.jmp (endLabel); 
	 
          gen.insLabel (elseLabel);

          hasReturn = m_statElse.transverse(env, err, gen, nesting, index) && hasReturn;
        }
      
        // generates the exit label for the if
        gen.insLabel (endLabel);
    }
    catch (IOException e) {
      err.message ("Error while generating code");
    }
    return hasReturn;
  }
}




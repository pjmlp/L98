/* ASTWhile.java : AST node for while loops
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
 * AST node for while loops.
 */
public class ASTWhile extends ASTStat {
  /**
   * Instructions part of the while loop.
   */
  private ASTStat m_stat;
   
  /**
   * Expression for the loop condition .
   */
  private ASTExp m_exp;

    
 /**
  * @param line where the expression was found.
  * @param exp  expression for the loop condition
  * @param stat code statements declared in the block
  */
  public ASTWhile (int line, ASTExp exp, ASTStat stat) {
    super (line); 
    m_exp = exp;
    m_stat = stat;
  }

  /**
   * @return Number of 32 bits words required for variables.
   */
  @Override
  public int alloc () { return m_stat.alloc (); }
  
  
  /**
   * Validates if the expression type is valid.
   * @param expType Type of the conditional expression
   * @param err used for error messages.
   * @return true if the expression is boolean
   */  
  private boolean typeCheck (Type expType, CompilerError err) {
    boolean retValue = true;
    
    if (expType != null && !(expType instanceof TypeBool))
      err.message (getLine() + ": condition not valid on while");
    
    return retValue;
  }
   
  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @return true if the code block has a return statement
   */ 
  @Override
  public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
    try {
      String condLabel = LabelGenerator.getLabel ();  // label for the loop condition
      String endLabel = LabelGenerator.getLabel ();   // lable for the exit statement
    
      // Generate the code for the loop condition
      gen.insLabel (condLabel);
      Type expType = m_exp.transverse (env, err, gen, nesting);
     
      // If the condition is valid, generate the proper exit code
      if (typeCheck (expType, err))
         gen.jpc (0, endLabel);
     
      m_stat.transverse(env, err, gen, nesting, index);
    
      // Generate the while exit code
      gen.jmp (condLabel);
      gen.insLabel (endLabel);
    }
    catch (IOException e) {
      err.message ("Error while generating the code");
    }
     
    return false;
  }
}

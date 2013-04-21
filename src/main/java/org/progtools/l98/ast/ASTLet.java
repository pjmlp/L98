/* ASTLet.java : AST node for code blocks
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



/**
 * AST node for code blocks.
 */
public class ASTLet extends ASTStat {
   /**
    * Variables and constants declared in the block.
    */
  private ASTDeclList m_varValDecls;

   /**
    * Functions and procedures declared in the block.
    */
  private ASTDeclList m_funProcDecls;
   
   /**
    *  Code statements used in the block.
    */
  private ASTStat     m_stat;

   
 /**
  * @param line where the expression was found.
  * @param varValDecls  variable and constant declarations
  * @param funProcDecls functions and procedures declarations
  * @param stat code statements declared in the block
  */  
  public ASTLet (int line, ASTDeclList varValDecls, ASTDeclList funProcDecls, ASTStat stat) {
    super (line);
    m_varValDecls = varValDecls;
    m_funProcDecls = funProcDecls;
    m_stat = stat;
  }
  
  /**
   * @return Number of 32 bits words required for variables.
   */
  public int alloc () { return m_varValDecls.alloc () + m_funProcDecls.alloc () + m_stat.alloc (); }

  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @return true if the code block has a return statement
   */ 
  public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index)  {
    boolean hasReturn = false;
    try {
      int nextIndex = m_varValDecls.transverse (env, err, gen, nesting, index);
    
      if (!m_funProcDecls.isEmpty ()) {
         // Code to jump over code related to the internal functions and procedures
         String bodyLabel = LabelGenerator.getLabel ();
         gen.jmp (bodyLabel);

         nextIndex = m_funProcDecls.transverse (env, err, gen, nesting, nextIndex);
         gen.insLabel (bodyLabel);
      }
    
      hasReturn = m_stat.transverse (env, err, gen, nesting, nextIndex);
      
      m_funProcDecls.clearEnviron (env);
      m_varValDecls.clearEnviron (env);
    }
    catch (IOException e) {
      err.message ("Error while generating the code");
    }
    return hasReturn;
  }
  
}

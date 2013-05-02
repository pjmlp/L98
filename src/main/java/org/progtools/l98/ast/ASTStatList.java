/* ASTStatList.java : AST node for statement lists
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.progtools.l98.CompilerError;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.table.Environ;


/**
 * AST node for statement lists as if it was a single instruction.
 */
public class ASTStatList extends ASTStat {
   /**
    * Instructions
    */
  private List<ASTStat> m_stats;

  /**
   * @param line where the expression was found.
   */   
  public ASTStatList (int line) {
    super (line);
    m_stats = new LinkedList<> ();
  }

  /**
   * @param stat instruction to add to the block
   */
  public void add (ASTStat stat) { m_stats.add (stat); }

  
  /**
   * @return Number of 32 bit units to allocate for the variables
   */
  public int alloc () {
    int retValue = 0;

    try {
       Iterator<ASTStat> iter = m_stats.iterator();
       ASTStat stat;
       int temp;

       while (iter.hasNext()) {
        stat = (ASTStat) iter.next ();
        temp = stat.alloc ();
	if (temp > retValue)
	   retValue = temp;
       }
     }
     catch (Exception e) {
       e.printStackTrace ();
       System.exit (2);
     }    
    
    
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
   public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
     boolean hasReturn = false;
     try {
       Iterator<ASTStat> iter = m_stats.iterator();
       ASTStat stat;

       while (iter.hasNext()&& !hasReturn) {
        stat = iter.next ();
        hasReturn = stat.transverse (env, err, gen, nesting, index);
       }
       
       if (hasReturn && iter.hasNext()) {
	  stat = (ASTStat) iter.next ();
	  err.warning (stat.getLine () + ": Superflus code from this line onwards, return has been found in the previous lines");
       }
     }
     catch (Exception e) {
       err.message("Error while generating code", e);
       System.exit (2);
     }
     return hasReturn;
  }
}


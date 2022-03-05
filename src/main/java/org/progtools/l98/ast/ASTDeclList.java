/* ASTDeclList.java : AST node for a list of type declarations
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
 * AST node for a list of type declarations
 */
public class ASTDeclList extends ASTDecl {
  /**
   * Declarations list
   */
  private final List<ASTDecl> m_decls;

  /**
   * @param line where the expression was found.
   */
  public ASTDeclList (int line) {
    super (line);
    m_decls = new LinkedList<> ();
  }

  
   /**
    * @param decl Adds a new declaration to the list
    */
  public void add (ASTDecl decl) { m_decls.add (decl); }

  /**
   * @return Number of 32 bit slots to allocate
   */
  @Override
  public int alloc () {
    int retValue = 0;

    try {
       Iterator<ASTDecl> iter = m_decls.iterator ();
       ASTDecl decl;

       while (iter.hasNext()) {
        decl = iter.next ();
        retValue += decl.alloc ();
       }
     }
     catch (Exception e) {
       //TODO: err.message("Error while generating code, terminating", e);
       System.exit (2);
     }    
    
    
    return retValue;
  }

   /**
    * @return true, if the list is empty.
    */
  public boolean isEmpty () { return m_decls.isEmpty (); }
   
   /**
   * Semantic analysis and code generation.
   * The declarations are added to the given environment.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @param index next slot available for variables.
   * @return the next available slot for the following instructions.
   */  
  @Override
  public int transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
     try {
       Iterator<ASTDecl> iter = m_decls.iterator ();
       ASTDecl decl;

       while (iter.hasNext()) {
        decl = iter.next ();
        index = decl.transverse (env, err, gen, nesting, index);
       }
     }
     catch (Exception e) {
       err.message("Error while generating code, terminating", e);
       System.exit (2);
     }
    return index;
  }

   /**
    * Removes the node declarations from the given environment.
    * Assumes the last set of declarations are the ones inserted by this node,
    * in a LIFO way.
    * @param env Current environment.
    */
  public void clearEnviron (Environ env) {
      for (ASTDecl m_decl : m_decls) {
          env.removeLast ();
      }
  }
}


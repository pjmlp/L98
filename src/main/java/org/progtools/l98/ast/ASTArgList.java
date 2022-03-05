/* ASTArgList.java : AST node for arguments' list.
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


import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import org.progtools.l98.table.Environ;


/**
 * NAST node for arguments list.
 */
public class ASTArgList extends ASTNode {
  /**
   * The arguments to keep around.
   */
  private final Deque<ASTArg> m_args;

  /**
   * Default constructor
   */
  public ASTArgList () {
    super (0);
    m_args = new LinkedList<> ();
  }

  /**
   * Adds an argument to the list.
     * @param arg an procedure/function argument
   */
  public void add (ASTArg arg) { m_args.add(arg); }

  /**
   * @return Number of arguments currently on the list.
   */
  public int length () { return m_args.size(); }
  
  /**
   * @return An enumeration for the list arguments
   */
  public Iterator<ASTArg> elements () { return m_args.iterator(); }
  

   /**
    * @return A reversed enumeration for the list arguments
    */
  public Iterator<ASTArg> elementsBackward () { return m_args.descendingIterator(); }  
  
  /**
   * Adds to the current environment the declarations existing in the list.
   * @param env current environment.
   * @param nesting current static lexical level.
   */   
  public void fillEnviron (Environ env, int nesting) {
    int index = -1;  
    
    for (ASTArg arg: m_args) {
       arg.transverse (env, nesting, index);
      index--;       
    }
  }

  /**
   * Removes from the environment all declarations existing in the list.
   * It assumes that the last n declarations  of the current environment belong
   * to the declarations done by fillEnviron.
   * @param env 
   */
  public void clearEnviron (Environ env) {
    for (int i = 0; i < m_args.size (); i++)
      env.removeLast ();
  }
}


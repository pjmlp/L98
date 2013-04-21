/* ASTElementList.java : AST node for an expression list.
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
import java.util.Enumeration;

import org.progtools.l98.util.List;


/**
 * AST node for an expression list.
 */
public class ASTElementList extends ASTNode{
  /**
   * Expressions
   */
  private List m_elements;

  public ASTElementList () {
    super (0);
    m_elements = new List ();
  }

   /**
    * @param element Expression to add to the list
    */
  public void add (ASTExp element) { m_elements.pushBack (element); }

   /**
    * @return An enumerator over the available expressions.
    */
  public Enumeration elements () { return m_elements.elements (); }
  
   /**
    * @return An enumerator over the available expressions in reversed order.
    */
  public Enumeration elementsBackward () { return m_elements.elementsBackward (); }
  
   /**
    * @return Amount of available expressions in the list.
    */
  public int length () { return m_elements.length (); }
  
}


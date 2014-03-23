/* ASTUpOp.java : AST root node for the unary operators.
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

/**
 * AST root node for the unary operators.
 */
public abstract class ASTUnOp extends ASTExp {
  /**
   * child node.
   */
  private final ASTExp m_node;

  /**
   * @param line where the expression was found.
   * @param node child node.
   */
  ASTUnOp (int line, ASTExp node) { 
    super (line);
    m_node = node;
  }

  /**
   * @return the child node
   */
  public ASTExp getNode () { return m_node; }
}

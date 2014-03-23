/* ASTBinOp.java : AST node for binary operations.
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
 * AST node for binary operations.
 */
public abstract class ASTBinOp extends ASTExp {
  /**
   * left tree structure
   */
  private final ASTExp m_left;

  /**
   * right tree structure
   */
  private final ASTExp m_right;

  /**
   * @param line where the expression was found.
   * @param left  left tree structure
   * @param right right tree structure
   */
  public ASTBinOp (int line, ASTExp left, ASTExp right) {
   super (line);
   m_left = left; m_right = right;
  }

  /**
   * @return left tree structure root node.
   */
  public ASTExp getLeft () { return m_left; }
   
  /**
   * @return right tree structure root node.
   */
  public ASTExp getRight () { return m_right; }
}

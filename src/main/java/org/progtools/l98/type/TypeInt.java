/* TypeInt.java : Represents integer types.
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
package org.progtools.l98.type;

/**
 * Represents the type int.
 */
public class TypeInt extends Type {

  /**
   * Makes sure two TypeInt instances can be properly compared.
   * @param other object to compare to.
   * @return true if objects are instances of TypeInt
   */
    @Override
  public boolean equals (Object other) { return other instanceof TypeInt; }
  
}

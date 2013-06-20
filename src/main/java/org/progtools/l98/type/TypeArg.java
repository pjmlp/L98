/* TypeArg.java : Represents argument types.
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

import java.util.Objects;


/**
 * Used to represent arguments of a given type.
 */
public class TypeArg extends Type {
  /**
   * Indicates if the argument was given by reference.
   */
  private boolean m_isVar;

  /**
   * The data type used to represent the argument.
   */
  private Type m_type;

  /**
   * @param isVar true if the argument is given by reference.
   * @param type  data type of the argument.
   */
  public TypeArg (boolean isVar, Type type) {
     m_isVar = isVar;
     m_type = type;
  }

  
  /**
   * @return argument's data type.
   */
  public Type getType ()  { return m_type; }


  /**
   * @return  true if this instance represents a variable argument.
   */
  public boolean isVar ()  { return m_isVar; }
    
  /**
   * Makes sure two TypeArg instances can be properly compared.
   * @param other object to compare to.
   * @returns true if objects are instances of TypeArg and deep comparisasion matches.
   */
  @Override
  public boolean equals (Object other) { 
    
    if (!(other instanceof TypeArg))
      return false;
     
    TypeArg otherArg = (TypeArg) other;
    
    return m_isVar == otherArg.m_isVar && m_type.equals (otherArg.m_type);
  }   

    /**
   * The usal hashing companion method to equals().
   */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.m_isVar ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.m_type);
        return hash;
    }
}

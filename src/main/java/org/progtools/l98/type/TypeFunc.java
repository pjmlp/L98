/* TypeFunc.java : Represents function data types.
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
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;



/**
 * Data type for functions.
 */
public class TypeFunc extends Type {
  /**
   * Data types used by the arguments.
   */
  private Deque<TypeArg> m_args;
  
  /**
   * Data types used by the resulted result.
   */
  private Type m_retType;

  /**
   * Offset where to store the function's result on the stack.
   */
  private int m_retOffset;
  
  /**
   * @param args Types used by the function arguments.
   * @param retType Return type of the function.
   * @param retOffset Offset inside the static frame link.
   */
  public TypeFunc (Deque<TypeArg> args, Type retType, int retOffset) {
    m_args = args;
    m_retType = retType;
    m_retOffset = retOffset;
  }

  /**
   * @return Data type of the function's result.
   */
  public Type getReturnType () { return m_retType; }
  
  /**
   * @return Stack's offset where to place the function's result.
   */
   public int getReturnOffset () { return m_retOffset; }
  

  /**
   * @returns An enumeration of the function's arguments.
   */
  public Iterator<TypeArg> elements () { return m_args.iterator(); }

  /**
   * @returns A reversed enumeration of the function's arguments.
   */
  public Iterator<TypeArg> elementsBackward () { return m_args.descendingIterator(); }


   /**
    * @return Amount of parameters expected by the function.
    */
  public int length () { return m_args.size(); }
  
  /**
   * Makes sure two TypeFunc instances can be properly compared.
   * @param other object to compare to.
   * @returns true if objects are instances of TypeFunc
   */
  @Override
  public boolean equals (Object other) { 
    
    if (!(other instanceof TypeFunc))
      return false;
    
    return m_args.equals (((TypeFunc)other).m_args);
    
   }   

  /**
   * The usal hashing companion method to equals().
   */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.m_args);
        return hash;
    }

  
}

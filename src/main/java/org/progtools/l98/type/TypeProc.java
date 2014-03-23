/* TypeProc.java : Represents procedures data types.
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
 * Data type for procedures.
 */
public class TypeProc extends Type {
  /**
   * Data types used by the arguments.
   */
  private final Deque<TypeArg> m_args;
  
  /**
   * @param args List of types used by the procedure arguments.
   */
  public TypeProc (Deque<TypeArg> args) { m_args = args; }

  /**
   * @returns An enumeration of the procedure's arguments.
   */
  public Iterator<TypeArg> elements () { return m_args.iterator(); }

  /**
   * @returns A reversed enumeration of the procedure arguments.
   */
  public Iterator<TypeArg> elementsBackward () { return m_args.descendingIterator(); }

   /**
    * @return Amount of parameters expected by the procedure.
    */
  public int length () { return m_args.size (); }
  
  /**
   * Makes sure two TypeProc instances can be properly compared.
   * @param other object to compare to.
   * @returns true if objects are instances of TypeInt
   */
  @Override
  public boolean equals (Object other) { 
    if (!(other instanceof TypeProc))
      return false;
    
    return m_args.equals (((TypeProc)other).m_args);
    
   }   

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.m_args);
        return hash;
    }
  
}

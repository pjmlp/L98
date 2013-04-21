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
import java.util.Enumeration;

import org.progtools.l98.util.List;


/**
 * Data type for functions.
 */
public class TypeFunc extends Type {
  /**
   * Data types used by the arguments.
   */
  private List m_args;
  
  /**
   * Data types used by the resulted result.
   */
  private Type m_retType;

  /**
   * Offset where to store the function's result on the stack.
   */
  private int m_retOffset;
  
  /**
   * @param args Lista de tipos a que se referem os argumentos
   * @param retType Tipo de retorno da funcao
   * @param retOffset Deslocamento da posicao onde colocar o resultado da funcao    
   */
  public TypeFunc (List args, Type retType, int retOffset) {
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
  public Enumeration elements () { return m_args.elements (); }

  /**
   * @returns A reversed enumeration of the function's arguments.
   */
  public Enumeration elementsBackward () { return m_args.elementsBackward (); }


   /**
    * @return Amount of parameters expected by the function.
    */
  public int length () { return m_args.length (); }
  
  /**
   * Makes sure two TypeFunc instances can be properly compared.
   * @param other object to compare to.
   * @returns true if objects are instances of TypeInt
   */
  @Override
  public boolean equals (Object other) { 
    
    if (!(other instanceof TypeFunc))
      return false;
    
    return m_args.equals (((TypeFunc)other).m_args);
    
   }   

  
}

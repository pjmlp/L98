/* Builtin.java : Access to builtin data types.
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
package org.progtools.l98.access;
import java.io.IOException;

import org.progtools.l98.generator.CodeGenerator;

/**
 * Access to builtin data types.
 */
public class Builtin extends Access {
  /**
   * printint procedure
   */
  public static final int PRINTINT = 0;
  
  /**
   * readint function
   */
  public static final int READINT = 1;
  
  /**
   * printbool procedure
   */
  public static final int PRINTBOOL = 2;
  /**
   * readbool function
   */
  public static final int READBOOL = 3;

  /**
   * println procedure
   */
  public static final int PRINTLN = 4;

  /**
   * Subrotina que este acesso representa
   */
  private int m_subrotine;
  
  /**
   * @param subrotine Type of function represented by this memory access.
   */
  public Builtin (int subrotine) { m_subrotine = subrotine; }


   public void genLoadAccess (CodeGenerator gen, int nesting) throws IOException {
      System.err.println ("Internal Compiler Error, invalid access");
      System.exit (1);
   }


   public void genLoadAdrAccess (CodeGenerator gen, int nesting) throws IOException {
      System.err.println ("Internal Compiler Error, invalid access");
      System.exit (1);
   }


   public void genStoreAccess (CodeGenerator gen, int nesting)  throws IOException {
      System.err.println ("Internal Compiler Error, invalid access");
      System.exit (1);
   }


   public void genCallAccess (CodeGenerator gen, int nesting, int argCount)  throws IOException {
     gen.csp (m_subrotine);
   }
}

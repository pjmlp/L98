/* Frame.java : Base class representing the activation frame for subroutines.
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
 * Base class representing the activation frame for subroutines.
 */
public class Frame extends Access {
   /**
    *  Level where the variable was declared.
    */
   private final int m_levels;

   /**
    * The offset inside the activation frame.
    */
   private final int m_offset;
   
   /**
    * @param levels level where the variable was declared.
    * @param offset the offset inside the activation frame.
    */
   public Frame (int levels, int offset) {
      m_levels = levels;
      m_offset = offset;
   }

   
   @Override
   public void genLoadAccess (CodeGenerator gen, int nesting) throws IOException {
     gen.loadVar (nesting - m_levels, m_offset);
   }


   @Override
   public void genLoadAdrAccess (CodeGenerator gen, int nesting) throws IOException {
     gen.loadVarA (nesting - m_levels, m_offset);
   }
  

   @Override
   public void genStoreAccess (CodeGenerator gen, int nesting)  throws IOException {
      gen.storeVar (nesting - m_levels, m_offset);
   }


   @Override
   public void genCallAccess (CodeGenerator gen, int nesting, int argCount)  throws IOException {
     System.err.println ("Internal Compiler Error, invalid access");
     System.exit (2);
   }
  
   /**
    * @return Number of levels to go up in the activation record.
    */
   public int getLevels () {
      return m_levels;
   }
   
   /**
    * @return The offset inside the activation frame.
    */
   public int getOffset () {
      return m_offset;
   }
}

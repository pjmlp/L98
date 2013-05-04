/* Global.java : Class dealing with global memory access.
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
 * Class dealing with global memory access.
 */
public class Global extends Access {
   /**
    * Offset at the global memory space.
    */
   private int m_offset;
   
   /**
    * @param offset Offset in the global memory space.
    */
   public Global (int offset) {
      m_offset = offset;
   }

   @Override
   public void genLoadAccess (CodeGenerator gen, int nesting) throws IOException {
      gen.loadGlobal (m_offset);
   }

   @Override
   public void genLoadAdrAccess (CodeGenerator gen, int nesting) throws IOException {
      gen.loadGlobalA (m_offset);
   }
  
   @Override
   public void genStoreAccess (CodeGenerator gen, int nesting)  throws IOException {
      gen.storeGlobal (m_offset);
   }
   

   @Override
   public void genCallAccess (CodeGenerator gen, int nesting, int argCount)  throws IOException {
     System.err.println ("Internal Compiler Error, invalid access");
     System.exit (2);
   }


   /**
    * @return Offset at the global memory space.
    */
   public int getLabel () {
      return m_offset;
   }
}

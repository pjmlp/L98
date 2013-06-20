/* IFrame.java : Base class representing the activation frame for subroutines.
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
 * Class that represents the storage of variables and constants on the activation frame.
 */
public class IFrame extends Frame {
   /**
    * @param levels level where the variable was declared.
    * @param offset the offset inside the activation frame.
    */
   public IFrame (int levels, int offset) { super (levels, offset); }

   

   @Override
   public void genLoadAccess (CodeGenerator gen, int nesting) throws IOException {
      super.genLoadAccess (gen, nesting);
      gen.loadInd ();
   }


   @Override
   public void genLoadAdrAccess (CodeGenerator gen, int nesting) throws IOException {
      super.genLoadAccess (gen, nesting);
   }
  

   @Override
   public void genStoreAccess (CodeGenerator gen, int nesting)  throws IOException {
      super.genLoadAccess (gen, nesting);
      gen.storeInd ();
   }
  
}

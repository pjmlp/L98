/* Access.java : Base class representing any kind of data access.
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
 * Base class representing any kind of data access.
 */
public abstract class Access {
   /**
    * Generates the required code for read access.
    * @param gen  code generator used for the real code.
    * @param nesting current frame nesting level
    * @throws IOException if an error happens while writing code to the file system.
    */
   public abstract void genLoadAccess (CodeGenerator gen, int nesting) throws IOException;

   /**
    * Generates the required code for read access of a memory address.
    * @param gen code generator used for the real code.
    * @param nesting current frame nesting level
    * @throws IOException if an error happens while writing code to the file system.
    */
   public abstract void genLoadAdrAccess (CodeGenerator gen, int nesting) throws IOException;
  
   /**
    * Generates the required code for write access.
    * @param gen code generator used for the real code.
    * @param nesting current frame nesting level
    * @throws IOException if an error happens while writing code to the file system.
    */
   public abstract void genStoreAccess (CodeGenerator gen, int nesting) throws IOException;
  
   /**
    * Generates code related to sub-routine calls.
    * @param gen code generator used for the real code.
    * @param nesting current frame nesting level.
    * @param argCount number of required arguments.
    * @throws IOException if an error happens while writing code to the file system.
    */
   public abstract void genCallAccess (CodeGenerator gen, int nesting, int argCount)  throws IOException;
}

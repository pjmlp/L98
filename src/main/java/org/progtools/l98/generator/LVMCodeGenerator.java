/* LVMCodeGenerator.java : Code generator for a textual representation of L98 bytecode
 * Copyright (C) 2013  Paulo Pinto
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
package org.progtools.l98.generator;
import java.io.*;

/**
 *  Code generator for a textual representation of L98 bytecode.
 */
public final class LVMCodeGenerator implements CodeGenerator {
   /**
    * Output source for the Assembly code.
    */
   private PrettyWritter m_out;
   
   /**
    * @param out Where to write the text representation of the Assembly code.
    * @param indent Desired identantion level when no labels are used.
    * @param size The amount of bytes to allocate in the global memory
    */
   public LVMCodeGenerator (OutputStream out, int indent, int size) throws IOException {
      m_out = new PrettyWritter (out, indent);
      m_out.writeDirective("GLOBALS " + size);
      m_out.writeLabel("P_START");
   }
   
   /**
    * Call to perform the required cleanup.
    */
    @Override
  public void close () throws IOException {
      halt ();
      m_out.close ();
  }
  
   @Override
   public void load (int value) throws IOException {
      m_out.writeInstruction ("LOAD " + value);
   }
   
    @Override
   public void loadVar (int level, int offset) throws IOException {
      m_out.writeInstruction ("LOADVAR " + level + ", " + offset); 
   }

    @Override
   public void storeVar (int level, int offset) throws IOException {
      m_out.writeInstruction ("STOREVAR " + level + ", " + offset); 
   }
   
   @Override
   public void loadGlobal (int offset) throws IOException {
      m_out.writeInstruction ("LOADGLOBAL " + offset); 
   }

    @Override
   public void storeGlobal (int offset) throws IOException {
      m_out.writeInstruction ("STOREGLOBAL " + offset); 
   }

    @Override
   public void loadVarA (int level, int offset) throws IOException {
      m_out.writeInstruction ("LOADVARA " + level + ", " + offset); 
   }

    @Override
   public void loadGlobalA (int offset) throws IOException {
      m_out.writeInstruction ("LOADGLOBALA " + offset); 
   }

    @Override
   public void loadInd () throws IOException {
      m_out.writeInstruction ("LOADIND"); 
   }

    @Override
   public void storeInd () throws IOException {
      m_out.writeInstruction ("STOREIND"); 
   }
   
    @Override
   public void alloc (int size) throws IOException {
      m_out.writeInstruction ("ALLOC " + size); 
   }
   
    @Override
   public void jpc (int value, String label) throws IOException {
      m_out.writeInstruction ("JPC " + value + ", " + label); 
   }
 
    @Override
   public void jmp (String label) throws IOException {
      m_out.writeInstruction ("JMP " + label); 
   }
    
    @Override
   public void call (int entry, String label) throws IOException {
      m_out.writeInstruction ("CALL " + entry + ", " + label); 
   }

    @Override
   public void ret () throws IOException {
      m_out.writeInstruction ("RET"); 
   }
   
    @Override
   public void csp (int subNum) throws IOException {
      m_out.writeInstruction ("CSP " + subNum); 
   }
   
    @Override
   public void halt () throws IOException {
      m_out.writeInstruction ("HALT"); 
   }
   
   
    @Override
   public void add () throws IOException {
      m_out.writeInstruction ("ADD"); 
   }

    @Override
   public void sub () throws IOException {
      m_out.writeInstruction ("SUB"); 
   }

    @Override
   public void mul () throws IOException {
      m_out.writeInstruction ("MUL"); 
   }

    @Override
   public void div () throws IOException {
      m_out.writeInstruction ("DIV"); 
   }

    @Override
   public void neg () throws IOException {
      m_out.writeInstruction ("NEG"); 
   }

    @Override
   public void not () throws IOException {
      m_out.writeInstruction ("NOT"); 
   }

    @Override
   public void and () throws IOException {
      m_out.writeInstruction ("AND"); 
   }

    @Override
   public void or () throws IOException {
      m_out.writeInstruction ("OR"); 
   }

    @Override
   public void eq () throws IOException {
      m_out.writeInstruction ("EQ"); 
   }

    @Override
   public void neq () throws IOException {
      m_out.writeInstruction ("NEQ"); 
   }
   
    @Override
   public void lt () throws IOException {
      m_out.writeInstruction ("LT"); 
   }

    @Override
   public void gt () throws IOException {
      m_out.writeInstruction ("GT"); 
   }

    @Override
   public void leq () throws IOException {
      m_out.writeInstruction ("LEQ"); 
   }

    @Override
   public void geq () throws IOException {
      m_out.writeInstruction ("GEQ"); 
   }
   
    @Override
   public void insLabel (String name) throws IOException {
      m_out.writeLabel(name);
   }

    @Override
   public void comment (String description) throws IOException {
      m_out.writeComment(description);
   }
}

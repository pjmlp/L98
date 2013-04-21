/* Label.java : Class dealing with labels for jump targets.
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
 * Class dealing with labels for jump targets.
 */
public class Label extends Access {
   /**
    * Nivel lexical onde a subrotina a que pertence a etiqueta esta declarada
    */
  private int m_level;
   
   /**
    * Etiqueta do ponto de entrada
    */
   private String m_label;
   
   /**
    * @param level Nivel lexical de quando a subrotina foi declarada
    * @param label  Etiqueta do ponto de entrada
    */
   public Label (int level, String label) {
      m_level = level;
      m_label = label;
   }


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
     int  dist = nesting - m_level - 1;

     gen.call (dist, m_label);
     if (argCount > 0)
       gen.alloc (-argCount);
   }
  

   /**
    * @return Nivel lexical onde a subrotina a que pertence a etiqueta esta declarada
    */
   public int getLevel () {
      return m_level;
   }

  
   /**
    * @return Etiqueta do ponto de entrada
    */
   public String getLabel () {
      return m_label;
   }
}
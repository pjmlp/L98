/* PrettyWritter.java : Class used to write nice formatted Assembly listenings
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

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Class used to write nice formatted Assembly listings.
 */
final class PrettyWritter implements Closeable {
   /**
    * Output destination.
    */
   private final PrintWriter m_out;
   
   /**
    * Indentation level used when no labels are written.
    */
   private final int m_indent;


   /**
    * Indicates if the last written Assembly was a label
    */
   private boolean m_lastWasALabel; 
   
   /**
    * 
    */
   private final CommentFormat m_commentStyle;
   
   
   /**
    * If set to L98 uses semicolons, otherwise C style comments are used.
    */
   public enum CommentFormat {L98, UNIX};
   
   public PrettyWritter (OutputStream out, int indent) {
      m_out = new PrintWriter (out);
      m_indent = indent;
      m_lastWasALabel = false;
      m_commentStyle = CommentFormat.L98;
   }
   
   public PrettyWritter (OutputStream out, int indent, CommentFormat style) {
      m_out = new PrintWriter (out);
      m_indent = indent;
      m_lastWasALabel = false;
      m_commentStyle = style;
   }
   

   @Override
    public void close() throws IOException {
        m_out.println ();
        m_out.close ();
    }

   public void writeLabel (String name) throws IOException {
      m_out.println ("\n" + name + ":");
      m_lastWasALabel = true;
   }
    
   public void writeComment (String description) throws IOException {
     if (!m_lastWasALabel)
       m_out.println ();
     else
       m_lastWasALabel = false;

      if (m_commentStyle == CommentFormat.L98)
          m_out.println ("; " + description);
      else
          m_out.printf ("/* %s */ \n", description);
   }    
    
   public void writeInstruction (String format, Object... args) throws IOException {
       toCol();
       m_out.printf(format, args);
   } 
   
   public void writeDirective (String format) throws IOException {
       m_out.printf("\n%s", format);
   } 
   
    /**
     * Indents the current line according to the desired identation level.
     */
    private void toCol() throws IOException {
        if (!m_lastWasALabel) {
            m_out.println();
        } else {
            m_lastWasALabel = false;
        }
        for (int i = 0; i < m_indent; i++) {
            m_out.print(' ');
        }
    }
    
}

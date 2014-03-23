/* CompilerError.java : Class for communicating error messages to the user.
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
package org.progtools.l98;

/**
 * Reports compiler errors.
 */
public class CompilerError {
  
  /**
   * Prefix to be shown before the colon(:) on the error messages.
   */
  private final String m_prefix;
  
  /**
   * Amount of error messages displayed so far.
   */
  private int m_errorCount;

  /**
   * Creates an error message reporter using an empty prefix.
   */
  public CompilerError () { 
    m_prefix = "";
    m_errorCount = 0;
  }
  
  /**
   * @param prefix Text to be displayed before the colon(:) on error messages.
   */
  public CompilerError (String prefix) { 
    m_prefix = prefix;
    m_errorCount = 0;
  }
  
  /**
   * @param msg An error message to be displayed.
   */
  public void message (String msg) {
    m_errorCount++;
    System.out.printf ("%s:%s\n", m_prefix, msg);
  }

  /**
   * @param msg A warning message to be displayed.
   */
  public void warning (String msg) {
    System.out.println (m_prefix + ": Warning : " + msg);
  }
   
  /**
   * @return Error messages written by this error component.
   */
  public int getErrorCount () { return m_errorCount; }
  
  /**
   * Sets the compiler errors back to zero.
   */
  public void resetErrorCount () { m_errorCount = 0; }

  /**
   * Writes a message to standard output with associated error.
   * @param msg Message to write
   * @param e An exception
   */
    public void message(String msg, Exception e) {
        m_errorCount++;
        System.out.printf ("%s:%s, %s\n", m_prefix, msg, e);
    }
  
}

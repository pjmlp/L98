/* Environ.java : Manages the 
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
package org.progtools.l98.table;
import java.util.HashMap;
import java.util.Stack;
import java.util.Map;


/**
 * Class responsible for managing identifier's environments.
 */
public class Environ {
  /**
   * Elements stored in the hash table
   */ 
  private class Element {
    String m_id;
    Object m_value;

    Element(String id, Object value) {
      m_id = id;
      m_value = value;
    }
  }

  /**
   * Environment being managed.
   */
  private Map<String, Stack> m_vars;
  
  /**
   * Identifiers added to the environment.
   * The stack allows for an easy way to remove them in the inverse order.
   */
  private Stack<String>    m_ids;

  Environ () { 
    m_vars = new HashMap<>();
    m_ids = new Stack<> ();
  }

  /**
   * Adds a new identifier to the environment.
   * If it already exits, its value is updated.
   * @param id Name
   * @param value Current associated value
   */
  public void update (String id, Object value) { 
   Stack temp = m_vars.get (id);

   if (temp == null) {
     temp = new Stack ();
     m_vars.put (id, temp);
   }
   
   m_ids.push (id);
   temp.push (new Element(id, value));
  }

  /**
   * Removes the last update done to the environment.
   */
  public void removeLast () { 
    String id = m_ids.pop ();
    Stack elems = m_vars.get (id);

    elems.pop (); 
  }

  /**
   * Gets the value mapped to the given identifier
   * @param id Name
   * @returns Associated value
   * @exception BadVarException If the identifier is not valid
   */
  public Object getVal (String id) throws BadVarException {
    Element elem;
    Stack elems = m_vars.get (id);

    if (elems == null)
      throw new BadVarException(id);
    
    for (int i = elems.size();i > 0; i--) {
      elem = (Element) elems.elementAt(i-1);
      if (elem.m_id.equals(id))
        return elem.m_value;
    }
    
    throw new BadVarException(id);
  }
}
    

/* Attributes.java : Represents the attributes associated with an identifier.
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
import org.progtools.l98.access.Access;
import org.progtools.l98.type.Type;



/**
 * Represents the attributes associated with an identifier.
 */
public class Attributes {
  /**
   * Identifier's type.
   */
  private final Type m_type;

  /**
   * Indentifier's storage type.
   */
  private final Access m_access;

  
  /**
   * Is the user allowed to change its value. 
   */
  private final boolean m_isVar;

  
  /**
   * Used as part of a function's argument.
   */
  private final boolean m_isArgument;
  
  /**
   * @param type Identifier's type.
   * @param access Storage type.
   * @param isVar  Used as a variable.
   * @param isArgument Used as parameter declaration.
   */
  public Attributes (Type type, Access access, boolean isVar, boolean isArgument) {
    m_type = type;
    m_access = access;
    m_isVar = isVar;
    m_isArgument = isArgument;
  }
  
  /**
   * @return Identifier's type.
   */
  public Type getType ()  { return m_type; }

  /**
   * @return Storage type.
   */
  public Access getAccess ()  { return m_access; }

  /**
   * @return true if the user allowed to change its value. 
   */
  public boolean isVar ()  { return m_isVar; }
  

  /**
   * @return true if the identifier represents a subroutine parameter.
   */
  public boolean isArgument ()  { return m_isArgument; }
}

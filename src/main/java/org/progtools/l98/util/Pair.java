/* Pair.java : Represents pairs of objects.
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

package org.progtools.l98.util;

/**
 * Represents pairs of elements, also known as 2 elements tuples.
 * It was originally modeled on C++'s pair class.
 */
public class Pair {
   /**
    * First element.
    */
   private final Object m_left;

   /**
    * Second element.
    */
   private final Object m_right;
   
   /**
    * @param left first element.
    * @param right second element.
    */
   public Pair (Object left, Object right) {
      m_left = left;
      m_right = right;
   }
   
   /**
    * @return first element.
    */
   public Object getLeft () { return m_left; }
   
   /**
    * @return second element.
    */
   public Object getRight () { return m_right; }   
}

/* LabelGenerator.java : Utility class for label generation.
 * Copyright (C) 2013  Paulo Pinto, Pablo Tavares.
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

/**
 *  Utility class for label generation.
 */
public final class LabelGenerator {
   /**
    * Next label to generate
    */
   private static int m_count = 0;
   
   /**
    * @return A label of the form Lnum.
    */
   public static String getLabel () {
      return "L" + m_count++;
   }
   
   /**
    * Restarts the label generator.
    */
   public static void reset () {
      m_count = 0;
   }
}

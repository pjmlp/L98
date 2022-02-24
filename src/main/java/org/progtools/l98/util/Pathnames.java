/* Pathnames.java : Utility class for pathname handling.
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

import java.io.File;

/**
 * Utility class for pathname handling.
 */
public final class Pathnames {
    /**
     * Changes the extension of the provided path.
     * @param filename the full path
     * @param extension the file extension
     * @return the same pathname with the extension replaced
     */
    public static String changeFileExtension(String filename, String extension) {
        int index = filename.lastIndexOf('.');
        if (index > 0) {
           return filename.substring(filename.lastIndexOf(File.separatorChar) + 1, filename.lastIndexOf('.')) + extension;
        } else {
            return filename;
        }
    }
    
    
}

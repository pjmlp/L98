/* CodeGeneratorFactory.java : Code generator selection.
 * Copyright (C) 2022  Paulo Pinto
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

import java.io.IOException;
import java.io.OutputStream;


/**
 * Provides the means to select the desired backend.
 */
public final class CodeGeneratorFactory {
    /**
     * Available set of backends.
     */
    public enum BackendKind { LVM, X86, WASM }
    
    /**
     * Retrieves the desired backend.
     * 
     * @param backend The set of backend to chose from.
     * @param out The stream to write the code into.
     * @param indent The amount of indent to use per line of code.
     * @param size The size of the global variables.
     * @return The code generator or null if none valid
     * @throws IOException In case there is an error writing into the data stream.
     */
    public static CodeGenerator get(BackendKind backend, OutputStream out, int indent, int size)  throws IOException {
        switch(backend) {
            case LVM:
                return new LVMCodeGenerator(out, indent, size);
                
            case X86:
                return new X86CodeGenerator(out, indent, size);
                
            case WASM:
                return new X86CodeGenerator(out, indent, size); 
                
            default:
                assert false: "Unknown backend kind";            
        }
        
        return null;
    }
    
}

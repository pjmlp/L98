/* CodeGenerator.java : Interface for code generators.
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

import java.io.IOException;

/**
 * The interface that all code generators need to support for
 * proper generation of executable code.
 * 
 * This refactoring was done to remove the dependency on NASM macros
 * as a way to generate native code, while making the whole generator
 * concept a bit more OO friendly.
 * 
 */
public interface CodeGenerator extends AutoCloseable {

    //--------- Arithmetic and logical operations
    
    /**
     * Adds the two top most values in the stack and pushes the result.
     */
    void add() throws IOException;
    
    /**
     * Subtracts the two top most values in the stack and pushes the result.
     */
    void sub() throws IOException;    

    /**
     * Inverts the sign of the stack's topmost value.
     */
    void neg() throws IOException;
    
    /**
     * Increments the SP register in size positions.
     * @param size Number of positions to increment. Both positive and negative values are allowed.
     */
    void alloc(int size) throws IOException;

    /**
     *  Performs a bitwise and operation with the two topmost values in the stack,
     * the result is pushed back into it.
     */
    void and() throws IOException;
    
    /**
     *  Performs a bitwise or operation with the two topmost values in the stack,
     * the result is pushed back into it.
     */
    void or() throws IOException;    

    /**
     * Inverts the logical value (boolean) of the topmost value in the stack.
     */
    void not() throws IOException;
    
    /**
     * Divides the two topmost values in the stack and stores back the result into
     * the stack.
     */
    void div() throws IOException;
    
    /**
     * Multiplies the two topmost values in the stack and stores back the result into
     * the stack.
     */
    void mul() throws IOException;

    /**
     *  Compares the two topmost values in the stack for equality and stores the
     * result into the stack.
     * 0 -> true
     * 1 -> false
     */
    void eq() throws IOException;
    
    /**
     *  Compares the two topmost values in the stack for inequality and stores the
     * result into the stack.
     * 0 -> true
     * 1 -> false
     */
    void neq() throws IOException;    

    /**
     *  Compares the two topmost values in the stack for greater than or equal equality and stores the
     * result into the stack.
     * 0 -> true
     * 1 -> false
     */
    void geq() throws IOException;
    
    /**
     *  Compares the two topmost values in the stack for less than or equal equality and stores the
     * result into the stack.
     * 0 -> true
     * 1 -> false
     */
    void leq() throws IOException;    

    /**
     *  Compares the two topmost values in the stack for greater than equality and stores the
     * result into the stack.
     * 0 -> true
     * 1 -> false
     */
    void gt() throws IOException;


    /**
     *  Compares the two topmost values in the stack for less than equality and stores the
     * result into the stack.
     * 0 -> true
     * 1 -> false
     */
    void lt() throws IOException;
    
    //--------- Utility Methods
    
    /**
     * Inserts a target label in the generated code.
     * @param name Target name.
     */
    void insLabel(String name) throws IOException;
    
    /**
     * Writes a comment in the generated file. Only respected if
     * the destination allows for storage of comments.
     * @param description Desired comment.
     */
    void comment(String description) throws IOException;

    //------------ Control flow
    
    /**
     * Calls a subroutine with starting execution at label.
     * @param entry Subroutine lexical level entry point.
     *              entry = -1 -> Target destination is on a lower level.
     *              entry =  0 -> Target destination is on the same level.
     *              entry =  1 -> Target destination is on a higher level.
     * @param label Destination label.
     */
    void call(int entry, String label) throws IOException;    

    /**
     * Invokes a pre-defined subroutine.
     * @param subNum Id number of the subroutine.
     *               subNum = 0 -> printint
     *               subNum = 1 -> readint
     *               subNum = 2 -> printbool
     *               subNum = 3 -> readbool
     *               subNum = 4 -> println
     */
    void csp(int subNum) throws IOException;    
    
    /**
     * Inconditional jump to a given destination.
     * @param label target destination.
     */
    void jmp(String label) throws IOException;

    
    /**
     * Conditional jump to a given destination.
     * The jump only takes place if the value stored at the top of the
     * stack matches the given value. In any case the stack topmost value
     * is removed.
     * @param value Desired comparisation value.
     * @param label Target destination.
     */
    void jpc(int value, String label) throws IOException;
    
    /**
     * Returns from a subroutine to the calling point.
     */
    void ret() throws IOException;    
    
    /**
     * Terminates execution.
     */
    void halt() throws IOException;    


    //------------------ Memory manipulation
    /**
     * Stores a constant value into the stack.
     * @param value Desired value to store.
     */
    void load(int value) throws IOException;

    /**
     * Stores the value located at the given memory offset into the stack.
     * @param offset Offset from the start of the data memory.
     */
    void loadGlobal(int offset) throws IOException;

    /**
     * Stores the value located at the top of the stack into the given memory offset.
     * @param offset Offset from the start of the data memory.
     */
    void storeGlobal(int offset) throws IOException;
    
    /**
     * Stores the memory address of value located at the given memory offset into the stack.
     * @param offset Offset from the start of the data memory.
     */
    void loadGlobalA(int offset) throws IOException;

    /**
     * Treats the value at the top of the stack as a memory address and
     * stores its contents into the top of the stack.
     * 
     * The former value at the top of the stack is removed.
     */
    void loadInd() throws IOException;

    /**
     * Treats the value at the top of the stack as a data value and the following
     * one as a memory address.
     * 
     * Both are removed from the stack and the value is stored at the given memory
     * address.
     */    
    void storeInd() throws IOException;
    
    /**
     * Stores into the stack the variable's value, located in a memory
     * position a few lexical levels above the current activation level.
     * @param level Lexical level of the variable.
     * @param offset Offset inside the frame for the lexical level.
     */
    void loadVar(int level, int offset) throws IOException;

    /**
     * Takes the top level stack value and stores it into the variable's
     * location, in a memory position a few lexical levels above the current
     * activation level.
     * @param level Lexical level of the variable.
     * @param offset Offset inside the frame for the lexical level.
     */
    void storeVar(int level, int offset) throws IOException;
    
    /**
     * Stores into the stack the variable's memory address, located in a memory
     * position a few lexical levels above the current activation level.
     * @param level Lexical level of the variable.
     * @param offset Offset inside the frame for the lexical level.
     */
    void loadVarA(int level, int offset) throws IOException;
}

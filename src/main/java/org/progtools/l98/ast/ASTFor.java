/* ASTFor.java : AST node representing for loops
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
package org.progtools.l98.ast;
import java.io.IOException;

import org.progtools.l98.table.Attributes;
import org.progtools.l98.CompilerError;
import org.progtools.l98.access.Access;
import org.progtools.l98.access.Frame;
import org.progtools.l98.access.Global;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.generator.LabelGenerator;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeInt;


/**
 * AST node representing for loops.
 */
public class ASTFor extends ASTStat {
  /**
   * Loop variable.
   */
   private final String m_id;
   
  /**
   * Initial loop expression.
   */
  private final ASTExp m_from;

  /**
   * Limit loop expression.
   */
  private final ASTExp m_to;

  /**
   * counting direction.
   */
  private final boolean m_direction;

   
  /**
   * code block.
   */
  private final ASTStat m_stat;
   

 /**
  * @param line where the expression was found.
  * @param id   loop's variable
  * @param from initial loop value
  * @param to   final loop value
  * @param direction Increment to the loop's variable, true -> 1, false -> -1
  * @param stat code statements declared in the block
  */  
  public ASTFor (int line, String id, ASTExp from, ASTExp to, boolean direction, ASTStat stat) {
    super (line); 
    m_id = id;
    m_from = from;
    m_to = to;
    m_direction = direction;
    m_stat = stat;
  }

  /**
   * @return Number of 32 bits words required for variables.
   */
   @Override
  public int alloc () { return 1 + m_stat.alloc (); }
  
  /**
   * Adds the loop control variable to the environment
   * @param env current environment.
   * @param nesting current static lexical level.
   * @param index Next position available for variable generation.
   */
  private Attributes addVarToEnv (Environ env, int nesting, int index) {
    Access valAccess;
    
    if (nesting == 1)
      valAccess= new Global (index);
    else
      valAccess= new Frame (nesting, index);
    
    Attributes attr = new Attributes (new TypeInt (), valAccess, false, false);    

    env.update (m_id, attr);
    return attr;
  }
  
  
  /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @param index Next position available for variable generation.
   * @return true if the code block has a return statement
   */
   @Override
  public boolean transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
    try {
      Attributes attr = addVarToEnv (env, nesting, index);
      if (attr != null) {
      Access varAccess = attr.getAccess ();
       
       
      String condLabel = LabelGenerator.getLabel ();  // Label for the testing code
      String endLabel = LabelGenerator.getLabel ();   // Label for the loops exit point
   
      // Validates the initial value for the loop
      Type expFromType = m_from.transverse (env, err, gen, nesting);
      if (expFromType != null && !(expFromType instanceof TypeInt))
         err.message (getLine() + ": The initial value has to be an integer");
   
      // generates the code around the loop
        varAccess.genStoreAccess (gen, nesting);

        gen.insLabel (condLabel);
        varAccess.genLoadAccess (gen, nesting);

	 
      // Performs the validation of the final value in the loop
      Type expToType = m_to.transverse (env, err, gen, nesting);
      if (expToType != null && !(expFromType instanceof TypeInt))
         err.message (getLine() + ": The final value has to be an integer");

      // Generate the code required for initializing the loop's variable
        if (m_direction) // to
          gen.leq ();
        else // downto
          gen.geq ();

        gen.jpc (0, endLabel);
      

      m_stat.transverse (env, err, gen, nesting, index + 1);

      // Generates the code to take care of incrementing/decrementing the loop's variable
        varAccess.genLoadAccess (gen, nesting);
        gen.load (1);

        if (m_direction) // to
          gen.add ();
        else // downto
          gen.sub ();

        varAccess.genStoreAccess (gen, nesting);
        gen.jmp (condLabel);

        gen.insLabel (endLabel);
	 
     
      // Removes the loop control variable from the curent environment
      env.removeLast ();
      }
    }
    catch (IOException e) {
      err.message ("Error while generating the code");
    }
    return false;
  }
}

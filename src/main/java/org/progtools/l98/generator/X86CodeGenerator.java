/* X86CodeGenerator.java : Generates textual version of x86 Assembly for Linux
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
import java.io.*;

/**
 *  Code generator for 32 bit x86 code in Assembly format compatible with Gas from
 * GNU binutils for Linux systems.
 */
public final class X86CodeGenerator implements CodeGenerator {
    private final int WORD_SIZE = 4; // 4 bytes (32 bits)
    
   /**
    * Output source for the Assembly code.
    */
   private final PrettyWritter m_out;
   
   /**
    * @param out Where to write the text representation of the Assembly code.
    * @param indent Desired identantion level when no labels are used.
    * @param size The amount of bytes to allocate in the global memory
    */
   public X86CodeGenerator (OutputStream out, int indent, int size) throws IOException {
       m_out = new PrettyWritter (out, indent, PrettyWritter.CommentFormat.UNIX);
       
      sectionBSS ();
      globalMemSize (size);
      sectionTEXT ();
      insLabel ("P_START");       
   }
   
   /**
    * Call to perform the required cleanup.
    */
    @Override
  public void close () throws IOException {
      halt ();
      m_out.close ();
  }
  
   //---- Memory instructions
   

    @Override
   public void load (int value) throws IOException {
      
      commentf ("LOAD %d", value);
      m_out.writeInstruction ("movl $%d, %%eax ", value);
      m_out.writeInstruction ("pushl %%eax");
   }
   
    @Override
   public void loadVar (int level, int offset) throws IOException {
      m_out.writeComment("LOADVAR " + level + ", " + offset); 
      staticLink(level);
      if (offset < 0) {
          m_out.writeInstruction ("movl %d(%%ebx), %%eax", (offset - 1) * -WORD_SIZE);
      } else {
          m_out.writeInstruction ("movl %d(%%ebx), %%eax", (offset + 1) * -WORD_SIZE);
      }
      m_out.writeInstruction ("pushl %%eax");
   }

    @Override
   public void storeVar (int level, int offset) throws IOException {
      
      m_out.writeComment(String.format("STOREVAR %d, %d", level, offset)); 
      m_out.writeInstruction ("popl %%eax "); 
      staticLink(level);
      if (offset < 0) {
          m_out.writeInstruction ("movl %%eax, %d(%%ebx)", (offset - 1) * -WORD_SIZE);
      } else {
          m_out.writeInstruction ("movl %%eax, %d(%%ebx)", (offset + 1) * -WORD_SIZE);
      }
   }
   

    @Override
   public void loadGlobal (int offset) throws IOException {
      commentf("LOADGLOBAL %d", offset);
      m_out.writeInstruction ("movl  $%d, %%esi", offset); 
      m_out.writeInstruction ("movl  globals(, %%esi, %d), %%eax", WORD_SIZE); 
      m_out.writeInstruction ("pushl %%eax"); 
   }


   @Override
   public void storeGlobal (int offset) throws IOException {
      commentf("STOREGLOBAL %d", offset);
      m_out.writeInstruction ("popl %%eax");
      m_out.writeInstruction ("movl  $%d, %%esi", offset); 
      m_out.writeInstruction ("movl %%eax, globals(, %%esi, %d)", WORD_SIZE);
   }

   /**
    *  Empilha o endereï¿½o da posiï¿½ao de memï¿½ria que se
    * encontra level nï¿½veis acima do registo de activaï¿½ao corrente.
    * @param level Numero de nï¿½veis
    * @param offset Deslocamento dentro do frame.
    */
    @Override
   public void loadVarA (int level, int offset) throws IOException {
      
      m_out.writeInstruction ("LOADVARA " + level + ", " + offset); 
   }

   /**
    *  Empilha o endereï¿½o daposiï¿½ao de memï¿½ria.
    * @param offset Deslocamento a partir da base da pilha.
    */
    @Override
   public void loadGlobalA (int offset) throws IOException {
      
      commentf("LOADGLOBALA %d", offset);
      m_out.writeInstruction("lea	eax, [globals+%d*%d]", WORD_SIZE, offset); 
      m_out.writeInstruction ("push 	eax"); 
   }

    @Override
   public void loadInd () throws IOException {
      comment("LOADIND");
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("pushl (%%eax)"); 
   }
        
    @Override
   public void storeInd () throws IOException {
      comment("STOREIND");
      m_out.writeInstruction ("popl %%ebx"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("movl %%eax, (%%ebx)"); 
   }
   
    @Override
   public void alloc (int size) throws IOException {
      commentf("ALLOC %d", size);
      m_out.writeInstruction("subl $%d, %%esp", (size * WORD_SIZE)); 
   }
   
   /* Controle */

    /**
    * Desempilha um valor v do topo da pilha, se for igual a value
    * salta para label, caso contrï¿½rio continua a execucaï¿½ao normalmente
    * @param value Valor da comparaï¿½ao.
    * @param label Etiqueta de destino.
    */
    @Override
   public void jpc (int value, String label) throws IOException {
      m_out.writeComment("JPC " + value + ", " + label);
      m_out.writeInstruction("popl %%eax"); 
      m_out.writeInstruction("cmp $%d, %%eax", value); 
      m_out.writeInstruction("jne 1f"); 
      m_out.writeInstruction("jmp %s", label); 
      m_out.writeLabel("1");
   }
 

    @Override
   public void jmp (String label) throws IOException {
      commentf("JMP %s", label);
      m_out.writeInstruction("jmp  %s", label); 
   }
    
   /**
    * Invoca uma subrotina que se encontra definida na etiqueta label.
    * @param entry Ponto de entrada. 
    *              entry = -1 -> A funï¿½ao destino estï¿½ num nï¿½vel inferior
    *              entry =  0 -> A funï¿½ao destino estï¿½ ao mesmo nï¿½vel
    *              entry =  1 -> A funï¿½ao destino estï¿½ num nï¿½vel superior    
    * @param label Etiqueta de destino.
    */
    @Override
   public void call (int entry, String label) throws IOException {
      m_out.writeComment ("CALL " + entry + ", " + label); 
      m_out.writeInstruction ("push %%ebp"); 
      staticLink(1 + entry);
      m_out.writeInstruction ("push %%ebx"); 
      m_out.writeInstruction ("mov %%esp, %%ebp"); 
      m_out.writeInstruction ("call %s", label); 
    }

    @Override
   public void ret () throws IOException {
      comment ("RET"); 
      m_out.writeInstruction ("movl %%ebp, %%esp"); 
      m_out.writeInstruction ("movl -%d(%%esp), %%eax", WORD_SIZE); 
      m_out.writeInstruction ("popl %%ebp"); 
      m_out.writeInstruction ("popl %%ebp"); 
      m_out.writeInstruction ("jmp  *%%eax"); 
   }
	
   
    @Override
   public void csp (PreDefinedRoutines subNum) throws IOException {
      m_out.writeComment("CSP " + subNum); 
      switch(subNum){
          case PRINT_INT:
              m_out.writeInstruction ("call print_int"); 
              m_out.writeInstruction ("addl $%d, %%esp", WORD_SIZE); 
              break;

          case READ_INT:
              m_out.writeInstruction ("call read_int"); 
              m_out.writeInstruction ("pushl %%eax"); 
              break;

          case PRINT_BOOL:
              m_out.writeInstruction ("call print_bool"); 
              m_out.writeInstruction ("addl $%d, %%esp", WORD_SIZE); 
              break;

          case READ_BOOL:
              m_out.writeInstruction ("call read_bool"); 
              m_out.writeInstruction ("pushl %%eax"); 
              break;

          case PRINT_LN:
              m_out.writeInstruction ("call print_ln"); 
              break;
              
          default: assert false: "Unknown CSP value";
      }
   }
   

    @Override
   public void halt () throws IOException {
      
      comment("HALT");
      m_out.writeInstruction ("jmp l98_os_terminate"); 
   }
   
   
   //----- Logic and arithmetic instructions

    @Override
   public void add () throws IOException {
      comment ("ADD"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("addl %%eax, (%%esp)"); 
   }

    @Override
   public void sub () throws IOException {
      comment("SUB");
      m_out.writeInstruction ("popl %%eax");
      m_out.writeInstruction ("subl %%eax, (%%esp)");
   }

    @Override
   public void mul () throws IOException {
      comment ("MUL"); 
      m_out.writeInstruction ("popl  %%eax"); 
      m_out.writeInstruction ("imull (%%esp)"); 
      m_out.writeInstruction ("movl  %%eax, (%%esp)"); 
   }

    @Override
   public void div () throws IOException {
      comment ("DIV"); 
      m_out.writeInstruction ("movl  %d(%%esp), %%eax", WORD_SIZE); 
      m_out.writeInstruction ("cdql"); 
      m_out.writeInstruction ("idivl (%%esp)"); 
      m_out.writeInstruction ("addl $%d, %%esp", WORD_SIZE); 
      m_out.writeInstruction ("movl %%eax, (%%esp)"); 
   }

	    
    @Override
   public void neg () throws IOException {
      
      comment ("NEG"); 
      m_out.writeInstruction ("negl (%%esp)"); 
   }

    @Override
   public void not () throws IOException {
      comment ("NOT"); 
      m_out.writeInstruction ("notl (%%esp)"); 
   }

    @Override
   public void and () throws IOException {
      comment ("AND"); 
      m_out.writeInstruction ("popl (%%eax)"); 
      m_out.writeInstruction ("addl %%eax, (%%esp)"); 
   }
    
    	
    @Override
   public void or () throws IOException {
      comment ("OR"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("orl  %%eax, (%%esp)"); 
   }

    @Override
   public void eq () throws IOException {
      m_out.writeComment ("EQ"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("cmpl (%%esp), %%eax"); 
      m_out.writeInstruction ("jne  1f"); 
      m_out.writeInstruction ("movl $1, (%%esp)"); 
      m_out.writeInstruction ("jmp  2f"); 
      m_out.writeLabel("1");
      m_out.writeInstruction ("movl $0, (%%esp)"); 
      m_out.writeLabel("2");
   }

    @Override
   public void neq () throws IOException {
      m_out.writeComment ("NEQ"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("cmpl (%%esp), %%eax"); 
      m_out.writeInstruction ("je   1f"); 
      m_out.writeInstruction ("movl $1, (%%esp)"); 
      m_out.writeInstruction ("jmp  2f"); 
      m_out.writeLabel("1");
      m_out.writeInstruction ("movl $0, (%%esp)"); 
      m_out.writeLabel("2");
   }
   
    @Override
   public void lt () throws IOException {
      m_out.writeComment ("LT"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("cmpl (%%esp), %%eax"); 
      m_out.writeInstruction ("jle  1f"); 
      m_out.writeInstruction ("movl $1, (%%esp)"); 
      m_out.writeInstruction ("jmp  2f"); 
      m_out.writeLabel("1");
      m_out.writeInstruction ("movl $0, (%%esp)"); 
      m_out.writeLabel("2");
   }

    @Override
   public void gt () throws IOException {
      m_out.writeComment ("GT"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("cmpl (%%esp), %%eax"); 
      m_out.writeInstruction ("jge  1f"); 
      m_out.writeInstruction ("movl $1, (%%esp)"); 
      m_out.writeInstruction ("jmp  2f"); 
      m_out.writeLabel("1");
      m_out.writeInstruction ("movl $0, (%%esp)"); 
      m_out.writeLabel("2");
   }

    @Override
   public void leq () throws IOException {
      m_out.writeComment ("LEQ"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("cmpl (%%esp), %%eax"); 
      m_out.writeInstruction ("jl   1f"); 
      m_out.writeInstruction ("movl $1, (%%esp)"); 
      m_out.writeInstruction ("jmp  2f"); 
      m_out.writeLabel("1");
      m_out.writeInstruction ("movl $0, (%%esp)"); 
      m_out.writeLabel("2"); 
   }

    @Override
   public void geq () throws IOException {
      m_out.writeComment ("GEQ"); 
      m_out.writeInstruction ("popl %%eax"); 
      m_out.writeInstruction ("cmpl (%%esp), %%eax"); 
      m_out.writeInstruction ("jg   1f"); 
      m_out.writeInstruction ("movl $1, (%%esp)"); 
      m_out.writeInstruction ("jmp  2f"); 
      m_out.writeLabel("1");
      m_out.writeInstruction ("movl $0, (%%esp)"); 
      m_out.writeLabel("2"); 
   }
   

    @Override
   public void insLabel (String name) throws IOException {
      m_out.writeLabel(name);
   }

   private void commentf (String format,Object... args) throws IOException {
       m_out.writeComment(String.format(format, args));
   }

   /**
    * Starts a non-initialized data section.
    */
   public void sectionBSS ()  throws IOException {
      m_out.writeDirective (".code32");
      m_out.writeDirective (".section .bss");
   }

   /**
    * Starts a code section.
    */
   public void sectionTEXT ()  throws IOException {
      m_out.writeDirective (".section .text");
      m_out.writeDirective (".globl P_START");
   }

   /**
    * Sets the required ammount of global memory.
    * This should only be called after a BSS section was started.
    * @param size The value is in words.
    */
   public void globalMemSize (int size)  throws IOException {
      m_out.writeDirective (".lcomm globals, " + (size * WORD_SIZE));
   }

    @Override
    public void comment(String description) throws IOException {
        m_out.writeComment (description);
    }
    
    /**
     * Generates the required Assembly code to access the static link
     * also known as frame pointer.
     */
    private void staticLink(int count) throws IOException {
        m_out.writeInstruction ("movl %%ebp, %%ebx");
        for (int  i = 0; i < count; i++) {
            m_out.writeInstruction ("movl (%%ebx), %%ebx");
        }
    }
}

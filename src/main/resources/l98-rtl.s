.code32
.global print_ln, print_bool, read_bool, print_int, read_int
.data
.lcomm buffer, 1024
.lcomm out_buffer, 1024

new_line:    
        .ascii  "\n"

true_str:
        .ascii "TRUE\n"

false_str:
        .ascii "FALSE\n"

.text


/* L98 runtime library for read_bool() */
read_bool:
        movl    $buffer, %eax     /* read the string written by the user */
        movl    $512, %ebx
        call    l98_os_read
        movl    $0x0, %eax
        movl    $0x0, %edi
        movl    $buffer, %esi

        /* Convert the received string to uppercase */
        movl    %ecx, %edx
        movl    $buffer, %ebx

read_bool_L1:    
        cmpl    $0x0, %edx
        je    read_bool_L3
        movb    (%ebx), %al
        cmpb    $0x61, %al       /* character a */
        jb    read_bool_L2
        cmpb    $0x7A, %al       /* character z */
        ja    read_bool_L2
        subb    $0x20, %al
        movb    %al, (%ebx)
    
read_bool_L2:    
        decl    %edx
        incl    %ebx
        jmp    read_bool_L1
    
read_bool_L3:    
        /* Has the user entered true? */
        movl    $buffer, %esi
        movl    $true_str, %edi
        movl    %ecx, %edx
        repz    cmpsb
        jecxz   read_bool_L4
        movl    $0x0, %eax
        jmp    read_bool_L5
    
read_bool_L4:
        /* TRUE was found, for everything else FALSE is assumed. No fancy error handling going on */
        movl    $0x1, %eax

read_bool_L5:

        ret
    
/* L98 runtime library for read_int() */
read_int:
        movl    $buffer, %eax
        movl    $512, %ebx
        call    l98_os_read
        movl    $0x0, %eax
        movl    $0x0, %edi
        movl    $buffer, %esi

read_int_L1:
        cmpl    $0x0, %ecx
        je      read_int_L2
        movl    $0x0, %eax
        movb    (%esi), %al
        cmpb    $0x30, %al          /* character 0 */
        jb    read_int_L2
        cmpb    $0x39, %al
        ja    read_int_L2
        subl    $0x30, %eax
        xchg    %edi, %eax
        movl    $0xA, %ebx
        mull    %ebx
        addl    %eax, %edi
        decl    %ecx
        incl    %esi
        jmp     read_int_L1

read_int_L2: 
        movl    %edi, %eax
        ret

        

/* L98 runtime library for print_bool(bool) */
print_bool:
        pushl    %ebp
        movl    %esp, %ebp

        cmpl    $0x0, 8(%ebp)
        jnz     print_bool_L1
        movl    $false_str, %eax
        movl    $0x6, %ebx
        jmp     print_bool_L2

print_bool_L1:
        movl    $true_str, %eax
        movl    $0x5, %ebx
 
print_bool_L2:
        call    l98_os_write

        leave
        ret    

/* L98 runtime library for print_int(num) */
print_int:
        pushl    %ebp
        movl    %esp, %ebp

        pushl   %ebx
        pushl   %ecx
        pushl   %edx
        pushl   %esi
        pushl   %edi


        movl    8(%ebp), %eax
        movl    $buffer, %esi
        movl    $0x0, %ecx
        movl    $0xA, %ebx

print_int_L1:
        movl    $0x0, %edx
        divl    %ebx, %eax
        addl    $0x30, %edx
        movl    %edx, (%esi, %ecx, 1)
        incl    %ecx
        cmpl    $0x0, %eax
        jnz     print_int_L1

/* reverse the buffer contents */
        movl    $out_buffer, %edi
        movl    %ecx, %edx
        decl    %edx
        movl    $0x0, %eax

print_int_L2:
        cmpl    %ecx, %eax
        jge     print_int_L3
        movl    (%esi, %edx, 1), %ebx
        movl    %ebx, (%edi, %eax, 1)
        decl    %edx
        incl    %eax
        jmp     print_int_L2
        
print_int_L3:

/* now display it to the user */

        movl    $out_buffer, %eax
        movl    %ecx, %ebx
        call    l98_os_write

        popl    %edi
        popl    %esi
        popl    %edx
        popl    %ecx
        popl    %ebx

        leave
        ret

/* L98 runtime library for print_ln() */
print_ln:
        movl    $new_line, %eax
        movl    $0x1, %ebx
        call    l98_os_write
        ret

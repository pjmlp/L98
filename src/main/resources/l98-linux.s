.global init, l98_os_read, l98_os_write, l98_os_terminate,_start
.data

input_handle: 
        .int    0

output_handle: 
        .int    0

written:
        .int    0

.text
.equ STD_INPUT_HANDLE, 0
.equ STD_OUTPUT_HANDLE, 1


_start:
        call    P_START

/* Exit to the operating system. The generated code can also jump directly here */
l98_os_terminate:
        /* exit(0) */
        mov     $1, %eax                # system call 1 is exit
        xor     %ebx, %ebx              # we want return code 0
        int     $0x80         

/* L98 Linux integration for reading buffer contents */
/* %eax = buffer, %ebx = counter, %ecx = read => %eax = count*/
l98_os_read:
        # read(0, message, 13)
        movl    %eax, %ecx              # address of string to write to
        movl     %ebx, %edx               # maximum number of bytes to read

        movl     $3, %eax                # system call 3 is read
        movl     $STD_INPUT_HANDLE, %ebx                # file handle 0 is stdin
        int     $0x80                   # invoke operating sys
        movl    %eax, %ecx
        ret

/* L98 Linux integration for writing buffer contents */
/* %eax = buffer, %ebx = size */
l98_os_write:
        # write(1, message, 13)
        movl    %eax, %ecx              # address of string to output
        movl     %ebx, %edx               # number of bytes to write
    
        movl     $4, %eax                # system call 4 is write
        movl     $STD_OUTPUT_HANDLE, %ebx                # file handle 1 is stdout
        int     $0x80                   # invoke operating sys
        ret


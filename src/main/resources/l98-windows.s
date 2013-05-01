.global init, l98_os_read, l98_os_write, l98_os_terminate
.data

input_handle: 
        .int    0

output_handle: 
        .int    0

written:
        .int    0

.text
.equ STD_INPUT_HANDLE, -10
.equ STD_OUTPUT_HANDLE, -11


init:
        /* initializes IO console handles */
        /* handle = GetStdHandle(STD_INPUT_HANDLE) */
        pushl   $STD_INPUT_HANDLE
        call    _GetStdHandle@4
        mov     %eax, input_handle  

        /* handle = GetStdHandle(STD_OUTPUT_HANDLE) */
        pushl   $STD_OUTPUT_HANDLE
        call    _GetStdHandle@4
        mov     %eax, output_handle

        call    P_START

/* Exit to the operating system. The generated code can also jump directly here */
l98_os_terminate:
        /* ExitProcess(0) */
        pushl   $0
        call    _ExitProcess@4

/* L98 Windows integration for reading buffer contents */
/* %eax = buffer, %ebx = counter, %ecx = read */
l98_os_read:
        /* ReadConsole(handle, &msg[0], 1, &written, 0) */
        pushl   $0x0
        pushl   $written
        pushl   %ebx
        pushl   %eax
        pushl   input_handle
        call    _ReadConsoleA@20
        movl    written, %ecx
        ret


        /* ReadConsole(handle, &msg[0], 5, &written, 0) *
        pushl   $0x0
        pushl   $written /*TODO: change this variable*
        pushl   $0x5
        pushl   $buffer
        pushl   input_handle
        call    _ReadConsoleA@20


/* L98 Windows integration for writing buffer contents */
/* %eax = buffer, %ebx = size */
l98_os_write:

        /* WriteConsole(handle, &msg[0], 1, &written, 0) */
        pushl   $0x0
        pushl   $written
        pushl   %ebx
        pushl   %eax
        pushl   output_handle
        call    _WriteConsoleA@20
        ret

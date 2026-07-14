global main

extern fprintf
extern stderr
extern strtoll
extern printf

section .data

usage_msg:
    db "Usage: program frac1 frac2", 10
    db "where frac can be either nat or nat/nat", 10, 0

fmt_plus_infinity :
    db 0x2b , 0xe2 , 0x88 , 0x9e , 0x00
fmt_minus_infinity :
    db 0x2d , 0xe2 , 0x88 , 0x9e , 0x00

fmt_num:
    db "%ld", 10, 0
fmt_frac:
    db "%ld/%ld", 10, 0
fmt_str:
    db "%s", 10, 0
fmt_nan:
    db "NaN", 10, 0

section .bss

frac1:
    resq 2

frac2:
    resq 2

endptr1:
    resq 1

endptr2:
    resq 1

section .text

main:
    push rbp
    mov rbp, rsp

    push rbx

    mov rbx, rsi

    cmp rdi, 3
    jne .usage_error

    mov rdi, [rbx + 8]
    lea rsi, [rel frac1]
    call parse_fraction

    cmp rax, 1
    jne .usage_error

    mov rdi, [rbx + 16]
    lea rsi, [rel frac2]
    call parse_fraction

    cmp rax, 1
    jne .usage_error

    mov r8, [rel frac1]
    mov r9, [rel frac1 + 8]
    mov r10, [rel frac2]
    mov r11, [rel frac2 + 8]

    mov rax, r9
    imul r11
    mov r13, rax

    mov rax, r8
    imul r11
    mov r12, rax

    mov rax, r10
    imul r9
    add r12, rax

    test r13, r13
    jnz .regular_math

    test r12, r12
    jz .do_nan
    js .do_minus_inf
    jmp .do_plus_inf

.regular_math:
    mov rdi, r12
    mov rsi, r13
    call gcd

    mov rbx, rax

    mov rax, r12
    cqo
    idiv rbx
    mov r12, rax

    mov rax, r13
    cqo
    idiv rbx
    mov r13, rax

    test r13, r13
    jns .print_result
    neg r12
    neg r13
    jmp .print_result

.usage_error:
    mov rdi, [rel stderr]
    lea rsi, [rel usage_msg]
    xor rax, rax
    call fprintf

    mov rax, 1
    pop rbx
    leave
    ret

parse_fraction:
    push rbp
    mov rbp, rsp

    push rbx
    push r12
    push r13

    mov rbx, rdi
    mov r12, rsi

    mov rdi, rbx
    lea rsi, [rel endptr1]
    mov rdx, 10
    call strtoll

    mov r13, rax

    mov rdx, [rel endptr1]
    cmp rdx, rbx
    je .invalid

    cmp byte [rdx], 0
    je .integer_case

    cmp byte [rdx], '/'
    jne .invalid

    lea rdi, [rdx + 1]
    lea rsi, [rel endptr2]
    mov rdx, 10
    call strtoll

    mov r8, rax

    mov rdx, [rel endptr1]
    lea r9, [rdx + 1]
    mov r10, [rel endptr2]

    cmp r10, r9
    je .invalid

    cmp byte [r10], 0
    jne .invalid

    mov [r12], r13
    mov [r12 + 8], r8

    mov rax, 1
    jmp .done

.integer_case:
    mov [r12], r13
    mov qword [r12 + 8], 1

    mov rax, 1
    jmp .done

.invalid:
    mov rax, 0

.done:
    pop r13
    pop r12
    pop rbx
    leave
    ret

gcd:
    mov rax, rdi
    test rax, rax
    jns .pos1
    neg rax
.pos1:
    mov rbx, rsi
    test rbx, rbx
    jns .loop
    neg rbx
.loop:
    test rbx, rbx
    jz .done_gcd
    xor rdx, rdx
    div rbx
    mov rax, rbx
    mov rbx, rdx
    jmp .loop
.done_gcd:
    ret

.do_nan:
    lea rdi, [rel fmt_str]
    lea rsi, [rel fmt_nan]
    xor rax, rax
    call printf
    jmp .exit_success

.do_plus_inf:
    lea rdi, [rel fmt_str]
    lea rsi, [rel fmt_plus_infinity]
    xor rax, rax
    call printf
    jmp .exit_success

.do_minus_inf:
    lea rdi, [rel fmt_str]
    lea rsi, [rel fmt_minus_infinity]
    xor rax, rax
    call printf
    jmp .exit_success

.print_result:
    cmp r13, 1
    je .print_only_num
    test r12, r12
    jz .print_only_num

    lea rdi, [rel fmt_frac]
    mov rsi, r12
    mov rdx, r13
    xor rax, rax
    call printf
    jmp .exit_success

.print_only_num:
    lea rdi, [rel fmt_num]
    mov rsi, r12
    xor rax, rax
    call printf

.exit_success:
    mov rax, 0
    pop rbx
    leave
    ret
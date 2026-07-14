section .data
    usage_msg db "Usage:", 10, "<program> <base> <number> <epsilon>", 10, 0
    prefix_fmt db "log_{%g}(%g) = ", 0
    result_fmt db "%.18Lg", 10, 0

section .text

global main
global disc_log

extern printf
extern fprintf
extern stderr
extern strtold

main:
    push rbp
    mov rbp, rsp
    sub rsp, 176

    mov [rbp-8], rsi

    cmp rdi, 4
    jne .usage

    ; base = strtold(argv[1], &endptr)
    mov rax, [rbp-8]
    mov rdi, [rax+8]
    lea rsi, [rbp-16]
    call strtold
    fstp tword [rbp-32]

    mov rax, [rbp-16]
    mov rcx, [rbp-8]
    cmp rax, [rcx+8]
    je .usage
    cmp byte [rax], 0
    jne .usage

    ; number = strtold(argv[2], &endptr)
    mov rax, [rbp-8]
    mov rdi, [rax+16]
    lea rsi, [rbp-16]
    call strtold
    fstp tword [rbp-48]

    mov rax, [rbp-16]
    mov rcx, [rbp-8]
    cmp rax, [rcx+16]
    je .usage
    cmp byte [rax], 0
    jne .usage

    ; epsilon = strtold(argv[3], &endptr)
    mov rax, [rbp-8]
    mov rdi, [rax+24]
    lea rsi, [rbp-16]
    call strtold
    fstp tword [rbp-64]

    mov rax, [rbp-16]
    mov rcx, [rbp-8]
    cmp rax, [rcx+24]
    je .usage
    cmp byte [rax], 0
    jne .usage

    ; result = disc_log_rec(base, number, epsilon)
    lea rdi, [rbp-32]
    lea rsi, [rbp-48]
    lea rdx, [rbp-64]
    call disc_log_rec

    fstp tword [rbp-80]

    ; print base and number as double
    fld tword [rbp-32]
    fstp qword [rbp-96]

    fld tword [rbp-48]
    fstp qword [rbp-104]

    lea rdi, [rel prefix_fmt]
    movsd xmm0, [rbp-96]
    movsd xmm1, [rbp-104]
    mov al, 2
    call printf

    ; print result as long double
    sub rsp, 16
    mov qword [rsp], 0
    mov qword [rsp+8], 0
    fld tword [rbp-80]
    fstp tword [rsp]

    lea rdi, [rel result_fmt]
    mov al, 0
    call printf

    add rsp, 16

    xor eax, eax
    leave
    ret

.usage:
    mov rdi, [rel stderr]
    lea rsi, [rel usage_msg]
    mov al, 0
    call fprintf

    mov eax, 1
    leave
    ret


disc_log:
    push rbp
    mov rbp, rsp
    sub rsp, 96

    movsd [rbp-8], xmm0
    fld qword [rbp-8]
    fstp tword [rbp-32]

    movsd [rbp-16], xmm1
    fld qword [rbp-16]
    fstp tword [rbp-48]

    movsd [rbp-24], xmm2
    fld qword [rbp-24]
    fstp tword [rbp-64]

    lea rdi, [rbp-32]
    lea rsi, [rbp-48]
    lea rdx, [rbp-64]
    call disc_log_rec

    fstp qword [rbp-8]
    movsd xmm0, [rbp-8]

    leave
    ret


disc_log_rec:
    push rbp
    mov rbp, rsp
    sub rsp, 128

    mov [rbp-8], rdi      ; pointer to a
    mov [rbp-16], rsi     ; pointer to b
    mov [rbp-24], rdx     ; pointer to epsilon

    ; if a < 1:
    ; log_a(b) = log_(1/a)(1/b)
    mov rax, [rbp-8]
    fld1
    fld tword [rax]
    fcomip st1            ; compare a ? 1
    fstp st0
    jb .a_less_than_one

    ; if b == 1:
    ; log_a(1) = 0
    mov rax, [rbp-16]
    fld tword [rax]
    fld1
    fcomip st1            ; compare 1 ? b
    fstp st0
    je .return_zero

    ; if b < 1:
    ; log_a(b) = -log_a(1/b)
    mov rax, [rbp-16]
    fld1
    fld tword [rax]
    fcomip st1            ; compare b ? 1
    fstp st0
    jb .b_less_than_one

    ; if a > b:
    ; return 1 / disc_log_rec(b, a, epsilon)
    mov rax, [rbp-16]
    fld tword [rax]       ; b

    mov rax, [rbp-8]
    fld tword [rax]       ; a

    fcomip st1            ; compare a ? b
    fstp st0
    ja .a_greater_b

    ; ratio = b / a
    mov rax, [rbp-16]
    fld tword [rax]       ; b

    mov rax, [rbp-8]
    fld tword [rax]       ; a

    fdivp st1, st0        ; b / a
    fstp tword [rbp-48]   ; ratio

    ; diff = ratio - 1
    fld tword [rbp-48]
    fld1
    fsubp st1, st0
    fstp tword [rbp-64]

    ; if diff <= epsilon:
    ; return 1
    mov rax, [rbp-24]
    fld tword [rax]       ; epsilon

    fld tword [rbp-64]    ; diff
    fcomip st1            ; compare diff ? epsilon
    fstp st0
    jbe .return_one

    ; return 1 + disc_log_rec(a, ratio, epsilon)
    mov rdi, [rbp-8]
    lea rsi, [rbp-48]
    mov rdx, [rbp-24]
    call disc_log_rec

    fld1
    faddp st1, st0
    jmp .done


.a_less_than_one:
    ; inv_a = 1 / a
    mov rax, [rbp-8]
    fld1
    fld tword [rax]
    fdivp st1, st0
    fstp tword [rbp-80]

    ; inv_b = 1 / b
    mov rax, [rbp-16]
    fld1
    fld tword [rax]
    fdivp st1, st0
    fstp tword [rbp-96]

    ; return disc_log_rec(1/a, 1/b, epsilon)
    lea rdi, [rbp-80]
    lea rsi, [rbp-96]
    mov rdx, [rbp-24]
    call disc_log_rec
    jmp .done


.b_less_than_one:
    ; inv_b = 1 / b
    mov rax, [rbp-16]
    fld1
    fld tword [rax]
    fdivp st1, st0
    fstp tword [rbp-80]

    ; return -disc_log_rec(a, 1/b, epsilon)
    mov rdi, [rbp-8]
    lea rsi, [rbp-80]
    mov rdx, [rbp-24]
    call disc_log_rec

    fchs
    jmp .done


.a_greater_b:
    ; return 1 / disc_log_rec(b, a, epsilon)
    mov rdi, [rbp-16]
    mov rsi, [rbp-8]
    mov rdx, [rbp-24]
    call disc_log_rec

    fld1
    fdivrp st1, st0
    jmp .done


.return_zero:
    fldz
    jmp .done


.return_one:
    fld1


.done:
    leave
    ret

section .note.GNU-stack noalloc noexec nowrite progbits
;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-advanced-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #t #t none #f () #f)))
;; Authors:
;; Alexander Siegler
;; Paul Konstantin Wagner
;; Yoshua Hitzel
;; Marcel Lackovic

;; ====== Secret message ======

(define turtle-code
  (list
      0     87      0   2559      0
      0      0   1148      0      0
   2987      0      0      0   2675
      0    649      0      0      0
   2675      0    649      0   2675
      0      0      0   1148      0
      0      0   2855      0    649
      0     57      0      0      0
    649      0   2855      0      0
      0   2987      0      0      0
   2675      0   2987      0      0
      0   2855      0   2987      0
      0      0   2675      0   2987
      0      0      0   2855      0
   2987      0   3249      0      0
      0   2987      0      0      0
   2855      0    649      0      0
      0   2675      0    649      0
      0      0   2855      0   2987
      0     57      0      0      0
      0   2675      0   2987      0
      0      0   2675      0   2987
      0      0      0   2675      0
    649      0      0      0   2675
      0    649      0   2675      0
      0      0     87      0      0
      0   2834      0    340      0
      0      0   2675      0   2987
      0   3249      0      0      0
   2987      0   2675      0      0
      0    649      0      0      0
   2675      0   2987      0   2675
      0      0      0   2987      0
      0      0   2675      0   2987
      0      0      0   2855      0
   2987      0      0      0   2675
     57      0      0   3249      0
      0      0      0   2675      0
   2987      0      0      0   2675
      0   2987      0      0      0
   2675      0    649      0      0
      0   2675      0    649      0
   2675      0      0      0     87
      0   2834      0      0      0
    340      0      0      0   2675
      0   2987      0   3249      0
      0      0      0   2675      0
   2987      0      0      0   2675
      0   2987      0      0      0
   2675      0    649      0      0
      0   2675      0    649      0
   2675      0      0      0     87
      0      0      0   2834      0
    340      0      0      0   2675
      0   2987      0   3249      0
      0      0   2987      0   2675
      0      0      0    649      0
      0      0   2675      0   2987
      0   2675      0      0      0
   2987      0      0      0   2675
      0   2987      0      0      0
   2855      0   2987      0      0
      0   2675     57      0      0
      0      0   2675      0   1148
      0   1957      0      0      0
    649      0      0      0   2855
      0   1148      0   2855      0
      0      0   2987      0   1957
      0      0     57      0      0
      0      0   2675      0   1148
      0   1957      0      0      0
    649      0      0      0   2855
      0   1148      0   2855      0
      0      0   2987      0   1957
      0      0     57      0      0
      0   2987      0      0      0
   2675      0    649      0      0
      0   2675      0    649      0
   2675      0      0      0   1148
      0      0      0   2855      0
    649      0     57      0      0
      0    649      0   2855      0
      0      0   2987      0   3249
      0      0      0   2987      0
      0      0   2855      0    649
      0      0      0   2675      0
    649      0      0      0   2675
      0    649      0      0      0
   1957   1957      0      0      0
   2987      0   2675      0      0
      0   2987      0      0      0
   2675     57      0      0      0
      0   2675      0   2987      0
      0      0   2855      0   2987
      0      0      0   2675      0
   2987      0      0      0   2855
      0   2987      0   3249      0
      0      0      0   2675      0
   2987      0      0      0   2855
      0   2987      0      0      0
   2675      0   2987      0      0
      0   2855      0   2987      0
   3249      0      0      0   2987
      0      0      0   2855      0
     87      0      0      0   2834
      0    649      0      0      0
   2834      0   2726      0   3249))

;; ====== Problem 1 ======

;; generate-sequence: nat nat -> (listof nat)
;; Generates a list of all numbers from i to e
;; Example: (generate-sequence 1 5) returns (list 1 2 3 4 5)
(define (generate-sequence i e)
  (if (> i e)
      empty
      (cons i (generate-sequence (+ i 1) e))))

;; prime-sieve: nat -> (listof nat)
;;
;; Returns an ordered list of numbers from 1 to n. This list contains 1 if n > 0 and
;; then only prime numbers up to n.
;;
;; This procedur uses the sieve of Eratosthenes. This means it creates a list of all numbers
;; from 2 to n and then filters all non-prime numbers.
;;
;; Example: (prime-sieve 11) = (list 1 2 3 5 7 11)
(define (prime-sieve n)
  (local
    ;; prime?: nat (listof nat) -> (listof nat)
    ;;
    ;; Checks if the given number is a prime number by testing the previous calculated prime numbers
    ;; against it. If it's not a prime number, the unmodified list will be returned.
    ;;
    ;; Example: (prime? 5 (list 1 2 3)) = (list 1 2 3 5)
    [(define (prime? check lst)
       ;; nat -> boolean
       ;;
       ;; Check if the given number can divide the check number and return after the first entry was found.
       (if (ormap (lambda (against) (= (remainder check against) 0))
                  ;; nat -> boolean
                  ;;
                  ;; According to the definitions of the sieve of eratosthenes, you only
                  ;; have to check all previous prime numbers up to square root of check.
                  ;;
                  ;; All other combinations between the square root and check are already checked
                  ;; using the previous prime numbers.
                  (filter (lambda (x) (and (<= x (integer-sqrt check))
                                           ; Ignore the 1, because it can divide every number
                                           (> x 1))) lst))
           ; It's not a prime number return the old list
           lst
           (append lst (list check))))]
    (foldl prime? empty (generate-sequence 1 n))))

;; Tests
(check-expect (prime-sieve 0) empty)
(check-expect (prime-sieve 1) '(1))
; Last number included
(check-expect (prime-sieve 11) (list 1 2 3 5 7 11))
; Last number not included
(check-expect (prime-sieve 12) (list 1 2 3 5 7 11))



;; ====== Problem 2 ======

;; prime-factors: nat -> (listof nat)
;;
;; Returns a list of prime numbers how the given number can be created with multiplication.
;;
;; If the given number is already a prime number, the returned list will only contain
;; one element (the prime number itself). Otherwise the list contains two numbers with the higher one
;; at the last position.
;; 
;; Example: (prime-factors 11) = (list 2 11) 
(define (prime-factors n)
  (local
    [(define primes (prime-sieve n))]
    ;; prime (listof number) -> (listof number)
    ;; Checks the the given prime number multiplicated another prime number
    ;; could give the same value as n. It returns how n can be created or empty if not possible.
    (foldl (lambda (prime1 def)
             ;; prime (listof number) -> (listof number)
             ;; Multiplicates the given prime number with the first prime number
             ;; and returns how n can be created or empty if not possible.
             (foldl (lambda (prime2 def) (cond
                                           [(= (* prime1 prime2) n) (if (= prime2 1)
                                                                        (list prime1)
                                                                        (list prime2 prime1))]
                                           [else def]))
                    def primes))
           empty primes)))

;; Tests
; cannot be created only with two prime numbers
(check-expect (prime-factors 12) empty)
(check-expect (prime-factors 22) (list 2 11))
(check-expect (prime-factors 9) (list 3 3))
(check-expect (prime-factors 10) (list 2 5))
; prime-number
(check-expect (prime-factors 7) (list 7))
(check-expect (prime-factors 3) (list 3))



;; ====== Problem 3 ======

;; find-second-key: nat nat nat -> nat
;;
;; Finds a valid private key for a given public key and
;; the prime numbers.
;;
;; Example: (find-second-key 11 7 3) = 11
(define (find-second-key public-key p q)
  (local
    [(define phi (* (- p 1) (- q 1)))
     ;; calc-private: nat -> nat
     ;; Finds the first private key which is greater than the given number
     ;; Example: (calc-private 1) = 3
     ;; if public = 3, p = 3 and q = 5
     (define (calc-private private)
       (if (= (remainder (* private public-key) phi) 1)
           private
           (calc-private (add1 private))))]
    (calc-private 1)))

;; Tests
(check-expect (find-second-key 11 7 3) 11)
(check-expect (find-second-key 3 3 5) 3)



;; ====== Problem 4 ======

;; break-code: nat nat -> (listof nat)
;; Given an RSA public key, determines the prime factors p,q and private key d
;; by factorization and returns them as (list p q d)
(define (break-code n e)
  (local
    ((define pq (prime-factors n))
     (define p (first pq))
     (define q (first (rest pq)))
     (define d (find-second-key e p q)))
    (list p q d)))

;; power-mod: nat nat nat -> nat
;; Solves (base^exponent) mod modulus efficiently by Fermat's algorithm
(define (power-mod base exponent modulus)
  (if
   (= modulus 1)
   0
   (foldl
    (lambda (e-prime c) (modulo (* c base) modulus))
    1
    (generate-sequence 1 exponent))))

;; decrypt: (listof nat) nat nat -> (listof nat)
;; Decrypts an RSA-encrypted message (given a private key and the modulus)
(define (decrypt lst n d)
  (map
   (lambda (c) (power-mod c d n))
   lst))

;; Turtle command list format: Move Turn Draw, all concatenated in a flat list

(require graphics/turtles)
;; execute-turtle-sequence: (listof number) -> void
;;
;; Interprets a list of turtle commands (format: see above) and executes them sequentially
;; 
;; Example: (execute-turtle-sequence (decrypt turtle-code 3337 (break-code 3337 2089)))
(define (execute-turtle-sequence seq)
  (begin
    [turtles true]
    [local
      ;; execute: (listof number) nat -> void
      ;;
      ;; Executes the sequentially the turtle commands. If the list is smaller than
      ;; three commands it will fail safetly.
      ;;
      ;; Example: (execute (list 20 90 5 15))
      [(define (execute lst type)
         (if (empty? lst)
             void
             (begin
               (cond
                 [(= type 0) (move (first lst))]
                 [(= type 1) (turn (first lst))]
                 [(= type 2) (draw (first lst))])
               (execute (rest lst) (remainder (add1 type) 3)))))]
      (execute seq 0)]))
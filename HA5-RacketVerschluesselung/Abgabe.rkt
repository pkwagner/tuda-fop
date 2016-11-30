;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-advanced-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #t #t none #f () #f)))
(require graphics/turtles)

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
    ;; prime?: nat -> boolean
    ;;
    ;; Checks if a given number is a prime number by checking all previous numbers if they can divide
    ;; the number.
    ;;
    ;; Example: (prime? 7) = true
    [(define (prime? check)
       ; Filter all non-prime numbers
       (foldl
        ;; nat nat -> boolean
        ;;
        ;; Checks if any element is 
        (lambda (against old) (if old
                                  ; If one element can divide "check", it's not prime number
                                  (not (= (remainder check against) 0))
                                  ; Previously found one element that could divide "check"
                                  false))
        true (generate-sequence 2 (- check 1))))]
    (filter prime? (generate-sequence 1 n))))

;; Tests
(check-expect (prime-sieve 0) empty)
(check-expect (prime-sieve 1) '(1))
; Last number included
(check-expect (prime-sieve 11) (list 1 2 3 5 7 11))
; Last number not included
(check-expect (prime-sieve 12) (list 1 2 3 5 7 11))



;;; ====== Problem 2 ======
;
;;; prime-factors: 
;;; 
;;; Example: 
;(define (prime-factors n)
;  ...)
;
;;; Tests
;    
;;; ====== Problem 3 ======
;
;;; find-second-key:
;;; 
;;; Example: 
;(define (find-second-key first-key p q)
;  ...)
;
;;; Tests
;
;;; ====== Problem 4 ======
;
;;; break-code: nat nat -> (listof nat)
;;; Given an RSA public key, determines the prime factors p,q and private key d
;;; by factorization and returns them as (list p q d)
;(define (break-code n e)
;  (local
;    ((define pq (prime-factors n))
;     (define p (first pq))
;     (define q (first (rest pq)))
;     (define d (find-second-key e p q)))
;    (list p q d)))
;
;;; power-mod: nat nat nat -> nat
;;; Solves (base^exponent) mod modulus efficiently by Fermat's algorithm
;(define (power-mod base exponent modulus)
;  (if
;   (= modulus 1)
;   0
;   (foldl
;    (lambda (e-prime c) (modulo (* c base) modulus))
;    1
;    (generate-sequence 1 exponent))))
;
;;; decrypt: (listof nat) nat nat -> (listof nat)
;;; Decrypts an RSA-encrypted message (given a private key and the modulus)
;(define (decrypt lst n d)
;  (map
;   (lambda (c) (power-mod c d n))
;   lst))
;
;;; Turtle command list format: Move Turn Draw, all concatenated in a flat list
;
;;; execute-turtle-sequence: (listof nat) -> (void)
;;; Interprets a list of turtle commands (format: see above) and executes them sequentially
;(define (execute-turtle-sequence seq)
;  ...)
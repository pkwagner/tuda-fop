;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Authors:
;; Alexander Siegler
;; Paul Konstantin Wagner
;; Yoshua Hitzel
;; Marcel Lackovic

;; ====== FOP Uebung 3 (Hausuebung) ======

;; textbook models selected data about a textbook
;; title: symbol - the book's title
;; utility: number - measuring how 'useful' the book is for learning (integer; 0-100%)
;; price: number - in Euro
;; lecture: symbol - the lecture for which this book was meant
(define-struct textbook (title utility price subject))

;; list of textbooks helpful for the first BSc. Informatik semester at TU Darmstadt
(define htdp       (make-textbook 'HowToDesignPrograms 90 45 'FOP))
(define java-insel (make-textbook 'JavaIstAuchEineInsel 70 30 'FOP))
(define analysis   (make-textbook 'AnalysisI 100 45 'MatheI))
(define lin-alg    (make-textbook 'LineareAlgebra 90 40 'MatheI))
(define ddca       (make-textbook 'DigitalDesignAndComputerArchitecture 80 30 'DT))
(define microarch  (make-textbook 'Microarchitecture 60 25 'DT))
(define theo-inf   (make-textbook 'TheoretischeInformatik 65 50 'AfSE))
(define tur-compl  (make-textbook 'TuringCompleteness 75 65 'AfSE))

;; additional textbooks for use in high school
(define faust      (make-textbook 'Faust 50 12 'Deutsch))
(define geometrie  (make-textbook 'Geometrie 80 25 'Mathe))

;; sample book lists for testing and examples
;; list with a single textbook
(define single-textbook (list faust))
;; list of German college book selection
(define small-textbooks (list faust geometrie))
;; list of CS-related books
(define avail-textbooks (list htdp java-insel analysis lin-alg
                              ddca microarch theo-inf tur-compl))

;; A x-set represents a set (a list of unique items) of X
;; size: number - the number of items
;; items: (listof X) - a list of unique items of type X
(define-struct x-set (size items))

;; ====== Problem 5.1 ======


;(define (x-set-member? set x pred)
;  (local [(define (f x) (+ x 5))
;          (define (t x) (- x 5))]
;    (f 1)
;    ))

;; x-set-member?: x-set X (X X -> boolean) -> boolean
;;
;; Checks if a element of type X is in the struct x-set
;; using the given predicate
;;
;; Ex: (x-set-member? (make-x-set 2 (list 1 2)) 2 =) -> true
(define (x-set-member? set x pred)
  (local [(define items (x-set-items set))
          ; Use a function instead of a variable in order to check empty? first
          (define (rest-set set) (make-x-set (- 1 (x-set-size set)) (rest items)))
          ]
    (cond
      [(empty? items) false]
      [(pred x (first items)) true]
      [else (x-set-member? (rest-set set) x pred)])))


;; Tests
(check-expect (x-set-member? (make-x-set 0 empty) 0 =) false)
(check-expect (x-set-member? (make-x-set 2 (list 1 2)) 2 =) true)
(check-expect (x-set-member? (make-x-set 2 (list 1 2)) 3 =) false)



;; x-set-insert: x-set X (X X -> boolean) -> x-set
;;
;; Inserts x into the set if the predicate evaluates false
;; and so the element isn't in the set yet.
;;
;; The set containing the new element at the beginning will be returned.
;;
;; Ex: (x-set-insert (make-x-set 2 (list 1 2)) 3 =) -> (make-x-set 3 (list 1 2 3)
(define (x-set-insert set x pred)
  (local [(define (insert x)
            (make-x-set (+ 1 (x-set-size set)) (cons x (x-set-items set))))
          ]
    (cond
      [(x-set-member? set x pred) set]
      [else (insert x)])))


;; Tests
(check-expect (x-set-insert (make-x-set 0 empty) 1 =) (make-x-set 1 (list 1)))
; No change
(check-expect (x-set-insert (make-x-set 1 (list 1)) 1 =) (make-x-set 1 (list 1)))
; New element added to a non-empty list
(check-expect (x-set-insert (make-x-set 1 (list 'B)) 'A symbol=?) (make-x-set 2 (list 'A 'B)))



;; ====== Problem 5.2 ======

;; A decision-tree-node models a single node in the decision tree and is either
;; 1. empty or
;; 2. contains the following data:
;;  - book: textbook - the textbook included (or not included) by decision of this node
;;  - left: decision-tree-node - root of the left subtree, ignoring the textbook ("0")
;;  - right: decision-tree-node - root of the right subtree, adopting the textbook ("1")
(define-struct decision-tree-node (book left right))

;; TODO implement 5.2

;; ====== Problem 5.3 ======

;; TODO implement 5.3

;; ====== Problem 5.4 ======

;; sum-up-utility: (listof textbook) (listof boolean) -> number
;; Sums up the utility of all textbooks included in the solution
;; Example: (check-expect (add-up-utility small-textbooks (list false false)) 0)
(define (sum-up-utility textbooks solution)
  (cond
    [;; if the solution contains no elements...
     (empty? solution)
     ;; ... then it has a utility value of 0
     0]
    [;; check if the first book was chosen in solution
     (first solution)
     ;; yes, so add its price and continue with the recursion
     (+ (textbook-utility (first textbooks))
        (sum-up-utility (rest textbooks) (rest solution)))]
    [;; the first book as not chosen, so ignore it...
     else
     ;; but do not forget to recurse over the rest of the books and solution!
     (sum-up-utility (rest textbooks) (rest solution))]))

;; Tests (you do NOT have to write your own tests for this!)
(check-expect (sum-up-utility empty empty) 0)
(check-expect (sum-up-utility small-textbooks (list false false)) 0)
(check-expect (sum-up-utility small-textbooks (list true false)) 50)
(check-expect (sum-up-utility small-textbooks (list false true)) 80)

;; TODO implement 5.4

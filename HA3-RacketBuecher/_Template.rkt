;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-reader.ss" "lang")((modname multi-constraint-knapsack-template) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
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

;; x-set-member?: 
;; 
;; Example:
(define (x-set-member? set item equal-op)
  ...)

;; Tests


;; x-set-insert: 
;; 
;; Example: 
(define (x-set-insert set item equal-op)
  ...)

;; Tests


;; Specializations

;; symbol-set-insert: set symbol -> set
;; see x-set-insert
(define (symbol-set-insert set item)
  ...)

;; Tests


;; ====== Problem 5.2 ======

;; A decision-tree-node models a single node in the decision tree and is either
;; 1. empty or
;; 2. contains the following data:
;;  - book: textbook - the textbook included (or not included) by decision of this node
;;  - left: decision-tree-node - root of the left subtree, ignoring the textbook ("0")
;;  - right: decision-tree-node - root of the right subtree, adopting the textbook ("1")
(define-struct decision-tree-node (book left right))

;; build-decision-tree: 
;; 
;; Example: 
(define (build-decision-tree textbooks)
  ...)

;; Tests
;; Sample test to allow you to see if your code works as excepted
;; does NOT count as "one of the two tests to be provided"!
;; 2 books => need complete binary tree
(check-expect (build-decision-tree small-textbooks)
              (make-decision-tree-node faust
               (make-decision-tree-node geometrie empty empty)
               (make-decision-tree-node geometrie empty empty)))

;; ====== Problem 5.3 ======
;; Solution format: list of booleans, same order as input textbook list

;; satisfies-constraints?: 
;; 
;; Example: 
(define (satisfies-constraints? all-textbooks solution-candidate num-subjects budget)
  ...)

;; Tests
;; Sample tests provided to help you determine if everything works fine
;; Of course, they do not count as "your" tests.
;; check if price limit is satisfied
(check-expect (satisfies-constraints?
               avail-textbooks (list true false true) 2 90) true)
;; check if categories are counted correctly
(check-expect (satisfies-constraints?
               avail-textbooks (list true false true) 3 90) false)
(check-expect (satisfies-constraints?
               avail-textbooks (list true false true false true false false true)
               4 190) true)


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


;; optimize-selection: 
;; 
;; Example: 
(define (optimize-selection textbooks decision-root num-subjects budget)
  ...)

;; Tests
;; Sample tests provided to help you determine if everything works fine
;; Of course, they do not count as "your" tests.
;; check correctness
(check-expect (optimize-selection small-textbooks
                                  (build-decision-tree small-textbooks) 1 12)
              (list true false))
(check-expect (optimize-selection small-textbooks
                                  (build-decision-tree small-textbooks) 1 25)
              (list false true))
(check-expect (optimize-selection small-textbooks
                                  (build-decision-tree small-textbooks) 1 40)
              (list true true))
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

;; x-set-member? : x-set X (X Y -> boolean) -> boolean
;;
;; Checks if a element of type X is in the struct x-set
;; using the given predicate
;;
;; Example: (x-set-member? (make-x-set 2 (list 1 2)) 2 =) = true
(define (x-set-member? set x pred)
  (local
    ;; exists? : Y -> boolean
    ;;
    ;; Checks if the given other element and the searching element x
    ;; returns true by using the given predicate from x-set-member?
    ;;
    ;; Example: (exists? 5) = true (if you search for x=5 and your
    ;;                        predicate is the '=')
    [(define (exists? other) (pred x other))]
    (ormap exists? (x-set-items set))
  )
)


;; Tests
(check-expect (x-set-member? (make-x-set 0 empty) 0 =) false)
(check-expect (x-set-member? (make-x-set 2 (list 1 2)) 2 =) true)
(check-expect (x-set-member? (make-x-set 2 (list 1 2)) 3 =) false)
; Should return true even if there are duplicate entries
(check-expect (x-set-member? (make-x-set 3 (list 1 2 2)) 2 =) true)


;; x-set-insert : x-set X (X Y -> boolean) -> x-set
;;
;; Inserts x into the set if the predicate evaluates false
;; and so the element isn't in the set yet.
;;
;; The set containing the new element at the beginning will be returned.
;;
;; Example: (x-set-insert (make-x-set 2 (list 1 2)) 3 =) = (make-x-set 3 (list 1 2 3))
(define (x-set-insert set x pred)
  (local
    ;; insert-start : x -> x-set
    ;;
    ;; Inserts the element x at the beginning of the set and therefore increases the size
    ;;
    ;; Example: (insert-start 5) = (make-x-set 3 (list 5 0 1)) 
    ;;                             for set = (2 (list 0 1)) from x-set-insert
    [(define (insert-start x)
       (make-x-set (+ (x-set-size set) 1) (cons x (x-set-items set))))]
    
    (cond
      [(x-set-member? set x pred) set]
      [else (insert-start x)])))


;; Tests
(check-expect (x-set-insert (make-x-set 0 empty) 1 =) (make-x-set 1 (list 1)))
; No change
(check-expect (x-set-insert (make-x-set 1 (list 1)) 1 =) (make-x-set 1 (list 1)))
; New element added to a non-empty list
(check-expect (x-set-insert (make-x-set 1 (list 'B)) 'A symbol=?) (make-x-set 2 (list 'A 'B)))


;; symbol-set-insert : x-set symbol -> x-set
;;
;; Inserts a symbol into the set if the element isn't in the set yet
;; The set containing the new element at the beginning will be returned.
;;
;; Example: (symbol-set-insert (make-x-set 1 (list 'B)) 'A) = (make-x-set 2 (list 'A 'B)
(define (symbol-set-insert set symbol)
        (x-set-insert set symbol symbol=?))

;; Tests
(check-expect (symbol-set-insert (make-x-set 0 empty) 'A) (make-x-set 1 (list 'A)))
; No change
(check-expect (symbol-set-insert (make-x-set 1 (list 'A)) 'A) (make-x-set 1 (list 'A)))
; New element added to a non-empty list
(check-expect (symbol-set-insert (make-x-set 1 (list 'B)) 'A) (make-x-set 2 (list 'A 'B)))



;; ====== Problem 5.2 ======

;; A decision-tree-node models a single node in the decision tree and is either
;; 1. empty or
;; 2. contains the following data:
;;  - book: textbook - the textbook included (or not included) by decision of this node
;;  - left: decision-tree-node - root of the left subtree, ignoring the textbook ("0")
;;  - right: decision-tree-node - root of the right subtree, adopting the textbook ("1")
(define-struct decision-tree-node (book left right))

;; build-decision-tree: (listof textbook) -> decision-tree-node
;;
;; Builds a decision tree based on the given list of textbooks.
;;
;; Example: (build-decision-tree small-textbooks) ->
;;   Following-Tree:
;;        faust
;;       /     \
;; geometrie  geometrie
(define (build-decision-tree textbooks)
  (cond
    [(empty? textbooks) empty]
    [else (local
            ; Builds a child node without the parent
            [(define child (build-decision-tree (rest textbooks)))]
            (make-decision-tree-node (first textbooks) child child))]))

;; Tests
(check-expect (build-decision-tree empty) empty)
(check-expect (build-decision-tree single-textbook)
              (make-decision-tree-node faust empty empty))
(check-expect (build-decision-tree (list faust geometrie htdp))
              (make-decision-tree-node faust
                                       (make-decision-tree-node geometrie
                                                                (make-decision-tree-node htdp empty empty)
                                                                (make-decision-tree-node htdp empty empty))
                                       (make-decision-tree-node geometrie
                                                                (make-decision-tree-node htdp empty empty)
                                                                (make-decision-tree-node htdp empty empty))))



;; ====== Problem 5.3 ======


;; satisfies-constraints?: (listof textbook) (listof boolean) number number -> boolean
;;
;; Checks if the given list of text books with their chose candidates are
;; * related to to a specified number of subjects or are higher
;; * The total price is lower or equal to the budget
;;
;; Example: (satisfies-constraints? avail-textbooks (list true) 1 1000) -> true
(define (satisfies-constraints? all-textbooks solution-candidate num-subjects budget)
  (local
    ;; filter-books:: (listof textbooks) (listof boolean) -> (listof textbooks)
    ;;
    ;; Checks if a textbook is a solution candidate by checking the boolean list if
    ;; there is a true at the same position
    ;;
    ;; If boolean list is shorter, it will assume the remaining are no solution candidates
    ;;
    ;; Example: (filter-books (list htdp ddca) (list false true)) -> (list ddca)
    [(define (filter-books books candidates)
       (cond
         [(or (empty? books) (empty? candidates)) empty]
         ; First book is selected - add it to the list of selected books
         [(first candidates) (cons (first books)
                                   (filter-books (rest books) (rest candidates)))]
         ; Remove the first book, because it's not selected
         [else (filter-books (rest books) (rest candidates))]
       )
     )
     
     ;; count-price:: (listof books) -> number
     ;;
     ;; Grabs the price of all books and sums it up
     ;;
     ;; Example: (count-price (list htdp ddca)) -> 85
     (define (count-price books) (foldl + 0 (map textbook-price books)))
     
     ;; symbol-set-insert-2:: symbol (listof symbol)
     ;;
     ;; Creates an alias for invoking the symbol-set-insert procedure with
     ;; a different parameter order in order to make usable by fold*
     ;;
     ;; Example: (symbol-set-insert2 'A (make-x-set 1 (list 'B))) -> (make-x-set 2 (list 'A 'B))
     (define (symbol-set-insert-2 x set) (symbol-set-insert set x))
     
     ;; subject-set:: (listof books) -> x-set
     ;;
     ;; Maps a list of books to a x-set which contain a unique list of subjects
     ;; as symbols. 
     ;;
     ;; Example: (subject-set (list htdp)) -> (make-x-set 1 (list 'FOP))
     (define (subject-set books) (foldl symbol-set-insert-2
                                        (make-x-set 0 empty)
                                        (foldl cons empty
                                               (map textbook-subject books))))

     ;; Define selected books as filtered-books
     (define filtered-books (filter-books all-textbooks solution-candidate))]
    
    (cond
      [(empty? filtered-books) false]
      [else (and
             ; the books cost less or equal than our budget
             (>= budget (count-price filtered-books))
             ; the subject number of books are greater or equal to our requirement
             (<= num-subjects (x-set-size (subject-set filtered-books))))])))

;; Tests
; No book selected
(check-expect (satisfies-constraints? avail-textbooks empty 10 10) false)
; No budget
(check-expect (satisfies-constraints? avail-textbooks (list true false) 1 0) false)
; Not enough subjects
(check-expect (satisfies-constraints? avail-textbooks (list true) 2 1000) false)
; Should return true (min one subject and enough budget)
(check-expect (satisfies-constraints? avail-textbooks (list true false true) 1 1000) true)
; Equal to the required subjects
(check-expect (satisfies-constraints? avail-textbooks (list true false true) 2 1000) true)


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


;; optimize-selection : (listof textbook) decision-tree-node number number -> (listof boolean)
;;
;; Builds all possible tree-nodes matching satisfies-constraints? and returns the best possibility
;;
;; Example: (optimize-selection small-textbooks '() 1 12) = (list true false)
(define (optimize-selection textbooks solution-tree num-subjects budget)
  (local [
     (define solution-tree-reversed (reverse solution-tree))
     ;; get-all-solutions : (listof textbook) decision-tree-node -> (listof (listof boolean))
     ;;
     ;; Returns all tree-nodes based on decision-tree-node (doesn't check if they satisfy constraints)
     ;;
     ;; Example: (get-all-solutions small-textbooks (list true)) -> (list (list true false) (list true true))
     (define (get-all-solutions textbooks solution-tree)
       (if (< (length solution-tree) (length textbooks))
           (append (get-all-solutions textbooks (cons false solution-tree)) (get-all-solutions textbooks (cons true solution-tree)))
           (cons (reverse solution-tree) empty))
     )

     ;; satisfies-constraints-filter : decision-tree-node -> boolean
     ;;
     ;; Wrapper for satisfies-constraints? that needs only a decision-tree-node and can be set into a filter
     ;; ...
     ;;
     ;; Example: ...
     (define (satisfies-constraints-filter solution)
             (satisfies-constraints? textbooks solution num-subjects budget)
     )
     
     (define (highest-utility-tree solution best-solution)
             (if (> (sum-up-utility textbooks solution) (sum-up-utility textbooks best-solution))
                 solution
                 best-solution
             )
     )
    ]
    
    (foldl
      highest-utility-tree
      empty
      (filter satisfies-constraints-filter
              (get-all-solutions textbooks solution-tree-reversed)
      )
    )
  )
)

;; Tests
; Tests from exercise
(check-expect (optimize-selection small-textbooks empty 2 10) empty)
(check-expect (optimize-selection small-textbooks empty 1 12) (list true false))
(check-expect (optimize-selection small-textbooks empty 2 40) (list true true))
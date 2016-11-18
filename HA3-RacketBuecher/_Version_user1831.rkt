
(define-struct textbook (title utility price subject))
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





;;5.1

(define-struct x-set (size items))

(define (x-set-member? set xitem op)
  (cond
    [(not (empty? (x-set-items set)))
     (
      local
       [
        (define items (x-set-items set))
        (define size (x-set-size set))
        (define curitem  (first items))
        (define rest-x (rest items))
        (define newset (make-x-set (- size 1) rest-x))
       ]
       (cond
         [(empty? items) empty]
         [(op curitem xitem) true]
         [else (x-set-member? newset xitem op)]
       )
    )
   ]
   [else false]
 )
)

(check-expect (x-set-member? (make-x-set 0 empty) 0 =) false)
(check-expect (x-set-member? (make-x-set 2 (list 1 2)) 2 =) true)
(check-expect (x-set-member? (make-x-set 2 (list 1 2)) 3 =) false)
(check-expect (x-set-member? (make-x-set 3 (list 1 2 2)) 2 =) true)



(define (x-set-insert set x op)
  (cond
    [(not (x-set-member? set x op)) (make-x-set (+ (x-set-size set) 1) (cons x (x-set-items set)))]
    [else set]
  )
)

(check-expect (x-set-insert (make-x-set 0 empty) 1 =) (make-x-set 1 (list 1)))
(check-expect (x-set-insert (make-x-set 1 (list 1)) 1 =) (make-x-set 1 (list 1)))
(check-expect (x-set-insert (make-x-set 1 (list 'B)) 'A symbol=?) (make-x-set 2 (list 'A 'B)))



(define (symbol-set-insert set symbol)
(x-set-insert set symbol symbol=?))



;;5.2

(define-struct decision-tree-node (book left right))



(define (build-decision-tree textbooks)
  (cond
    [(empty? textbooks) empty]
    [else
     (make-decision-tree-node (first textbooks) (build-decision-tree (rest textbooks)) (build-decision-tree (rest textbooks)))
    ]
  )
)


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





;;5.3

(define (satisfies-constraints? textbooks bools min budget)
  (cond
    [(or (empty? textbooks) (empty? bools))   false]
    [else 
     (local
       [(define tb (first textbooks))
        (define restT (rest textbooks))
        (define restB (rest bools))
        (define result (and (boolean=? (first bools) true) (>= budget (textbook-price tb))))
       ]
       (cond
         [(and (empty? restB) (or (= min 0) (and (= min 1) result))) true]
         [(and (empty? restB) (> min 0)) false]
         [(and (not (empty? restB)) result) (satisfies-constraints? restT restB (- min 1) (- budget (textbook-price tb)))]
         [else (satisfies-constraints? restT restB min budget) ]
       )
     )
    ]
   )
)

(check-expect (satisfies-constraints? avail-textbooks empty 10 10) false)

(check-expect (satisfies-constraints? avail-textbooks (list true false) 1 0) false)

(check-expect (satisfies-constraints? avail-textbooks (list true) 2 1000) false)

(check-expect (satisfies-constraints? avail-textbooks (list true false true) 1 1000) true)
(check-expect (satisfies-constraints? avail-textbooks (list true false true) 2 1000) true)




;;5.4

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




;; TODO 5.4 is missing!!!


;; Constants
(define tolerance 0.001)
(define base-interest 0.005)

;; TODOs
;; * Vertraege
;; * Tests
;; * Korrekter Header/definierte sprache
;; * Bisschen schoenere Struktur (insbesondere saving-plans)
;; * Dynamischer (braucht man das ueberhaupt? Ja)
;; * TODOs in code...

;; 9.1
;; add-interest:: number number -> number
;; returns the new capital after adding the interest rate
;; Example: (add-interest 10000 0.025) should return 10250
(define (add-interest capital interestRate)
  (cond	[(< interestRate 0) (error "no negative interest rates allowed")]
        [(<= capital 0) capital ] 
        [(> capital 0) (* capital (+ interestRate 1))]
  )
)
	

;; Tests
(check-error (add-interest 100 -0.01) "no negative interest rates allowed")
(check-expect (add-interest 100 0.01) 101)
(check-expect (add-interest 0 0.01) 0)
(check-expect (add-interest -1 0.01) -1)

;; 9.2
;; average-yearly-return:: number number number -> number
;; returns the average of yearly interest rates based 
;; Ex: (average-yearly-return 3000 1000 1) should return 2
(define (average-yearly-return capitalBefore capitalAfter duration)
  (if (> duration 0)
	(expt (- (/ capitalBefore capitalAfter) 1) (/ 1 duration))
        (error "no negative duration allowed")
  )
)


;; Tests
(check-within (average-yearly-return 100 101 1) 0.01 tolerance)
(check-within (average-yearly-return 100 225 2) 0.5 tolerance)

(average-yearly-return 3000 2000 5)

;; 9.3
;; savings-plan-a:: number number -> number
;; 
;; 
;; 
;; Ex:
;; TODO round (floor) duration
(define (savings-plan-a capital duration)
	(if (= duration 0)
            capital
            (savings-plan-a
             (add-interest capital
                (cond   [(= duration 1) base-interest]
                        [(= duration 2) (* base-interest 2)]
                        [(= duration 3) (* base-interest 3)]
                )
             )
             (- duration 1)
            )
    )
)

;; Tests
(check-error (savings-plan-a 100 0) "invalid runtime")
(check-error (savings-plan-a 100 4) "invalid runtime")
(check-expect (savings-plan-a 100 1) 105)
(check-expect (savings-plan-a 100 1.5) 105)
;; Includes the bonus
(check-within (savings-plan-a 100 3) 133.027 tolerance)

;; TODO round (floor) duration
;; savings-plan-b::
;;
;; 
;; 
;; Ex:
(define (savings-plan-b capital duration)
  (if (= duration 0)
      capital
      (savings-plan-b (+ (add-interest capital base-interest (if (= duration 3) 30 0)) (- duration 1))))
  )

;; Tests
(check-error (savings-plan-b 100 0) "invalid runtime")
(check-error (savings-plan-b 100 4) "invalid runtime")
(check-expect (savings-plan-b 100 1) 100.5)
(check-expect (savings-plan-b 100 1.5) 100.5)
(check-within (savings-plan-b 100 3) 101.507 tolerance)

;; 9.4
;; best-savings-plan::
;;
;; 
;; 
;; Ex:
(define (best-savings-plan capital duration)
	(if (>= (average-yearly-return capital (savings-plan-a capital duration) duration)
                (average-yearly-return capital (savings-plan-b capital duration) duration))
            'SavingsPlanA
            'SavingsPlanB))

;; Tests
(check-expect (best-savings-plan 100 1) 'SavingsPlanA)
(check-expect (best-savings-plan 100 3) 'SavingsPlanB)
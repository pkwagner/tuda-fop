;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
;;------------------------------------------------------------;;
;; TODOs
;; * Bisschen schoenere Struktur (insbesondere saving-plans)
;; * Dynamischer (braucht man das ueberhaupt? Ja)
;; * TODOs in code...
;;------------------------------------------------------------;;



;; Constants
(define tolerance 0.001)
(define base-interest 0.005)



;; 9.1
;; add-interest:: number number -> number
;; 
;; Returns the new capital after adding the interest rate
;; 
;; Example: (add-interest 10000 0.025) should return 10250
(define (add-interest capital interestRate)
  (cond [(< interestRate 0) (error "no negative interest rates allowed")]
        [(<= capital 0) capital] 
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
;;
;; Returns the average of yearly interest rates based on the start and end capital
;; and the duration in years
;;
;; Ex: (average-yearly-return 3000 1000 1) = 2
(define (average-yearly-return capitalBefore capitalAfter duration)
  (if (and (> duration 0) (> capitalAfter capitalBefore))
    (- (expt (/ capitalAfter capitalBefore) (/ 1 duration)) 1)
    (error "no negative duration allowed")
  )
)

;; Tests
(check-within (average-yearly-return 100 101 1) 0.01 tolerance)
(check-within (average-yearly-return 100 225 2) 0.5 tolerance)
(check-error (average-yearly-return 100 100 -1) "no negative duration allowed")



;; 9.3

;; savings-plan-a:: number number -> number
;;
;; Berechnet das Endkapital nach Ablauf einer Zeitperiode 
;; Laufzeit (für Jahre) gemäß des Sparplans A 
;; von einem gewissen Startkapital start-kapital
;;
;; Ex: 100€ Startkapital für 3 Jahre
;;   (savings-plan-b 100 3) = 133.027 Endkapital
(define (savings-plan-a capital duration)
  (cond
    [(or (< duration 1) (> duration 3)) (error "invalid runtime")]
    [(= (floor duration) 1) (add-interest capital base-interest)]
    [(= (floor duration) 2) (add-interest (savings-plan-a capital (- duration 1)) (* base-interest 2))]
    [(= (floor duration) 3) (add-interest (savings-plan-a capital (- duration 1)) (* base-interest 3))]))

;; Tests
(check-error (savings-plan-a 100 0) "invalid runtime")
(check-error (savings-plan-a 100 4) "invalid runtime")
(check-expect (savings-plan-a 100 1) 100.5)
(check-expect (savings-plan-a 100 1.5) 100.5)
(check-within (savings-plan-a 100 3) 103.027 tolerance)



;; savings-plan-b:: number number -> number
;;
;; Berechnet das Endkapital nach Ablauf einer Zeitperiode 
;; Laufzeit (für Jahre) gemäß des Sparplans B 
;; von einem gewissen Startkapital start-kapital
;; 
;; Ex: 100€ Startkapital für 3 Jahre
;;  (savings-plan-b 100 3) = 101.507 Endkapital
(define (savings-plan-b capital duration)
  (cond
    ; 3.5 Jahre sollten auch erlaubt werden, da laut Aufgabenstellung nur nicht mehr als 3 komplette Jahre erlaubt sind
    [(or (< duration 1) (> (floor duration) 3)) (error "invalid runtime")]
    [(= (floor duration) 1) (add-interest capital base-interest)]
    [else (+ (add-interest (savings-plan-b capital (- (floor duration) 1)) base-interest)
             (if (= (floor duration) 3)
                 30
                 0))]))

;; Tests
(check-error (savings-plan-b 100 0) "invalid runtime")
(check-error (savings-plan-b 100 4) "invalid runtime")
(check-expect (savings-plan-b 100 1) 100.5)
(check-expect (savings-plan-b 100 1.5) 100.5)
;; Includes the bonus
(check-within (savings-plan-b 100 3) 131.507 tolerance)



   )
)


;; 9.4
;; best-savings-plan:: number number -> symbol
;;
;; Returns the best saving plan for a certain start captial and a duration in years
;;
;; If the plans are identical the first one (A) will be returned
;; 
;; Ex: (best-savings-plan 100 3) = 'SavingsPlanB 
(define (best-savings-plan capital duration)
  (if (>= (average-yearly-return capital (savings-plan-a capital duration) duration) (average-yearly-return capital (savings-plan-b capital duration) duration))
    'SavingsPlanA
    'SavingsPlanB
  )
)

;; Tests
(check-expect (best-savings-plan 100 1) 'SavingsPlanA)
(check-expect (best-savings-plan 100 2) 'SavingsPlanA)
(check-expect (best-savings-plan 100 3) 'SavingsPlanB)
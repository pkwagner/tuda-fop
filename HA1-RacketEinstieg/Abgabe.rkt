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
  (if (= duration 0)
    capital
    (savings-plan-a
      (add-interest capital
        (cond [(= duration 1) base-interest]
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
  (if (= duration 0)
    capital
    (savings-plan-b
      (+ (add-interest capital base-interest) (if (= duration 3) 30 0))
      (- duration 1)
    )
  )
)

;; Tests
(check-error (savings-plan-b 100 0) "invalid runtime")
(check-error (savings-plan-b 100 4) "invalid runtime")
(check-expect (savings-plan-b 100 1) 100.5)
(check-expect (savings-plan-b 100 1.5) 100.5)
;; Includes the bonus
(check-within (savings-plan-b 100 3) 131.507 tolerance)



;; round-down:: number -> number
;;
;; Rundet den Wert, sodass die Nachkommastellen entfernt werden
;;
;; Ex: round-down(1.5) = 1
;;     round-down(2.5) = 2
(define (round-down val) 
  (if (integer? val)
    ;; Zahl muss nicht gerunden werden. Es ist keine Dezimalzahl
    val
    ;; Runde die Zahl
    (round (- val 0.5))
   )
)

;; Tests
(check-expect (round-down 3) 3)
(check-expect (round-down 1.5) 1)
(check-expect (round-down 2.5) 2)
(check-expect (round-down 0.7) 0)



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
(check-expect (best-savings-plan 100 2) 'SavingsPlanB)
(check-expect (best-savings-plan 100 3) 'SavingsPlanB)
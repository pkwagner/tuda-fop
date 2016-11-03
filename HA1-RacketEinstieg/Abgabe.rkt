;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; TODOs
;; * Vertraege
;; * Tests
;; * Korrekter Header/definierte sprache
;; * Bisschen schoenere Struktur (insbesondere saving-plans)
;; * Dynamischer (braucht man das ueberhaupt?)
;; * TODOs in code...

;; 9.1
;; add-interest::
;;
;; 
;; 
;; Ex:
(define (add-interest capital interestRate) 
	(* capital (+ interestRate 1)))

;; Tests
(check-error (add-interest 100 -0.01) "no negative interest rates allowed")
(check-expect (add-interest 100 0.01) 101)
(check-expect (add-interest 0 0.01) 0)
(check-expect (add-interest -1 0.01) -1)

;; 9.2
;; average-yearly-return::
;;
;; 
;; 
;; Ex:
(define (average-yearly-return capitalBefore capitalAfter duration) 
	(expt (- (/ capitalAfter capitalBefore) 1) (/ 1 duration)))

;; Tests
(check-within (average-yearly-return 100 101 1) 0.01 0.001)
(check-within (average-yearly-return 100 225 2) 0.5 0.001)

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

;; 9.3
;; savings-plan-a::
;;
;; 
;; 
;; Ex:
;; TODO round (floor) duration
(define (savings-plan-a capital duration)
	(if (= duration 0) capital (savings-plan-a (add-interest capital (/
		(cond	[(= duration 1) (/ 1 2)]
                        [(= duration 2) 1]
                        [(= duration 3) (/ 3 2)]) 100))
	(- duration 1))))

;; Tests
(check-error (savings-plan-a 100 0) "invalid runtime")
(check-error (savings-plan-a 100 4) "invalid runtime")
(check-expect (savings-plan-a 100 1) 105)
(check-expect (savings-plan-a 100 1.5) 105)
;; Includes the bonus
(check-within (savings-plan-a 100 3) 133.027 0.001)

;; TODO round (floor) duration
;; savings-plan-b::
;;
;; 
;; 
;; Ex:
(define (savings-plan-b capital duration)
	(if (= duration 0)
            capital
            (savings-plan-b (+ (add-interest capital (/ (/ 1 2) 100)) (if (= duration 3) 30 0)) (- duration 1))))

;; Tests
(check-error (savings-plan-b 100 0) "invalid runtime")
(check-error (savings-plan-b 100 4) "invalid runtime")
(check-expect (savings-plan-b 100 1) 100.5)
(check-expect (savings-plan-b 100 1.5) 100.5)
(check-within (savings-plan-b 100 3) 101.507 0.001)
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
(define (add-interest capital interestRate) 
	(* capital (+ interestRate 1)))

;; 9.2
(define (average-yearly-return capitalBefore capitalAfter duration) 
	(expt (- (/ capitalAfter capitalBefore) 1) (/ 1 duration)))

;; 9.4
(define (best-savings-plan capital duration)
	(if (>= (average-yearly-return capital (savings-plan-a capital duration) duration) (average-yearly-return capital (savings-plan-b capital duration) duration)) 'SavingsPlanA 'SavingsPlanB))


;; 9.3
;; TODO round (floor) duration
(define (savings-plan-a capital duration)
	(if (= duration 0) capital (savings-plan-a (add-interest capital (/
		(cond	[(= duration 1) (/ 1 2)]
                        [(= duration 2) 1]
                        [(= duration 3) (/ 3 2)]) 100))
	(- duration 1))))

;; TODO round (floor) duration
(define (savings-plan-b capital duration)
	(if (= duration 0) capital (savings-plan-b (+ (add-interest capital (/ (/ 1 2) 100)) (if (= duration 3) 30 0)) (- duration 1))))

;; TODO delete line in final version
(best-savings-plan 10000 3)
;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Authors:
;; Alexander Siegler
;; Paul Konstantin Wagner
;; Yoshua Hitzel
;; Marcel Lackovic



;; Constants
(define TOLERANCE 0.001)
(define BASE-INTEREST 0.005)
(define BONUS 30)



;; round-down : number -> number
;;
;; Returns the down-rounded value of a given number
;;
;; Example: (round-down 3.7) = 3
(define (round-down num)
  (round (- num 0.499)))

;; Tests
(check-expect (round-down 7.2) 7)
(check-expect (round-down 3.9) 3)
(check-expect (round-down 3.5) 3)
(check-expect (round-down 4) 4)

;; 9.1
;; add-interest : number number -> number
;; 
;; Returns the new capital after adding the interest rate
;; 
;; Example: (add-interest 10000 0.025) = 10250
(define (add-interest capital interestRate)
  (cond [(< interestRate 0) (error "no negative interest rates allowed")]
        [(<= capital 0) capital] 
        [(> capital 0) (* capital (+ interestRate 1))])
)

;; Checks
(check-error (add-interest 100 -0.01) "no negative interest rates allowed")
(check-expect (add-interest 100 0.01) 101)
(check-expect (add-interest 0 0.01) 0)
(check-expect (add-interest -1 0.01) -1)

;; 9.2
;; average-yearly-return : number number number -> number
;;
;; Returns the average of yearly interest rates based on the start and end capital
;; and the duration in years
;;
;; Example: (average-yearly-return 3000 1000 1) = 2
(define (average-yearly-return capitalAfter capitalBefore duration)
  (cond [(< duration 0) (error "no negative duration allowed")]
        [(< capitalAfter capitalBefore) (error "no capital decrease allowed")]
        [ else (- (expt (/ capitalAfter capitalBefore) (/ 1 duration)) 1)])
)

;; Checks
(check-within (average-yearly-return 101 100 1) 0.01 TOLERANCE)
(check-within (average-yearly-return 225 100 2) 0.5 TOLERANCE)
(check-within (average-yearly-return 100 100 2) 0 TOLERANCE)
(check-error (average-yearly-return 100 100 -1) "no negative duration allowed")
(check-error (average-yearly-return 80 100 2) "no capital decrease allowed")

;; 9.3
;; savings-plan : number number -> number
;; 
;; This method checks the correct usage of the parameters in the savings-plan-X
;; methods and calculates the basic case for having saving-plan for just one year.
;;
;; Example: (savings-plan 100 1) = 100.5
(define (savings-plan capital duration)
  (cond
    ; 3.5 Jahre sollten auch erlaubt werden, da laut Aufgabenstellung nur nicht mehr als 3 komplette Jahre erlaubt sind
    ; Also überprüfen wir Jahre gleich oder größer 4
    [(or (< duration 1) (>= duration 4)) (error "invalid runtime")]
    ; Diese Fall-Unterscheidung bildet den Rekursionsanker beider Funktionenen
    [(= (round-down duration) 1) (add-interest capital BASE-INTEREST)]
    ; Die genauen Spezialisierungen (Plan-A und Plan-B) sollen zuerst aufgerufen werden und dort findet man
    ; die Berechnung Fälle über ein Jahr
    [else (error "missing implementation")])
)

;; Checks
(check-error (savings-plan 100 0) "invalid runtime")
(check-error (savings-plan 100 4) "invalid runtime")
(check-error (savings-plan 100 2) "missing implementation")
(check-expect (savings-plan 100 1) 100.5)


;; savings-plan-a : number number -> number
;;
;; Returns the result capital after a certain time period of years
;; and given a specific start capital
;; based on savings plan A
;;
;; Saving-Plan-A starts with 0.5% and increases each year with 0.5%
;; up to 1.5% after three years
;;
;; Example: Endkapital für 100€ Startkapital nach 3 Jahren
;;   (savings-plan-a 100 3) = 133.027
(define (savings-plan-a capital duration)
  (cond
    [(or (< duration 2) (>= duration 4)) (savings-plan capital duration)]
    [else (add-interest
           ; Rufe zuerst rekursiv auf
           ; damit das Startkapital mit dem Startprozentsatz (BASE-INTEREST)
           ; zuerst verrechnet wird und dem höchsten Wert am Schluss
           (savings-plan-a capital (- duration 1))
           ; Berechne Zinssatz für das entsprechende Jahr (Jahr 1 -> 0.5%=1*0.5*; Jahr 2 -> 1.0%=2*0.5%)
           (* BASE-INTEREST (round-down duration)))])
)

;; Checks
(check-error (savings-plan-a 100 0) "invalid runtime")
(check-error (savings-plan-a 100 4) "invalid runtime")
(check-expect (savings-plan-a 100 1) 100.5)
(check-expect (savings-plan-a 100 1.5) 100.5)
(check-within (savings-plan-a 100 3) 103.027 TOLERANCE)


;; savings-plan-b : number number -> number
;;
;; Returns the result capital after a certain time period of years
;; and given a specific start capital
;; based on savings plan B
;;
;; Saving-Plan-B have a constant 0.5% interest value, but you receive a bonus after three years
;;
;; Example: Endkapital für 100€ Startkapital nach 3 Jahren
;;  (savings-plan-b 100 3) = 101.507 
(define (savings-plan-b capital duration)
  (cond [(or (< duration 2) (>= duration 4)) (savings-plan capital duration)]
        [else (+ (add-interest (savings-plan-b capital (- (round-down duration) 1)) BASE-INTEREST)
              (if (= (round-down duration) 3)
                BONUS
                0))])
)

;; Checks
(check-error (savings-plan-b 100 0) "invalid runtime")
(check-error (savings-plan-b 100 4) "invalid runtime")
(check-expect (savings-plan-b 100 1) 100.5)
(check-expect (savings-plan-b 100 1.5) 100.5)
;; Includes the bonus
(check-within (savings-plan-b 100 3) 131.507 TOLERANCE)


;; 9.4
;; best-savings-plan : number number -> symbol
;;
;; Returns the best saving plan for a certain start captial and a duration in years
;;
;; If the plans are identical the first one (A) will be returned
;; 
;; Example: (best-savings-plan 100 3) = 'SavingsPlanB 
(define (best-savings-plan capital duration)
  (if (>= (average-yearly-return (savings-plan-a capital duration) capital duration)
          (average-yearly-return (savings-plan-b capital duration) capital duration))
      'SavingsPlanA
      'SavingsPlanB)
)

;; Checks
(check-expect (best-savings-plan 100 1) 'SavingsPlanA)
(check-expect (best-savings-plan 100 2) 'SavingsPlanA)
(check-expect (best-savings-plan 100 3) 'SavingsPlanB)
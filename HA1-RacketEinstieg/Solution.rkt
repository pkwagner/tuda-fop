;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname sparplan-vergleich) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; Error tolerance: 3 decimal digits
(define tolerance 0.001)

;; === Problem 9.1 ===

;; add-interest: number number -> number
;; Adds the yearly interest of the given rate on top of the capital invested, 
;; negative interest rates result in an error
;; Example: (add-interest 100 0.02) should return 102
(define (add-interest capital interest)
  (if (>= interest 0)
      (if (>= capital 0)
          (* capital (+ 1 interest))
          capital)
      (error 'add-interest "no negative interest rates allowed")))

;; Tests
(check-expect (add-interest 100 0.02) 102)
(check-within (add-interest 1337.15 0.008) 1347.8472 tolerance)
(check-error (add-interest 4711 -0.005) "add-interest: no negative interest rates allowed")

;; === Problem 9.2 ===

;; average-yearly-return: number number -> number
;; Given a capital before and after a savings plan, calculate the average yearly 
;; interest for a fixed time span in years
;; Example: (average-yearly-return 110 100 5) should return approx. 0.0192
(define (average-yearly-return capital-after capital-before years)
  (- (expt (/ capital-after capital-before) (/ 1 years)) 1))

;; Tests
(check-within (average-yearly-return 110 100 5) 0.0192 tolerance)
(check-within (average-yearly-return 3500 3000 10) 0.0155 tolerance)

;; === Problem 9.3 ===

;; compound-interest123: number number symbol number number number -> number
;; Calculates increased capital after 1-3 years, with yearly interest application of 
;; different rates
;; Example: (compount-interest123 100 3 'savings-plan-a 0.01 0.02 0.03)
;; should return approx. 106.111
(define (compound-interest123 capital years plan interest1 interest2 interest3)
  (if (or (< years 1) (> years 3))
      (error plan "invalid runtime")
      (cond
        [(< years 2) (add-interest capital interest1)]
        [(< years 3) (add-interest (add-interest capital interest1) interest2)]
        [else (add-interest (add-interest (add-interest capital interest1)
                                          interest2)
                            interest3)])))

;; savings-plan-a: number number -> number
;; Returns the increased capital after investing a specified amount of money for 
;; 1-3 years in savings plan A
;; Example: (savings-plan-a 1000 3) should return approx. 1030.276
(define (savings-plan-a capital years)
  (compound-interest123 capital years 'savings-plan-a 0.005 0.01 0.015))

;; Tests
(check-within (savings-plan-a 1000 1) 1005 tolerance)
(check-within (savings-plan-a 1000 2) 1015.05 tolerance)
(check-within (savings-plan-a 1000 3) 1030.276 tolerance)
(check-error (savings-plan-a 1000 0.5) "savings-plan-a: invalid runtime")
(check-error (savings-plan-a 1000 4) "savings-plan-a: invalid runtime")

;; savings-plan-b: number number -> number
;; Returns the increased capital after investing a specified amount of money for 
;; 1-3 years in savings plan B
;; Example: (savings-plan-b 1000 3) should return approx. 1045.075
(define (savings-plan-b capital years)
  (if (= years 3)
      (+ 30 (compound-interest123 capital years 'savings-plan-b 0.005 0.005 0.005))
      (compound-interest123 capital years 'savings-plan-b 0.005 0.005 0.005)))

;; Tests      
(check-within (savings-plan-b 1000 1) 1005 tolerance)
(check-within (savings-plan-b 1000 2) 1010.025 tolerance)
(check-within (savings-plan-b 1000 3) 1045.075 tolerance)
(check-error (savings-plan-b 1000 0.5) "savings-plan-b: invalid runtime")
(check-error (savings-plan-b 1000 4) "savings-plan-b: invalid runtime")

;; === Problem 9.4 ====

;; best-savings-plan: number number -> symbol
;; Determine the best savings plan ('SavingsPlanA or 'SavingsPlanB), given a fixed capital 
;; and investment duration
;; Example: (best-savings-plan 2000 3) should return 'SavingsPlanA
(define (best-savings-plan capital years)
  (if (>= (savings-plan-a capital years) (savings-plan-b capital years))
      'SavingsPlanA
      'SavingsPlanB))

;; Tests
(check-expect (best-savings-plan 2000 3) 'SavingsPlanA)
(check-expect (best-savings-plan 1000 3) 'SavingsPlanB)
(check-expect (best-savings-plan 500 2) 'SavingsPlanA)
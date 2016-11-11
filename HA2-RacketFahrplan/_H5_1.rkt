;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Author*s:
;; Paul Wagner


;; The following line is only for clarity reasons and should be removed later
(require "_Template.rkt")

;; Define Dummys used in previous tasks
;; - NONE -



;; 5.1
;; find-stops : (listof station) symbol -> (listof station)
;;
;; Returns only the stations supporting a special train kind (train_kind)
;; out of a given station list
;;
;; Example: (find-stops (list (make-station 'Hanau '(RE SE VIAS IC) 13) (make-station 'Offenbach '(RE SE VIAS) 5.5) (make-station 'Frankfurt '(RE SE VIAS IC) 0)) 'IC)
;;          = (list (make-station 'Hanau '(RE SE VIAS IC) 13) (make-station 'Frankfurt '(RE SE VIAS IC) 0))
(define (find-stops stations train_kind)
	(cond
		[(empty? stations) empty]

		[
			(member? train_kind (station-train-kinds (first stations)))
			(cons
				(first stations)
				(find-stops (rest stations) train_kind)
			)
		]

		[else (find-stops (rest stations) train_kind)]
	)
)

;; Tests
(check-expect (find-stops empty 'IC)
              empty)
(check-expect (find-stops test-network empty)
              empty)
(check-expect (find-stops test-network 'IC)
              (list
               (make-station 'AStadt (list 'IC 'SE) 2.5)
               (make-station 'CStadt (list 'IC 'SE)  0)
              )
             )
(check-expect (find-stops test-network 'SE)
              (list
               (make-station 'AStadt (list 'IC 'SE) 2.5)
               (make-station 'BDorf (list 'SE) 6)
               (make-station 'CStadt (list 'IC 'SE) 0)
              )
             )
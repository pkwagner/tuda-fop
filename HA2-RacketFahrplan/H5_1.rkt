;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Author*s:
;; Paul Wagner


;; The following line is only for clarity reasons and should be removed later
(require "_Template.rkt")

;; Define Dummys used in previous tasks
;; - NONE -



(define (find-stops stations train_kind)
	(cons
		(if (member? train_kind (station-train-kinds (first stations)))
			(first stations)
			(empty)
		)
		(find-stops (rest stations) train_kind)
	)
)
;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Author*s:
;; ???


;; The following line is only for clarity reasons and should be removed later
(require "_Template.rkt")

;; Define Dummys used in previous tasks
(define (find-stops stations train_kind) '(a-stadt c-stadt))
(define (distance-table stations startStation) '((make-station 'AStadt '(IC SE) 0) (make-station 'BDorf '(SE) 2.5) (make-station 'CStadt '(IC SE) 8.5)))
(define (train-schedule stations train) '((make-stop 'IC002 'AStadt 200) (make-stop 'IC002 'CStadt 208.5)))



;; !!! YOUR CODE !!!
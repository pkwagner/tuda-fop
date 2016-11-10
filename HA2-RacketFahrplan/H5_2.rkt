;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname H5_2) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Author*s:
;; Yoshua Hitzel




;; The following line is only for clarity reasons and should be removed later
(require "_Template.rkt")

;; Define Dummys used in previous tasks
(define (find-stops stations train_kind) '(a-stadt c-stadt))



;; ====== Problem 5.2 =======

;; distance-table-offset: 
;;
;; 
;; Example:
;*
(define (distance-table-offset stations offset)
  (define newOffset (+ offset (station-distance-to-next (first stations))))
  (cond
    [(empty? stations) empty]
    [(empty? (rest stations)) (distance-table-offset (first stations) offset)]
    [(not (empty? (rest stations))) (cons (make-station (station-name (first stations)) (station-train-kinds (first stations)) newOffset)  (distance-table-offset (rest stations) offset) )]
  )
)

;; Tests (you have to provide at least two different tests)


;; distance-table: 
;; 
;; Example: 


(define (distance-table stations from-station)
  (cond
    [(?empty stations) empty]
    [(eq? (station-name (first stations)) from-station) (distance-table-offset (rest stations) 0)]
  )
  (distance-table (rest stations) from-station)
)

;; Tests (you have to provide at least two different tests)
;; The following test is provided by us to help you.
;; It does NOT count as one of the two mandatory tests!

;; TODO Uncomment this
;;(check-expect (distance-table (list (make-station 'AStadt '(IC SE) 0)
;;                    (make-station 'BDorf '(SE) 2.5)
;;                    (make-station 'CStadt '(IC SE) 8.5)
;;              ) 'AStadt)
;;              
;;)
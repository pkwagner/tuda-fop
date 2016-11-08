;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Authors:
;; Alexander Siegler
;; Paul Konstantin Wagner
;; Yoshua Hitzel
;; Marcel Lackovic

;; ====== Structures ======

;; station is a structure representing a train station
;; name: symbol - the unique name of the station
;; train-kinds: (listof symbol) - all kinds of trains serving that station
;;    (e.g. 'SE, 'IC, 'VIAS)
;; distance-to-next: number - distance to next station in direction of 'Frankfurt/'CStadt
(define-struct station (name train-kinds distance-to-next))

;; service is a structure representing a train service/line ("Kursbuchlinie")
;; kind: symbol - kind of train (e.g. 'SE, 'IC, 'VIAS)
;; from: symbol - the station of initial departure in direction of 'Frankfurt/'CStadt
;; avg-velocity: number - the average velocity on the tracks, taking into account stops and waits
(define-struct service (kind from avg-velocity))

;; train is a structure representing a specific train service at a specific time
;; identifier: symbol - unique train identifier 
;; start-time: number - time of initial departure in minutes after midnight
(define-struct train (identifier service start-time))

;; stop is a structure representing an entry in a schedule
;;    (a train stopping at a specific station at a given time)
;; train-identifier: symbol - the stopping train's unique identifier
;; station-name: symbol - the station's name as a symbol
;; stop-time: number - the time of day for the stop in minutes after midnight
(define-struct stop (train-identifier station-name stop-time))

;; ====== Smaller dataset =======

;; Network AStadt -> BDorf -> CStadt
(define a-stadt (make-station 'AStadt '(IC SE) 2.5))
(define b-dorf  (make-station 'BDorf '(SE) 6))
(define c-stadt (make-station 'CStadt '(IC SE) 0))

;; An example train network as a (listof station)
(define test-network (list a-stadt b-dorf c-stadt))

;; Two example services: a SE & an IC from A->C
(define SE-A-C (make-service 'SE 'AStadt 0.5))
(define IC-A-C (make-service 'IC 'AStadt 1))

;; An example (listof service)
(define test-services (list SE-A-C IC-A-C))

;; Two example trains (one of each type)
(define SE001 (make-train 'SE001 SE-A-C 100))
(define IC002 (make-train 'IC002 IC-A-C 200))

;; An example (listof train)
(define test-trains (list SE001 IC002))

;; ========== Problems ==========

;; 5.4
;; all-stops: (listof station) (listof train) -> (listof stop)
;; Gets a list of stops that all trains at all stations in the network perform
;; Example: (all-stops test-network (list SE001)) returns
;; (list (make-stop 'SE001 'AStadt 100) (make-stop 'SE001 'BDorf 105) (make-stop 'SE001 'CStadt 117))
(define (all-stops network trains)
  (cond
    ;; no more trains - exit
    [(empty? trains) empty]
    [else
     ;; create schedule for first train in list...
     (append
      (train-schedule
       (find-stops
        (distance-table
         network
         (service-from (train-service (first trains))))
        (service-kind
         (train-service
          (first trains))))
       (first trains))
      ;; ... and append it to schedule of remaining trains
      (all-stops network (rest trains)))]))

;; Tests (no additional tests required for this procedure!)
;(check-expect (all-stops empty empty) empty)
;(check-expect (all-stops test-network empty) empty)
;(check-expect (all-stops empty (list SE001 IC002)) empty)
;(check-expect (all-stops test-network (list SE001))
;              (list (make-stop 'SE001 'AStadt 100) (make-stop 'SE001 'BDorf 105)
;                    (make-stop 'SE001 'CStadt 117)))
;(check-expect (all-stops test-network (list IC002))
;              (list (make-stop 'IC002 'AStadt 200) (make-stop 'IC002 'CStadt 208.5)))
;(check-expect (all-stops test-network (list SE001 IC002))
;              (list (make-stop 'SE001 'AStadt 100) (make-stop 'SE001 'BDorf 105)
;                    (make-stop 'SE001 'CStadt 117) (make-stop 'IC002 'AStadt 200)
;                    (make-stop 'IC002 'CStadt 208.5)))
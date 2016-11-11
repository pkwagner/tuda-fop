;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname H5_3_NEU) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
;; Author:
;; Marcel Lakcovic


;; Dummy START
;; Define Dummys used in previous tasks

;; 5.2
;; distance-table-offset: (listof stations) number -> (listof stations)
;; Adds the distances of every further stations to the following station
;; Example: (distance-table (list
;;            (make-station 'Waechtersbach '(RE SE) 4.5)
;;            (make-station 'Wirtheim '(SE) 3)
;;            (make-station 'HaitzHoechst '(SE) 3)
;;          ) 0
;;        = (list (make-station 'Waechtersbach (list 'RE 'SE) 4.5) (make-station 'Wirtheim (list 'SE) 7.5) (make-station 'HaitzHoechst (list 'SE) 10.5))
(define (distance-table-offset stations offset)
  (cond
    [(empty? stations) empty]
    [(empty? (rest stations)) (list (make-station (station-name (first stations)) (station-train-kinds (first stations)) (+ offset (station-distance-to-next (first stations)))))]
    [else (cons (make-station (station-name (first stations)) (station-train-kinds (first stations)) (+ offset (station-distance-to-next (first stations))))  (distance-table-offset (rest stations) (+ offset (station-distance-to-next (first stations)))) )]
  )
)

;; distance-table: (listof stations) symbol -> (listof stations)
;; Calculates the distance from the start station to all following stations
;; Example: (distance-table (list
;;            (make-station 'Waechtersbach '(RE SE) 4.5)
;;            (make-station 'Wirtheim '(SE) 3)
;;            (make-station 'HaitzHoechst '(SE) 3)
;;          ) 'Waechtersbach 
;;       =  (list (make-station 'Wirtheim (list 'SE) 3) (make-station 'HaitzHoechst (list 'SE) 6))

(define (distance-table stations from-station)
  (cond
    [(empty? stations) empty]
    [(eq? (station-name (first stations)) from-station) (distance-table-offset stations (station-distance-to-next (first stations)))]
    [else (distance-table (rest stations) from-station)]
  )
  
)
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
(define a-stadt (make-station 'AStadt '(IC SE) 0))
(define b-dorf  (make-station 'BDorf '(SE) 6))
(define c-stadt (make-station 'CStadt '(IC SE) 2.5))

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
;; Dummy END

;;;;;;;;;;;;;;;;;;;;;
;;!!! YOUR CODE !!!;;
;;;;;;;;;;;;;;;;;;;;;

;; 5.3
;;
;; train-schedule: (listof station) train -> (listof stop)
;; Calculates the train-schedule for a specific train, based on the train stops.
;; Example: (train-schedule (find-stops (distance-table test-network 'AStadt) 'IC) IC002)
;;          = (list (make-stop 'IC002 'AStadt 200) (make-stop 'IC002 'CStadt 208.5))

(define (train-schedule stations train)
  (cond
    [(empty? stations) empty]
    [(empty? (rest stations)) (list (make-stop (train-identifier train) (station-name (first stations)) (+ (/ (station-distance-to-next (first stations)) (service-avg-velocity (train-service train))) (train-start-time train))))]
    [else (cons (make-stop (train-identifier train) (station-name (first stations)) (+ (/ (station-distance-to-next (first stations)) (service-avg-velocity (train-service train))) (train-start-time train))) (train-schedule (rest stations) train))]
  )
 )

;; Tests
(check-expect (train-schedule (find-stops (distance-table empty 'AStadt) 'IC) IC002)
              empty)
(check-expect (train-schedule (find-stops (distance-table test-network empty) 'IC) IC002)
              empty)
(check-expect (train-schedule (find-stops (distance-table test-network 'AStadt) empty) IC002)
              empty)
(check-expect (train-schedule (find-stops (distance-table test-network 'AStadt) 'IC) IC002)
              (list (make-stop 'IC002 'AStadt 200) (make-stop 'IC002 'CStadt 208.5)))
(check-expect (train-schedule (find-stops (distance-table test-network 'AStadt) 'SE) SE001)
              (list (make-stop 'SE001 'AStadt 100) (make-stop 'SE001 'BDorf 112) (make-stop 'SE001 'CStadt 117)))
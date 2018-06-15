;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname fahrplan-scheme-template) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
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
   
;; ====== Larger dataset ======

;; Network Waechtersbach -> Frankfurt
(define ff-network
  (list
   (make-station 'Waechtersbach '(RE SE) 4.5)
   (make-station 'Wirtheim '(SE) 3)
   (make-station 'HaitzHoechst '(SE) 3)
   (make-station 'Gelnhausen '(RE SE) 3)
   (make-station 'HailerMeerholz '(SE) 3)
   (make-station 'Niedermittlau '(SE) 4)
   (make-station 'Langenselbold '(RE SE) 3.5)
   (make-station 'Rodenbach '(SE) 5)
   (make-station 'Wolfgang '(SE) 2.5)
   (make-station 'Hanau '(RE SE VIAS IC) 13)
   (make-station 'Offenbach '(RE SE VIAS) 5.5)
   (make-station 'FrankfurtSued '(RE SE VIAS) 4.5)
   (make-station 'Frankfurt '(RE SE VIAS IC) 0)))

;; Services
(define SE-WB-FRA (make-service 'SE 'Waechtersbach 1))
(define IC-FU-FRA (make-service 'IC 'Hanau 2))
(define VIAS-HU-FRA (make-service 'VIAS 'Hanau 1.5))

(define ff-services (list SE-WB-FRA IC-FU-FRA VIAS-HU-FRA))

;; Trains
(define SE50101 (make-train 'SE50101 SE-WB-FRA 481))
(define SE50102 (make-train 'SE50102 SE-WB-FRA 406))
(define SE50201 (make-train 'SE50201 SE-WB-FRA 319))
(define SE50202 (make-train 'SE50202 SE-WB-FRA 377))
(define SE50203 (make-train 'SE50203 SE-WB-FRA 439))
(define IC50B01 (make-train 'IC50B01 IC-FU-FRA 360))
(define IC50B02 (make-train 'IC50B02 IC-FU-FRA 480))
(define VIA65001 (make-train 'VIA65001 VIAS-HU-FRA 410))
(define VIA65002 (make-train 'VIA65002 VIAS-HU-FRA 473))

(define ff-trains29105
  (list
   SE50101
   SE50102
   SE50201
   SE50202
   SE50203
   IC50B01
   IC50B02
   VIA65001
   VIA65002))

;; ====== Problem 5.1 =======

;; find-stops: 
;;
;; 
;; Example: 
(define (find-stops stations train-kind)
  ...)

;; Tests
;; The following test is provided by us to help you.
;; It does NOT count as one of the two mandatory tests!

;; TODO Uncomment this
;(check-expect (find-stops test-network 'IC) (list a-stadt c-stadt))


;; ====== Problem 5.2 =======

;; distance-table-offset: 
;;
;; 
;; Example: 
(define (distance-table-offset stations offset)
  ...)

;; Tests (you have to provide at least two different tests)


;; distance-table: 
;; 
;; Example: 
(define (distance-table stations from-station)
  ...)

;; Tests (you have to provide at least two different tests)
;; The following test is provided by us to help you.
;; It does NOT count as one of the two mandatory tests!

;; TODO Uncomment this
;(check-expect (distance-table test-network 'AStadt)
;              (list (make-station 'AStadt '(IC SE) 0)
;                    (make-station 'BDorf '(SE) 2.5)
;                    (make-station 'CStadt '(IC SE) 8.5)))

;; ====== Problem 5.3 =======

;; train-schedule:
;; 
;; Example: 
(define (train-schedule service-distance-table train)
  ...)

;; Tests (you have to provide at least two different tests)
;; The following test is provided by us to help you.
;; It does NOT count as one of the two mandatory tests!

;; TODO Uncomment this
;(check-expect (train-schedule (find-stops (distance-table test-network 'AStadt) 'IC) IC002)
;              (list (make-stop 'IC002 'AStadt 200) (make-stop 'IC002 'CStadt 208.5)))

;; ====== Problem 5.4 =======

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
(check-expect (all-stops empty empty) empty)
(check-expect (all-stops test-network empty) empty)

;; TODO Uncomment this
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

;; station-schedule: 
;; 
;; Example: 
(define (station-schedule stops station)
  ...)
      
;; Tests (you have to provide at least two different tests)


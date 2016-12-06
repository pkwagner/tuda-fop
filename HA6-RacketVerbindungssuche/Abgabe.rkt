;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Authors:
;; Alexander Siegler
;; Paul Konstantin Wagner
;; Yoshua Hitzel
;; Marcel Lackovic

;; A struct modelling a train connection from a common station
;; to-station: symbol - identifier of the train's destination
;; train-identifier: symbol - unique identifier (usually train kind + running number) of train
;; duration: number - durantion of trainride in minutes
(define-struct connection (to-station identifier duration))

;; A struct modelling a train station with its connections
;; identifier: symbol - a unique identifier for the station (here: its name)
;; connections: (listof connection) - a list of trains serving this station and their destinations
(define-struct station (identifier connections))

;; An entry in Dijkstra's distance table
;; station: symbol - a station's name
;; parent: symbol - name of the parent station
;; train: symbol - train to take from parent's station
;; distance: number - distance between parent station and this station (in travel minutes)
(define-struct distance-entry (station parent train distance))

;; A constant as initial value for invalid distance table entries
(define INFINITY 999999)

;; ====== Data: Small test network ======
(define test-network
  (list (make-station 'AStadt (list (make-connection 'BDorf 'RB1 10)
                                    (make-connection 'CStadt 'IC2 5)))
        (make-station 'BDorf (list (make-connection 'AStadt 'RB1 10)))
        (make-station 'CStadt (list (make-connection 'AStadt 'IC2 5)))))

;; ====== Data: Coarse-grain RMV network ======
(define
  rmv-network
  (list
   (make-station 'Darmstadt (list
                             (make-connection 'Mainz 'RB75 32)
                             (make-connection 'Langen 'RB60 8)
                             (make-connection 'GrossUmstadt 'R65 31)))
   (make-station 'GrossUmstadt (list
                                (make-connection 'Darmstadt 'R65 31)
                                (make-connection 'Hanau 'R64 42)))
   (make-station 'Mainz (list
                         (make-connection 'Darmstadt 'RB75 32)
                         (make-connection 'Wiesbaden 'RB75 12)
                         (make-connection 'FrankfurtFlughafen 'S8 26)))
   (make-station 'FrankfurtFlughafen (list
                                      (make-connection 'Mainz 'S8 26)
                                      (make-connection 'Frankfurt 'S8 10)))
   (make-station 'Langen (list
                          (make-connection 'Darmstadt 'RB60 8)
                          (make-connection 'Frankfurt 'RB60 9)))
   (make-station 'Wiesbaden (list
                             (make-connection 'Mainz 'RB75 12)
                             (make-connection 'Frankfurt 'R10 35)
                             (make-connection 'Limburg 'R21 69)))
   (make-station 'Limburg (list
                           (make-connection 'Wiesbaden 'R21 69)
                           (make-connection 'Giessen 'RE25 52)))
   (make-station 'Frankfurt (list
                             (make-connection 'Wiesbaden 'R10 35)
                             (make-connection 'FrankfurtFlughafen 'S8 10)
                             (make-connection 'Langen 'RB60 9)
                             (make-connection 'FrankfurtSued 'RE50 5)
                             (make-connection 'Friedberg 'R98 22)))
   (make-station 'FrankfurtSued (list
                                 (make-connection 'Frankfurt 'RE50 5)
                                 (make-connection 'Offenbach 'RE50 4)))
   (make-station 'Offenbach (list
                             (make-connection 'FrankfurtSued 'RE50 4)
                             (make-connection 'Hanau 'RE50 8)))
   (make-station 'Hanau (list
                         (make-connection 'Offenbach 'RE50 8)
                         (make-connection 'GrossUmstadt 'R64 42)
                         (make-connection 'Gelnhausen 'RE50 14)))
   (make-station 'Gelnhausen (list
                              (make-connection 'Hanau 'RE50 14)
                              (make-connection 'Fulda 'RE50 46)
                              (make-connection 'Nidda 'R36 39)))
   (make-station 'Fulda (list
                         (make-connection 'Gelnhausen 'RE50 46)
                         (make-connection 'Giessen 'R25 100)))
   (make-station 'Giessen (list
                           (make-connection 'Fulda 'R25 100)
                           (make-connection 'Marburg 'R98 14)
                           (make-connection 'Limburg 'RE25 52)
                           (make-connection 'Friedberg 'R98 17)
                           (make-connection 'Nidda 'R36 45)))
   (make-station 'Nidda (list
                         (make-connection 'Gelnhausen 'R36 39)
                         (make-connection 'Giessen 'R36 45)
                         (make-connection 'Friedberg 'R32 35)))
   (make-station 'Friedberg (list
                             (make-connection 'Giessen 'R98 17)
                             (make-connection 'Nidda 'R32 35)
                             (make-connection 'Frankfurt 'R98 22)))
   (make-station 'Marburg (list
                           (make-connection 'Giessen 'R98 14)))))
                           
;; ====== Problem 7.1 ======

;; a)

;; create-distance-table: (listof station) symbol -> (listof distance-entry)
;;
;; Creates a distance table for the given network. Each element of the network will be replaced
;; with a distance-entry struct where every entry has parent and train as empty. Moreover every entry
;; defaults to INFINITY distance, except the departure station that distance is 0.
;;
;; Example: (create-distance-table test-network 'AStadt) =
;;                    (list (make-distance-entry 'AStadt empty empty 0)
;;                      (make-distance-entry 'BDorf empty empty INFINITY)
;;                      (make-distance-entry 'CStadt empty empty INFINITY))
(define (create-distance-table network dep-station)
  (if (empty? network)
      empty
      (local
        [(define first-name (station-identifier (first network)))
         (define distance (if (symbol=? first-name dep-station) 0 INFINITY))]
           (cons (make-distance-entry first-name empty empty distance)
                 (create-distance-table (rest network) dep-station)))))
  
;; Tests
(check-expect (create-distance-table empty 'A) empty)
(check-expect (create-distance-table test-network 'CStadt)
              (list (make-distance-entry 'AStadt empty empty INFINITY)
                    (make-distance-entry 'BDorf empty empty INFINITY)
                    (make-distance-entry 'CStadt empty empty 0)))
(check-expect (create-distance-table test-network 'AStadt)
              (list (make-distance-entry 'AStadt empty empty 0)
                    (make-distance-entry 'BDorf empty empty INFINITY)
                    (make-distance-entry 'CStadt empty empty INFINITY)))



;; =========================
  
;; b)

;; query-elem: (X -> symbol) (listof X) symbol -> X
;;
;; If it cannot find such element it returns empty.
;;
;; The id-op procedure should return unique id based on the input and return the same id for the same
;; input. Otherwise it could happen that this procedure returns something differant than the expected
;; instance.
;;
;; Example: (query-elem (lambda (id) id) (list 'A 'B 'C) 'B) = 'B
 (define (query-elem id-op lst elem)
  (if (empty? lst)
      empty
      (local
        [(define current (first lst))
         (define id-cur (id-op current))]
        (if (symbol=? id-cur elem)
            current
            (query-elem id-op (rest lst) elem)))))
  
;; Tests
(check-expect (query-elem (lambda (x) x) empty 'A) empty)
(check-expect (query-elem (lambda (x) x) (list 'A) 'A) 'A)
(check-expect (query-elem (lambda (x) x) (list 'A) 'B) empty)
(check-expect (query-elem (lambda (x) x) (list 'A 'B 'C) 'B) 'B)



;; query-distance-entry: 
;; 
;; Example: 
(define (query-distance-entry distance-table station)
  ...)
  
;; Tests



;; =========================
  
;; query-station: (listof station) symbol -> station
;; 
;; Example: 
(define (query-station network station)
  ...)
  
;; Tests



;; =========================
  
;; c)

;; query-min-distance-station: 
;; 
;; Example: 
(define (query-min-distance-station distance-table unvisited)
  ...)
  
;; Tests



;; =========================  

;; update-distances-batch: (listof distance-entry) (listof distance-entry) -> (listof distance-entry)
;; Update distance table wit a list of new entries - but check for all entries if distance is smaller
;; Example: (update-distances-batch (list (make-distance-entry 'CStadt empty empty INFINITY)) (list (make-distance-entry 'CStadt 'AStadt 'IC2 5))
;;          returns (list (make-distance-entry 'CStadt AStadt 'IC2 5))
(define (update-distances-batch distance-table new-entries)
  (local
    ( ;; begin local definitions
     ;; update-distance-entry: distance-entry (listof distance-entry) -> (listof distance-entry)
     ;; Upates a single distance-entry in the table - if the station is found and the new entry has a smaller distance
     ;; Example: see above
     (define (update-distance-entry new-entry distance-table)
       (local
         (;; begin local definitions
          ;; replace-entry: distance-entry -> (listof distance-entry)
          ;; Given a single distance-entry, checks if the one we're trying to insert has the same station,
          ;; then returns the one with the smaller distance
          (define (replace-entry old-entry)
            (if (and (symbol=?
                      ;; check if the entries represent the same station...
                      (distance-entry-station old-entry)
                      (distance-entry-station new-entry))
                     ;; ... if so, return the one with the smaller distance
                     (< (distance-entry-distance new-entry)
                        (distance-entry-distance old-entry)))
                new-entry
                old-entry))
          ) ;; end local definitions
         ;; apply the relacement function to the whole list
         (map replace-entry distance-table))))
    ;; use fold to apply the update-function to the whole list
    (foldr update-distance-entry distance-table new-entries)))
    
;; Tests
(check-expect (update-distances-batch (create-distance-table test-network 'AStadt) empty)
              (create-distance-table test-network 'AStadt))
(check-expect (update-distances-batch (create-distance-table test-network 'AStadt)
                                      (list
                                       (make-distance-entry 'CStadt 'AStadt 'IC2 5)))
              (list
               (make-distance-entry 'AStadt empty empty 0)
               (make-distance-entry 'BDorf empty empty INFINITY)
               (make-distance-entry 'CStadt 'AStadt 'IC2 5)))              
(check-expect (update-distances-batch (create-distance-table test-network 'AStadt)
                                      (list
                                       (make-distance-entry 'CStadt 'AStadt 'IC2 (+ INFINITY 1))))
              (list
               (make-distance-entry 'AStadt empty empty 0)
               (make-distance-entry 'BDorf empty empty INFINITY)
               (make-distance-entry 'CStadt empty empty INFINITY)))



;; =========================

;; shortest-paths: (listof station) symbol -> (listof distance-entry)
;; Finds shortest paths (in the sense of travel time) from the departure station to all other station in the network
;; by using Dijkstra's algorithm -- as a side effect, we get a shortest-distance tree
;; Example: (shortest-paths test-network 'AStadt) returns
;;          (list
;;            (make-distance-entry 'AStadt empty empty 0)
;;            (make-distance-entry 'BDorf 'AStadt 'RB1 10)
;;            (make-distance-entry 'CStadt 'AStadt 'IC1 5))
(define (shortest-paths network dep-station)
  (local
    (;; begin local definitions
     (define
       ;; shortest-path-step: (listof distance-entry) (listof symbol) -> (listof distance-entry)
       ;; Executes one Dijkstra step: given the list of unvisited stations, selects the one with
       ;; the minimu distance and updates the table with this station as transit
       (shortest-path-step distance-table unvisited)
       (if (empty? unvisited) ;; no unvisited stations -> done, return current table!
           distance-table
           (local
             (;; begin local definitions
              ;; cache the minimum distance entry
              (define u (query-min-distance-station distance-table unvisited))
              (define s-u (query-station network u))
              (define cur-entry (query-distance-entry distance-table u))
              ;; tentative-distance: connection -> distance-entry
              ;; Compute the tentative distance to a neighboring station (add time to current transit and distance from transit to this station)
              ;; and pack that as distance entry
              (define (tentative-distance conn)
                (make-distance-entry (connection-to-station conn) u (connection-identifier conn) (+ (distance-entry-distance cur-entry) (connection-duration conn))))
              ) ;; end local definitions
             ;; execute next step on modified distance table...
             (shortest-path-step
              ;; try inserting tentative distances to neighbors into distance table
              (update-distances-batch
               distance-table
               ;; compute tentative distances for all neighboring stations
               (map tentative-distance (station-connections s-u)))
              ;; upate unvisited queue
              (remove u unvisited)))))
     ) ;; end local definitions
    ;; Start Dijkstra with initial table and a list of all stations
    (shortest-path-step (create-distance-table network dep-station) (map station-identifier network))))

;; Tests
(check-expect
 (shortest-paths test-network 'AStadt)
 (list
  (make-distance-entry 'AStadt empty empty 0)
  (make-distance-entry 'BDorf 'AStadt 'RB1 10)
  (make-distance-entry 'CStadt 'AStadt 'IC2 5)))
(check-expect
 (shortest-paths
  rmv-network
  'Darmstadt)
 (list
  (make-distance-entry 'Darmstadt empty empty 0)
  (make-distance-entry 'GrossUmstadt 'Darmstadt 'R65 31)
  (make-distance-entry 'Mainz 'Darmstadt 'RB75 32)
  (make-distance-entry 'FrankfurtFlughafen 'Frankfurt 'S8 27)
  (make-distance-entry 'Langen 'Darmstadt 'RB60 8)
  (make-distance-entry 'Wiesbaden 'Mainz 'RB75 44)
  (make-distance-entry 'Limburg 'Giessen 'RE25 108)
  (make-distance-entry 'Frankfurt 'Langen 'RB60 17)
  (make-distance-entry 'FrankfurtSued 'Frankfurt 'RE50 22)
  (make-distance-entry 'Offenbach 'FrankfurtSued 'RE50 26)
  (make-distance-entry 'Hanau 'Offenbach 'RE50 34)
  (make-distance-entry 'Gelnhausen 'Hanau 'RE50 48)
  (make-distance-entry 'Fulda 'Gelnhausen 'RE50 94)
  (make-distance-entry 'Giessen 'Friedberg 'R98 56)
  (make-distance-entry 'Nidda 'Friedberg 'R32 74)
  (make-distance-entry 'Friedberg 'Frankfurt 'R98 39)
  (make-distance-entry 'Marburg 'Giessen 'R98 70)))
(check-expect
 (shortest-paths
  rmv-network
  'Gelnhausen)
 (list
  (make-distance-entry 'Darmstadt 'Langen 'RB60 48)
  (make-distance-entry 'GrossUmstadt 'Hanau 'R64 56)
  (make-distance-entry 'Mainz 'FrankfurtFlughafen 'S8 67)
  (make-distance-entry 'FrankfurtFlughafen 'Frankfurt 'S8 41)
  (make-distance-entry 'Langen 'Frankfurt 'RB60 40)
  (make-distance-entry 'Wiesbaden 'Frankfurt 'R10 66)
  (make-distance-entry 'Limburg 'Giessen 'RE25 122)
  (make-distance-entry 'Frankfurt 'FrankfurtSued 'RE50 31)
  (make-distance-entry 'FrankfurtSued 'Offenbach 'RE50 26)
  (make-distance-entry 'Offenbach 'Hanau 'RE50 22)
  (make-distance-entry 'Hanau 'Gelnhausen 'RE50 14)
  (make-distance-entry 'Gelnhausen empty empty 0)
  (make-distance-entry 'Fulda 'Gelnhausen 'RE50 46)
  (make-distance-entry 'Giessen 'Friedberg 'R98 70)
  (make-distance-entry 'Nidda 'Gelnhausen 'R36 39)
  (make-distance-entry 'Friedberg 'Frankfurt 'R98 53)
  (make-distance-entry 'Marburg 'Giessen 'R98 84)))



;; ====== Problem 7.2 ======

;; A structure representing a node in the path tree
;; identifier: symbol - station that node represents
;; children: (listof path-node) - a list of children (no fixed node degree)
;; train: symbol - the train connecting this station/node to its parent
;; duration-to-start: number - the travel time to start node
(define-struct path-node (identifier children train duration-to-start))

;; find-root: (listof distance-entry) -> distance-entry
;; Given a distance table, finds the root station/node for the path tree (representing the departure station)
;; Example: (find-root (create-distance-table test-network)) returns (make-distance-entry 'AStadt empty empty 0)
(define (find-root distance-table)
  (local
    ( ;; begin local definitions
     ;; distance-entry -> true
     ;; Checks if the parent field in a distance-entry is empty
     (define filtered
       (filter
        ;; : distance-entry -> boolean
        ;; returns true if the parent of the entry is empty
        (lambda (x) (empty? (distance-entry-parent x)))
        distance-table)))
    (if (empty? filtered)
        empty
        (first filtered))))

;; Tests
(check-expect (find-root empty) empty)
(check-expect (find-root (create-distance-table test-network 'AStadt)) (make-distance-entry 'AStadt empty empty 0))
(check-expect (find-root (list
                          (make-distance-entry 'CStadt 'AStadt 'IC2 5)
                          (make-distance-entry 'BDorf empty empty 0)))
              (make-distance-entry 'BDorf empty empty 0))




;; =========================

;; construct-path-tree:
;; 
;; Example: 
(define (construct-path-tree distance-table)
  ...)




;; ====== Problem 7.3 ======


;; A structure representing a transit entry in a connection plan
;; train: symbol - train's identifier
;; to: symbol - station name for train destination
;; duration: number - travel time
(define-struct transit (train to duration))

;; in-subtree?: 
;; 
;; Example: 
(define (in-subtree? subtree station)
  ...)
  
;; Tests




;; =========================
    
;; find-connection: 
;; 
;; Example: 
(define (find-connection path-tree to-station)
  ...)
  
;; Tests

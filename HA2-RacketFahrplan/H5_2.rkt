;; Yoshua Hitzel

;; 5.2

;; Dummy
(define-struct station (name train-kinds distance-to-next))

;; Network AStadt -> BDorf -> CStadt
(define a-stadt (make-station 'AStadt '(IC SE) 2.5))
(define b-dorf  (make-station 'BDorf '(SE) 6))
(define c-stadt (make-station 'CStadt '(IC SE) 0))

;; An example train network as a (listof station)
(define test-network (list a-stadt b-dorf c-stadt))

;; distance-table-offset: (listof stations) number -> (listof stations)
;; Calculates the distance from the start station to every further station by adding the particular
;; distance from the particular previous station
;; Example: (distance-table (list
;;            (make-station 'Waechtersbach '(RE SE) 4.5)
;;            (make-station 'Wirtheim '(SE) 3)
;;            (make-station 'HaitzHoechst '(SE) 3)
;;          ) 0 )
;;        = (list (make-station 'Waechtersbach (list 'RE 'SE) 4.5)
;;                (make-station 'Wirtheim (list 'SE) 7.5)
;;                (make-station 'HaitzHoechst (list 'SE) 10.5)
;;          )
(define (distance-table-offset stations offset)
  (cond
    [(empty? stations) empty]
    [(empty? (rest stations)) (list
                               (make-station
                                (station-name (first stations))
                                (station-train-kinds (first stations))
                                (+ offset (station-distance-to-next (first stations)))
                               )
                              ) ]
    [else (cons
           (make-station
            (station-name (first stations))
            (station-train-kinds (first stations))
            (+ offset (station-distance-to-next (first stations)))
           )
           (distance-table-offset (rest stations) (+ offset (station-distance-to-next (first stations))))
          ) ]
  )
)


;; Test
(check-expect (distance-table-offset test-network 0 )
              (list
               (make-station 'AStadt (list 'IC 'SE) 2.5)
               (make-station 'BDorf (list 'SE) 8.5)
               (make-station 'CStadt (list 'IC 'SE) 8.5)
              )
             )
(check-expect (distance-table-offset test-network 2 )
              (list
               (make-station 'AStadt (list 'IC 'SE) 4.5)
               (make-station 'BDorf (list 'SE) 10.5)
               (make-station 'CStadt (list 'IC 'SE) 10.5)
              )
             )



;; distance-table: (listof stations) symbol -> (listof stations)
;; Calculates the distance from the start station to all following stations
;; Example: (distance-table (list
;;            (make-station 'Waechtersbach '(RE SE) 4.5)
;;            (make-station 'Wirtheim '(SE) 3)
;;            (make-station 'HaitzHoechst '(SE) 3)
;;          ) 'Waechtersbach )
;;       =  (list (make-station 'Wirtheim (list 'SE) 3) (make-station 'HaitzHoechst (list 'SE) 6))


(define (distance-table stations from-station)
  (cond
    [(empty? stations) empty]
    [(eq? (station-name (first stations)) from-station) (distance-table-offset stations (* -1 (station-distance-to-next (first stations))))]
    [else (distance-table (rest stations) from-station)]
  )
)

;; Tests
(check-expect (distance-table test-network 'BDorf)
              (list (make-station 'CStadt (list 'IC 'SE) 0)) )
(check-expect (distance-table test-network 'ADorf)
              empty )

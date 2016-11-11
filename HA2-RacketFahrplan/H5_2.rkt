#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Author:
;; Yoshua Hitzel

;; 5.2

;; Dummy
(define-struct station (name train-kinds distance-to-next))

;; distance-table-offset: (listof stations) number -> (listof stations)
;; Calculates the distance from the start station to every further station by adding the particular
;; distance from previous stations
;; Example: (distance-table (list
;;            (make-station 'Waechtersbach '(RE SE) 4.5)
;;            (make-station 'Wirtheim '(SE) 3)
;;            (make-station 'HaitzHoechst '(SE) 3)
;;          ) 'Waechtersbach )
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

;; Tests
(check-expect (distance-table (list
                                 (make-station 'Waechtersbach '(RE SE) 4.5)
                                 (make-station 'Wirtheim '(SE) 3)
                               ) 'Waechtersbach)
              (list (make-station 'Wirtheim (list 'SE) 3)) )
(check-expect (distance-table (list
                                 (make-station 'Waechtersbach '(RE SE) 4.5)
                               ) 'Waechtersbach)
              empty )


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
    [(eq? (station-name (first stations)) from-station) (distance-table-offset (rest stations) 0)]
    [else (distance-table (rest stations) from-station)]
  )
  
)

;; Tests
(check-expect (distance-table-offset (list (make-station 'BDorf '(SE) 2.5)
                                           (make-station 'CStadt '(IC SE) 8.5)
                                     ) 0 )
              (list (make-station 'BDorf (list 'SE) 2.5) (make-station 'CStadt (list 'IC 'SE) 11))  )
(check-expect (distance-table-offset (list (make-station 'BDorf '(SE) 2.5)
                                     ) 0 )
              (list (make-station 'BDorf (list 'SE) 2.5) )  )


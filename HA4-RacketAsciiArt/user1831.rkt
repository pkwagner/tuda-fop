;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-reader.ss" "lang")((modname user1831) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

;; Authors:
;; lost

(define ERROR 0.01)

;; ====== Problem 6.1 ======

;; A struct representing a fully opaque color in the HSL color system, see
;; e.g. https://en.wikipedia.org/wiki/HSL_and_HSV
;; H: number - hue component (normalized to [0, 1])
;; S: number - saturation component (normalized to [0, 1])
;; L: number - lightness component (normalized to [0, 1])
(define-struct hsl (H S L))

(define-struct color (red green blue))

(define (betrag num)
  (cond [(< num 0) (* -1 num)][else num])
)

;; rgb-to-hsl: 
;;
;;
;;
;; Example:
(define (fmod a b)
	(- a (* b (floor (/ a b))))
)


(define (rgb-to-hsl rgb)
(local
  [
   (define r (/ (color-red rgb) 255))
   (define g (/ (color-green rgb) 255))
   (define b (/ (color-blue rgb) 255))
   (define M (max r g b))
   (define m (min r g b))
   (define C (- M m))
   (define L (/ (+ M m) 2))
   (define S
     (cond
               [(or (= C 0) (= L 0) (= L 1))   0  ]
               [else   (/ C (- 1 (betrag (- (* 2 L) 1))))  ]
     )
   )
   
   (define H (/
              (cond
               [(= C 0)   0  ]
               [(= M r)   (fmod (/ (- g b) C) 6)  ]
               [(= M g)   (+ (/ (- b r) C) 2)  ]
               [(= M b)   (+ (/ (- r g) C) 4)  ]
             )
             6)
   )
  ]
  (make-hsl H S L)
)
  )


(rgb-to-hsl (make-color 10 0 0))





;; Tests




;; rgb-to-hsl-list: 
;;
;;
;;
;; Example: 
(define (rgb-to-hsl-list colors)
  (map rgb-to-hsl colors)
)


;; Tests



;; ====== Problem 6.2 ======

;; hsl-to-ascii: 
;;
;;
;;
;; Example: 
(define (hsl-to-ascii hsl)
(round
 (local
         [
          (define H (hsl-H hsl))
          (define L (hsl-L hsl))
         ]
         (cond
         [(<= L 0.05)   35  ]
         [(and (> L 0.05) (<= L 0.5))    (+ 65 (* H (- 90 65)))  ]
         [(and (> L 0.5) (<= L 0.95))     (+ 97 (* H (- 122 97)))  ]
         [else   32]                            
         )
 )
)


  )

;; Tests
              


;; hsl-to-ascii-list: 
;;
;;
;;
;; Example: 
(define (hsl-to-ascii-list hsl-list)
(map hsl-to-ascii hsl-list)

  )

;; Tests



;; ====== Problem 6.3 ======

;; average3: 
;;
;;
;;
;; Example: 
(define (average3 image)
  ...)

;; No tests necessary here



;; ====== Problem 6.4 ======

;; downscale:
;;
;;
;;
;; Example: 
(define (downscale image width)
  ...)

;; No tests necessary here



;; ====== Provided code ======

; Commented out because they depend on unimplemented procedures from above.
;;; downsample: (listof color) integer -> (listof color)
;;; Downsamples an image by a factor of 2, i.e. first blurs the image with horizontal kernel
;;; 0.25 * [1 2 1], then removes every second pixel horizontally and every second row, pads with (0, 0, 0);
;;; Example: (downsample (list (make-color 0 0 0) (make-color 1 1 1) (make-color 2 2 2) (make-color 3 3 3) 
;;;                            (make-color 4 5 5) (make-color 5 5 5) (make-color 6 6 6) (make-color 7 7 7)) 4) returns
;;;          (list (make-color 0 0 0) (make-color 2 2 2)) with width of 2
;(define (downsample rgb-list width)
;  (if
;   (empty? rgb-list)
;   empty
;   (downscale (average3 rgb-list) width)))
;
;;; Test
;;; error here -> student did forget to check for an empty list
;(check-expect (downsample empty 0) empty)
;;; error here -> student assumes that list-length is greater than 1 or pads wrong
;(check-expect (downsample (list (make-color 128 128 128)) 1) (list (make-color 64 64 64 255)))
;;; error here -> student probably does not take the imge's width correctly into account
;(check-expect (downsample (list
;                           (make-color 0 0 0) (make-color 1 1 1) (make-color 2 2 2) (make-color 3 3 3)
;                           (make-color 4 4 4) (make-color 5 5 5) (make-color 6 6 6) (make-color 7 7 7)) 4)
;              (list (make-color 0 0 0) (make-color 2 2 2)))
;              
;
;;; downsample-pyramid: (listof color) integer integer -> (listof color)
;;; Executes pyramidical downsampling (i.e. n times downscaling by factor two), the resulting width is width / 2^(n - 1)
;;; Example: (visual)
;(define (downsample-pyramid rgb-list width n)
;  (if
;   (= n 0)
;   ;; no more steps for downscaling - return image
;   rgb-list
;   ;; otherwise, sample one more step
;   (downsample (downsample-pyramid rgb-list width (- n 1)) (/ width (expt 2 (- n 1))))))
;
;;; here: only visual tests (load, downsample/average3 and export image, do a visual comparison)
;;; load-averag3-save: string string integer -> boolean (and PNG-image on disk)
;(define (load-average3-save in-path out-path)
;  (local
;    ((define in-img (bitmap/file in-path)))
;    (save-image
;     (color-list->bitmap
;      (average3 (image->color-list in-img))
;      (image-width in-img)
;      (image-height in-img))
;     out-path)))
;
;;; load-downscale-save: string string integer -> boolean (and PNG-image on disk)
;(define (load-downscale-save in-path out-path)
;  (local
;    ((define in-img (bitmap/file in-path)))
;    (save-image
;     (color-list->bitmap
;      (downscale (image->color-list in-img) (image-width in-img))
;      (/ (image-width in-img) 2)
;      (/ (image-height in-img) 2))
;     out-path)))
;
;;; Visual tests (Only check for successful save)
;(check-expect (load-average3-save "fop-u04-material/gcc_logo.jpg" "fop-u04-material/gcc_logo_blurred.png") true)
;(check-expect (load-average3-save "fop-u04-material/solid_snake.jpg" "fop-u04-material/solid_snake_blurred.png") true)
;(check-expect (load-average3-save "fop-u04-material/big_boss.jpg" "fop-u04-material/big_boss_blurred.png") true)
;
;(check-expect (load-downscale-save "fop-u04-material/gcc_logo.jpg" "fop-u04-material/gcc_logo_scaled.png") true)
;(check-expect (load-downscale-save "fop-u04-material/solid_snake.jpg" "fop-u04-material/solid_snake_scaled.png") true)
;(check-expect (load-downscale-save "fop-u04-material/big_boss.jpg" "fop-u04-material/big_boss_scaled.png") true)
;                
;;; ascii-artify: string number -> string
;;; Loads an image from the given path, downscales it n times by 2 and converts it to ascii art, the saves it as HTML-page in fop-04-material/ascii_art.html
;(define (ascii-artify img-path n)
;  (local
;    ;; save-ascii-file: (listof integer) integer string -> filename (and ascii-image wrapped into HTML page on disk)
;    ;; Saves a list of integers as ASCII image of a gven width in HTML page (zoomable)
;    ((define (save-ascii-image ascii-list width filename)
;       (local
;         ;; converts integers in input ist to characters
;         ((define char-list (map integer->char ascii-list))
;          ;; add-newlines: (listof char) integer integer -> (listof char)
;          ;; Adds a newline character (#\newline) every width characters
;          (define (add-newlines lst i width)
;            (cond
;              [(empty? lst) lst]
;              ;; passed width characters -> add <br /> and #\newline for HTML view
;              [(and (= (modulo i width) 0) (> i 0)) (append (list #\< #\b #\r #\/ #\> (first lst)) (add-newlines (rest lst) (+ i 1) width))]
;              ;; else just keep character
;              [else (cons (first lst) (add-newlines (rest lst) (+ i 1) width))])))
;         ;; write ASCII file as HTML page
;         (write-file
;          filename
;          ;; add custom square font to HTML page and keep formatting of ASCII art
;          (string-append
;           "<html><head><title>FOP Ascii Art</title>"
;           "<style type=\"text/css\">@font-face {font-family: Square; src: url(\"square.ttf\") format(\"truetype\")}</style>"
;           "</head><body><pre style=\"font-family:Square, monospace;\">"
;           (list->string (add-newlines char-list 0 width))
;           "</pre></body></html>"))))
;     (define img (bitmap/file img-path)))
;    ;; save downscaled image
;    (save-ascii-image
;     (hsl-to-ascii-list (rgb-to-hsl-list (downsample-pyramid (image->color-list img) (image-width img) n)))
;     (/ (image-width img) (expt 2 n))
;     "fop-u04-material/ascii_art.html")))
;
;;; Execution - CALL HERE FOR ASCII ART GENERATION!
;(ascii-artify "fop-u04-material/gcc_logo.jpg" 2)

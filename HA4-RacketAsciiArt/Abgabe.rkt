;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname Abgabe) (read-case-sensitive #t) (teachpacks ((lib "batch-io.rkt" "teachpack" "2htdp") (lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "batch-io.rkt" "teachpack" "2htdp") (lib "image.rkt" "teachpack" "2htdp")))))

;; Authors:
;; Alexander Siegler
;; Paul Konstantin Wagner
;; Yoshua Hitzel
;; Marcel Lackovic

(define ERROR .01)

;; ====== Problem 6.1 ======

;; A struct representing a fully opaque color in the HSL color system, see
;; e.g. https://en.wikipedia.org/wiki/HSL_and_HSV
;; H: number - hue component (normalized to [0, 1])
;; S: number - saturation component (normalized to [0, 1])
;; L: number - lightness component (normalized to [0, 1])
(define-struct hsl (H S L))

;; rgb-to-hsl: color -> hsl
;;
;; Converts a color struct to the equivalent hsl struct
;;
;; Example: (rgb-to-hsl (make-color 85 85 85)) = (make-hsl 0 0 0.33)
(define (rgb-to-hsl rgb-col)
  (local [
          (define max-color 255)
          ; Color attribute shortcuts
          (define red (/ (color-red rgb-col) max-color))
          (define green (/ (color-green rgb-col) max-color))
          (define blue (/ (color-blue rgb-col) max-color))
          
          (define maximum (max red green blue))
          (define minimum (min red green blue))
          (define difference (- maximum minimum))
          
          (define hue (/ (local
                           [
                            ;; fmod: number number -> number
                            ;;
                            ;; Just like modulo, but you could also invoke this method with
                            ;; floats/doubles and so it will return the rest of division as a float/double if 
                            ;; 
                            ;; Example: (fmod 11.5 3) = 2.5
                            (define (fmod a b) (- a (* b (floor (/ a b)))))

                            ;; f: number number number -> number
                            ;;
                            ;; Calculates the hue formal:
                            ;; (color-attr1 - color-attr2) / difference + X
                            ;;
                            ;; Where color-attr* is the color attribute of a color-struct. Either
                            ;; red, green or blue and X is the summand like in case:
                            ;; * Max = Rred -> X = 0
                            ;; * Max = Green -> X = 2
                            ;; * Max = Blue -> X = 4
                            ;;
                            ;; Example: (f 20 10 2) = 4 if difference = 5
                            (define (f first second sum) (+ (/ (- first second) difference) sum))]
                           (cond
                             [(= difference 0) 0]
                             [(= maximum red) (fmod (f green blue 0) 6)]
                             [(= maximum green) (f blue red 2)]
                             [(= maximum blue) (f red green 4)]))
                         ; Instead of multiplikation it with 1/6, we could also just divide it by 6
                         6))
          (define lightness (/ (+ maximum minimum) 2))
          (define saturation (if
                              (or (= difference 0) (= lightness 1) (= lightness 0))
                              0 (/ difference (- 1
                                                 (abs (sub1 (* 2 lightness)))))))
          ]
    (make-hsl hue saturation lightness)))

;; Tests
; black
(check-expect (rgb-to-hsl (make-color 0 0 0)) (make-hsl 0 0 0))
; White
(check-expect (rgb-to-hsl (make-color 255 255 255)) (make-hsl 0 0 1))
; Max color elements
(check-expect (rgb-to-hsl (make-color 255 0 0)) (make-hsl 0 1 0.5))
(check-within (rgb-to-hsl (make-color 0 255 0)) (make-hsl .33 1 .5) ERROR)
(check-within (rgb-to-hsl (make-color 0 0 255)) (make-hsl .66 1 .5) ERROR)

(check-within (rgb-to-hsl (make-color 85 85 85)) (make-hsl 0 0 .33) ERROR)


;; rgb-to-hsl-list: (listof color) -> (listof hsl)
;;
;; Converts sequentially all colors structs to the hsl struct. The returned list
;; keeps the same order.
;;
;; Example:
;;   (rgb-to-hsl-list (list (make-color 0 0 0) (make-color 255 255 255)))
;;   = (list (make-hsl 0 0 0) (make-hsl 0 0 1))
(define (rgb-to-hsl-list rgb-list)
  (map rgb-to-hsl rgb-list))

;; Tests
(check-expect (rgb-to-hsl-list empty) empty)
(check-expect (rgb-to-hsl-list (list (make-color 0 0 0) (make-color 255 255 255)))
              (list (make-hsl 0 0 0) (make-hsl 0 0 1)))



;; ====== Problem 6.2 ======

;; round-half-up: number -> integer
;;
;; Rounds the number to the nearest neighbor
;;
;; Example: (round-half-up 2.5) = 3
(define (round-half-up number) (floor (+ number .5)))

;; Tests
(check-expect (round-half-up 2.5) 3)
(check-expect (round-half-up 2.45) 2)
(check-expect (round-half-up 2.6) 3)
(check-expect (round-half-up 3) 3)



;; hsl-to-ascii: hsl -> integer
;;
;; Converts a hsl struct to number in the ascii table. This character will look-like
;; the corresponding hsl value of a pixel.
;;
;; Example: (hsl-to-ascii (make-hsl 1 0 0.5)) = 90 where 90 in the ascii table stands for Z
(define (hsl-to-ascii hsl)
  (local
    [
     ; Shortcuts for hsl attributes
     (define lightness (hsl-L hsl))
     (define hue (hsl-H hsl))

     ; Some magic numbers from the ascii table
     (define space 35)
     ; #
     (define number-sign 32)
     ; alphabet (A-Z and a-z)
     (define alph-A 65)
     (define alph-Z 90)
     (define alph-a 97)
     (define alph-z 122)
      
     ;; range: number number number -> number
     ;;
     ;; Selects a number from a certain range where perc is the percent value
     ;; between 0 and 1. 0 stands for start and 1 stands for end.
     ;;
     ;; Example: (range 50 70 .5) = 60
     (define (range start end perc) (+ start
                                       ; Round the value because there is no decimal number in the ascii table
                                       (round-half-up (* perc (- end start)))))]
    (cond
      [(<= lightness .05) space]
      [(<= lightness .5) (range alph-A alph-Z hue)]
      [(<= lightness .95) (range alph-a alph-z hue)]
      [else number-sign])))

;; Tests
; black -> 35 = #
(check-expect (hsl-to-ascii (make-hsl 0 0 0)) 35)
(check-expect (hsl-to-ascii (make-hsl 1 0 .5)) 90)
(check-expect (hsl-to-ascii (make-hsl 1 0 .55)) 122)
; white -> 32 = Space
(check-expect (hsl-to-ascii (make-hsl 0 0 1)) 32)
; decimal inputs should still return a non-decimal number
(check-expect (hsl-to-ascii (make-hsl .33 .33 .33)) 73)



;; hsl-to-ascii-list: (listof hsl) -> (listof integer)
;;
;; Converts sequentially all hsl structs to an ascii character. The returned list
;; keeps the same order.
;;
;; Example:
;;   (hsl-to-ascii-list (list (make-hsl 0 0 0) (make-hsl 0 0 1)))
;;   = (list 35 32)
(define (hsl-to-ascii-list hsl-list)
  (map hsl-to-ascii hsl-list))

;; Tests
(check-expect (hsl-to-ascii-list empty) empty)
(check-expect (hsl-to-ascii-list (list (make-hsl 0 0 0) (make-hsl 0 0 1)))
              (list 35 32))



;; ====== Problem 6.3 ======

;; average3: (listof color) -> (listof (triple of number))
;;  
;; Blurs the image
;;
;; Example: (average3 (list (make-color 4 4 4) (make-color 5 5 5) (make-color 12 12 12)))
;; = (list (make-color 3 3 3) (make-color 7 7 7) (make-color 7 7 7))
(define (average3 image)
        (local [
                (define (downsample input output last-value)
                        (if (empty? input)
                            empty
                            (downsample (rest input)
                                        (cons (/ (+ last-value (* (first input) 2) (if (empty? (rest input))
                                                                                               0
                                                                                               (second input)
                                                                                   )) 4) output
                                        )
                                        (first input)
                            )
                        )
                )
               ]
        
               (downsample (list 1  5  7  3  12  13  11  4  16  20  17  1  14  22  13  5) empty 0)
        )
)

(average3 empty)

;; No tests necessary here (but it makes sense to use it)
(check-expect (average3 empty) empty)
(check-expect (average3 (list (make-color 2 2 2))) (list (make-color 1 1 1)))
(check-expect (average3 (list (make-color 4 2 8))) (list (make-color 2 1 4)))
(check-expect (average3 (list (make-color 4 4 4) (make-color 5 5 5) (make-color 12 12 12)))
              (list (make-color 3 3 3) (make-color 7 7 7) (make-color 7 7 7)))



;; ====== Problem 6.4 ======

;; downscale: (listof color) integer -> (listof color)
;;
;; Downscale an image represented as a list of colors to certain target width
;;
;; Example: (downscale (average3 (list (make-color 1 1 1) (make-color 2 2 2) (make-color 3 3 3) (make-color 4 4 4) 1)))
;; = (list (make-color 1 1 1))
(define (downscale image width)
  ; 1. Count all elements and pair them with the value
  ; 2. Remove all odd rows
  ; 3. Remove all odd elements in a row
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
;; here: only visual tests (load, downsample/average3 and export image, do a visual comparison)
;; load-averag3-save: string string integer -> boolean (and PNG-image on disk)
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

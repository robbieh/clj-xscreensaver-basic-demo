(ns clj-xscreensaver-basic-demo.arcs
  (:import
    [java.awt.image BufferedImage]
    [java.awt Graphics Graphics2D]
    [java.awt BasicStroke Color RenderingHints]
    [java.awt.geom Arc2D Arc2D$Float]))

(def state (atom {:width nil :height nil :arcs nil}))

(defn print-arc [a]
  (let [width (.getWidth a)
        height (.getHeight a)
        start (.getAngleStart a)
        extent (.getAngleExtent a)]
    (println width height start extent)))
;(doseq [a (:arcs @state)] (print-arc (:arc a)))

(defn random-arc [x y w h]
  (let [size (rand-int h) ;y as a proxy for max radius
        hsize (* 0.5 size)
        start (rand-int 360)
        extent (+ 30 (rand-int 30))]
    (new Arc2D$Float (- x hsize) (- y hsize) size size start extent Arc2D/OPEN)))

(defn set-up-state [^BufferedImage canvas]
  (let [width (.getWidth canvas)
        height (.getHeight canvas)
        hw (* 0.5 width)
        hh (* 0.5 height)
        num-levels 10
        arcs       (repeatedly 25 #(hash-map :arc (random-arc hw hh width height)
                                             :step (rand-nth (range -2 2 0.1))
                                             ))]
    (swap! state assoc :width width)
    (swap! state assoc :height height)
    (swap! state assoc :arcs arcs)))

(defn rotate-arc! [arc-map]
  (.setAngleStart (:arc arc-map)  
                  (mod (+ (.getAngleStart (:arc arc-map)) (:step arc-map)) 360)))

(defn draw [^BufferedImage canvas]
  (let [g (.getGraphics canvas)
        width (:width @state)
        height (:height @state)]
    ;(.setRenderingHints g RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON)
    (.setBackground g Color/BLACK)
    (.setColor g Color/GREEN)
    (.setStroke g (new BasicStroke 5))
    (.clearRect g 0 0 width height)
    (doseq [arc (:arcs @state)]
        (.draw g (:arc arc)))
    (.dispose g))
  (run! rotate-arc! (:arcs @state))
  nil)


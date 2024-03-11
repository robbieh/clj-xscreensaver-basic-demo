(ns clj-x11.core
  (:require [clojure.reflect :refer [reflect]]
            [clj-x11.arcs :as arcs])
  (:import 
    [com.sun.jna Native Pointer Memory]
    [com.sun.jna.platform.unix X11]
    [com.sun.jna.platform.unix X11$Display X11$XGCValues X11$XImage X11$Pixmap X11$Visual X11$Window X11$XWindowAttributes X11$WindowByReference]
    [com.sun.jna Pointer Structure PointerType Native NativeLong ToNativeConverter ToNativeContext NativeMappedConverter]
    [com.sun.jna.ptr ByReference IntByReference NativeLongByReference PointerByReference IntByReference NativeLongByReference]
    [sun.awt.X11 XlibWrapper XToolkit]
    [sun.java2d.x11 XSurfaceData X11Renderer]
    [java.awt Color]
    [java.awt.image BufferedImage ]
    [java.awt.geom Ellipse2D Ellipse2D$Float]
    [java.awt Graphics Graphics2D Point]
    [javax.imageio ImageIO]
    [java.io File]
    [sun.java2d.x11 X11VolatileSurfaceManager]
    [sun.java2d SunGraphics2D SurfaceData]
    [sun.java2d.loops Blit]
    )
  (:gen-class))


;setup 
(when-not (System/getenv "DISPLAY")
  (println "Please set the $DISPLAY")
  (System/exit 1))

(defn setup [window-id]
  (def x (X11/INSTANCE))
  (def dpy (.XOpenDisplay x (System/getenv "DISPLAY")))
  (def xwa (new X11$XWindowAttributes))
  (def xgcv (new X11$XGCValues))
  (def win (new X11$Window window-id )) 
  ;(def xgc (.XCreateGC x dpy win (new NativeLong 0) xgcv)) ;TODO - what is '0' here?
  (def xgc (.XCreateGC x dpy win (new NativeLong 0) xgcv)) 
  (.XGetWindowAttributes x dpy win xwa)
  (def width (.width xwa))
  (def height (.height xwa))

  (def canvas (BufferedImage. width height BufferedImage/TYPE_INT_ARGB))
  (def offset (new Point))
  (def pixel (int-array 4))
  (def pixels (int-array (* width height)))
  (def buffer (new Memory (* 4 width height)))
  (def image (.XCreateImage x dpy (.visual xwa) 32 2 0 buffer width height 32 (* 4 width)))
  )

(defn cleanup []
  (println "Cleaning up")
  (try
    (.XFree x (.getPointer image))
    (.XFreeGC x dpy xgc)
    (.XCloseDisplay x dpy)
    (catch java.lang.IllegalArgumentException e nil)))

(defn xput [^BufferedImage canvas]
  (let [raster (.getData canvas)]
    (.write buffer 0 (.getData (.getDataBuffer raster)) 0 (.getSize (.getDataBuffer raster)))
    (.XPutImage x dpy win xgc image 0 0 0 0 width height)))

(defn -main [& args]
  (.addShutdownHook (Runtime/getRuntime) 
                    (Thread. #(cleanup)))

  ;get $XSCREENSAVER_WINDOW from environment
  (let [window-id (System/getenv "XSCREENSAVER_WINDOW")]
    (println "Window id variable: " (System/getenv "XSCREENSAVER_WINDOW"))
    (println "Window id parsed: " (read-string window-id))
    (setup (read-string window-id)) 
    (println "setup done")
    (arcs/set-up-state canvas)
    (loop []
      (arcs/draw canvas)
      (xput canvas)
      (recur))))

(comment
(setup 102768748 ) ; konsole
(setup 136319517 ) 
(setup (read-string "0x5a00003"))
(def image (.XCreateImage x dpy (.visual xwa) 32 2 0 buffer width height 32 (* 4 width)))
(def canvas (BufferedImage. width height BufferedImage/TYPE_INT_RGB))
(.x xwa)
(.width xwa)
(.depth xwa)
(.visual xwa)
(.root xwa)
(.colormap xwa)
(.map_installed xwa)
(.bits_per_rgb xwa)
(arcs/set-up-state canvas)
(do (arcs/draw canvas) (xput canvas))
(cleanup)
)


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

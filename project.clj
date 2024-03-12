(defproject clj-xscreensaver-basic-demo "0.0.1"
  :description "A basic demonstration of using Clojure to write an XScreenSaver hack"
  :url "https://github.com/robbieh/clj-xscreensaver-basic-demo"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [net.java.dev.jna/jna "5.9.0"]
                 [net.java.dev.jna/jna-platform "5.9.0"]
                 ;[clojure2d "1.4.5"]
                 ;[generateme/fastmath "2.2.1" :exclusions [com.github.haifengl/smile-mkl org.bytedeco/openblas]]
                 ]
  :main  clj-xscreensaver-basic-demo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :jvm-opts ["--add-exports" "java.desktop/sun.java2d.x11=ALL-UNNAMED"]
  :javac-opts ["--add-exports" "java.desktop/sun.java2d.x11=ALL-UNNAMED"]
  )

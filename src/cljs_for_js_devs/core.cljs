(ns cljs-for-js-devs.core
  (:require [reagent.core :as r]
            [stylefy.core :as stylefy]
            [cljs-for-js-devs.app :as app]))

;; Global styles
(def body-style
  {:margin "20px"
   :background-color "#E9EAEC"
   :font-family "'Lato', sans-serif"})
(def global-style
  {:box-sizing "inherit"
   :margin "0"
   :padding "0"})
(def html-style
  {:box-sizing "border-box"})

(defn start []
  (stylefy/init)
  (stylefy/tag "body" body-style)
  (stylefy/tag "*" global-style)
  (stylefy/tag "html" html-style)
  (r/render [app/root]
    (.getElementById js/document "root")))

(start)

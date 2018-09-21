(ns cljs-for-js-devs.app
    (:require [reagent.core :as r :refer [atom]]
              [stylefy.core :refer [use-style]]))

(def avatar-style
  {:color "#00C19E"
   :width "30px"
   :height "30px"
   :border-radius "100%"
   :text-align "center"
   :border "2px solid #00C19E"
   :font-size "12px"
   :font-weight "900"
   :display "flex"
   :justify-content "center"
   :flex-direction "column"
   :float "left"})

(def details-style
  {:float "left"
   :font-size "12px"
   :margin "0 0 0 10px"
   :width "calc(100% - 40px)"})

(def username-style
  {:font-weight "900"
   :float "left"})

(def pipe-separator-style
  {:float "left"
   :margin "0 5px"
   :color "#75787B"})

(def timestamp-style
  {:float "left"
   :color "#75787B"
   :font-size "10px"
   :font-weight "900"
   :line-height "16px"})

(def message-body-style
  {:float "left"
   :width "100%"
   :font-size "12px"
   :margin "5px 0 0 0"})

(def message-style
  {:font-size "12px"
   :width "100%"
   :float "left"
   :margin "0 0 10px 0"})

(def button-style
  {:font-size "14px"
   :width "100%"
   :background "#00C19E"
   :color "white"
   :font-weight "900"
   :text-transform "capitalize"
   :padding "15px"
   :text-align "center"
   :float "left"})

(def reply-input-wrap-style
  {:width "calc(100% - 40px)"
   :float "left"
   :margin "0 20px 20px 20px"
   :position "relative"})
   
(def icon-style
  {:position "absolute"
   :padding "11px"
   :opacity ".6"})

(def reply-input-style
  {:width "100%"
   :float "left"
   :padding "10px 10px 10px 35px"})  

(def thread-style
  {:padding "20px"
   :width "100%"
   :float "left"
   :max-height "400px"
   :overflow-y "auto"})

(def messenger-style
  {:width "450px"
   :background "white"
   :float "left"})

(defonce messages (r/atom (sorted-map)))

(defonce counter (r/atom 0))

(defonce user (r/atom {:initials "BA" :username "Brad Alexander"}))

(def messageBody (r/atom ""))

(defn add-message [initials username timestamp body]
  (let [id (swap! counter inc)]
    (swap! messages assoc id {:key id :initials initials :username username :timestamp timestamp :body body})
    (reset! messageBody "")))

(defonce init (do
  (add-message "AW" "Andre Wheeler" "Sept 19 at 6:34PM" "We have to make sure that we have all the punch list items accounted for. Cory Robinson I want you to take the lead on this.")))

(defn message [messageObj]
  (fn [{:keys [id initials username timestamp body] :as messageObj}]
    [:div (use-style message-style)
      [:div (use-style avatar-style) initials]
      [:div (use-style details-style)
        [:div (use-style username-style) username]
        [:div (use-style pipe-separator-style) "|"]
        [:div (use-style timestamp-style) timestamp]
        [:div (use-style message-body-style) body]]]))

(defn thread [messages]
  [:div (use-style thread-style)
    (for [[index messageObj] messages]
      ^{:key (:id index)}
      [message messageObj])])

(defn on-key-down [k initials username timestamp body]
  (let [key-pressed (.-which k)]
    (condp = key-pressed
      13 (add-message initials username timestamp body)
      nil)))

(defn reply-input []
    (fn []
      [:div (use-style reply-input-wrap-style)
        [:img (use-style icon-style
          {:src "https://s3.amazonaws.com/bakery-assets.workframe.com/ui/resources/user-icon.svg"})]
        [:input 
          {:type "text"
            :placeholder "Enter reply here"
            :style reply-input-style
            :value @messageBody
            :on-change #(reset! messageBody (-> % .-target .-value))
            :on-key-down #(on-key-down % (@user :initials) (@user :username) "Sept 19 at 6:34PM" @messageBody)
          }]]))

(defn button [title]
    [:div (use-style button-style
      {:on-click #(add-message (@user :initials) (@user :username) "Sept 19 at 6:34PM" @messageBody )})
    title])

(defn messenger [messages]
  [:div (use-style messenger-style)
   [thread messages]
   [reply-input]
   [button "reply"]])


(defn root []
  [messenger @messages])

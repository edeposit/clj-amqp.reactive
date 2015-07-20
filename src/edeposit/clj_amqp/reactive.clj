(ns edeposit.clj-amqp.reactive
  (:require    [rx.lang.clojure.core :as rx]
               )
  (:import  [rx Observable]
            [java.util.concurrent TimeUnit]
            )
  )

(defn make-queue-obs [fn interval time-unit]
  (def my-time (Observable/interval interval time-unit))
  (->> my-time 
       (rx/map fn)
      )
  )

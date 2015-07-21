(ns edeposit.clj-amqp.reactive
  (:require    [rx.lang.clojure.core :as rx]
               [langohr.basic :as lb]
               )
  (:import  [rx Observable]
            [java.util.concurrent TimeUnit]
            )
  )

(defn make-queue-obs [fn & [interval time-unit]]
  (def my-time (Observable/interval (or interval 100) 
                                    (or time-unit TimeUnit/MILLISECONDS)))
  (->> my-time 
       (rx/map fn)
       (rx/filter (complement nil?))
      )
  )

(defn amqp-queue->obs [channel queue-name & [interval time-unit]]
  (make-queue-obs (fn [val] (lb/get channel queue-name)) 
                  (or interval 100) 
                  (or time-unit TimeUnit/MILLISECONDS))  
  
  )

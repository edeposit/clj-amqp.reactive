(ns edeposit.clj-amqp.reactive-test
  (:require [clojure.test :refer :all]
            [edeposit.clj-amqp.reactive :as rea]
            [rx.lang.clojure.core :as rx]
            [rx.lang.clojure.blocking :as rxb]
            )
  (:import [java.util.concurrent TimeUnit]
           [rx Observable]
           )
  )

(deftest observable-queue-test
  (testing "new observable queue"
    (def obs (rea/make-queue-obs (fn [val] 1) 10 TimeUnit/MILLISECONDS))
    (def sum (->> obs
                  (rx/take 10)
                  (rx/reduce + 0)
                  (rxb/last)
                  )
      )
    (is (= sum 10)))
  )

(defn odd-numbers [val]
  (if (odd? val)
    val
    nil
    )
  )
(deftest just-non-nil-values
  (testing "just non nil values queue emits"
    (def obs (rea/make-queue-obs odd-numbers 10 TimeUnit/MILLISECONDS))
    (is (= (rxb/o->seq (->> obs (rx/take 5 ))) (list 1 3 5 7 9))) 
    )
  )

(deftest implicit-arguments
  (testing "implicit arguments with timeunit"
    (def obs (rea/make-queue-obs odd-numbers))
    (is (= (rxb/o->seq (->> obs (rx/take 5 ))) (list 1 3 5 7 9))) 
    )
  )

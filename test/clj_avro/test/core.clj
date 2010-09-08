(ns clj-avro.test.core
  (:use [clj-avro.core] :reload)
  (:use [clojure.test]))

(def *test-schema*
         (defschema
           {:namespace "test.avro",
            :name "FacebookUser",
            :type "record",
            :fields [{:name "name", :type "string"},
                     {:name "num_likes", :type "int"},
                     {:name "num_photos", :type "int"},
                     {:name "num_groups", :type "int"}]}))


(deftest test-can-roundtrip
  (let [data {:name "bob"
              :num_likes 1
              :num_photos 2
              :num_groups 3}]
    (is (= data (thaw *test-schema* (freeze *test-schema* data))))))

;; (test-can-roundtrip)

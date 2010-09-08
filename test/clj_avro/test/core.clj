(ns clj-avro.test.core
  (:use [clj-avro.core] :reload)
  (:use [clojure.test]))

(def *test-simple-schema*
     (defschema
       {:namespace "test.avro"
        :name "FacebookUser"
        :type "record"
        :fields [{:name "name"       :type "string"}
                 {:name "num_likes"  :type "int"}
                 {:name "num_photos" :type "int"}
                 {:name "num_groups" :type "int"}]}))

(deftest test-can-roundtrip-simple
  (let [data {:name "bob"
              :num_likes 1
              :num_photos 2
              :num_groups 3}]
    (is (= data (thaw *test-simple-schema* (freeze *test-simple-schema* data))))))


;(def *test-nested-map-schema*
;     (defschema
;       {:namespace "test.avro"
;        :name "FacebookUserWithProps"
;        :type "record"
;        :fields [{:name "name"    :type "string"}
;                 {:name "params"  :type {:type "map" :values "string"}}]}))
;
;(deftest test-can-roundtrip-nested-map
;  (let [data {:name "Frank"
;              :params {:last_name "Schlobatnik"
;                       :age       73}}]
;    (is (= data (thaw *test-nested-map-schema* (freeze *test-nested-map-schema* data))))))
;
;; (test-can-roundtrip)

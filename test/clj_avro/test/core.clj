(ns clj-avro.test.core
  (:use [clj-avro.core] :reload)
  (:use [clojure.test]))

;; (run-all-tests)

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

;; (test-can-roundtrip-simple)

(def *test-nested-map-schema*
    (defschema
      {:namespace "test.avro"
       :name "FacebookUserWithProps"
       :type "record"
       :fields [{:name "name"    :type "string"}
                {:name "params"  :type {:type "map" :values "string"}}]}))

(deftest test-can-roundtrip-nested-map
 (let [data {:name "Frank"
             :params {:last_name "Schlobatnik"
                      :age       "73"}}]
   (is (= data (thaw *test-nested-map-schema* (freeze *test-nested-map-schema* data))))))

;; (test-can-roundtrip-nested-map)


;; (def *test-weird-field-names-schema*
;;     (defschema
;;       {:namespace "test.avro"
;;        :name "ThingWithWeirdFieldNames"
;;        :type "record"
;;        :fields [{:name "name yeah@?'"    :type "string"}]}))

;; (deftest test-weird-field-names
;;  (let [data {"name yeah@?'" "stuff"}]
;;    (is (= data (thaw *test-weird-field-names-schema* (freeze *test-weird-field-names-schema* data))))))


;; (test-weird-field-names)
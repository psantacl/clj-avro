(ns clj-avro.core
  (:import [org.apache.avro Schema]
           [org.apache.avro.util Utf8]
           [org.apache.avro.io JsonEncoder JsonDecoder]
           [org.apache.avro.generic GenericData GenericData$Record GenericDatumWriter GenericDatumReader]
           [java.io File FileOutputStream FileInputStream ByteArrayOutputStream ByteArrayInputStream]
           [java.util Map])
  (:require [clojure.contrib.json.write :as json]))

(defn schema->json [m]
  (json/json-str m))

(defn defschema [m]
  (Schema/parse (schema->json m)))

(defmulti value-marshal class)

(defmethod value-marshal :default [val]
  val)

(defmethod value-marshal String [val]
  (Utf8. val))

(defmethod value-marshal clojure.lang.Keyword [val]
  (Utf8. (name val)))


(defmethod value-marshal Map [val]
  (reduce (fn [m k]
            (assoc m (value-marshal k) (value-marshal (get val k))))
          {}
          (keys val)))

(defmulti value-unmarshal class)

(defmethod value-unmarshal Utf8 [val]
  (.toString val))

(defmethod value-unmarshal Map [val]
  (reduce (fn [m k]
            (assoc m
              (value-unmarshal k)
              (value-unmarshal (get val k))))
          {}
          (keys val)))

(defmethod value-unmarshal :default [val]
  val)

(defmulti key-marshal class)

(defmethod key-marshal clojure.lang.Keyword [k]
  (name k))

(defmethod key-marshal :default [k]
  (str k))

;; TODO: we're only supporting linear record for now, in the future
;; we'll need to assume data (a map) is a tree with substructure and
;; map that onto the avro schema/record correctly.
(defn freeze [schema data]
  (let [bao (ByteArrayOutputStream.)
        w (GenericDatumWriter. schema)
        e (JsonEncoder. schema bao)
        generic-record (GenericData$Record. schema)]
    (doseq [k (keys data)]
      (.put generic-record (key-marshal k) (value-marshal (k data))))
    (.write w generic-record e)
    (.flush e)
    (.toString bao)))

(comment

  (freeze *some-schema*
          {:name "bob"
           :num_likes 1
           :num_photos 2
           :num_groups 3})

  )


(defn generic-record->map [rec]
  (reduce (fn [m field]
            (assoc m (keyword (.name field))
                   (value-unmarshal (.get rec (.name field)))))
          {}
          (.getFields (.getSchema rec))))

;; (generic-record->map *foo*)

;; TODO: we're only supporting linear record for now, in the future
;; we'll need to assume data (a map) is a tree with substructure and
;; map that onto the avro schema/record correctly.
(defn raw-thaw [schema data]
  (let [datum-reader (GenericDatumReader. schema)
        decoder      (JsonDecoder. schema (ByteArrayInputStream. (.getBytes data)))]
    (.read datum-reader nil decoder)))

(defn thaw [schema data]
  (generic-record->map (raw-thaw schema data)))

(comment

  (def *foo*  (raw-thaw *some-schema*
                    (freeze *some-schema*
                            {:name "bob"
                             :num_likes 1
                             :num_photos 2
                             :num_groups 3})))
  (thaw *some-schema*
                    (freeze *some-schema*
                            {:name "bob"
                             :num_likes 1
                             :num_photos 2
                             :num_groups 3}))

  (def *foo*  (thaw *some-schema*
                    (freeze *some-schema*
                            {:name "bob"
                             :num_likes 1
                             :num_photos 2
                             :num_groups 3})))
  (map (memfn name) (vec *foo*))

  (map #((memfn name) %1) (vec *foo*))

  (map #(keyword ((memfn name) %1)) (vec *foo*))


  )


(comment


  (let [bao (ByteArrayOutputStream.)
        w (GenericDatumWriter. *some-schema*)
        e (JsonEncoder. *some-schema* bao)
        fos (FileOutputStream. (File. "test_data.avro"))
        generic-record (GenericData$Record. *some-schema*)]
    (.init e fos)
    (.put generic-record "name" (Utf8. "Doctor Who"))
    (.put generic-record "num_likes", 1)
    (.put generic-record "num_photos", 0)
    (.put generic-record "num_groups", 423)
    (.write w generic-record e)
    (.flush e))


  (let [bao (ByteArrayOutputStream.)
        w (GenericDatumWriter. *some-schema*)
        e (JsonEncoder. *some-schema* bao)
        ;; fos (FileOutputStream. (File. "test_data.avro"))
        generic-record (GenericData$Record. *some-schema*)]
    ;; (.init e fos)
	;;    (.init e bao)
    (.put generic-record "name" (Utf8. "Doctor Who"))
    (.put generic-record "num_likes", 1)
    (.put generic-record "num_photos", 0)
    (.put generic-record "num_groups", 423)
    ;; (.put generic-record "not_there", "haha")
    (.write w generic-record e)
    (.flush e)
    (.toString bao)
    )



  (let [datum-reader (GenericDatumReader. *some-schema*)
        decoder      (JsonDecoder. *some-schema* (FileInputStream. (File. "test_data.avro")))]
    (str (.get (.read datum-reader nil decoder) "name")))


  )

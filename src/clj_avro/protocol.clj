(ns clj-avro.protocol
  (:require
   [clj-avro.core :as avro]
   [clojure.contrib.json :as json])
  (:use
   [clj-etl-utils.lang-utils :only [raise resource-as-string]]))


(def *protocol-registry* (atom {}))

(defn register-protocol [proto-name protocol]
  (swap! *protocol-registry* assoc proto-name protocol))

(defn lookup-protocol [proto-name]
  (get @*protocol-registry* proto-name))

(defn register-protocol-from-string [proto-name definition]
  (register-protocol proto-name (avro/parse-protocol definition)))

(defn register-protocol-from-file [proto-name f]
  (register-protocol proto-name (avro/parse-protocol (slurp f))))

(defn register-protocol-from-resource [proto-name res]
  (register-protocol proto-name (avro/parse-protocol (resource-as-string res))))

;; NB: this should be memoized
(defn get-schema
  ([proto-and-schema]
     (get-schema (keyword (.getNamespace proto-and-schema))
                 (.getName proto-and-schema)))
  ([proto schema]
      (if-let [p (lookup-protocol proto)]
        (.getType p (name schema)))))

(defn avro-encode! [proto-and-schema thing]
  (avro/freeze (get-schema proto-and-schema) thing))

(defn avro-decode! [proto-and-schema s]
  (avro/thaw (get-schema proto-and-schema) s))

;; NB: these are for 'avro light', they create and consume just basic
;; JSON, but do not create or validate actual avro encdoed JSON.  They
;; can be used in cases where you want to use lightweight JSON object
;; (leaving out optional fields in their entirety), but still wish to
;; have validation performed on the structure and content of the data
;; structures.
(defn json-decode! [type body]
  (let [obj (json/read-json body)]
    (avro-encode! type obj)
    obj))

(defn json-decode [type body]
  (json/read-json body))

(defn json-encode! [type thing]
  (avro-encode! type thing)
  (json/json-str thing))

(defn json-encode [type thing]
  (json/json-str thing))

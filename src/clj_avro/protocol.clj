(ns clj-avro.protocol
  (:require
   [clj-avro.core :as avro])
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


(comment

(register-protocol-from-file
 :wall
 "/home/superg/projects/rn-wall/rn-wall-service/resources/public/api/wall-api-v1.json")

(get-schema :wall "relay.APIResponse")

(avro-decode!
 :wall/relay.APIResponse
 (avro-encode! :wall/relay.APIResponse {:status 1 :foo 3}))

(APIResponse/to-json {:status 1})
(APIResponse/parse "{\"status\": 1}")

(to-json (APIResponse. 1))

(get-schema :wall/relay.APIResponse)


)
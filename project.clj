(defproject org.clojars.relaynetwork/clj-avro "1.0.9-SNAPSHOT"
  :description "Avro Wrapper for Clojure"
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]]
  :plugins [[lein-release "1.0.0-SNAPSHOT"]]
  :lein-release {:deploy-via :clojars}
  :local-repo-classpath true
  :dependencies [[org.clojure/clojure "1.3.0"]
                 ;; [org.apache.avro/avro "1.5.4"]
                 [org.apache.avro/avro "1.6.1"]
                 [org.clojure/data.json "0.1.3"]
                 [org.clojars.kyleburton/clj-etl-utils        "1.0.49-SNAPSHOT"]])

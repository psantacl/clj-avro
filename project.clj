(defproject org.clojars.relaynetwork/clj-avro "1.0.8"
  :description "Avro Wrapper for Clojure"
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]]
  :plugins [[lein-release "1.0.0-SNAPSHOT"]]
  :lein-release {:deploy-via :clojars}
  :local-repo-classpath true
  :dependencies [[org.clojure/clojure "1.2.0"]
                 ;; [org.apache.avro/avro "1.5.4"]
                 [org.apache.avro/avro "1.6.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojars.kyleburton/clj-etl-utils        "1.0.41"]])

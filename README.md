# clj-avro

Basic wrapper for avro functionality.  Currently, only supports flat/linear schemas with no nested attributes.

## Usage

    (def *some-schema*
         (defschema
           {:namespace "test.avro",
            :name "FacebookUser",
            :type "record",
            :fields [{:name "name", :type "string"},
                     {:name "num_likes", :type "int"},
                     {:name "num_photos", :type "int"},
                     {:name "num_groups", :type "int"}]}))

    (freeze *some-schema*
            {:name "bob"
             :num_likes 1
             :num_photos 2
             :num_groups 3})

    (thaw *some-schema*
        (freeze *some-schema*
                {:name "bob"
                 :num_likes 1
                 :num_photos 2
                 :num_groups 3}))


    (def *avro-schema-with-map*
         (avro/defschema
           {:namespace "thank-you-tours"
                :name "APayloadCarryingThing"
                :type "record"
                :fields [{:name "command" :type "string"},
                         {:name "payload" :type {:type "map" :values "string"}}]}))
    (avro/thaw
     *avro-schema-with-map*
     (avro/freeze
       *avro-schema-with-map*
       {:command "did-you-get-that-thing-i-sent-you"
        :payload {"key" "value"}}))


    ;; Example using a nested "map of maps"
    (def *avro-schema-with-map*
       (avro/defschema
         {:namespace "avro.examples"
              :name "MapOfMaps"
              :type "record"
              :fields [{:name "command" :type "string"}
                       {:name "payload" :type {:type "map" :values ["string" "null" "int" {:type "map" :values "string"}]}}]}))

## Value Marshalling and Unmarshalling

The library contains multimethods to help assist with value
marshalling and unmarshalling.  This section is unfinsihed at this
time.


## Installation

Lineingen:

    [org.clojars.relaynetwork/clj-avro "1.0.0"]


Maven:

    <dependency>
      <groupId>org.clojars.relaynetwork</groupId>
      <artifactId>clj-avro</artifactId>
      <version>1.0.0</version>
    </dependency>



## License

Copyright (C) 2010 Relay Network

Distributed under the Eclipse Public License, the same as Clojure.

(ns ^:figwheel-load zot-world.server.middleware
  (:require [cljs.nodejs :as nodejs]
            [cljs.reader :as reader]
            [clojure.string :as str]))

(defonce body-parser (nodejs/require "body-parser"))
(defonce csrf-package (nodejs/require "csurf"))
(defonce twilio-package (nodejs/require "twilio"))

(defonce csrf
  (csrf-package #js {:cookie true}))

(defonce form-parser
  (.urlencoded body-parser #js {:extended false}))

(defonce edn-parser
  (.raw body-parser #js {:type "application/edn"}))

(defn twilio [validate?]
  (.webhook twilio-package #js {:includeHelpers false
                                :validate validate?}))

(defn restrict [req res nxt]
  (if (some? (.. req -signedCookies -id))
    (nxt)
    (.redirect res "/login")))
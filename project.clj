(defproject clucky "0.0.1"
  :description "HTTP proxy for StatsD events"
  :url "http://github.com/zachpendleton/clucky"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.16"]
                 [compojure "1.2.0"]
                 [ring "1.3.2"]
                 [environ "1.0.0"]
                 [com.stuartsierra/component "0.2.2"]]
  :main ^:skip-aot clucky.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev [:dev-common :dev-overrides]
             :dev-common {:dependencies [[org.clojure/tools.namespace "0.2.7"]]
                          :source-paths ["dev"]}
             :dev-overrides {}})

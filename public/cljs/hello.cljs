(require
 '[reagent.dom :as rdom])

(defn ->f
  "Given a quoted Reagent body (which might use functions like
  `reagent.core/with-let` or third-party components, not just Hiccup syntax),
  returns a no-argument-function component that renders an `eval`-ed version of
  `body`."
  [body]
  (eval (list 'fn [] body)))


;; --- emmy viewer code demo ---

(require
 '[emmy.env :as e]
 '[emmy.mafs :as mafs]
 )

(def some-graph
  (mafs/mafs
   {:height 300}
   (mafs/cartesian)
   (mafs/vector [1 2] {:color :blue})
   (mafs/of-x {:y (fn [x] (e/square (e/sin (e/+ x 3)))) :color :blue})
   (mafs/text "face" {:color :green})))

(defn my-component []
  [:div
   [->f some-graph]])

(rdom/render [my-component] (.getElementById js/document "app"))

(ns tinker
  (:refer-clojure
   :exclude [+ - * / zero? compare divide numerator denominator
             infinite? abs ref partial =])
  (:require
   [clojure.pprint :refer [pprint]]
   [clojure.walk :refer [postwalk]]
   [emmy.env :as e :refer [+ - * / zero? compare divide numerator denominator
                           infinite? abs ref partial =
                           ->infix ->TeX
                           square sin cos asin tanh
                           D cube simplify
                           literal-function Lagrange-equations up]]
   [emmy.viewer :as ev]
   [emmy.mafs :as mafs]
   [mathbox.core :as mathbox]
   [mathbox.primitives :as mb]
   [emmy.mathbox.plot :as plot]
   [reagent.core :as r]
   [reagent.dom :as rdom])
  )

(def viewer-name :emmy.scittle/reagent)

(def xform-key
  "Currently transforms are stored under this key for compatibility with the
  Emmy-Viewers Clerk code."
  :nextjournal.clerk/viewer)

(defn ^:no-doc strip-meta
  "Given an unevaluated Reagent body, returns an identical form with all metadata
  stripped off."
  [form]
  (postwalk
   (fn [x]
     (if (meta x)
       (vary-meta x dissoc xform-key)
       x))
   form))

(defn ->f
  "Given a quoted Reagent body (which might use functions like
  `reagent.core/with-let` or third-party components, not just Hiccup syntax),
  returns a no-argument-function component that renders an `eval`-ed version of
  `body`."
  [body]
  (eval
   (list 'fn [] (strip-meta body))))

(defn render-to-app [component & [docid]]
  (rdom/render [component] (js/document.getElementById
                            (or docid "app"))))

(comment
  ;; smoke checking
  (println "Hello there!...")
  (pprint {:a 10, :b true, :c [1]})
  )

(comment

  ;; Reagent
  ;; ------------------------------------------------------------
  (def state (r/atom {}))
  (defn my-component [{:keys [name]}]
    [:p name])
  (rdom/render [my-component {:name "ridho"}] (js/document.getElementById "app"))

  (defn render-to-app [component]
    (rdom/render [component] (js/document.getElementById "app")))

  (render-to-app
   (fn []
     (r/with-let [a 10]
       [:div
        ["A" a]])))



  ;; Emmy
  ;; ------------------------------------------------------------
  (->infix (/ (e/sin 'a) (e/cos 'a)))
  (->TeX 1)

  (- (* 7 (/ 1 2)) 2)
  (asin -10)

  (def render (comp ->infix simplify))
  (square (sin (+ 'a 3)))
  (render (square (sin (+ 'a 3))))

  (defn L-central-polar [m U]
    (fn [[_ [r] [rdot thetadot]]]
      (- (* 1/2 m
            (+ (square rdot)
               (square (* r thetadot))))
         (U r))))

  (let [potential-fn (literal-function 'U)
        L     (L-central-polar 'm potential-fn)
        state (up (literal-function 'r)
                  (literal-function 'theta))]
    (render
     (((Lagrange-equations L) state) 't)))


  ;; EmmyViewers
  ;; ------------------------------------------------------------
  ;; mafs
  ;; ------------------------------------------------------------
  (mafs/of-x sin {:color :blue})

  (defn mafs1 []
    [->f (mafs/of-x sin {:color :blue})])

  (defn component []
    [:div
     [:h1 "Basic 2D Graph"]
     [->f (mafs/mafs
           {:height 300}
           (mafs/cartesian)
           ;;(mafs/vector [1 -3] {:color :red})
           (mafs/of-x {:y (fn [x] (square x)) :color :green})
           ;;(mafs/of-x {:y (fn [x] (cube x)) :color :blue})
           (mafs/of-x {:y (fn [x] ((D square) x)) :color :blue})
           ;;(mafs/of-x {:y (fn [x] (* 2 x)) :color :cyan})
           (mafs/text "face" {:color :gold}))]])

  (render-to-app component)


  (defn component2 []
    (let [scene (plot/scene
                 (plot/of-x {:z sin})
                 (plot/parametric-curve
                  {:f (up sin cos (/ identity 3))
                   :t [-10 10]
                   :color :green}))]
      [:div
       [:h3 "Example 3D Plot 1"]
       [->f scene]]))

  (render-to-app component2)


  (defn component3 []
    [:div
     [:h3 "Example 3D Plot 2"]
     [mathbox/MathBox
      {:container {:style {:height "400px" :width "100%"}}
       :renderer  {:background-color 0xeeeeee
                   :max-distance 4}
       :scale 720
       :focus 1}
      [mb/Camera
       {:position [3.5 1 2.5]
        :proxy true}]
      [mb/Cartesian {:range [[0 1] [0 1] [0 1]]}
       [mb/Volume
        {:width 8 :height 5 :depth 20
         :items 1
         :channels 4
         :live false
         :expr (fn [emit x y z]
                 (emit x y z 1.0))}]
       [mb/Point
        {:points "<"
         :colors "<"
         :color 0xffffff
         :size 20}]]]])

  (render-to-app component3)

  )


(comment

  (->infix (/ (sin 'a) (cos 'a)))

  (mafs/cartesian)
  (mafs/of-x {:x e/sin})

  (meta (mafs/of-x {:x e/sin}))
  ;; =>
  {:portal.viewer/reagent? true,
   :portal.viewer/default :emmy.portal/mafs,
   :nextjournal.clerk/viewer '#_object[emmy$mafs$default_viewer],
   :portal.viewer/mafs? true}

  (mafs/of-x {:x e/sin :color :blue})
  (ev/expand (mafs/of-x {:x e/sin :color :blue}))
  (eval (ev/expand (mafs/of-x {:x e/sin :color :blue})))


  (pprint (mafs/of-x {:x e/sin :color :blue}))
  (pprint (ev/expand (mafs/of-x {:x e/sin :color :blue})))
  (pprint (eval (ev/expand (mafs/of-x {:x e/sin :color :blue}))))

  )

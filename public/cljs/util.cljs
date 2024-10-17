(require '[reagent.dom :as rdom])

(defn render [component]
  (rdom/render [component] (.getElementById js/document "app")))

(set! (. js/window -__RENDER) render)

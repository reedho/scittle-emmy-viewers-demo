# Introduction

## Scittle

This is [SCI](https://github.com/babashka/sci) exposed for usage in html script tags. For example, put this snippets on your html:

```html
<script type="application/x-scittle">
  (js/alert
   (cljs.pprint/cl-format
    false "Here our cljs code running on: ~a" js/window.navigator.userAgent))
</script>
```

In this environment, as shown above, `SCI` the interpreter is running on top browser javascript engine.

`SCI` on browser **is not** self-hosted ClojureScript, it is basically Clojure(Script) code compiled with ClojureScript compiler with advanced compilation.

> ClojureScript code block inside this script tag is not "normal" ClojureScript, but the Small Clojure Interpreter (`scittle.js`) running inside the browser. SCI itself can be compiled with advanced compilation, yielding a fairly small bundle size compared to self-hosted CLJS, but obviously it doesn’t run as fast compared to compiled CLJS code. There are trade-offs to be made between startup time, bundle size and runtime performance. SCI is usually used for applications that need a bit of dynamic evaluation at runtime. Using it in a script tag is just another way of using it.

Refs:

- https://clojureverse.org/t/scittle-evaluate-cljs-in-a-script-tag-using-sci/7687/3
- https://blog.klipse.tech/clojurescript/2016/04/04/self-host-part-1.html

## Reagent

There is 3 type of Reagent components:

1. Form-1 (an ordinary clojure function)
2. Form-2 (a 2nd order function, e.g. a function that return form-1)
3. Form-3 (`reagent/create-class`)

```clojure
;; form-1
(defn hello [] [:div "Hello"])

;; form-2
(defn counter []
  (let [x (r/atom 0)]
    (fn [] [:div @x])))

;; syntactic sugar creating form-2 component using with-let
(defn counter []
  (r/with-let [x (r/atom 0)]
    [:div @x]))
```

If component is form-1, there is 2 possible why when using this component inside parent component, either use `()` to eval it as ordinary function, or use `[]` to interpret it as a child component.

When the component is a form of form-2 or form-3, we must use `[]` when using the component.

After render function return a hiccup vector, Reagent interpretes it.

If Reagent sees a vector where first element is a function, it interprets that function as a renderer and builds a react components around that renderer.

```clojure
;; using `()` when incorporating/using component inside render function
(defn parent []
  [:div
    [:h1 "A"]
    (hello)])

;; using `[]`
(defn parent []
  [:div
    [:h1 "A"]
    [hello]])
```

There is `reagent.core/adapt-react-class` which will turn a React component into something that can be placed in the first position of a hiccup form, as if it were a Reagent render function.

```clojure
(defn parent []
  [:div
    [:h1 "A"]
    [(r/adapt-react-class FancyInputText) #js {:props1 "..."} "children"]])

;; Use special keyword `:>` to do the same
(defn parent []
  [:div
    [:h1 "A"]
    [:> FancyInputText #js {:props1 "..."} "children"]])
```

## Emmy

TODO

## Emmy Viewer

TODO


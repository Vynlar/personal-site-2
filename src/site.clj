#!/usr/bin/env bb

(require '[hiccup.core :refer [html]])

(defn join [sep coll]
  (apply str
   (drop 1
    (flatten
     (for [item coll]
       [sep item])))))

(def all-rounded-options ["rounded-tl-full"
                          "rounded-tr-full"
                          "rounded-bl-full"
                          "rounded-br-full"
                          ""
                          ""
                          ""])
(def all-color-options ["bg-blue-500" "bg-brand-200" "bg-red-500" "bg-white" "bg-black"])

(def rounded-side->rounded-options
  {"rounded-tl-full" [[] ["rounded-tl-full"]
                      ["rounded-tl-full"] all-rounded-options]
   "rounded-tr-full" [["rounded-tr-full"] []
                      all-rounded-options ["rounded-tr-full"]]
   "rounded-bl-full" [["rounded-bl-full"] all-rounded-options
                      [] ["rounded-bl-full"]]
   "rounded-br-full" [all-rounded-options ["rounded-br-full"]
                      ["rounded-br-full"] []]
   "" [all-rounded-options
       all-rounded-options
       all-rounded-options
       all-rounded-options]})

(def rounded-side->color-options
  {"rounded-tl-full" [[""] all-color-options
                      all-color-options all-color-options]
   "rounded-tr-full" [all-color-options [""]
                      all-color-options all-color-options]
   "rounded-bl-full" [all-color-options all-color-options
                      [""] all-color-options]
   "rounded-br-full" [all-color-options all-color-options
                      all-color-options [""]]
   "" [all-color-options all-color-options all-color-options all-color-options]})

(defn basic-cell [n {:keys [rounded-options color-options] :as opts}]
  (if (= 0 n) [:div]
      (let [base-styles "aspect-square transition-all duration-500 transform hover:scale-105 hover:z-10"
            rounded-side (rand-nth rounded-options)
            bg-color (rand-nth color-options)]
        [:div {:class (join " " [base-styles
                                 rounded-side
                                 bg-color
                                 (when (not= bg-color "bg-transparent")
                                   "hover:shadow-lg")])}
         [:div
          {:class "aspect-square grid grid-cols-2"}
          (for [[sub-rounded-options sub-color-options]
                (map vector
                     (rounded-side->rounded-options rounded-side)
                     (rounded-side->color-options rounded-side))]
            (let [invisible? (case n
                               4 (> 1 (rand-int 10))
                               3 (> 8 (rand-int 10))
                               2 (> 9 (rand-int 10))
                               1 (> 5 (rand-int 10))
                               0 (> 1 (rand-int 10)))]

              (if (empty? sub-rounded-options)
                [:div]
                (basic-cell (dec n)
                            (assoc opts
                                   :rounded-options sub-rounded-options
                                   :color-options (if invisible?
                                                    ["bg-transparent"]
                                                    (remove #{bg-color} sub-color-options)))))))]])))

(def ^:private default-config
  {:rounded-options all-rounded-options
   :color-options all-color-options})

(def site
  [:html
   [:head
    [:title]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:link {:rel "preconnect", :href "https://fonts.googleapis.com"}]
    [:link
     {:rel "preconnect",
      :href "https://fonts.gstatic.com",
      :crossorigin ""}]
    [:link
     {:href "https://fonts.googleapis.com/css2?family=Gloock&display=swap",
      :rel "stylesheet",
      :type "text/css"}]
    [:link
     {:rel "stylesheet",
      :href "/main.css",
      :type "text/css",
      :media "screen"}]]
   [:body
    [:div
     {:class "min-h-screen pt-36"}
     [:div
      {:class
       "max-w-screen-lg px-4 md:px-12 mx-auto space-y-6 flex flex-col min-h-screen"}
      [:header
       [:h1
        {:class "font-bold font-serif text-5xl"}
        "adrian\n            aleixandre"]
       [:p
        {:class "font-sans"}
        "UI engineer"]]
      [:div
       {:class "grid grid-cols-4"}
       (for [i (range 8)]
         (basic-cell 4 default-config))]
      [:div {:class "h-12"}]
      [:section {:class "space-y-8"}
       [:h2 {:class "font-serif font-bold text-4xl text-center"} "my work"]
       [:div {:class "max-w-md mx-auto"}
        [:p {:class "font-sans"} "I am a product engineer helping fast-moving startups build, adopt, and grow products with a special focus on design systems. I have experience in the full lifecycle of both products and design systems: from building institutional support, to design and development, to documentation, support and maintenence."]]]
      [:div {:class "h-12"}]
      [:div {:class "flex gap-2 justify-center"}
       (for [i (range 3)]
         [:div {:class (join " " ["w-2 h-2 rounded-full" (nth all-color-options i)])}])]
      [:div {:class "h-12"}]
      [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-8"}
       [:section {:class "space-y-2"}
        [:h2 {:class "font-serif font-bold text-xl"} "my skills"]
        [:p {:class "font-sans"} "I am an expert in UI development and proficient in many other technical areas. I have enough design skills to make collaborating with designers second-nature."]]

       [:section {:class "space-y-2"}
        [:h2 {:class "font-serif font-bold text-xl"} "product philosophy"]
        [:p {:class "font-sans"} "The best products are built by teams of diverse skills and backgrounds, collaborating closely with real users. They are built day by day, learning continuously, not executing on an upfront plan."]]]]

     [:footer
      {:class "bg-black text-white mt-20 py-12"}

      [:div {:class "max-w-screen-lg px-4 md:px-12 mx-auto"}
       [:h3 {:class "font-serif text-lg"}
        "adrian aleixandre"]]]]]])

(spit "./public/index.html" (html site))

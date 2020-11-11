package Backtracking;


public class Potions {

    public static class Ingredient {

        String name;
        int cost;

        public Ingredient(String name, int cost) {
            this.name = name;
            this.cost = cost;
        }

        // use this method to compare two ingredients.
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            }
            Ingredient i = (Ingredient) o;
            return name == i.name && cost == i.cost;
        }

    }

    public static class Step {

        Ingredient[] input; // the step's input ingredients (1:many)
        Ingredient output; // the step's output ingredient (exactly 1)

        public Step(Ingredient output, Ingredient[] input) {
            this.output = output;
            this.input = input;
        }

        // prints a step
        public void print() {
            System.out.print("  <STEP> ");
            for (Ingredient i : input) {
                System.out.print(i.name + ", ");
            }
            System.out.println(" -> " + output.name + " </STEP>");
        }

        // prints a sequence of steps (e.g., a recipe).
        public static void print(Step[] steps) {
            System.out.println("<STEPS>");
            for (Step s : steps) {
                s.print();
            }
            System.out.println("</STEPS>");
        }

    }

    // checks if an ingredient is available on the table
    // or in the pantry.
    public static boolean available(Ingredient ing, Ingredient[] place) {
        for (Ingredient ing2 : place) {
            if (ing.equals(ing2))
                return true;
        }
        return false;
    }

    // removes an ingredient from the table or pantry.
    public static Ingredient[] remove(Ingredient ing, Ingredient[] place) {
        Ingredient[] result = new Ingredient[place.length - 1];
        boolean done = false;
        int c = 0;
        for (int i = 0; i < place.length; ++i) {
            if (!done && ing.equals(place[i])) {
                done = true;
            } else {
                result[c++] = place[i];
            }
        }
        assert (done); // assertion if ing not in place.
        return result;
    }

    // adds a new ingredient 'ing' to the table or pantry.
    public static Ingredient[] add(Ingredient ing, Ingredient[] place) {
        Ingredient[] result = new Ingredient[place.length + 1];
        result[0] = ing;
        for (int i = 0; i < place.length; ++i) {
            result[i + 1] = place[i];
        }
        return result;
    }

    public static Step[] add_recipe(Step ing, Step[] place) {
        Step[] result = new Step[place.length + 1];
        result[0] = ing;
        for (int i = 0; i < place.length; ++i) {
            result[i + 1] = place[i];
        }
        return result;
    }

    /*
     * Create a potion recipe using a greedy strategy.
     *
     * @param goal : The potion to produce.
     *
     * @param book : The book containing the possible brewing steps.
     *
     * @param pantry: The reserve of available ingredients.
     */
    public static Step[] greedy(Ingredient goal, Step[] book, Ingredient[] pantry) {

        Ingredient[] goals = new Ingredient[] { goal };
        Step[] recipe = new Step[0];
        boolean abbruch = false;
        boolean schleifenAbbruch = false;

        while (goals.length != 0 || schleifenAbbruch != true) {
            /*
             * goals[0] kann immer direkt rausgenommen werden, da es entweder in der Kammer
             * ist oder wenn es nicht in der Kammer ist sowieso ersetzt wird und goals
             * überschrieben
             */
            Ingredient ing = goals[0];
            goals = remove(ing, goals);

            // nachschauen ob Zutat in Kammer
            if (available(ing, pantry)) {
                pantry = remove(ing, pantry);
                // Wenn in Kammer, aus Kammer löschen für nächste Iteration
            }

            else {
                // Wenn nicht in Kammer, ersetzten mithilfe von Buch

                for (Step step : book) {
                    // prüfen mit For Each schleife wo im Buch output == Zutat ist
                    if (step.output.equals(ing)) {
                        recipe = add_recipe(step, recipe);
                        // die input Ingredients in goals packen
                        for (Ingredient ing2 : step.input) {
                            goals = add(ing2, goals);
                        }
                        /*
                         * Schleife dann abbrechen, da keine weitere Prüfung ob Zutat verügbar oder
                         * ersetzbar indem abbruch und schleifenAbbruch nicht auf true gesetzt werden
                         * dann bricht while ab bzw. gibt leeres Array zurück
                         */
                        abbruch = true;
                        schleifenAbbruch = true;
                    } // End if
                }
                // prüft ob die if output bedingung true war, sonst beenden
                // weil Zutat nicht ersetzbar
                if (abbruch == false) {
                    Step[] leer = new Step[0];

                    return leer;
                }
            } // End if else
        } // End while
        return recipe;

    }

    /*
     * Create a potion recipe using a backtracking strategy.
     *
     * @param goal : The potion to produce.
     *
     * @param book : The book containing the possible brewing steps.
     *
     * @param pantry: The reserve of available ingredients.
     */

    // Globale Variablen
    static int bestCost = 0;
    static Step[] bestRecipe = new Step[] {};

    public static Step[] backtracking(Ingredient goal, Step[] rulebook, Ingredient[] pantry) {

        bestRecipe = new Step[0];
        Ingredient[] goals = new Ingredient[] { goal };
        Step[] recipe = new Step[0];
        bestCost = Integer.MAX_VALUE;

        backtrackingCost(goals, recipe, 0, rulebook, pantry); // zweite Methode aufrufen mit Parametern

        return bestRecipe;
    }

    public static void backtrackingCost(Ingredient[] goals, Step[] recipe, int cost, Step[] rulebook,
                                        Ingredient[] pantry) {
        if (cost >= bestCost) {
            return;
        }
        // Wenn goals leer ist und cost < minCost, wird cost neuer minCost und recipe
        // neues minCostRecipe
        if (goals.length == 0 && cost < bestCost) {

            bestRecipe = recipe;
            bestCost = cost;
            return;
        }
        // erste Zutat wieder aus dem Array goals nehmen
        Ingredient ing = goals[0];
        goals = remove(ing, goals);

        // prüfen ob es in der Kammer erhältlich ist
        if (available(ing, pantry)) {
            Ingredient[] tempPantry = remove(ing, pantry);// aus Kammer löschen und in neuem Step
            // Pantry Array speichern

            // Kosten aufrechnen
            cost += ing.cost;

            backtrackingCost(goals, recipe, cost, rulebook, tempPantry);// recursive aufrufen
            // Kosten aufsummieren für nächsten durchlauf, mit der neuen Kammer aufrufen
        }
        // Wenn nicht in Kammer verfügbar,ginge auch ohne if aber leserlicher für uns
        if (!(available(ing, pantry))) {
            for (Step step : rulebook) {
                if (step.output.equals(ing)) {
                    // goals Array klonen in goals_clone um damit weiter zu arbeiten
                    Ingredient[] goals_clone = goals.clone();
                    // wenn ersetzet werden kann, zu Rezept hinzufügen
                    Step[] currentRecipe = add_recipe(step, recipe);
                    print(currentRecipe);// Ausgabe der Schritte im Rezept um nachzuvollziehen was im Schritt passiert
                    // ist

                    // Inputs in umgekehrter Reihenfolge zu Goals hinzufügen
                    for (int i = step.input.length - 1; i >= 0; i--) {
                        goals_clone = add(step.input[i], goals_clone);
                    }
                    // recursiv aufrufen
                    backtrackingCost(goals_clone, currentRecipe, cost, rulebook, pantry);

                }

            }
        }
    }

    private static void print(Step[] stepRecipe) {
        for (int i = 0; i < stepRecipe.length; i++) {
            System.out.println("<STEPS>" + " Test:" + i);
            for (Step s : stepRecipe) {
                s.print();
            }
            System.out.println("</STEPS>");

        }
    }

}

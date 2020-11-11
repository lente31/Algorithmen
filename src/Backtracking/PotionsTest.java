package Backtracking;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

import Backtracking.Potions.Ingredient;
import Backtracking.Potions.Step;


public class PotionsTest {

    static int FAIL = -1;

    public static int brew(Step[] recipe,
                           Ingredient goal,
                           Ingredient[] pantry) {

        // can assume that potion is not in pantry.
        if (recipe.length == 0) {
            return FAIL;
        }

        // last step of recipe should produce goal
        if (!recipe[recipe.length-1].output.equals(goal)) {
        }

        int cost = 0;
        Ingredient[] table = new Ingredient[] {};

        for (Step step : recipe) {

            for (Ingredient ing : step.input) {
                if (Potions.available(ing, table)) {
                    table = Potions.remove(ing, table);
                } else if (Potions.available(ing, pantry)) {
                    pantry = Potions.remove(ing, pantry);
                    cost += ing.cost;
                } else {
                    System.out.println("Abort brewing: Missing " + ing.name);
                    return FAIL;
                }
            }

            table = Potions.add(step.output, table);

        }

        return cost;
    }


    /*
     *   TEST CASE 1: SIMPLE, ONLY ONE RULE.
     *
     *   fluxweed,seeds -> polyjuice.
     */

    static Potions.Ingredient polyjuice = new Potions.Ingredient("polyjuice", 100);
    static Potions.Ingredient fluxweed  = new Potions.Ingredient("fluxweed", 8);
    static Potions.Ingredient seeds     = new Potions.Ingredient("seeds", 3);

    // the book with possible steps
    static Potions.Step[] book1 = new Potions.Step[] {
            new Potions.Step(polyjuice,
                    new Potions.Ingredient[] {fluxweed, seeds})
    };

    /*
     *   TEST CASE 2: THE ONE FROM THE EXERCISE (SUBATO).
     *
     */

    static Potions.Ingredient liquid_luck = new Potions.Ingredient("liquid_luck", 100);
    static Potions.Ingredient tincture  = new Potions.Ingredient("tincture", 25);
    static Potions.Ingredient thyme     = new Potions.Ingredient("thyme",10);
    static Potions.Ingredient oil       = new Potions.Ingredient("oil", 2);
    static Potions.Ingredient powder    = new Potions.Ingredient("powder", 7);
    static Potions.Ingredient laurel    = new Potions.Ingredient("laurel", 1);

    static Potions.Step[] book2 = new Potions.Step[] {
            new Potions.Step(tincture,
                    new Potions.Ingredient[] {thyme, oil}),
            new Potions.Step(powder,
                    new Potions.Ingredient[] {laurel}),
            new Potions.Step(laurel,
                    new Potions.Ingredient[] {seeds, seeds}),
            new Potions.Step(liquid_luck,
                    new Potions.Ingredient[] {tincture, tincture,
                            powder})
    };


    /*
     *
     * HELP METHODS
     *
     */

    // runs the greedy algorithm, brews the resulting recipe,
    // and checks that the resulting cost match the expected cost.
    public Step[] runTestGreedy(Ingredient goal,
                                Ingredient[] pantry,
                                Step[] rulebook,
                                int expected_cost) {
        Potions.Ingredient[] pantry2 = pantry.clone();
        Step[] recipe = Potions.greedy(goal, rulebook, pantry);
        int cost = brew(recipe, goal, pantry2);
        assertEquals(expected_cost, cost);
        return recipe;
    }

    // runs backtracking, brews the resulting recipe,
    // and checks that the resulting cost match the expected cost.
    public Step[] runTestBacktracking(Ingredient goal,
                                      Ingredient[] pantry,
                                      Step[] rulebook,
                                      int expected_cost) {
        Potions.Ingredient[] pantry2 = pantry.clone();
        Step[] recipe = Potions.backtracking(goal, rulebook, pantry);
        int cost = brew(recipe, goal, pantry2);
        assertEquals(expected_cost, cost);
        return recipe;
    }


    /*
     * TEST CASES
     */

    @Test
    public void testGreedy1() {
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                seeds, seeds, fluxweed
        };
        runTestGreedy(polyjuice, pantry, book1, 11);
    }

    @Test
    public void testBacktracking1() {
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                seeds, seeds, fluxweed
        };
        runTestBacktracking(polyjuice, pantry, book1, 11);
    }

    @Test
    public void testGreedy2() {
        // not doable (missing fluxweed)
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                seeds, seeds
        };
        runTestGreedy(polyjuice, pantry, book1, -1);
    }

    @Test
    public void testBacktracking2() {
        // not doable (missing fluxweed)
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                seeds, seeds
        };
        runTestBacktracking(polyjuice, pantry, book1, -1);
    }

    @Test
    public void testGreedy3() {
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                laurel, thyme, seeds, seeds, thyme, oil, oil
        };
        runTestGreedy(liquid_luck, pantry, book2, 25);
    }

    @Test
    public void testBacktracking3() {
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                laurel, thyme, seeds, seeds, thyme, oil, oil
        };
        runTestBacktracking(liquid_luck, pantry, book2, 25);
    }

    @Test
    public void testGreedy4() {
        // tincture available (but more expensive than thyme+oil)
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                laurel, thyme, tincture, seeds, seeds, thyme, oil, oil
        };
        runTestGreedy(liquid_luck, pantry, book2, 38);
    }

    @Test
    public void testBacktracking4() {
        // tincture available (but more expensive than thyme+oil)
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                laurel, thyme, tincture, seeds, seeds, thyme, oil, oil
        };
        runTestBacktracking(liquid_luck, pantry, book2, 25);
    }

    @Test
    public void testGreedy5() {
        // thyme only once available -> must use tincture once.
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                laurel, thyme, tincture, seeds, seeds, oil, oil
        };
        runTestGreedy(liquid_luck, pantry, book2, 38);
    }

    @Test
    public void testBacktracking5() {
        // thyme only once available -> must use tincture once.
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                laurel, thyme, tincture, seeds, seeds, oil, oil
        };
        runTestBacktracking(liquid_luck, pantry, book2, 38);
    }

    @Test
    public void testGreedy6() {
        // thyme only once available -> must use tincture once.
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                thyme, tincture, seeds, seeds, oil, oil
        };
        runTestGreedy(liquid_luck, pantry, book2, 43);
    }

    @Test
    public void testBacktracking6() {
        Potions.Ingredient[] pantry = new Potions.Ingredient[] {
                thyme, tincture, seeds, seeds, oil, oil
        };
        runTestBacktracking(liquid_luck, pantry, book2, 43);
    }


}


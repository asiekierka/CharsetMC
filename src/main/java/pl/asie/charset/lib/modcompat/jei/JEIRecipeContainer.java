package pl.asie.charset.lib.modcompat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import pl.asie.charset.lib.recipe.InventoryCraftingIterator;

import java.util.Collections;

public class JEIRecipeContainer implements IRecipeWrapper {
    public static class Shapeless extends JEIRecipeContainer {
        public Shapeless(InventoryCraftingIterator.Container recipe) {
            super(recipe);
        }
    }

    public static class Shaped extends JEIRecipeContainer implements IShapedCraftingRecipeWrapper {
        public Shaped(InventoryCraftingIterator.Container recipe) {
            super(recipe);
        }

        @Override
        public int getWidth() {
            return recipe.getWidth();
        }

        @Override
        public int getHeight() {
            return recipe.getHeight();
        }
    }

    public static JEIRecipeContainer create(InventoryCraftingIterator.Container recipe) {
        return recipe.isShapeless() ? new Shapeless(recipe) : new Shaped(recipe);
    }

    protected final InventoryCraftingIterator.Container recipe;

    private JEIRecipeContainer(InventoryCraftingIterator.Container recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, JEIPluginCharset.STACKS.expandRecipeItemStackInputs(recipe.getInputs()));
        ingredients.setOutputLists(ItemStack.class, Collections.singletonList(Lists.newArrayList(recipe.getOutput())));
    }
}
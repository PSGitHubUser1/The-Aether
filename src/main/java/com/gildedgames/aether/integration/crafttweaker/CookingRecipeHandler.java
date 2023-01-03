package com.gildedgames.aether.integration.crafttweaker;

import java.util.Map;
import java.util.Optional;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.IngredientUtil;
import com.blamejared.crafttweaker.api.util.ItemStackUtil;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.gildedgames.aether.recipe.AetherRecipeTypes;
import com.gildedgames.aether.recipe.recipes.item.EnchantingRecipe;
import com.gildedgames.aether.recipe.recipes.item.FreezingRecipe;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

@IRecipeHandler.For(EnchantingRecipe.class)
@IRecipeHandler.For(FreezingRecipe.class)
public class CookingRecipeHandler implements IRecipeHandler<AbstractCookingRecipe> {

    @FunctionalInterface
    private interface CookingRecipeFactory<T extends AbstractCookingRecipe> {
        
        T create(final ResourceLocation id, final String group, final Ingredient ingredient, final ItemStack result, final float experience, final int cookTime);
        
    }

    private static Map<RecipeType<?>, Pair<String, CookingRecipeFactory<?>>> LOOKUP;

    private static Map<RecipeType<?>, Pair<String, CookingRecipeFactory<?>>> LOOKUP() {
        var l = LOOKUP;
        if (l != null) return l;
        return LOOKUP = ImmutableMap
            .<RecipeType<?>, Pair<String, CookingRecipeFactory<?>>>builder()
            .put(AetherRecipeTypes.ENCHANTING.get(), Pair.of("altarEnchanting", EnchantingRecipe::new))
            .put(AetherRecipeTypes.FREEZING.get(), Pair.of("freezing", FreezingRecipe::new))
            .build();
    }

    @Override
    public String dumpToCommandString(IRecipeManager<? super AbstractCookingRecipe> manager, AbstractCookingRecipe recipe) {
        return String.format(
            "%s.addRecipe(%s, %s, %s, %s, %s);",
            LOOKUP().get(recipe.getType()).getFirst(),
            StringUtil.quoteAndEscape(recipe.getId()),
            ItemStackUtil.getCommandString(recipe.getResultItem()),
            IIngredient.fromIngredient(recipe.getIngredients().get(0)).getCommandString(),
            recipe.getExperience(),
            recipe.getCookingTime()
        );
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super AbstractCookingRecipe> manager, AbstractCookingRecipe firstRecipe, U secondRecipe) {
        return IngredientUtil.canConflict(firstRecipe.getIngredients().get(0), secondRecipe.getIngredients().get(0));
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super AbstractCookingRecipe> manager, AbstractCookingRecipe recipe) {
        final IIngredient ingredient = IIngredient.fromIngredient(recipe.getIngredients().get(0));
        final IDecomposedRecipe decomposition = IDecomposedRecipe.builder()
            .with(BuiltinRecipeComponents.Metadata.GROUP, recipe.getGroup())
            .with(BuiltinRecipeComponents.Input.INGREDIENTS, ingredient)
            .with(BuiltinRecipeComponents.Processing.TIME, recipe.getCookingTime())
            .with(BuiltinRecipeComponents.Output.EXPERIENCE, recipe.getExperience())
            .with(BuiltinRecipeComponents.Output.ITEMS, IItemStack.of(recipe.getResultItem()))
            .build();
        return Optional.of(decomposition);
    }

    @Override
    public Optional<AbstractCookingRecipe> recompose(IRecipeManager<? super AbstractCookingRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        final String group = recipe.getOrThrowSingle(BuiltinRecipeComponents.Metadata.GROUP);
        final IIngredient input = recipe.getOrThrowSingle(BuiltinRecipeComponents.Input.INGREDIENTS);
        final int cookTime = recipe.getOrThrowSingle(BuiltinRecipeComponents.Processing.TIME);
        final float experience = recipe.getOrThrowSingle(BuiltinRecipeComponents.Output.EXPERIENCE);
        final IItemStack output = recipe.getOrThrowSingle(BuiltinRecipeComponents.Output.ITEMS);
        
        if(input.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: empty ingredient");
        }
        if(cookTime <= 0) {
            throw new IllegalArgumentException("Invalid cooking time: less than min allowed 1: " + cookTime);
        }
        if(experience < 0) {
            throw new IllegalArgumentException("Invalid experience: less than min allowed 0:" + experience);
        }
        if(output.isEmpty()) {
            throw new IllegalArgumentException("Invalid output: empty stack");
        }
        
        final CookingRecipeFactory<?> factory = LOOKUP().get(manager.getRecipeType()).getSecond();
        return Optional.of(factory.create(name, group, input.asVanillaIngredient(), output.getInternal(), experience, cookTime));
    }

}

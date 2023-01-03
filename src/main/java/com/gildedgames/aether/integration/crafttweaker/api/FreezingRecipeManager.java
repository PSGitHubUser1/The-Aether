package com.gildedgames.aether.integration.crafttweaker.api;

import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.ICookingRecipeManager;
import com.gildedgames.aether.blockentity.FreezerBlockEntity;
import com.gildedgames.aether.recipe.AetherRecipeTypes;
import com.gildedgames.aether.recipe.recipes.item.FreezingRecipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

/**
 * @docParam this freezer
 */
@ZenRegister
@ZenCodeType.Name("mods.aether.FreezingRecipeManager")
public enum FreezingRecipeManager implements ICookingRecipeManager<FreezingRecipe> {
    
    @ZenCodeGlobals.Global("freezer")
    INSTANCE;

    @Override
    public FreezingRecipe makeRecipe(String name, IItemStack output, IIngredient input, float xp, int cookTime) {
        return new FreezingRecipe(CraftTweakerConstants.rl(name), "", input.asVanillaIngredient(), output.getInternal(), xp, cookTime);
    }

    @Override
    public RecipeType<FreezingRecipe> getRecipeType() {
        return AetherRecipeTypes.FREEZING.get();
    }

    @ZenCodeType.Method
    public void setFuel(Item item, int cookingTime) {
        FreezerBlockEntity.addItemFreezingTime(item, cookingTime);
    }

}

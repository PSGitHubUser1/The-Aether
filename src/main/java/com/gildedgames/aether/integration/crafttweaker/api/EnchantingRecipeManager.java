package com.gildedgames.aether.integration.crafttweaker.api;

import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.ICookingRecipeManager;
import com.gildedgames.aether.blockentity.AltarBlockEntity;
import com.gildedgames.aether.recipe.AetherRecipeTypes;
import com.gildedgames.aether.recipe.recipes.item.EnchantingRecipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

/**
 * @docParam this altarEnchanting
 */
@ZenRegister
@ZenCodeType.Name("mods.aether.EnchantingRecipeManager")
public enum EnchantingRecipeManager implements ICookingRecipeManager<EnchantingRecipe> {
    
    @ZenCodeGlobals.Global("altarEnchanting")
    INSTANCE;

    @Override
    public EnchantingRecipe makeRecipe(String name, IItemStack output, IIngredient input, float xp, int cookTime) {
        return new EnchantingRecipe(CraftTweakerConstants.rl(name), "", input.asVanillaIngredient(), output.getInternal(), xp, cookTime);
    }

    @Override
    public RecipeType<EnchantingRecipe> getRecipeType() {
        return AetherRecipeTypes.ENCHANTING.get();
    }

    @ZenCodeType.Method
    public void setFuel(Item item, int cookingTime) {
        AltarBlockEntity.addItemEnchantingTime(item, cookingTime);
    }

}

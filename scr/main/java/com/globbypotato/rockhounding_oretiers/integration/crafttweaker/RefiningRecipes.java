package com.globbypotato.rockhounding_oretiers.integration.crafttweaker;

import com.globbypotato.rockhounding_oretiers.compat.jei.refiner.RefinerRecipeWrapper;
import com.globbypotato.rockhounding_oretiers.handlers.ModRecipes;
import com.globbypotato.rockhounding_oretiers.machines.recipes.RefinerRecipes;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.rockhounding_oretiers.CoalRefiner")
public class RefiningRecipes {
	private static String name = "Basic Coal Refiner";

    @ZenMethod
    public static void add(IItemStack input, IItemStack output) {
        if(input == null || output == null) {MineTweakerAPI.logError(name + ": Invalid recipe."); return;}
        MineTweakerAPI.apply(new AddToRefining(new RefinerRecipes(CTSupport.toStack(input), CTSupport.toStack(output))));
    }
		    private static class AddToRefining implements IUndoableAction {
		    	private RefinerRecipes recipe;
		    	public AddToRefining(RefinerRecipes recipe){
		          super();
		          this.recipe = recipe;
		    	}
		    	@Override
		    	public void apply() {
		    		ModRecipes.refinerRecipe.add(this.recipe);
		    		MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new RefinerRecipeWrapper(this.recipe));
		    	}
		    	@Override
		    	public void undo() {
		    		for(RefinerRecipes recipe : ModRecipes.refinerRecipe){
		    			if(recipe.equals(this.recipe)){
		    				MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new RefinerRecipeWrapper(recipe));
		    				ModRecipes.refinerRecipe.remove(recipe);	
	                        break;
		    			}
		    		}
		    	}
		    	@Override
		    	public boolean canUndo() {return true;}
		    	@Override
		    	public String describe() {return null;}
		    	@Override
		    	public String describeUndo() {return null;}
		    	@Override
		    	public Object getOverrideKey() {return null;}
		    }


    @ZenMethod
    public static void remove(IItemStack input) {
        if(input == null) {MineTweakerAPI.logError(name + ": Invalid recipe."); return;}
        MineTweakerAPI.apply(new RemoveFromRefining(CTSupport.toStack(input)));    
    }
		    private static class RemoveFromRefining implements IUndoableAction {
		    	private ItemStack input;
		    	private RefinerRecipes removedRecipe;
		    	public RemoveFromRefining(ItemStack input) {
		    		super();
		    		this.input = input;
		    	}
		    	@Override
		    	public void apply() {
		    		for(RefinerRecipes recipe : ModRecipes.refinerRecipe){
		    			if(this.input != null && recipe.getInput().isItemEqual(this.input)){
				    		this.removedRecipe = recipe;
		    				MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new RefinerRecipeWrapper(recipe));
		    				ModRecipes.refinerRecipe.remove(recipe);	
	                        break;
		    			}
		    		}
		    	}
		    	@Override
		    	public void undo() {
		    		if(this.removedRecipe != null){
			    		ModRecipes.refinerRecipe.add(this.removedRecipe);
			    		MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new RefinerRecipeWrapper(this.removedRecipe));
		    		}
		    	}
		    	@Override
		    	public boolean canUndo() {return true;}
		    	@Override
		    	public String describe() {return null;}
		    	@Override
		    	public String describeUndo() {return null;}
		    	@Override
		    	public Object getOverrideKey() {return null;}
		    }
}
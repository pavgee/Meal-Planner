package com.techelevator.controller;

import com.techelevator.dao.IngredientDao;
import com.techelevator.dao.RecipeBuilderDao;
import com.techelevator.dao.RecipeDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Ingredient;
import com.techelevator.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping
@PreAuthorize("isAuthenticated()")
public class RecipeController {

    @Autowired
    private RecipeDao recipeDao;
    private UserDao userDao;
    private RecipeBuilderDao recipeBuilderDao;
    private IngredientDao ingredientDao;

    public RecipeController(RecipeDao recipeDao, UserDao userDao, RecipeBuilderDao recipeBuilderDao, IngredientDao ingredientDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
        this.recipeBuilderDao = recipeBuilderDao;
        this.ingredientDao = ingredientDao;
    }

    @GetMapping("/recipes/detail/{id}")
    public Recipe getRecipeByRecipeId(@PathVariable int id) {
        return recipeDao.getRecipeByRecipeId(id);
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeDao.getAllRecipes();
    }

    //getting list of all recipes created by a single user
    @GetMapping("/recipes/{createdBy}")
    public List<Recipe> getRecipeByCreatedBy(@PathVariable int createdBy) {
        return recipeDao.getAllRecipesByCreatedBy(createdBy);
    }

    @GetMapping("/recipes/latest")
    public List<Recipe> getLatestRecipes() {
        return recipeDao.getLatestRecipes();
    }

    @GetMapping("/recipes/favorites")
    public List<Recipe> getAllSavedRecipesByUserId(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        return recipeDao.getAllSavedRecipesByUserId(userId);
    }

    @PostMapping("/recipes/favorites")
    public void putARecipeIntoSavedRecipes(Principal principal, @RequestBody Recipe recipe) {
        int userId = userDao.findIdByUsername(principal.getName());
        recipeDao.putARecipeIntoSavedRecipes(userId, recipe);
    }

    @PutMapping("/recipes/favorites")
    public void updateRecipetoRecipeDB(Principal principal, @RequestBody Recipe recipe) {
        int userId = userDao.findIdByUsername(principal.getName());
        recipeBuilderDao.updateRecipeToRecipeDB(recipe, userId);
    }

    @DeleteMapping("/recipes/favorites/{recipeId}")
    public int removeRecipeFromSavedRecipes(Principal principal, @PathVariable int recipeId) {
        int userId = userDao.findIdByUsername(principal.getName());
        int success = recipeDao.removeARecipeFromSavedRecipes(userId, recipeId);
        return success;
    }

    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable int recipeId) {
        recipeDao.deleteRecipeById(recipeId);
    }

    @GetMapping("/favoritelist")
    public List<Integer> getListOfFavoriteRecipeIdsByUserId(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        return recipeDao.getListOfFavoriteRecipeIdsByUserId(userId);
    }

    @PostMapping("/recipes")
    public int addRecipeToRecipeDB(Principal principal, @Valid @RequestBody Recipe recipe) {
        int userId = userDao.findIdByUsername(principal.getName());
        return recipeBuilderDao.addRecipeToRecipeDB(recipe.getRecipeName(), recipe.getImage(), userId);
    }

    @PostMapping("/ingredient")
    public int addIngredientToDB(String ingredientName) {
        return recipeBuilderDao.addIngredientToDB(ingredientName);
    }

    @PostMapping("/recipes/{recipeId}/ingredient")
    public void addIngredientToRecipe(@PathVariable int recipeId, @Valid @RequestBody Recipe recipe) {
        List<Ingredient> ingredientList = recipe.getIngredientList();
        for (Ingredient eachIngredient : ingredientList) {
            recipeBuilderDao.addIngredientToRecipe(eachIngredient.getIngredientId(), recipe.getRecipeId(),
                    eachIngredient.getQuantity(), eachIngredient.getUnit());
        }
    }
    @DeleteMapping("/ingredient/{ingredientId}")
    public void removeIngredientFromRecipe(@PathVariable int ingredientId, int recipeId) {
        recipeBuilderDao.removeIngredientFromRecipe(ingredientId, recipeId);
    }

    @PostMapping("/recipes/{recipeId}/instruction")
    public int addInstructionToRecipe(@PathVariable int recipeId, int sequence, String instructionText) {
        return recipeBuilderDao.addInstructionToRecipe(recipeId, sequence, instructionText);
    }

    @DeleteMapping("/instruction/{instructionId}")
    public void removeInstructionFromRecipe(@PathVariable int instructionId) {
        recipeBuilderDao.removeInstructionFromRecipe(instructionId);
    }

    @PutMapping("/recipes/{createdBy}")
    public void updateIngredientQuantityToRecipe(@RequestBody Ingredient ingredient, @PathVariable int createdBy) {
        recipeBuilderDao.updateIngredientQuantityToRecipe(ingredient, ingredient.getRecipeId());
    }
    @GetMapping("/ingredient")
    public List<Ingredient> getAllIngredients(){
        return ingredientDao.getAllIngredients();
    }
}




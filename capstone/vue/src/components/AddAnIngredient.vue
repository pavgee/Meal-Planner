<template>
  <div id="addIngredient">
    <div class="form">
      <form
        id="addAnIngredientToExistingRecipe"
        @submit.prevent="addIngredientToRecipe"
      >      <h4 id="addingHeader">Adding:</h4>
        <input
          id="ingredient-to-add"
          name="ingredient-to-add"
          type="text"
          :value="selectedIngredient.name"
          required
          readonly="readonly"
        />

        <br />

        <label for="enter-quantity">Quantity: </label><br>
        <input
          id="enter-quantity"
          name="enter-quantity"
          type="number"
          step="0.25"
          v-model="quantity"
          required=">0"
        />

        <br />
<label for="enter-unit">Unit: </label><br>
        <input
          id="enter-unit"
          name="enter-unit"
          type="text"
          v-model="unit"
          required
        />

        <br />

        <button>Add To Recipe</button>
      </form>
    </div>

    <div class="search-select">
      <input
        type="text"
        class="search-entry"
        v-model="ingredientSearch"
        placeholder="Search Ingredient Database"
      />
      <ul>
        <li
          v-for="ingredient in filteredIngredient"
          :key="ingredient.id"
          @click="selectIngredient(ingredient)"
        >
          {{ ingredient.name }}
        </li>
      </ul>
    </div>

    <div class="add-new">
      <h4>Can't find the ingredient you need?</h4>
      <input
        v-model="newIngredient.name"
        type="text"
        id="addIngred"
        placeholder="Add a new Ingredient"
      />
      <button @click="addNewIngredient">Add It</button>
    </div>
  </div>
</template>

<script>
import RecipeService from "../services/RecipeService";
export default {
  name: "addAnIngredient",
  data() {
    return {
      allIngredients: [],
      ingredientSearch: "",
      quantity: 0.0,
      unit: "",
      selectedIngredient: {
        ingredientId: "",
        name: "",
      },
      newIngredient: {
        name: "",
      },
    };
  },
  computed: {
    filteredIngredient() {
      const ingQuery = this.ingredientSearch.toLowerCase();
      if (this.ingredientSearch === "") {
        return this.allIngredients;
      }
      return this.allIngredients.filter((ingredient) => {
        return Object.values(ingredient).some((word) =>
          String(word).toLowerCase().includes(ingQuery)
        );
      });
    },
  },

  created() {
    RecipeService.getAllIngredients().then((response) => {
      this.allIngredients = response.data;
    });
  },

  methods: {
    selectIngredient(ingredient) {
      this.selectedIngredient.ingredientId = ingredient.ingredientId;
      this.selectedIngredient.name = ingredient.name;
    },
    addNewIngredient() {
      RecipeService.addIngredientToDB(this.newIngredient).then((response) => {
        if (response.status === 200) {
          this.selectedIngredient.name = this.newIngredient.name;
          this.selectedIngredient.ingredientId = response.data;
        } else {
          alert("Something went wrong.");
        }
      });
    },
    addIngredientToRecipe() {
      let workingIngredient = {
        ingredientId: this.selectedIngredient.ingredientId,
        name: this.selectedIngredient.name,
        type: "",
        quantity: this.quantity,
        unit: this.unit,
        recipeId: this.$store.state.workingId
      };

      RecipeService.addSingleIngredientToRecipe(workingIngredient).then(
        (response) => {
          if (response.status == 200) {
            this.selectedIngredient = {
              ingredientId: "",
              name: "",
            };
            this.ingredientSearch = "";
          } else {
              alert("Something went wrong.");
          }
        }
      );
    },
  },
};
</script>

<style>
#addIngredient {
  position: fixed;
  left: 0;
  right: 0;
  margin: auto;
  width: 80%;
  /* height: 30%; */
  box-shadow: -5px 3px 3px darkslategray;
  border-radius: 15px;
  background-color: white;
  z-index: 99999;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-areas: "form select new";
}

.form {
  grid-area: form;
}

.add-new {
  grid-area: new;
}

.form input {
  width: 100%;
}

.search-select {
  grid-area: select;
  width: 100%;
  background-color: #ffffff60;
  border-radius: 15px;
}

.search-entry {
  width: 90%;
  height: 50px;
  margin-top: 20px;
  margin-bottom: -10px;
  font-size: 1.5em;
}

.search-select ul {
  list-style: none;
  text-align: left;
  padding-left: 8px;
  height: 180px;
  overflow-y: scroll;
  overflow-x: hidden;
  border: 1px solid lightgray;
}

.search-select li {
  width: 100%;
  border-bottom: 1px solid lightgray;
  padding: 10px;
  cursor: pointer;
}

.search-select li:hover {
  background-color: darkslategray;
  color: white;
  font-weight: bold;
}

#ingredient-to-add {
  border-style: none;
  width: 90%;
  font-size: 1.25em;
  height: 1.5em;
  margin-bottom: 0px;
}

#addingHeader {
  font-size: 1em;
  margin-top: -15px;
  margin-bottom: 0px;
}

#addIngred {
    width: 90%;
  height: 50px;
  margin-top: 20px;
  margin-bottom: -10px;
  font-size: 1.5em;
}
</style>
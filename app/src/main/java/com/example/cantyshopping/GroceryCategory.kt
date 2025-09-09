package com.example.cantyshopping

enum class GroceryCategory(val displayName: String) {
    PRODUCE("Produce"),
    DAIRY("Dairy"),
    MEAT("Meat & Seafood"),
    BAKERY("Bakery"),
    FROZEN("Frozen Foods"),
    CANNED("Canned Goods"),
    DRY_GOODS("Dry Goods & Pasta"),
    SNACKS("Snacks & Candy"),
    BEVERAGES("Beverages"),
    HOUSEHOLD("Household"),
    PERSONAL_CARE("Personal Care"),
    OTHER("Other")
}

fun guessCategory(itemName: String): GroceryCategory {
    val name = itemName.lowercase().trim()

    // PRODUCE
    val produce = listOf(
        // Fruits
        "apple", "orange", "banana", "grape", "strawberry", "berry", "lemon", "lime",
        "pear", "peach", "plum", "melon", "watermelon", "cantaloupe", "kiwi", "mango",
        "pineapple", "avocado", "cherry", "nectarine", "apricot",
        // Vegetables
        "lettuce", "spinach", "kale", "arugula", "broccoli", "cauliflower", "carrot",
        "celery", "pepper", "onion", "garlic", "potato", "tomato", "cucumber", "zucchini",
        "eggplant", "mushroom", "bean", "pea", "corn", "asparagus", "radish", "cabbage",
        "squash", "salad", "greens"
    )

    // DAIRY
    val dairy = listOf(
        "milk", "cream", "butter", "cheese", "cheddar", "mozzarella", "parmesan",
        "yogurt", "sour cream", "egg", "cottage", "ricotta", "whipped cream", "half-and-half",
        "almond milk", "oat milk", "soy milk", "yoghurt"
    )

    // MEAT
    val meat = listOf(
        "chicken", "turkey", "beef", "steak", "ground beef", "hamburger", "pork", "chop",
        "bacon", "ham", "sausage", "hot dog", "lamb", "veal", "fish", "salmon", "tuna",
        "tilapia", "shrimp", "crab", "lobster", "clam", "mussel", "tofu", "tempeh", "meat",
        "chuck", "brisket", "rib", "loin", "roast", "fillet", "liver", "jerky", "burger"
    )

    // BAKERY
    val bakery = listOf(
        "bread", "roll", "bun", "bagel", "muffin", "croissant", "donut", "pastry",
        "cake", "pie", "cookie", "brownie", "toast", "tortilla", "pita", "naan",
        "baguette", "sourdough", "rye", "wheat", "bake", "dough", "cupcake", "english muffin"
    )

    // FROZEN
    val frozen = listOf(
        "frozen", "ice cream", "popsicle", "sorbet", "gelato", "frosty"
    )

    // CANNED
    val canned = listOf(
        "can", "canned", "soup", "broth", "stock", "preserve", "jar", "pickled", "pickle"
    )

    // DRY GOODS
    val dryGoods = listOf(
        "pasta", "noodle", "spaghetti", "macaroni", "rice", "quinoa", "cereal", "oatmeal",
        "flour", "sugar", "baking", "oil", "vinegar", "spice", "herb", "seasoning",
        "salt", "pepper", "oregano", "basil", "thyme", "cinnamon", "nutmeg", "sauce",
        "honey", "syrup", "peanut butter", "jelly", "jam", "mix", "pancake", "instant",
        "dry", "bean", "lentil", "grain"
    )

    // BEVERAGES
    val beverages = listOf(
        "water", "soda", "pop", "coke", "pepsi", "sprite", "juice", "coffee", "tea",
        "beer", "wine", "liquor", "vodka", "whiskey", "rum", "drink", "beverage", "cola",
        "lemonade", "energy drink", "milk", "almond milk", "soy milk", "sparkling", "mineral"
    )

    // SNACKS
    val snacks = listOf(
        "chip", "crisp", "pretzel", "popcorn", "cracker", "cookie", "candy", "chocolate",
        "nut", "peanut", "almond", "cashew", "trail mix", "granola", "bar", "snack",
        "treat", "marshmallow", "gum", "jerky", "fruit snack", "cereal bar", "rice cake"
    )

    // HOUSEHOLD
    val household = listOf(
        "paper", "towel", "toilet", "tissue", "napkin", "dish soap", "detergent", "cleaner",
        "wipe", "sponge", "trash", "garbage", "bag", "foil", "wrap", "plastic", "storage",
        "light bulb", "battery", "broom", "mop", "bucket", "bleach", "laundry", "softener"
    )

    // PERSONAL CARE
    val personalCare = listOf(
        "soap", "shampoo", "conditioner", "deodorant", "toothpaste", "toothbrush", "floss",
        "mouthwash", "razor", "shave", "lotion", "cream", "sunscreen", "vitamin", "supplement",
        "medicine", "bandage", "cotton", "makeup", "nail", "tampon", "pad", "diaper"
    )

    return when {
        produce.any { it in name } -> GroceryCategory.PRODUCE
        dairy.any { it in name } -> GroceryCategory.DAIRY
        meat.any { it in name } -> GroceryCategory.MEAT
        bakery.any { it in name } -> GroceryCategory.BAKERY
        frozen.any { it in name } -> GroceryCategory.FROZEN
        canned.any { it in name } -> GroceryCategory.CANNED
        dryGoods.any { it in name } -> GroceryCategory.DRY_GOODS
        beverages.any { it in name } -> GroceryCategory.BEVERAGES
        snacks.any { it in name } -> GroceryCategory.SNACKS
        household.any { it in name } -> GroceryCategory.HOUSEHOLD
        personalCare.any { it in name } -> GroceryCategory.PERSONAL_CARE
        else -> GroceryCategory.OTHER
    }
}
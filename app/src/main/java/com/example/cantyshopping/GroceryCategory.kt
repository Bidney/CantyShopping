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

// Function to guess category based on item name
fun guessCategory(itemName: String): GroceryCategory {
    val lowerName = itemName.lowercase()

    return when {
        lowerName.matches(Regex(".*(apple|banana|orange|lettuce|tomato|carrot|vegetable|fruit|produce).*")) ->
            GroceryCategory.PRODUCE

        lowerName.matches(Regex(".*(milk|cheese|yogurt|butter|cream|dairy).*")) ->
            GroceryCategory.DAIRY

        lowerName.matches(Regex(".*(chicken|beef|steak|fish|meat|pork|bacon).*")) ->
            GroceryCategory.MEAT

        lowerName.matches(Regex(".*(bread|bagel|roll|bun|pastry|cake).*")) ->
            GroceryCategory.BAKERY

        lowerName.matches(Regex(".*(frozen|ice cream).*")) ->
            GroceryCategory.FROZEN

        lowerName.matches(Regex(".*(can|soup|beans|canned).*")) ->
            GroceryCategory.CANNED

        lowerName.matches(Regex(".*(pasta|rice|cereal|flour|sugar|baking).*")) ->
            GroceryCategory.DRY_GOODS

        lowerName.matches(Regex(".*(chip|cookie|candy|snack|chocolate).*")) ->
            GroceryCategory.SNACKS

        lowerName.matches(Regex(".*(water|soda|juice|drink|coffee|tea|beverage).*")) ->
            GroceryCategory.BEVERAGES

        lowerName.matches(Regex(".*(cleaner|paper towel|toilet|detergent|household).*")) ->
            GroceryCategory.HOUSEHOLD

        lowerName.matches(Regex(".*(soap|shampoo|toothpaste|deodorant|personal).*")) ->
            GroceryCategory.PERSONAL_CARE

        else -> GroceryCategory.OTHER
    }
}
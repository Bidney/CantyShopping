package io.github.bidney.cantyshopping

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

class GroceryRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("grocery_preferences", Context.MODE_PRIVATE)

    private val grocerylistkey = "grocery_list"

    // Function to save grocery items
    fun saveGroceryItems(items: List<GroceryItem>) {
        // Convert the list to a JSON array
        val jsonArray = JSONArray()

        items.forEach { item ->
            val jsonObject = JSONObject().apply {
                put("id", item.id)
                put("name", item.name)
                put("isChecked", item.isChecked)
                put("quantity", item.quantity)
                put("category", item.category.name) // Store enum as string name
            }
            jsonArray.put(jsonObject)
        }

        // Save to SharedPreferences
        sharedPreferences.edit {
            putString(grocerylistkey, jsonArray.toString())
        }
    }

    // Function to get grocery items
    fun getGroceryItems(): List<GroceryItem> {
        // Get the JSON string from SharedPreferences
        val jsonString = sharedPreferences.getString(grocerylistkey, "[]") ?: "[]"

        return try {
            // Parse the JSON array
            val jsonArray = JSONArray(jsonString)
            val groceryItems = mutableListOf<GroceryItem>()

            // Convert each JSON object to a GroceryItem
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                // Convert category string back to enum
                val categoryName = jsonObject.getString("category")
                val category = try {
                    GroceryCategory.valueOf(categoryName)
                } catch (e: Exception) {
                    GroceryCategory.OTHER // Default if category not found
                }

                val item = GroceryItem(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    isChecked = jsonObject.getBoolean("isChecked"),
                    quantity = jsonObject.getString("quantity"),
                    category = category
                )

                groceryItems.add(item)
            }

            groceryItems
        } catch (e: Exception) {
            // If there's an error parsing, return an empty list
            emptyList()
        }
    }

    // Convert grocery items to JSON string for export
    fun getGroceryItemsAsJson(): String {
        val jsonArray = JSONArray()

        getGroceryItems().forEach { item ->
            val jsonObject = JSONObject().apply {
                put("id", item.id)
                put("name", item.name)
                put("isChecked", item.isChecked)
                put("quantity", item.quantity)
                put("category", item.category.name)
            }
            jsonArray.put(jsonObject)
        }

        return jsonArray.toString(2) // Pretty-printed with 2-space indentation
    }

    // Load grocery items from JSON string (from import)
    fun loadGroceryItemsFromJson(jsonString: String) {
        try {
            val jsonArray = JSONArray(jsonString)
            val groceryItems = mutableListOf<GroceryItem>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                // Convert category string back to enum
                val categoryName = jsonObject.getString("category")
                val category = try {
                    GroceryCategory.valueOf(categoryName)
                } catch (e: Exception) {
                    GroceryCategory.OTHER // Default if category not found
                }

                val item = GroceryItem(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    isChecked = jsonObject.getBoolean("isChecked"),
                    quantity = jsonObject.getString("quantity"),
                    category = category
                )

                groceryItems.add(item)
            }

            // Save the imported items
            saveGroceryItems(groceryItems)
        } catch (e: Exception) {
            throw Exception("Invalid format in imported file")
        }
    }
}
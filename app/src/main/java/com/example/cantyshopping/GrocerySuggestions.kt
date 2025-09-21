package io.github.bidney.cantyshopping

/**
 * Contains predefined grocery items organized by categories for suggestion and auto-categorization.
 */
object GrocerySuggestions {

    // Map of category to list of common items in that category
    private val categoryKeywords = mapOf(
        GroceryCategory.PRODUCE to listOf(
            // Fruits
            "apple", "apricot", "avocado", "banana", "blackberry", "blueberry", "cantaloupe",
            "cherry", "clementine", "coconut", "cranberry", "date", "dragonfruit", "fig",
            "grapefruit", "grape", "honeydew", "kiwi", "kumquat", "lemon", "lime", "lychee",
            "mango", "nectarine", "orange", "papaya", "peach", "pear", "persimmon",
            "pineapple", "plum", "pomegranate", "raspberry", "strawberry", "tangerine",
            "watermelon",

            // Vegetables
            "artichoke", "arugula", "asparagus", "bamboo shoot", "bean sprout", "beet",
            "bell pepper", "bok choy", "broccoli", "brussels sprout", "cabbage", "carrot",
            "cauliflower", "celery", "chili pepper", "collard green", "corn", "cucumber",
            "eggplant", "endive", "fennel", "garlic", "ginger", "green bean", "kale",
            "leek", "lettuce", "mushroom", "mustard green", "okra", "onion", "parsnip",
            "pea", "potato", "pumpkin", "radicchio", "radish", "rhubarb", "romaine",
            "rutabaga", "scallion", "shallot", "spinach", "squash", "sweet potato",
            "swiss chard", "taro", "tomatillo", "tomato", "turnip", "watercress",
            "zucchini",

            // Fresh Herbs
            "basil", "bay leaf", "chive", "cilantro", "dill", "mint", "oregano", "parsley",
            "rosemary", "sage", "tarragon", "thyme"
        ),

        GroceryCategory.DAIRY to listOf(
            // Milk
            "whole milk", "skim milk", "1% milk", "2% milk", "buttermilk", "chocolate milk",
            "almond milk", "soy milk", "oat milk", "coconut milk", "rice milk", "cashew milk",
            "goat milk", "lactose-free milk", "organic milk", "ultra-filtered milk",

            // Cream
            "heavy cream", "light cream", "half-and-half", "whipping cream", "clotted cream",
            "sour cream", "crème fraîche", "coffee creamer",

            // Butter & Margarine
            "butter", "unsalted butter", "grass-fed butter", "ghee", "margarine", "butter spread",

            // Cheese
            "american cheese", "asiago", "blue cheese", "brie", "camembert", "cheddar",
            "colby", "cottage cheese", "cream cheese", "edam", "feta", "goat cheese", "gouda",
            "gruyère", "havarti", "mascarpone", "monterey jack", "mozzarella", "muenster",
            "parmesan", "pepper jack", "provolone", "ricotta", "romano", "swiss cheese",
            "string cheese", "shredded cheese",

            // Yogurt
            "yogurt", "greek yogurt", "plain yogurt", "vanilla yogurt", "fruit yogurt",
            "kefir", "yogurt drink", "probiotic yogurt", "dairy-free yogurt",

            // Eggs
            "eggs", "egg whites", "egg substitute", "free-range eggs", "organic eggs",
            "quail eggs", "duck eggs",

            // Refrigerated Items (moved from other categories)
            "tofu", "tempeh", "refrigerated dough", "crescent roll dough", "cookie dough",
            "pie crust", "refrigerated biscuit", "refrigerated pizza dough"
        ),

        GroceryCategory.DELI to listOf(
            // Deli Meats
            "deli meat", "sliced turkey", "sliced ham", "roast beef", "pastrami", "salami",
            "pepperoni", "bologna", "liverwurst", "mortadella", "chorizo", "prosciutto",

            // Prepared Foods
            "hummus", "guacamole", "prepared salad", "coleslaw", "potato salad", "pasta salad",
            "deli salad", "prepared sandwich", "rotisserie chicken", "prepared soup",
            "prepared meal", "sushi", "poke", "dip", "tzatziki", "baba ganoush",

            // Refrigerated Items
            "fresh salsa", "refrigerated dip", "premade sandwich", "lunch kit"
        ),

        GroceryCategory.MEAT to listOf(
            // Beef
            "beef", "ground beef", "steak", "ribeye", "sirloin", "filet mignon", "chuck roast",
            "brisket", "short rib", "flank steak", "skirt steak", "beef tenderloin",
            "beef stew meat", "corned beef",

            // Pork
            "pork", "pork chop", "bacon", "ham", "pork loin", "pork tenderloin", "pork shoulder",
            "pork belly", "ground pork", "pork sausage", "pork rib",

            // Poultry
            "chicken", "chicken breast", "chicken thigh", "chicken wing", "chicken leg",
            "whole chicken", "ground chicken", "chicken tender", "chicken drumstick",
            "chicken sausage", "turkey", "ground turkey", "turkey breast", "turkey bacon",
            "duck", "cornish hen",

            // Lamb
            "lamb", "lamb chop", "ground lamb", "lamb shank", "rack of lamb",

            // Fresh Fish & Seafood
            "fish", "salmon", "tuna", "cod", "halibut", "tilapia", "mahi mahi", "trout",
            "sea bass", "catfish", "sardine", "swordfish", "mackerel",
            "shrimp", "crab", "lobster", "scallop", "clam", "mussel", "oyster", "squid",
            "octopus",

            // Sausages & Hot Dogs
            "hot dog", "bratwurst", "kielbasa", "sausage", "summer sausage", "italian sausage",
            "breakfast sausage", "andouille sausage",

            // Plant-Based (Fresh)
            "veggie burger", "plant-based meat", "beyond meat", "impossible meat", "seitan",
            "veggie sausage", "plant-based chicken", "plant-based ground"
        ),

        GroceryCategory.BAKERY to listOf(
            // Bread
            "bread", "white bread", "wheat bread", "whole grain bread", "multigrain bread",
            "sourdough bread", "rye bread", "pumpernickel", "french bread", "italian bread",
            "ciabatta", "brioche", "challah", "focaccia", "garlic bread", "gluten-free bread",
            "flatbread", "pita", "naan", "english muffin", "bagel",
            "baguette", "croissant", "breadstick", "bread roll", "kaiser roll", "hamburger bun",
            "hot dog bun", "dinner roll", "sandwich roll",

            // Sweet Baked Goods
            "muffin", "scone", "donut", "pastry", "danish", "cinnamon roll", "coffee cake",
            "pound cake", "angel food cake", "cake", "cupcake", "brownie",
            "cookie", "pie", "tart", "strudel", "baklava", "eclair", "macaroon", "cannoli",

            // Tortillas & Wraps (moved from Bakery)
            "tortilla", "corn tortilla", "flour tortilla", "spinach wrap", "whole wheat wrap"
        ),

        GroceryCategory.FROZEN to listOf(
            // Frozen Meals
            "frozen meal", "frozen dinner", "tv dinner", "frozen pizza", "frozen burrito",
            "frozen lasagna", "frozen pasta dish", "frozen stir fry", "frozen pot pie", "frozen soup",
            "hot pocket", "frozen sandwich", "frozen breakfast sandwich",

            // Frozen Vegetables
            "frozen vegetable", "frozen broccoli", "frozen corn", "frozen spinach",
            "frozen green bean", "frozen pea", "frozen carrot", "frozen cauliflower",
            "frozen mixed vegetable", "frozen stir fry mix", "frozen pepper", "frozen potato",
            "french fry", "tater tot", "hashbrown",

            // Frozen Fruits
            "frozen fruit", "frozen berry", "frozen strawberry", "frozen blueberry",
            "frozen raspberry", "frozen mango", "frozen peach", "frozen banana",
            "frozen pineapple", "frozen cherry", "frozen mixed fruit",

            // Frozen Desserts
            "ice cream", "gelato", "sorbet", "frozen yogurt", "popsicle", "ice cream sandwich",
            "ice cream cone", "frozen pie", "frozen cheesecake", "frozen dessert",

            // Frozen Breakfast
            "frozen waffle", "frozen pancake", "frozen breakfast", "frozen breakfast burrito",

            // Frozen Appetizers & Sides
            "frozen appetizer", "frozen mozzarella stick", "frozen chicken wing",
            "frozen onion ring", "frozen egg roll", "frozen spring roll", "frozen samosa",
            "frozen garlic bread", "frozen breadstick", "frozen potato skin",

            // Frozen Meat & Seafood
            "frozen chicken", "frozen fish", "frozen shrimp", "frozen burger", "frozen meatball"
        ),

        GroceryCategory.CANNED to listOf(
            // Canned Vegetables
            "canned vegetable", "canned corn", "canned green bean", "canned pea",
            "canned carrot", "canned tomato", "canned beet", "canned mushroom",
            "canned asparagus", "canned artichoke", "canned pumpkin", "canned potato",
            "canned mixed vegetable", "canned water chestnut", "canned bamboo shoot",

            // Canned Fruits
            "canned fruit", "canned peach", "canned pear", "canned pineapple", "canned mandarin",
            "canned orange", "canned mixed fruit", "canned cherry", "applesauce", "fruit cup",

            // Canned Protein
            "canned tuna", "canned salmon", "canned sardine", "canned chicken", "canned ham",
            "spam", "canned crab", "canned clam", "canned shrimp", "canned oyster",
            "canned anchovy", "canned vienna sausage", "beef jerky",

            // Canned Beans & Legumes
            "canned bean", "canned black bean", "canned kidney bean", "canned pinto bean",
            "canned chickpea", "canned lentil", "canned refried bean", "canned baked bean",
            "canned lima bean", "canned navy bean", "canned cannellini bean",

            // Canned Soup & Broth
            "canned soup", "tomato soup", "chicken noodle soup", "clam chowder", "beef stew",
            "minestrone soup", "mushroom soup", "vegetable soup", "lentil soup", "chili",
            "chicken broth", "beef broth", "vegetable broth", "bone broth", "stock",

            // Canned Sauce & Pasta
            "canned sauce", "tomato sauce", "marinara sauce", "alfredo sauce", "pasta sauce",
            "pizza sauce", "canned spaghetti", "canned ravioli", "canned pasta",
            "canned macaroni and cheese", "canned beefaroni",

            // Canned Other
            "canned olive", "canned jalapeño", "canned green chile", "canned pickle",
            "canned sauerkraut", "canned gravy", "creamed corn", "condensed milk",
            "evaporated milk", "canned coconut milk", "coconut cream", "pie filling"
        ),

        GroceryCategory.DRY_GOODS to listOf(
            // Pasta & Noodles - moved all pasta here from FROZEN
            "pasta", "spaghetti", "fettuccine", "linguine", "penne", "rigatoni", "rotini",
            "macaroni", "lasagna noodle", "egg noodle", "ramen", "udon", "soba", "rice noodle",
            "couscous", "orzo", "gnocchi", "angel hair pasta", "elbow macaroni", "bow tie pasta",
            "shell pasta", "ziti", "pastina",

            // Rice & Grains
            "rice", "white rice", "brown rice", "jasmine rice", "basmati rice", "wild rice",
            "arborio rice", "instant rice", "rice pilaf", "quinoa", "barley", "bulgur",
            "farro", "millet", "wheat berries", "polenta", "grits", "oats", "steel cut oats",

            // Cereal & Breakfast
            "cereal", "oatmeal", "cream of wheat", "granola", "muesli", "corn flakes",
            "cheerios", "rice krispies", "pancake mix", "waffle mix", "breakfast bar",
            "instant breakfast",

            // Baking Supplies
            "flour", "all-purpose flour", "bread flour", "cake flour", "wheat flour",
            "gluten-free flour", "almond flour", "coconut flour", "sugar", "brown sugar",
            "confectioners sugar", "raw sugar", "honey", "maple syrup", "corn syrup",
            "molasses", "baking powder", "baking soda", "yeast", "cornstarch", "vanilla extract",
            "almond extract", "cocoa powder", "chocolate chip", "baking chocolate",
            "marshmallow", "food coloring", "sprinkles",

            // Spices & Seasonings
            "salt", "pepper", "garlic powder", "onion powder", "cinnamon", "nutmeg", "cumin",
            "paprika", "chili powder", "curry powder", "ginger", "turmeric", "oregano",
            "basil", "thyme", "rosemary", "sage", "bay leaf", "allspice", "cloves", "cardamom",
            "coriander", "dill", "italian seasoning", "cajun seasoning", "taco seasoning",
            "seasoning salt", "herb", "spice blend", "black pepper", "crushed red pepper",
            "seasoning", "poultry seasoning",

            // Oils, Vinegars & Dressings
            "oil", "olive oil", "vegetable oil", "canola oil", "coconut oil", "sesame oil",
            "peanut oil", "avocado oil", "cooking spray", "vinegar", "white vinegar",
            "balsamic vinegar", "apple cider vinegar", "red wine vinegar", "rice vinegar",
            "salad dressing", "ranch dressing", "italian dressing", "thousand island",
            "blue cheese dressing", "caesar dressing", "french dressing", "oil and vinegar",

            // Condiments & Sauces
            "ketchup", "mustard", "mayonnaise", "relish", "hot sauce", "bbq sauce",
            "worcestershire sauce", "soy sauce", "teriyaki sauce", "fish sauce", "oyster sauce",
            "hoisin sauce", "sriracha", "peanut sauce", "cocktail sauce", "tartar sauce",
            "salsa", "taco sauce", "enchilada sauce", "chutney", "horseradish",
            "pesto", "tomato paste", "tomato puree", "stir-fry sauce", "gravy mix", "sauce mix",

            // Beans, Legumes & Nuts
            "dry bean", "lentil", "split pea", "chickpea", "black bean", "kidney bean",
            "pinto bean", "navy bean", "lima bean", "northern bean", "peanut", "almond",
            "walnut", "pecan", "cashew", "hazelnut", "pistachio", "macadamia", "nut mix",
            "trail mix", "peanut butter", "almond butter", "nutella", "seed butter", "tahini",

            // Dried Fruits & Vegetables
            "dried fruit", "raisin", "dried cranberry", "dried apricot", "dried date",
            "dried fig", "prune", "dried mango", "dried banana", "sun-dried tomato",
            "dried mushroom", "seaweed", "fruit leather",

            // Broth & Bouillon
            "broth", "bouillon", "soup mix",

            // Dessert & Baking Mixes
            "cake mix", "brownie mix", "cookie mix", "frosting", "pie crust mix", "jello",
            "pudding mix", "gelatin", "chocolate syrup", "caramel sauce",

            // International Food Ingredients
            "taco shell", "taco seasoning", "tortilla chip", "sushi rice", "wasabi", "nori",
            "curry paste", "curry sauce", "coconut milk", "coconut cream", "chili oil",
            "rice paper", "spring roll wrapper", "wonton wrapper", "dumpling wrapper",
            "miso paste", "gochujang", "kimchi", "sauerkraut"
        ),

        GroceryCategory.BEVERAGES to listOf(
            // Water
            "water", "bottled water", "sparkling water", "mineral water", "flavored water",
            "seltzer", "club soda", "tonic water", "distilled water",

            // Soda & Soft Drinks
            "soda", "cola", "coke", "pepsi", "sprite", "dr pepper", "root beer", "ginger ale",
            "cream soda", "orange soda", "lemon lime soda", "diet soda", "soft drink",

            // Juices
            "juice", "orange juice", "apple juice", "grapefruit juice", "cranberry juice",
            "grape juice", "pineapple juice", "tomato juice", "vegetable juice", "lemon juice",
            "lime juice", "pomegranate juice", "aloe juice", "coconut water", "fruit punch",
            "juice blend", "juice concentrate", "juice box",

            // Coffee & Tea
            "coffee", "ground coffee", "coffee bean", "instant coffee", "coffee pod",
            "k cup", "cold brew", "espresso", "tea", "black tea", "green tea", "herbal tea",
            "chamomile tea", "earl grey", "chai tea", "iced tea", "tea bag", "loose leaf tea",

            // Non-Dairy Drinks (shelf-stable)
            "shelf-stable milk", "shelf-stable almond milk", "shelf-stable soy milk",
            "shelf-stable oat milk", "shelf-stable coconut milk", "shelf-stable rice milk",
            "shelf-stable cashew milk", "shelf-stable hemp milk",

            // Sports & Energy Drinks
            "energy drink", "red bull", "monster", "sports drink", "gatorade", "powerade",
            "electrolyte drink", "protein shake", "muscle milk", "protein drink",

            // Alcoholic Beverages
            "beer", "ale", "lager", "stout", "ipa", "wine", "red wine", "white wine", "rosé",
            "champagne", "prosecco", "liquor", "vodka", "whiskey", "rum", "gin", "tequila",
            "brandy", "bourbon", "scotch", "hard seltzer", "hard cider", "wine cooler",
            "cocktail mix", "mixers", "alcoholic", "spirits", "sake", "vermouth",

            // Other Beverages
            "lemonade", "iced tea", "kombucha", "horchata", "smoothie", "milkshake",
            "hot chocolate", "cider", "apple cider", "drink mix", "powdered drink", "kool-aid",
            "tang", "crystal light", "drink", "beverage", "cocktail"
        ),

        GroceryCategory.SNACKS to listOf(
            // Chips & Crisps
            "chip", "potato chip", "tortilla chip", "corn chip", "nacho", "pringles",
            "kettle chip", "veggie chip", "plantain chip", "cheese puff", "cheetos",
            "doritos", "fritos", "funyuns", "sun chip", "popcorn", "microwave popcorn",
            "rice cake", "rice crisp", "bean chip", "kale chip", "crackling", "pork rind",
            "cheese crisp",

            // Crackers
            "cracker", "saltine", "ritz", "graham cracker", "cheez-it", "goldfish",
            "wheat thin", "triscuit", "water cracker", "rice cracker", "pita chip", "pretzel",

            // Nuts & Seeds
            "nut", "peanut", "almond", "cashew", "walnut", "pecan", "hazelnut", "pistachio",
            "macadamia", "brazil nut", "mixed nut", "trail mix", "sunflower seed", "pumpkin seed",
            "chia seed", "flax seed", "hemp seed", "sesame seed", "seed",

            // Sweet Snacks
            "cookie", "oreo", "chocolate chip cookie", "candy", "chocolate", "gummy", "licorice",
            "hard candy", "lollipop", "mint", "gum", "jelly bean", "m&m", "skittles",
            "starburst", "twizzler", "snickers", "kit kat", "hershey", "reese", "chocolate bar",
            "brownie", "brownie bite",

            // Granola & Cereal Bars
            "granola bar", "protein bar", "cereal bar", "breakfast bar", "energy bar",
            "fiber bar", "cliff bar", "kind bar", "quest bar", "nature valley", "nutri-grain",
            "fig newton", "rice krispie treat",

            // Dried Fruit & Fruit Snacks
            "dried fruit", "raisin", "craisin", "dried cranberry", "dried apricot",
            "dried mango", "banana chip", "fruit roll-up", "fruit snack", "fruit leather",
            "fruit strip", "fruit by the foot", "fruit cup",

            // Savory Snacks
            "jerky", "beef jerky", "turkey jerky", "meat stick", "slim jim", "cheese stick",
            "string cheese", "babybel", "laughing cow", "cracker and cheese",
            "bean dip", "onion dip", "french onion dip",
            "veggie straw", "cheese cracker", "snack mix", "chex mix", "gardetto", "nut thins",

            // Popcorn
            "popcorn", "kettle corn", "caramel corn", "cheese popcorn", "popcorn seasoning",

            // Rice & Corn Cakes
            "rice cake", "corn cake", "quinoa cake", "rice cracker",

            // Pudding & Gelatin
            "pudding", "jello", "gelatin", "custard", "mousse", "tapioca", "rice pudding"
        ),

        GroceryCategory.HOUSEHOLD to listOf(
            // Paper Products
            "paper towel", "toilet paper", "tissue", "facial tissue", "kleenex", "napkin",
            "paper plate", "paper bowl", "paper cup", "paper straw", "wax paper",
            "parchment paper", "coffee filter", "cupcake liner", "paper bag",

            // Cleaning Supplies
            "cleaner", "all-purpose cleaner", "disinfectant", "disinfecting wipe", "clorox wipe",
            "bleach", "floor cleaner", "wood cleaner", "glass cleaner", "windex", "bathroom cleaner",
            "toilet cleaner", "shower cleaner", "grout cleaner", "tile cleaner", "stainless steel cleaner",
            "furniture polish", "dusting spray", "carpet cleaner", "spot remover", "stain remover",
            "air freshener", "febreze", "room spray", "fabric refresher", "potpourri", "incense",

            // Laundry Products
            "laundry detergent", "fabric softener", "dryer sheet", "wool dryer ball", "stain remover",
            "bleach", "laundry booster", "oxiclean", "fabric dye", "lint roller", "clothespin",

            // Dish Cleaning
            "dish soap", "dishwasher detergent", "dishwasher pod", "rinse aid", "dishwasher cleaner",
            "dish scrubber", "steel wool", "sponge", "brillo pad", "dishcloth", "dish towel",

            // Storage & Organization
            "storage bag", "ziploc bag", "sandwich bag", "freezer bag", "storage container",
            "tupperware", "plastic wrap", "aluminum foil", "cling wrap", "food storage",
            "plastic bag", "trash bag", "garbage bag", "compost bag", "vacuum storage bag",

            // Kitchen Supplies
            "baking cup", "baking paper", "foil", "plastic wrap", "food storage container",
            "toothpick", "skewer", "straw", "plastic utensil", "paper plate", "paper towel",
            "napkin", "lunch bag", "coffee filter",

            // Pest Control
            "insect repellent", "bug spray", "ant trap", "mouse trap", "roach killer",
            "fly paper", "mosquito coil", "mothball",

            // Batteries & Lights
            "battery", "aa battery", "aaa battery", "d battery", "9v battery", "light bulb",
            "led bulb", "flashlight", "candle", "tea light", "match", "lighter",

            // Disposables
            "disposable glove", "plastic cup", "paper cup", "plastic plate", "paper plate",
            "plastic utensil", "plastic fork", "plastic knife", "plastic spoon",

            // Miscellaneous
            "tape", "duct tape", "masking tape", "packaging tape", "scotch tape", "glue",
            "super glue", "staple", "paper clip", "rubber band", "twist tie", "safety pin",
            "sewing kit", "scissors", "charcoal", "propane", "firewood", "fire starter",
            "lighter fluid", "ice pack", "water filter", "filter", "furnace filter", "carbon monoxide detector",
            "smoke detector"
        ),

        GroceryCategory.PERSONAL_CARE to listOf(
            // Hair Care
            "shampoo", "conditioner", "hair mask", "deep conditioner", "leave-in conditioner",
            "hair oil", "hair gel", "hair mousse", "hair spray", "hair wax", "pomade",
            "dry shampoo", "hair dye", "hair color", "hair bleach", "purple shampoo",
            "dandruff shampoo", "hair serum", "detangler", "hair brush", "hair comb",
            "hair tie", "bobby pin", "hair clip", "hair dryer", "flat iron", "curling iron",
            "blow dryer", "hair removal", "hair trimmer", "clipper",

            // Skin Care
            "face wash", "cleanser", "facial cleanser", "exfoliator", "scrub", "toner",
            "face serum", "eye cream", "face cream", "moisturizer", "sunscreen", "face mask",
            "sheet mask", "acne treatment", "spot treatment", "makeup remover", "micellar water",
            "face wipe", "cotton pad", "cotton ball", "q-tip", "lip balm", "chapstick",
            "vaseline", "body lotion", "body butter", "hand cream", "hand lotion",

            // Bath & Shower
            "body wash", "shower gel", "soap", "bar soap", "bath bomb", "bubble bath",
            "bath salt", "bath oil", "loofah", "shower pouf", "bath sponge", "body scrub",
            "back scrubber", "shower cap", "shower curtain", "shower liner", "shower caddy",

            // Oral Care
            "toothpaste", "toothbrush", "electric toothbrush", "toothbrush head", "floss",
            "dental floss", "floss pick", "mouthwash", "mouth rinse", "dental rinse",
            "whitening strip", "denture cleaner", "denture adhesive", "tongue scraper",
            "breath mint", "breath strip", "breath freshener",

            // Deodorant & Antiperspirant
            "deodorant", "antiperspirant", "deodorant spray", "body spray", "cologne",
            "perfume", "aftershave",

            // Shaving & Hair Removal
            "razor", "razor blade", "refill blade", "disposable razor", "electric razor",
            "shaving cream", "shaving gel", "shaving foam", "aftershave", "aftershave balm",
            "wax strip", "hair removal cream", "depilatory", "epilator", "trimmer",

            // Feminine Care
            "pad", "panty liner", "tampon", "menstrual cup", "period underwear", "feminine wipe",
            "feminine wash", "yeast infection treatment", "vaginal moisturizer",

            // Eye Care
            "contact solution", "contact lens case", "eye drops", "artificial tears",
            "eye mask", "glasses cleaner", "glasses cloth",

            // Hand & Foot Care
            "hand soap", "hand sanitizer", "nail clipper", "nail file", "emery board",
            "nail polish", "nail polish remover", "acetone", "cotton ball", "foot cream",
            "foot scrub", "foot file", "pedicure kit", "manicure kit", "cuticle oil",
            "hand cream", "foot powder",

            // First Aid
            "band-aid", "bandage", "gauze", "medical tape", "antiseptic", "antibiotic ointment",
            "hydrogen peroxide", "rubbing alcohol", "isopropyl alcohol", "cotton swab", "q-tip",
            "cotton ball", "first aid kit", "thermometer", "heating pad", "ice pack",
            "cold compress", "hot compress", "ace bandage", "ankle brace", "wrist brace",
            "knee brace", "pain relief patch", "blister pad", "corn pad", "callus pad",

            // Medications & Remedies
            "pain reliever", "tylenol", "advil", "aspirin", "ibuprofen", "acetaminophen",
            "aleve", "cold medicine", "cough syrup", "cough drop", "throat lozenge",
            "antihistamine", "allergy medicine", "benadryl", "claritin", "zyrtec",
            "decongestant", "nasal spray", "vicks", "chest rub", "sinus medicine",
            "stomach medicine", "pepto bismol", "antacid", "tums", "acid reducer",
            "gas relief", "laxative", "fiber supplement", "sleep aid", "melatonin",
            "vitamin", "multivitamin", "supplement", "protein powder", "medicine", "pill",
            "prescription", "ointment", "cream",

            // Sun Care
            "sunscreen", "after sun", "aloe vera gel", "self tanner", "sunless tanner",
            "bronzer", "tanning oil",

            // Baby Care
            "baby wipe", "diaper", "baby powder", "baby lotion", "baby shampoo", "baby wash",
            "baby oil", "diaper cream", "diaper rash cream", "baby food", "formula",
            "baby bottle", "pacifier", "teething ring", "teething gel",

            // Misc Personal Care
            "cotton swab", "cotton ball", "q-tip", "tissue", "toilet paper", "wet wipe",
            "hand sanitizer", "face mask", "personal lubricant", "condom", "contraceptive",
            "pregnancy test"
        ),

        GroceryCategory.OTHER to listOf(
            // Items that don't fit elsewhere
            "specialty item", "seasonal item", "holiday item", "gift card",
            "lottery ticket", "prepaid card", "magazine", "newspaper", "greeting card",
            "party supply", "balloon", "gift bag", "wrapping paper", "birthday candle",
            "floral arrangement", "plant", "flower", "potting soil", "plant food",
            "pet food", "cat food", "dog food", "pet treat", "pet toy", "cat litter",
            "aquarium supply", "bird seed", "small animal bedding"
        )
    )

    /**
     * Guesses the most likely category for a given item name.
     */
    fun guessCategory(itemName: String): GroceryCategory {
        val lowercaseName = itemName.lowercase().trim()

        // First pass: look for exact matches
        categoryKeywords.forEach { (category, keywords) ->
            if (keywords.contains(lowercaseName)) {
                return category
            }
        }

        // Second pass: look for word boundary matches (complete words)
        categoryKeywords.forEach { (category, keywords) ->
            for (keyword in keywords) {
                // Split the keyword into words
                val keywordWords = keyword.split(" ")
                if (keywordWords.contains(lowercaseName) || lowercaseName.split(" ").contains(keyword)) {
                    return category
                }
            }
        }

        // Third pass: fall back to substring matching
        categoryKeywords.forEach { (category, keywords) ->
            for (keyword in keywords) {
                if (lowercaseName.contains(keyword) || keyword.contains(lowercaseName)) {
                    return category
                }
            }
        }

        // No matches found, return OTHER
        return GroceryCategory.OTHER
    }

    /**
     * Generate suggestions based on input text and existing items.
     */
    fun generateSuggestions(input: String, existingItems: List<GroceryItem>): List<String> {
        if (input.length < 2) return emptyList() // Only show suggestions after 2 characters

        val lowercaseInput = input.lowercase().trim()
        val suggestions = mutableSetOf<String>()

        // Find matching keywords from all categories
        categoryKeywords.values.flatten().forEach { keyword ->
            if (keyword.contains(lowercaseInput) || lowercaseInput.contains(keyword)) {
                suggestions.add(keyword)
            }
        }

        // Add from previous items (avoid duplicates)
        suggestions.addAll(
            existingItems
                .map { it.name }
                .distinct()
                .filter {
                    it.lowercase().contains(lowercaseInput) || lowercaseInput.contains(it.lowercase())
                }
        )

        // Sort and limit suggestions
        return suggestions
            .sortedWith(compareBy(
                { !it.lowercase().startsWith(lowercaseInput) }, // Prioritize items that start with input
                { it.length }, // Shorter items next
                { it } // Alphabetical for same length
            ))
            .take(5) // Limit to 5 suggestions
    }
}
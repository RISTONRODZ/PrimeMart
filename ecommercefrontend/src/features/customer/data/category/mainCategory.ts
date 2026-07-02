export interface SubCategory {
  name: string;
  categoryId: string;
  parentCategoryId: string;
  level: number;
}

export interface MainCategory {
  name: string;
  categoryId: string;
  level: number;
  levelTwoCategory?: SubCategory[];
  levelTowCategory?: SubCategory[];
}

export const mainCategory: MainCategory[] = [
  {
    name: "Men",
    categoryId: "men",
    level: 1,
    levelTwoCategory:[
        {
            "name": "Topwere",
            "categoryId": "men_topwear",
            "parentCategoryId":"men",
            "level":2
        },
        {
            "name": "Bottomwere",
            "categoryId": "men_bottomwear",
            "parentCategoryId":"men",
            "level":2
        },
        {
            "name": "Innerwere And Sleepwere",
            "categoryId": "men_innerwear_and_sleepwear",
            "parentCategoryId":"men",
            "level":2
        },
        {
            "name": "Footwere",
            "categoryId": "men_footwear",
            "parentCategoryId":"men",
            "level":2
        },
        {
            "name": "Persional Care And  grooming",
            "categoryId": "men_personal_care_and_grooming",
            "parentCategoryId":"men",
            "level":2
        },
        {
            "name": "Fashion Accessories",
            "categoryId": "men_fashion_accessories",
            "parentCategoryId":"men",
            "level":2
        },
        {
            "name": "Gadgets",
            "categoryId": "men_gadgets",
            "parentCategoryId":"men",
            "level":2
        },
        {
            "name": "Bags And Backpacks",
            "categoryId": "men_bags_and_backpacks",
            "parentCategoryId":"men",
            "level":2
        }
    ]
  },
  {
    name: "Women",
    categoryId: "women",
    level: 1,
    levelTowCategory:[
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Indian & fusion Wear",
          "categoryId": "women_indian_and_fusion_wear"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"western wear",
          "categoryId": "women_western_wear"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Footwear",
          "categoryId": "women_footwear"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Sports & Active Wear",
          "categoryId": "women_sports_active_wear"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Lingerie Sleepwear",
          "categoryId": "women_lingerie_sleepwear"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Buauty & Personal Care",
          "categoryId": "women_beauty_personal_care"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Gadgets",
          "categoryId": "women_gadgets"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Jewellery",
          "categoryId": "women_jewellery"
        },
        {
          "parentCategoryId":"women",
          "level":2,
          "name":"Handbags, Bags & wallets",
          "categoryId": "women_handbags_bags_wallets"
        }
      ]
      
  },
  {
    name: "Home & Furniture",
    categoryId: "home_furniture",
    level: 1,
    levelTwoCategory:[
        {
            "name": "Furniture",
            "categoryId": "home_furniture_furniture",
            "parentCategoryId":"home_furniture",
            "level":2
        },
        {
            "name": "Decor",
            "categoryId": "home_furniture_decor",
            "parentCategoryId":"home_furniture",
            "level":2
        },
        {
            "name": "Kitchen & Dining",
            "categoryId": "home_furniture_kitchen_dining",
            "parentCategoryId":"home_furniture",
            "level":2
        },
        {
            "name": "Bedding",
            "categoryId": "home_furniture_bedding",
            "parentCategoryId":"home_furniture",
            "level":2
        },
        {
            "name": "Bath",
            "categoryId": "home_furniture_bath",
            "parentCategoryId":"home_furniture",
            "level":2
        }
    ]
  },
  {
    name: "Electronics",
    categoryId: "electronics",
    level: 1,
    levelTwoCategory:[
        {
            "name": "Mobiles",
            "categoryId": "electronics_mobiles",
            "parentCategoryId":"electronics",
            "level":2
        },
        {
            "name": "Laptops",
            "categoryId": "electronics_laptops",
            "parentCategoryId":"electronics",
            "level":2
        },
        {
            "name": "Audio",
            "categoryId": "electronics_audio",
            "parentCategoryId":"electronics",
            "level":2
        },
        {
            "name": "Cameras",
            "categoryId": "electronics_cameras",
            "parentCategoryId":"electronics",
            "level":2
        },
        {
            "name": "Gaming",
            "categoryId": "electronics_gaming",
            "parentCategoryId":"electronics",
            "level":2
        },
        {
            "name": "Accessories",
            "categoryId": "electronics_accessories",
            "parentCategoryId":"electronics",
            "level":2
        }
    ]
  },
];

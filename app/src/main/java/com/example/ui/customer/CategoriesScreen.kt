package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.ui.components.ProductCard

@Composable
fun CategoriesScreen(
  categories: List<ProductCategory>,
  products: List<Product>,
  cartItems: List<CartItem>,
  wishlistIds: Set<String>,
  selectedCategoryId: String?,
  onSelectCategory: (String?) -> Unit,
  onProductClick: (Product) -> Unit,
  onAddToCart: (Product) -> Unit,
  onIncrease: (Product) -> Unit,
  onDecrease: (Product) -> Unit,
  onWishlistToggle: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val activeCat = selectedCategoryId ?: categories.firstOrNull()?.id ?: "cat_fv"
  val categoryProducts = products.filter { it.categoryId == activeCat }

  Row(
    modifier = modifier
      .fillMaxSize()
      .testTag("categories_screen")
  ) {
    // Left Navigation Rail for all 11 categories
    LazyColumn(
      modifier = Modifier
        .width(105.dp)
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      items(categories) { cat ->
        val isSelected = cat.id == activeCat
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectCategory(cat.id) }
            .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .testTag("cat_tab_${cat.id}")
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(text = cat.iconEmoji, fontSize = 22.sp)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = cat.name,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 12.sp
          )
        }
      }
    }

    // Right Content Area (Products in selected category)
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 12.dp)
    ) {
      val currentCategory = categories.find { it.id == activeCat }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = currentCategory?.iconEmoji ?: "",
          fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "${currentCategory?.name ?: ""} (${categoryProducts.size})",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
      ) {
        val pairs = categoryProducts.chunked(2)
        items(pairs) { pair ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            val p1 = pair[0]
            val cartQty1 = cartItems.find { it.product.id == p1.id }?.quantity ?: 0
            ProductCard(
              product = p1,
              cartQuantity = cartQty1,
              isWishlisted = wishlistIds.contains(p1.id),
              onProductClick = { onProductClick(p1) },
              onAddToCart = { onAddToCart(p1) },
              onIncrease = { onIncrease(p1) },
              onDecrease = { onDecrease(p1) },
              onWishlistToggle = { onWishlistToggle(p1.id) },
              modifier = Modifier.weight(1f)
            )

            if (pair.size > 1) {
              val p2 = pair[1]
              val cartQty2 = cartItems.find { it.product.id == p2.id }?.quantity ?: 0
              ProductCard(
                product = p2,
                cartQuantity = cartQty2,
                isWishlisted = wishlistIds.contains(p2.id),
                onProductClick = { onProductClick(p2) },
                onAddToCart = { onAddToCart(p2) },
                onIncrease = { onIncrease(p2) },
                onDecrease = { onDecrease(p2) },
                onWishlistToggle = { onWishlistToggle(p2.id) },
                modifier = Modifier.weight(1f)
              )
            } else {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
        }
      }
    }
  }
}

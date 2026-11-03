package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@Composable
fun CustomerHomeScreen(
  categories: List<ProductCategory>,
  products: List<Product>,
  cartItems: List<CartItem>,
  wishlistIds: Set<String>,
  searchQuery: String,
  selectedCategoryId: String?,
  onSearchChange: (String) -> Unit,
  onCategorySelect: (String?) -> Unit,
  onProductClick: (Product) -> Unit,
  onAddToCart: (Product) -> Unit,
  onIncrease: (Product) -> Unit,
  onDecrease: (Product) -> Unit,
  onWishlistToggle: (String) -> Unit,
  onViewAllCategoriesClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("customer_home_screen"),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Search Bar Box
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.primary)
          .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchChange,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("home_search_input"),
          placeholder = {
            Text(
              text = "Search 'milk', 'apples', 'atta', 'lays'...",
              fontSize = 13.sp,
              color = Color.Gray
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = MaterialTheme.colorScheme.primary
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchChange("") }) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Clear",
                  tint = Color.Gray
                )
              }
            } else {
              Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Voice search",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          },
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = AaruhaYellowAccent,
            unfocusedBorderColor = Color.Transparent
          ),
          singleLine = true
        )
      }
    }

    // Flash Promo Banner Carousel
    item {
      OfferBannersSection()
    }

    // Categories Horizontal Strip
    item {
      Column(modifier = Modifier.padding(top = 12.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Explore Categories",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          TextButton(onClick = onViewAllCategoriesClick) {
            Text(
              text = "See All (${categories.size})",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }

        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // "All" pill
          item {
            CategoryPill(
              emoji = "🛒",
              name = "All Items",
              isSelected = selectedCategoryId == null,
              onClick = { onCategorySelect(null) }
            )
          }
          items(categories) { cat ->
            CategoryPill(
              emoji = cat.iconEmoji,
              name = cat.name,
              isSelected = selectedCategoryId == cat.id,
              onClick = {
                onCategorySelect(if (selectedCategoryId == cat.id) null else cat.id)
              }
            )
          }
        }
      }
    }

    // Flash Deals Row (with 10-min countdown badge)
    val flashDeals = products.filter { it.isFlashDeal }
    if (flashDeals.isNotEmpty() && selectedCategoryId == null && searchQuery.isBlank()) {
      item {
        Column(modifier = Modifier.padding(top = 18.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "⚡ Flash Deals",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(8.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(DiscountRedBg)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "Ends in 02h 45m",
                  color = DiscountRed,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(flashDeals) { product ->
              val cartQty = cartItems.find { it.product.id == product.id }?.quantity ?: 0
              ProductCard(
                product = product,
                cartQuantity = cartQty,
                isWishlisted = wishlistIds.contains(product.id),
                onProductClick = { onProductClick(product) },
                onAddToCart = { onAddToCart(product) },
                onIncrease = { onIncrease(product) },
                onDecrease = { onDecrease(product) },
                onWishlistToggle = { onWishlistToggle(product.id) }
              )
            }
          }
        }
      }
    }

    // Best Sellers Row
    val bestSellers = products.filter { it.isBestSeller }
    if (bestSellers.isNotEmpty() && selectedCategoryId == null && searchQuery.isBlank()) {
      item {
        Column(modifier = Modifier.padding(top = 18.dp)) {
          Text(
            text = "🔥 Best Sellers in Hyderabad",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
          )

          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(bestSellers) { product ->
              val cartQty = cartItems.find { it.product.id == product.id }?.quantity ?: 0
              ProductCard(
                product = product,
                cartQuantity = cartQty,
                isWishlisted = wishlistIds.contains(product.id),
                onProductClick = { onProductClick(product) },
                onAddToCart = { onAddToCart(product) },
                onIncrease = { onIncrease(product) },
                onDecrease = { onDecrease(product) },
                onWishlistToggle = { onWishlistToggle(product.id) }
              )
            }
          }
        }
      }
    }

    // Trending & Categorized Products Grid/List
    item {
      Column(modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp)) {
        val title = when {
          searchQuery.isNotBlank() -> "Search Results for '$searchQuery' (${products.size})"
          selectedCategoryId != null -> {
            val catName = categories.find { it.id == selectedCategoryId }?.name ?: "Category"
            "$catName Products (${products.size})"
          }
          else -> "All Fresh Products & Staples (${products.size})"
        }

        Text(
          text = title,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(bottom = 12.dp)
        )

        if (products.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "🔍", fontSize = 48.sp)
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "No products found matching '$searchQuery'",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              TextButton(onClick = { onSearchChange("") }) {
                Text("Clear search")
              }
            }
          }
        }
      }
    }

    // Products in 2-column rows
    val chunked = products.chunked(2)
    items(chunked) { pair ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        val first = pair[0]
        val firstCartQty = cartItems.find { it.product.id == first.id }?.quantity ?: 0
        ProductCard(
          product = first,
          cartQuantity = firstCartQty,
          isWishlisted = wishlistIds.contains(first.id),
          onProductClick = { onProductClick(first) },
          onAddToCart = { onAddToCart(first) },
          onIncrease = { onIncrease(first) },
          onDecrease = { onDecrease(first) },
          onWishlistToggle = { onWishlistToggle(first.id) },
          modifier = Modifier.weight(1f)
        )

        if (pair.size > 1) {
          val second = pair[1]
          val secondCartQty = cartItems.find { it.product.id == second.id }?.quantity ?: 0
          ProductCard(
            product = second,
            cartQuantity = secondCartQty,
            isWishlisted = wishlistIds.contains(second.id),
            onProductClick = { onProductClick(second) },
            onAddToCart = { onAddToCart(second) },
            onIncrease = { onIncrease(second) },
            onDecrease = { onDecrease(second) },
            onWishlistToggle = { onWishlistToggle(second.id) },
            modifier = Modifier.weight(1f)
          )
        } else {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }
  }
}

@Composable
private fun OfferBannersSection() {
  LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      BannerCard(
        tag = "LIGHTNING SPEED",
        title = "Delivered in 10 Mins ⚡",
        subtitle = "From your local Aaruha dark store",
        couponCode = "FREEDEL",
        brush = Brush.horizontalGradient(listOf(Color(0xFF0C733F), Color(0xFF15803D)))
      )
    }
    item {
      BannerCard(
        tag = "WELCOME OFFER",
        title = "FLAT 50% OFF",
        subtitle = "On your first 3 grocery orders",
        couponCode = "AARUHA50",
        brush = Brush.horizontalGradient(listOf(Color(0xFF854D0E), Color(0xFFCA8A04)))
      )
    }
    item {
      BannerCard(
        tag = "ORGANIC & FRESH",
        title = "Farm Fresh Veggies",
        subtitle = "Picked at dawn, at your doorstep by noon",
        couponCode = "SUPER100",
        brush = Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF2563EB)))
      )
    }
  }
}

@Composable
private fun BannerCard(
  tag: String,
  title: String,
  subtitle: String,
  couponCode: String,
  brush: Brush
) {
  Box(
    modifier = Modifier
      .width(290.dp)
      .height(130.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(brush)
      .padding(14.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = tag,
          fontSize = 10.sp,
          fontWeight = FontWeight.ExtraBold,
          color = AaruhaYellowAccent,
          letterSpacing = 0.5.sp
        )
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "Code: $couponCode",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Column {
        Text(
          text = title,
          color = Color.White,
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Text(
          text = subtitle,
          color = Color.White.copy(alpha = 0.85f),
          fontSize = 11.sp,
          maxLines = 1
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Shop Now",
          color = AaruhaYellowAccent,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        Icon(
          imageVector = Icons.Default.ArrowForward,
          contentDescription = null,
          tint = AaruhaYellowAccent,
          modifier = Modifier.size(12.dp)
        )
      }
    }
  }
}

@Composable
private fun CategoryPill(
  emoji: String,
  name: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(72.dp)
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
      .padding(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(54.dp)
        .clip(CircleShape)
        .background(
          if (isSelected) MaterialTheme.colorScheme.primaryContainer
          else MaterialTheme.colorScheme.surfaceVariant
        )
        .border(
          width = if (isSelected) 2.dp else 0.dp,
          color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
          shape = CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = emoji,
        fontSize = 24.sp
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = name,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
      maxLines = 2,
      lineHeight = 13.sp,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

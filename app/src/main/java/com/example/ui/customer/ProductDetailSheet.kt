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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.Product
import com.example.model.Review
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
  product: Product,
  cartItems: List<CartItem>,
  reviews: List<Review>,
  similarProducts: List<Product>,
  isWishlisted: Boolean,
  onDismiss: () -> Unit,
  onAddToCart: (Product, String) -> Unit,
  onIncrease: (Product) -> Unit,
  onDecrease: (Product) -> Unit,
  onWishlistToggle: () -> Unit,
  onAddReview: (rating: Int, comment: String) -> Unit
) {
  var selectedWeight by remember { mutableStateOf(product.weight) }
  var showReviewDialog by remember { mutableStateOf(false) }

  val productReviews = reviews.filter { it.productId == product.id }
  val cartItem = cartItems.find { it.product.id == product.id && it.selectedWeight == selectedWeight }
  val currentQty = cartItem?.quantity ?: 0

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = Modifier.testTag("product_detail_sheet")
  ) {
    Box(modifier = Modifier.fillMaxHeight(0.92f)) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 80.dp),
        contentPadding = PaddingValues(16.dp)
      ) {
        // Hero Image Carousel / Box
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(220.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = product.emoji,
              fontSize = 96.sp
            )

            // Discount Badge
            if (product.offerDiscountPercent > 0) {
              Box(
                modifier = Modifier
                  .align(Alignment.TopStart)
                  .padding(12.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(DiscountRed)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "${product.offerDiscountPercent}% OFF",
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Wishlist Button
            IconButton(
              onClick = onWishlistToggle,
              modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
            ) {
              Icon(
                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Wishlist",
                tint = if (isWishlisted) DiscountRed else Color.Gray,
                modifier = Modifier.size(26.dp)
              )
            }

            // Delivery Promise Pill
            Row(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = AaruhaYellowAccent,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Delivery in 8-10 mins",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Brand & Title
        item {
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = product.brand,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = product.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        // Weight Selector Chips
        item {
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Select Pack Size:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            product.availableWeights.forEach { wt ->
              val isSelected = wt == selectedWeight
              FilterChip(
                selected = isSelected,
                onClick = { selectedWeight = wt },
                label = { Text(wt) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
              )
            }
          }
        }

        // Price, Savings & Stock Availability
        item {
          Spacer(modifier = Modifier.height(14.dp))
          Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(
                    text = "₹${product.sellingPrice.toInt()}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  if (product.mrp > product.sellingPrice) {
                    Text(
                      text = "MRP ₹${product.mrp.toInt()}",
                      fontSize = 14.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      textDecoration = TextDecoration.LineThrough
                    )
                  }
                }
                val savings = (product.mrp - product.sellingPrice).toInt()
                if (savings > 0) {
                  Text(
                    text = "You save ₹$savings (${product.offerDiscountPercent}% off)",
                    fontSize = 12.sp,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              // Stock Status Badge
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (product.stockQuantity > 0) SuccessGreenBg else DiscountRedBg)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = if (product.stockQuantity > 0) "In Stock (${product.stockQuantity})" else "Out of Stock",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (product.stockQuantity > 0) SuccessGreen else DiscountRed
                )
              }
            }
          }
        }

        // Product Description & Highlights
        item {
          Spacer(modifier = Modifier.height(18.dp))
          Text(
            text = "Product Details",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = product.description,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 19.sp
          )

          Spacer(modifier = Modifier.height(10.dp))
          // 3 Feature Badges
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FeatureHighlightBadge("⚡ 10-Min Fast", modifier = Modifier.weight(1f))
            FeatureHighlightBadge("🌿 100% Fresh", modifier = Modifier.weight(1f))
            FeatureHighlightBadge("🛡️ Clean Packed", modifier = Modifier.weight(1f))
          }
        }

        // Reviews Section
        item {
          Spacer(modifier = Modifier.height(20.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Ratings & Reviews (${productReviews.size + 120})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = Color(0xFFD97706),
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = " ${product.rating} / 5 • Verified Customers",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
            OutlinedButton(
              onClick = { showReviewDialog = true },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text("Write Review", fontSize = 12.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          if (productReviews.isEmpty()) {
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "Pooja R. ★★★★★",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
                Text(
                  text = "Super fast delivery within 8 minutes! Very fresh and well-packed.",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          } else {
            productReviews.forEach { review ->
              Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = review.customerName,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp
                    )
                    Text(
                      text = "★".repeat(review.rating),
                      color = Color(0xFFD97706),
                      fontSize = 12.sp
                    )
                  }
                  Text(
                    text = review.comment,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }

      // Sticky Bottom Action Bar
      Surface(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Total Price",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "₹${(product.sellingPrice * if (currentQty > 0) currentQty else 1).toInt()}",
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          if (currentQty == 0) {
            Button(
              onClick = { onAddToCart(product, selectedWeight) },
              modifier = Modifier
                .weight(1.5f)
                .height(48.dp)
                .testTag("detail_add_to_cart"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Add To Cart", fontWeight = FontWeight.Bold)
            }
          } else {
            Row(
              modifier = Modifier
                .weight(1.5f)
                .height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              IconButton(onClick = { onDecrease(product) }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
              }
              Text(
                text = "$currentQty in Cart",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              IconButton(onClick = { onIncrease(product) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
              }
            }
          }
        }
      }
    }
  }

  // Write Review Dialog
  if (showReviewDialog) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showReviewDialog = false },
      title = { Text("Rate & Review Product") },
      text = {
        Column {
          Text("How was your experience with ${product.name}?")
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            (1..5).forEach { star ->
              IconButton(onClick = { rating = star }) {
                Icon(
                  imageVector = if (star <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                  contentDescription = "$star stars",
                  tint = Color(0xFFD97706)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Write your thoughts...") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(onClick = {
          if (comment.isNotBlank()) {
            onAddReview(rating, comment)
            showReviewDialog = false
          }
        }) {
          Text("Submit")
        }
      },
      dismissButton = {
        TextButton(onClick = { showReviewDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun FeatureHighlightBadge(text: String, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
      .padding(vertical = 8.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onPrimaryContainer
    )
  }
}

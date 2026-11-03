package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Address
import com.example.model.AppRole
import com.example.model.Product
import com.example.ui.theme.*

@Composable
fun AaruhaTopBar(
  currentRole: AppRole,
  selectedAddress: Address?,
  onSwitchRoleClick: () -> Unit,
  onAddressClick: () -> Unit,
  onSearchClick: () -> Unit,
  onWishlistClick: () -> Unit,
  wishlistCount: Int
) {
  Surface(
    color = MaterialTheme.colorScheme.primary,
    shadowElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Delivery Location Pill
        Row(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onAddressClick() }
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(AaruhaYellowAccent),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = "Fast Delivery",
              tint = Color.Black,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "10 MINS DELIVERY",
                color = AaruhaYellowAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
              )
              Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select location",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
            Text(
              text = selectedAddress?.let { "${it.title} - ${it.fullAddress}" } ?: "Hitec City, Hyderabad",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Role Switcher Badge
        AssistChip(
          onClick = onSwitchRoleClick,
          label = {
            Text(
              text = currentRole.badge,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.Black
            )
          },
          leadingIcon = {
            Icon(
              imageVector = when (currentRole) {
                AppRole.CUSTOMER -> Icons.Default.ShoppingBag
                AppRole.ADMIN -> Icons.Default.AdminPanelSettings
                AppRole.EMPLOYEE -> Icons.Default.Badge
                AppRole.DELIVERY_PARTNER -> Icons.Default.DirectionsBike
              },
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(14.dp)
            )
          },
          colors = AssistChipDefaults.assistChipColors(
            containerColor = AaruhaYellowAccent
          ),
          modifier = Modifier.testTag("role_switcher_chip")
        )

        IconButton(
          onClick = onWishlistClick,
          modifier = Modifier.testTag("wishlist_button")
        ) {
          BadgedBox(badge = {
            if (wishlistCount > 0) {
              Badge(
                containerColor = DiscountRed,
                contentColor = Color.White
              ) {
                Text(wishlistCount.toString())
              }
            }
          }) {
            Icon(
              imageVector = Icons.Outlined.FavoriteBorder,
              contentDescription = "Wishlist",
              tint = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
fun ProductCard(
  product: Product,
  cartQuantity: Int,
  isWishlisted: Boolean,
  onProductClick: () -> Unit,
  onAddToCart: () -> Unit,
  onIncrease: () -> Unit,
  onDecrease: () -> Unit,
  onWishlistToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .width(168.dp)
      .clip(RoundedCornerShape(14.dp))
      .clickable { onProductClick() }
      .testTag("product_card_${product.id}"),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = CardDefaults.outlinedCardBorder()
  ) {
    Column(
      modifier = Modifier.padding(10.dp)
    ) {
      // Image Box with Discount Badge & Wishlist Heart
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(115.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
      ) {
        // Large Emoji Icon Representation
        Text(
          text = product.emoji,
          fontSize = 52.sp
        )

        // Discount Tag
        if (product.offerDiscountPercent > 0) {
          Box(
            modifier = Modifier
              .align(Alignment.TopStart)
              .clip(RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
              .background(DiscountRed)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "${product.offerDiscountPercent}% OFF",
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Wishlist Button
        IconButton(
          onClick = onWishlistToggle,
          modifier = Modifier
            .align(Alignment.TopEnd)
            .size(32.dp)
            .testTag("wishlist_toggle_${product.id}")
        ) {
          Icon(
            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Wishlist",
            tint = if (isWishlisted) DiscountRed else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
        }

        // ETA Pill
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Black.copy(alpha = 0.65f))
            .padding(horizontal = 4.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = null,
            tint = AaruhaYellowAccent,
            modifier = Modifier.size(10.dp)
          )
          Text(
            text = "8 MINS",
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Brand & Weight
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = product.brand,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = "★ ${product.rating}",
          fontSize = 10.sp,
          color = Color(0xFFD97706),
          fontWeight = FontWeight.Bold
        )
      }

      // Title
      Text(
        text = product.name,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 16.sp,
        modifier = Modifier.height(34.dp)
      )

      // Weight
      Text(
        text = product.weight,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 2.dp)
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Price & Add Button Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "₹${product.sellingPrice.toInt()}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          if (product.mrp > product.sellingPrice) {
            Text(
              text = "₹${product.mrp.toInt()}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textDecoration = TextDecoration.LineThrough
            )
          }
        }

        // Add to Cart / Quantity Stepper Button
        if (cartQuantity == 0) {
          Button(
            onClick = onAddToCart,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer,
              contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            border = ButtonDefaults.outlinedButtonBorder(),
            modifier = Modifier
              .height(32.dp)
              .testTag("add_to_cart_${product.id}")
          ) {
            Text(
              text = "ADD",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        } else {
          Row(
            modifier = Modifier
              .height(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.primary)
              .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = onDecrease,
              modifier = Modifier.size(24.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
              )
            }
            Text(
              text = cartQuantity.toString(),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 6.dp)
            )
            IconButton(
              onClick = onIncrease,
              modifier = Modifier.size(24.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun FloatingCartPreviewBar(
  itemCount: Int,
  totalAmount: Double,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = itemCount > 0,
    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
    modifier = modifier
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clickable { onClick() }
        .shadow(8.dp, RoundedCornerShape(12.dp))
        .testTag("floating_cart_bar"),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary
      )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "$itemCount ${if (itemCount == 1) "ITEM" else "ITEMS"} • ₹${totalAmount.toInt()}",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = "⚡ Extra savings applied",
              color = AaruhaYellowAccent,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "View Cart",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

@Composable
fun RoleSwitcherDialog(
  currentRole: AppRole,
  onRoleSelected: (AppRole) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Column {
        Text(
          text = "Switch Module / Role",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
        Text(
          text = "Experience all 4 modules of Aaruha Groceries",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        AppRole.values().forEach { role ->
          val isSelected = role == currentRole
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .clickable {
                onRoleSelected(role)
                onDismiss()
              }
              .testTag("role_select_${role.name}"),
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            border = if (isSelected) CardDefaults.outlinedCardBorder() else null
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (role) {
                    AppRole.CUSTOMER -> Icons.Default.ShoppingCart
                    AppRole.ADMIN -> Icons.Default.AdminPanelSettings
                    AppRole.EMPLOYEE -> Icons.Default.Storefront
                    AppRole.DELIVERY_PARTNER -> Icons.Default.DirectionsBike
                  },
                  contentDescription = null,
                  tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = role.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Text(
                  text = when (role) {
                    AppRole.CUSTOMER -> "Instant delivery, live GPS tracking, cart & checkout"
                    AppRole.ADMIN -> "Executive KPI dashboard, revenue reports, employee & product control"
                    AppRole.EMPLOYEE -> "Store inventory, quick stock updates, order packaging"
                    AppRole.DELIVERY_PARTNER -> "Rider order assignments, step-by-step delivery & earnings"
                  },
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Selected",
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close")
      }
    }
  )
}

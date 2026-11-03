package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun CustomerProfileScreen(
  profile: CustomerProfile,
  orders: List<Order>,
  products: List<Product>,
  chatMessages: List<ChatMessage>,
  onToggleDarkMode: () -> Unit,
  onReorder: (String) -> Unit,
  onAddAddress: (Address) -> Unit,
  onSetDefaultAddress: (String) -> Unit,
  onTrackOrder: (String) -> Unit,
  onSendChatMessage: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddressDialog by remember { mutableStateOf(false) }
  var showChatDialog by remember { mutableStateOf(false) }
  var selectedTab by remember { mutableStateOf(0) } // 0: Orders, 1: Addresses, 2: Wishlist, 3: Support

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("customer_profile_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // User Profile Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "AK",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = profile.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
              Text(
                text = profile.phone,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = profile.email,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(8.dp))

          // Dark Mode & Support Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (profile.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Dark Mode Theme", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Switch(
              checked = profile.isDarkMode,
              onCheckedChange = { onToggleDarkMode() }
            )
          }
        }
      }
    }

    // Secondary Navigation Tabs: Orders, Addresses, Wishlist, Support
    item {
      val tabs = listOf("Orders (${orders.size})", "Addresses (${profile.addresses.size})", "Wishlist (${profile.wishlistProductIds.size})", "24x7 Support")
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 0.dp,
        containerColor = Color.Transparent,
        divider = {}
      ) {
        tabs.forEachIndexed { index, tabTitle ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = { Text(tabTitle, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
          )
        }
      }
    }

    // Tab Content
    when (selectedTab) {
      0 -> {
        // Orders List
        if (orders.isEmpty()) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("No orders placed yet.")
            }
          }
        } else {
          items(orders) { order ->
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              border = CardDefaults.outlinedCardBorder(),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(text = "Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = order.date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(
                        if (order.status == OrderStatus.DELIVERED) SuccessGreenBg else AaruhaYellowContainer
                      )
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = order.status.title,
                      color = if (order.status == OrderStatus.DELIVERED) SuccessGreen else Color(0xFF92400E),
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))
                order.items.forEach { itm ->
                  Text(
                    text = "${itm.emoji} ${itm.productName} (${itm.weight}) × ${itm.quantity}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Total: ₹${order.finalAmount.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )

                  Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (order.status != OrderStatus.DELIVERED && order.status != OrderStatus.CANCELLED) {
                      Button(
                        onClick = { onTrackOrder(order.id) },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AaruhaGreenPrimary)
                      ) {
                        Text("Live Track", fontSize = 12.sp)
                      }
                    }
                    OutlinedButton(
                      onClick = { onReorder(order.id) },
                      shape = RoundedCornerShape(8.dp),
                      contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                      Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Reorder", fontSize = 12.sp)
                    }
                  }
                }
              }
            }
          }
        }
      }

      1 -> {
        // Addresses List
        item {
          Button(
            onClick = { showAddressDialog = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.AddLocation, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Delivery Address")
          }
        }

        items(profile.addresses) { addr ->
          Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
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
              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(addr.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  if (addr.isDefault) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SuccessGreenBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text("DEFAULT", color = SuccessGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(addr.fullAddress, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Landmark: ${addr.landmark}, ${addr.city} - ${addr.pincode}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }

              if (!addr.isDefault) {
                TextButton(onClick = { onSetDefaultAddress(addr.id) }) {
                  Text("Set Default", fontSize = 11.sp)
                }
              }
            }
          }
        }
      }

      2 -> {
        // Wishlist
        val wishlisted = products.filter { profile.wishlistProductIds.contains(it.id) }
        if (wishlisted.isEmpty()) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("Your wishlist is empty.")
            }
          }
        } else {
          items(wishlisted) { prod ->
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              border = CardDefaults.outlinedCardBorder(),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(prod.emoji, fontSize = 28.sp)
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("₹${prod.sellingPrice.toInt()}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  }
                }
                Text("⚡ 8 mins", fontSize = 11.sp, color = AaruhaGreenPrimary, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      3 -> {
        // Support Tab
        item {
          Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text("Aaruha Customer Care", fontWeight = FontWeight.Bold, fontSize = 16.sp)
              Spacer(modifier = Modifier.height(6.dp))
              Text("Domain: aaruhaservices.in", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("Email: support@aaruhaservices.in", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("Phone: 1800-200-AARUHA (+91 98480 22334)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Spacer(modifier = Modifier.height(14.dp))
              Button(
                onClick = { showChatDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
              ) {
                Icon(Icons.Default.SupportAgent, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Live Chat Support")
              }
            }
          }
        }
      }
    }
  }

  // Add Address Dialog
  if (showAddressDialog) {
    var title by remember { mutableStateOf("Home") }
    var flat by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Hyderabad") }
    var pincode by remember { mutableStateOf("500081") }

    AlertDialog(
      onDismissRequest = { showAddressDialog = false },
      title = { Text("Add Delivery Address") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Label (Home/Work)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = flat, onValueChange = { flat = it }, label = { Text("Flat / House / Building") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = landmark, onValueChange = { landmark = it }, label = { Text("Landmark") }, modifier = Modifier.fillMaxWidth())
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, modifier = Modifier.weight(1f))
          }
        }
      },
      confirmButton = {
        Button(onClick = {
          if (flat.isNotBlank()) {
            onAddAddress(
              Address(
                id = "addr_${System.currentTimeMillis()}",
                title = title,
                fullAddress = flat,
                landmark = landmark,
                city = city,
                pincode = pincode,
                isDefault = false
              )
            )
            showAddressDialog = false
          }
        }) {
          Text("Save Address")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddressDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Support Chat Dialog
  if (showChatDialog) {
    var chatInput by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showChatDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.SupportAgent, contentDescription = null, tint = AaruhaGreenPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Aaruha 24x7 Help Desk")
        }
      },
      text = {
        Column(modifier = Modifier.height(340.dp)) {
          LazyColumn(
            modifier = Modifier
              .weight(1f)
              .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(chatMessages) { msg ->
              Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (msg.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
              ) {
                Card(
                  colors = CardDefaults.cardColors(
                    containerColor = if (msg.isFromUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                  ),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.widthIn(max = 240.dp)
                ) {
                  Column(modifier = Modifier.padding(10.dp)) {
                    Text(msg.message, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      msg.timestamp,
                      fontSize = 9.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.align(Alignment.End)
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = chatInput,
              onValueChange = { chatInput = it },
              placeholder = { Text("Ask about orders, delivery, refunds...", fontSize = 11.sp) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(8.dp),
              singleLine = true
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = {
              if (chatInput.isNotBlank()) {
                onSendChatMessage(chatInput)
                chatInput = ""
              }
            }) {
              Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showChatDialog = false }) { Text("Close") }
      }
    )
  }
}

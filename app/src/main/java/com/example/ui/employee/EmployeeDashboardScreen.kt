package com.example.ui.employee

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
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.ui.theme.*

@Composable
fun EmployeeDashboardScreen(
  products: List<Product>,
  orders: List<Order>,
  onUpdateStock: (String, Int) -> Unit,
  onOrderPackCompleted: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedTab by remember { mutableStateOf(0) } // 0: Order Fulfillment, 1: Quick Stock Update

  val filteredProducts = if (searchQuery.isBlank()) products else {
    products.filter { it.name.contains(searchQuery, true) || it.brand.contains(searchQuery, true) }
  }

  val packingQueue = orders.filter { it.status == OrderStatus.CONFIRMED || it.status == OrderStatus.PLACED }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("employee_dashboard_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Employee Identity Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(16.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "STORE OPERATIONS & PACKING",
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "Priya Sharma (EMP-102)",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Role: Lead Inventory Specialist • Shift: Active (08:45 AM)",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Role Security Notice (Prompt: "Employees Cannot View Sensitive Revenue Data or Change Admin Settings")
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Role Restricted: Financial analytics & system admin settings are protected.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Tab Navigation
    item {
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.Transparent
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Packing Queue (${packingQueue.size})", fontWeight = FontWeight.Bold) }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("Inventory Stock (${products.size})", fontWeight = FontWeight.Bold) }
        )
      }
    }

    when (selectedTab) {
      0 -> {
        // Packing Queue
        if (packingQueue.isEmpty()) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✅", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("All orders packed!", fontWeight = FontWeight.Bold)
                Text("Waiting for new incoming customer orders...", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        } else {
          items(packingQueue) { ord ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              border = CardDefaults.outlinedCardBorder(),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text("Order #${ord.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Placed ${ord.date}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(AaruhaYellowContainer)
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text("Target: 3 Mins", color = Color(0xFF92400E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Items to Bag:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                ord.items.forEach { itm ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text("• ${itm.emoji} ${itm.productName} (${itm.weight})", fontSize = 12.sp)
                    Text("Qty: ${itm.quantity}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                  onClick = { onOrderPackCompleted(ord.id) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = AaruhaGreenPrimary)
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Mark Packed & Hand Over To Rider", fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      1 -> {
        // Quick Stock Editor
        item {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter items to update stock...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )
        }

        items(filteredProducts) { prod ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Text(prod.emoji, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                  Text("Category: ${prod.categoryName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  Text(
                    text = "Current Stock: ${prod.stockQuantity} units",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (prod.stockQuantity > 5) SuccessGreen else DiscountRed
                  )
                }
              }

              // Instant Quick Adjust Buttons
              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedButton(
                  onClick = { onUpdateStock(prod.id, -1) },
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text("-1")
                }
                Button(
                  onClick = { onUpdateStock(prod.id, 5) },
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text("+5")
                }
                Button(
                  onClick = { onUpdateStock(prod.id, 20) },
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text("+20")
                }
              }
            }
          }
        }
      }
    }
  }
}

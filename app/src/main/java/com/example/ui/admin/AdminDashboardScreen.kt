package com.example.ui.admin

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
  orders: List<Order>,
  employees: List<Employee>,
  deliveryPartners: List<DeliveryPartner>,
  products: List<Product>,
  categories: List<ProductCategory>,
  monthlyRevenue: List<MonthlyRevenueRecord>,
  onAddEmployee: (Employee) -> Unit,
  onToggleEmployeeStatus: (String) -> Unit,
  onSaveProduct: (Product) -> Unit,
  onDeleteProduct: (String) -> Unit,
  onUpdateStock: (String, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableStateOf(0) } // 0: Executive KPIs & Revenue, 1: Employees, 2: Products, 3: Delivery Fleet
  var showAddEmployeeDialog by remember { mutableStateOf(false) }
  var showAddProductDialog by remember { mutableStateOf(false) }

  val totalOrders = orders.size + 1420
  val pendingOrders = orders.count { it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED }
  val deliveredOrders = orders.count { it.status == OrderStatus.DELIVERED } + 1410
  val cancelledOrders = orders.count { it.status == OrderStatus.CANCELLED } + 10

  val todayRevenue = 74500.0
  val monthlyTotal = monthlyRevenue.lastOrNull()?.revenue ?: 726000.0
  val monthlyProfit = monthlyRevenue.lastOrNull()?.profit ?: 236000.0

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("admin_dashboard_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Admin Header
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "AARUHA GROCERIES ADMIN",
                color = AaruhaYellowAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
              )
              Text(
                text = "Executive Control Panel",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White)
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Domain: aaruhaservices.in • Dark Store Cluster #01 (Madhapur, Hyd)",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 11.sp
          )
        }
      }
    }

    // Tabs
    item {
      val tabs = listOf("Revenue & KPIs", "Employees (${employees.size})", "Catalog (${products.size})", "Riders (${deliveryPartners.size})")
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 0.dp,
        containerColor = Color.Transparent,
        divider = {}
      ) {
        tabs.forEachIndexed { idx, title ->
          Tab(
            selected = selectedTab == idx,
            onClick = { selectedTab = idx },
            text = { Text(title, fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal) }
          )
        }
      }
    }

    when (selectedTab) {
      0 -> {
        // KPI Cards Grid (2x2)
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            KpiCard(
              title = "Today's Revenue",
              value = "₹${todayRevenue.toInt()}",
              subtitle = "+18% vs yesterday",
              icon = Icons.Default.CurrencyRupee,
              color = AaruhaGreenPrimary,
              modifier = Modifier.weight(1f)
            )
            KpiCard(
              title = "Net Profit (MTD)",
              value = "₹${monthlyProfit.toInt()}",
              subtitle = "32.5% Margin",
              icon = Icons.Default.TrendingUp,
              color = Color(0xFF0284C7),
              modifier = Modifier.weight(1f)
            )
          }
        }

        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            KpiCard(
              title = "Total Orders",
              value = totalOrders.toString(),
              subtitle = "$deliveredOrders Delivered",
              icon = Icons.Default.ShoppingBag,
              color = Color(0xFFD97706),
              modifier = Modifier.weight(1f)
            )
            KpiCard(
              title = "Active Staff",
              value = "${employees.count { it.status == "Active" }} Staff / ${deliveryPartners.count { it.isActive }} Riders",
              subtitle = "100% On-Duty",
              icon = Icons.Default.People,
              color = Color(0xFF7C3AED),
              modifier = Modifier.weight(1f)
            )
          }
        }

        // Monthly Income & Expenses Table
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
            shape = RoundedCornerShape(14.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Monthly Financial Breakdown",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  IconButton(onClick = {
                    Toast.makeText(context, "Exporting Financial Report (PDF)...", Toast.LENGTH_SHORT).show()
                  }) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF", tint = DiscountRed, modifier = Modifier.size(20.dp))
                  }
                  IconButton(onClick = {
                    Toast.makeText(context, "Exporting Revenue Sheet (Excel)...", Toast.LENGTH_SHORT).show()
                  }) {
                    Icon(Icons.Default.TableChart, contentDescription = "Export Excel", tint = AaruhaGreenPrimary, modifier = Modifier.size(20.dp))
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Table Header
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(MaterialTheme.colorScheme.surfaceVariant)
                  .padding(vertical = 8.dp, horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Month", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.3f))
                Text("Orders", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(0.9f))
                Text("Revenue", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.2f))
                Text("Expenses", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.2f))
                Text("Profit", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.2f))
              }

              monthlyRevenue.forEach { rec ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(rec.month, fontSize = 11.sp, modifier = Modifier.weight(1.3f))
                  Text("${rec.ordersCount}", fontSize = 11.sp, modifier = Modifier.weight(0.9f))
                  Text("₹${(rec.revenue / 1000).toInt()}k", fontSize = 11.sp, modifier = Modifier.weight(1.2f))
                  Text("₹${(rec.expenses / 1000).toInt()}k", fontSize = 11.sp, color = DiscountRed, modifier = Modifier.weight(1.2f))
                  Text("₹${(rec.profit / 1000).toInt()}k", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
              }
            }
          }
        }
      }

      1 -> {
        // Employee Management
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Store Employees (${employees.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Button(
              onClick = { showAddEmployeeDialog = true },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Employee", fontSize = 12.sp)
            }
          }
        }

        items(employees) { emp ->
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
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emp.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (emp.status == "Active") SuccessGreenBg else DiscountRedBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        emp.status,
                        color = if (emp.status == "Active") SuccessGreen else DiscountRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                  Text(
                    text = "${emp.designation} (${emp.id})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                  )
                }

                TextButton(onClick = { onToggleEmployeeStatus(emp.id) }) {
                  Text(if (emp.status == "Active") "Mark Leave" else "Mark Active", fontSize = 11.sp)
                }
              }

              Spacer(modifier = Modifier.height(6.dp))
              Text("📞 ${emp.phone} • ✉️ ${emp.email}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("🎓 ${emp.education} (${emp.qualification})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("🆔 Aadhaar: ${emp.aadhaarNumber}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("💰 Salary: ₹${emp.salary.toInt()} / month", fontSize = 11.sp, fontWeight = FontWeight.Bold)

              Spacer(modifier = Modifier.height(8.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(6.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant)
                  .padding(8.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Login: ${emp.loginTime}", fontSize = 11.sp)
                  Text("Hours: ${emp.workingHoursToday} hrs", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      2 -> {
        // Product Management
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Product Catalog (${products.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Button(
              onClick = { showAddProductDialog = true },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Product", fontSize = 12.sp)
            }
          }
        }

        items(products) { prod ->
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
                Text(prod.emoji, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                  Text("${prod.categoryName} • ${prod.brand}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  Text("Selling: ₹${prod.sellingPrice.toInt()} (MRP ₹${prod.mrp.toInt()})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
              }

              // Stock Management Stepper
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "Stock: ${prod.stockQuantity}",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (prod.stockQuantity > 10) SuccessGreen else DiscountRed
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  OutlinedIconButton(
                    onClick = { onUpdateStock(prod.id, -5) },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Text("-5", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  }
                  Spacer(modifier = Modifier.width(4.dp))
                  Button(
                    onClick = { onUpdateStock(prod.id, 10) },
                    modifier = Modifier.height(28.dp),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                  ) {
                    Text("+10", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }
      }

      3 -> {
        // Delivery Partners Fleet
        item {
          Text("Delivery Partners (${deliveryPartners.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        items(deliveryPartners) { rider ->
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("🛵", fontSize = 24.sp)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(rider.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(rider.vehicleType, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (rider.isActive) SuccessGreenBg else DiscountRedBg)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    if (rider.isActive) "Active" else "Offline",
                    color = if (rider.isActive) SuccessGreen else DiscountRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text("Vehicle No: ${rider.vehicleNumber} • License: ${rider.drivingLicense}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("Deliveries: ${rider.totalDeliveries} • Today's Earnings: ₹${rider.todayEarnings.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }

  // Add Employee Dialog
  if (showAddEmployeeDialog) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+91 ") }
    var email by remember { mutableStateOf("") }
    var aadhaar by remember { mutableStateOf("XXXX-XXXX-") }
    var designation by remember { mutableStateOf("Fulfillment Associate") }
    var salaryStr by remember { mutableStateOf("28000") }

    AlertDialog(
      onDismissRequest = { showAddEmployeeDialog = false },
      title = { Text("Add Store Employee") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = designation, onValueChange = { designation = it }, label = { Text("Designation") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = salaryStr, onValueChange = { salaryStr = it }, label = { Text("Monthly Salary (₹)") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(onClick = {
          if (name.isNotBlank()) {
            onAddEmployee(
              Employee(
                id = "EMP-${(100..999).random()}",
                name = name,
                phone = phone,
                email = email,
                aadhaarNumber = aadhaar,
                education = "Graduate",
                qualification = "Certified Store Associate",
                address = "Madhapur, Hyderabad",
                joiningDate = "Today",
                salary = salaryStr.toDoubleOrNull() ?: 25000.0,
                designation = designation,
                role = "Store Operations",
                status = "Active",
                loginTime = "09:00 AM",
                logoutTime = "Pending",
                workingHoursToday = 0.0
              )
            )
            showAddEmployeeDialog = false
          }
        }) {
          Text("Save Employee")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddEmployeeDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Add Product Dialog
  if (showAddProductDialog) {
    var prodName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("Aaruha") }
    var priceStr by remember { mutableStateOf("99") }
    var mrpStr by remember { mutableStateOf("120") }
    var stockStr by remember { mutableStateOf("50") }
    var emoji by remember { mutableStateOf("🥑") }

    AlertDialog(
      onDismissRequest = { showAddProductDialog = false },
      title = { Text("Add New Product") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = prodName, onValueChange = { prodName = it }, label = { Text("Product Name") }, modifier = Modifier.fillMaxWidth())
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Brand") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = emoji, onValueChange = { emoji = it }, label = { Text("Emoji") }, modifier = Modifier.weight(1f))
          }
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = mrpStr, onValueChange = { mrpStr = it }, label = { Text("MRP (₹)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Selling Price (₹)") }, modifier = Modifier.weight(1f))
          }
          OutlinedTextField(value = stockStr, onValueChange = { stockStr = it }, label = { Text("Stock Quantity") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(onClick = {
          if (prodName.isNotBlank()) {
            val mrp = mrpStr.toDoubleOrNull() ?: 100.0
            val sp = priceStr.toDoubleOrNull() ?: 80.0
            onSaveProduct(
              Product(
                id = "prod_${System.currentTimeMillis()}",
                name = prodName,
                categoryId = "cat_fv",
                categoryName = "Fruits & Vegetables",
                brand = brand,
                weight = "500 g",
                availableWeights = listOf("500 g", "1 kg"),
                mrp = mrp,
                sellingPrice = sp,
                stockQuantity = stockStr.toIntOrNull() ?: 50,
                description = "Freshly sourced high quality item from Aaruha Groceries.",
                emoji = emoji.ifBlank { "🥑" },
                rating = 4.8,
                reviewCount = 10,
                isBestSeller = false,
                isFlashDeal = true
              )
            )
            showAddProductDialog = false
          }
        }) {
          Text("Add Product")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddProductDialog = false }) { Text("Cancel") }
      }
    )
  }
}

@Composable
private fun KpiCard(
  title: String,
  value: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = CardDefaults.outlinedCardBorder(),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
      Text(subtitle, fontSize = 10.sp, color = color, fontWeight = FontWeight.Bold)
    }
  }
}

package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object EntreeMenu : Screen("entree_menu")
    object SideDishMenu : Screen("side_dish_menu")
    object AccompanimentMenu : Screen("accompaniment_menu")
    object Checkout : Screen("checkout")
}

data class Order(
    var entree: String = "",
    var sideDish: String = "",
    var accompaniment: String = "",
    var totalPrice: Double = 0.0
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val order = remember { mutableStateOf(Order()) }
    AppNavHost(navController = navController, order = order)
}

@Composable
fun AppNavHost(navController: NavHostController, order: MutableState<Order>) {
    NavHost(navController = navController, startDestination = Screen.Start.route) {
        composable(Screen.Start.route) { StartScreen(navController) }
        composable(Screen.EntreeMenu.route) { EntreeMenuScreen(navController, order) }
        composable(Screen.SideDishMenu.route) { SideDishMenuScreen(navController, order) }
        composable(Screen.AccompanimentMenu.route) { AccompanimentMenuScreen(navController, order) }
        composable(Screen.Checkout.route) { CheckoutScreen(navController, order) }
    }
}

@Composable
fun TopBar(title: String, canNavigateBack: Boolean, navigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = if (canNavigateBack) {
            {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        } else null
    )
}

@Composable
fun StartScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Start Screen",
                canNavigateBack = false,
                navigateUp = { }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate(Screen.EntreeMenu.route) }) {
                Text("Start Order")
            }
        }
    }
}

data class MenuItem(
    val title: String,
    val description: String,
    val price: Double
)

@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        backgroundColor = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.1f) else MaterialTheme.colors.surface,
        elevation = if (isSelected) 4.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(menuItem.title, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
                Text(menuItem.description, style = MaterialTheme.typography.body1)
                Text("${menuItem.price}$", style = MaterialTheme.typography.subtitle2)
            }
            RadioButton(
                selected = isSelected,
                onClick = { onClick() }
            )
        }
    }
}

fun resetOrder(order: MutableState<Order>, selectedEntree: MutableState<String>, selectedSideDish: MutableState<String>, selectedAccompaniment: MutableState<String>) {
    order.value = Order()
    selectedEntree.value = "None"
    selectedSideDish.value = "None"
    selectedAccompaniment.value = "None"
}

@Composable
fun EntreeMenuScreen(navController: NavHostController, order: MutableState<Order>) {
    val entrees = listOf(
        MenuItem("Matoke", "Steamed green bananas served with sauces or meats.", 12.99),
        MenuItem("Jollof Rice", "Flavorful rice cooked in tomato sauce, often served with chicken or fish.", 10.99),
        MenuItem("Braai", "Traditional South African barbecue featuring grilled meats and various side dishes.", 14.99),
        MenuItem("Efo Riro", "Spicy vegetable soup made from leafy greens and served with rice or yam.", 11.99),
    )
    var selectedEntree by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = "Entree Menu",
                canNavigateBack = true,
                navigateUp = { navController.popBackStack() }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            entrees.forEach { menuItem ->
                MenuItemCard(
                    menuItem = menuItem,
                    isSelected = selectedEntree == menuItem.title,
                    onClick = {
                        selectedEntree = menuItem.title
                        order.value = order.value.copy(entree = menuItem.title, totalPrice = menuItem.price)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
                Button(onClick = { navController.navigate(Screen.SideDishMenu.route) }) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun SideDishMenuScreen(navController: NavHostController, order: MutableState<Order>) {
    val sideDishes = listOf(
        MenuItem("Pasta Shells", "Short, curved pasta shapes.", 5.99),
        MenuItem("Fried Nails", "Crispy fried vegetable slices, often seasoned and served as a crunchy side dish.", 4.99),
        MenuItem("Steamed Turnip", "Tender, lightly steamed turnip slices; a healthy and flavorful vegetable side.", 3.99)
    )
    var selectedSideDish by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = "Side Dish Menu",
                canNavigateBack = true,
                navigateUp = { navController.popBackStack() }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sideDishes.forEach { menuItem ->
                MenuItemCard(
                    menuItem = menuItem,
                    isSelected = selectedSideDish == menuItem.title,
                    onClick = {
                        selectedSideDish = menuItem.title
                        order.value = order.value.copy(sideDish = menuItem.title, totalPrice = order.value.totalPrice + menuItem.price)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Previous")
                }
                Button(onClick = { navController.navigate(Screen.AccompanimentMenu.route) }) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun AccompanimentMenuScreen(navController: NavHostController, order: MutableState<Order>) {
    val accompaniments = listOf(
        MenuItem("XRUCHEVO", "ketchup, sprite, salt, strawberry", 0.99),
        MenuItem("Soda", "Assorted soft drinks", 1.99),
        MenuItem("Water", "Bottled spring water", 0.99)
    )
    var selectedAccompaniment by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = "Accompaniment Menu",
                canNavigateBack = true,
                navigateUp = { navController.popBackStack() }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            accompaniments.forEach { menuItem ->
                MenuItemCard(
                    menuItem = menuItem,
                    isSelected = selectedAccompaniment == menuItem.title,
                    onClick = {
                        selectedAccompaniment = menuItem.title
                        order.value = order.value.copy(accompaniment = menuItem.title, totalPrice = order.value.totalPrice + menuItem.price)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Previous")
                }
                Button(onClick = { navController.navigate(Screen.Checkout.route) }) {
                    Text("Checkout")
                }
            }
        }
    }
}

@Composable
fun CheckoutScreen(navController: NavHostController, order: MutableState<Order>) {
    var showConfirmation by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Order Checkout",
                canNavigateBack = true,
                navigateUp = {
                    resetOrder(order, mutableStateOf("None"), mutableStateOf("None"), mutableStateOf("None"))
                    navController.popBackStack()
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showConfirmation) {
                Text("Thank you for your order!")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    resetOrder(order, mutableStateOf("None"), mutableStateOf("None"), mutableStateOf("None"))
                    navController.navigate(Screen.Start.route)
                }) {
                    Text("Back to Start")
                }
            } else {
                if (showErrorMessage) {
                    Text("You have not selected anything!", color = MaterialTheme.colors.error)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text("Order Summary")
                Spacer(modifier = Modifier.height(16.dp))

                Text("Entree: ${if (order.value.entree.isEmpty()) "None" else order.value.entree}")
                Text("Side Dish: ${if (order.value.sideDish.isEmpty()) "None" else order.value.sideDish}")
                Text("Accompaniment: ${if (order.value.accompaniment.isEmpty()) "None" else order.value.accompaniment}")

                val totalPrice = String.format("%.2f", order.value.totalPrice)
                Text("Total: $totalPrice$")
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (order.value.entree.isEmpty() && order.value.sideDish.isEmpty() && order.value.accompaniment.isEmpty()) {
                        showErrorMessage = true
                    } else {
                        showConfirmation = true
                        showErrorMessage = false
                    }
                }) {
                    Text("Submit Order")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    resetOrder(order, mutableStateOf("None"), mutableStateOf("None"), mutableStateOf("None"))
                    navController.popBackStack()
                }) {
                    Text("Edit Order")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val order = remember { mutableStateOf(Order()) }
    MaterialTheme {
        StartScreen(navController = NavHostController(LocalContext.current))
    }
}

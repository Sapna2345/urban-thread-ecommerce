const BASE_URL = "http://localhost:8080";
let allProducts = [];

async function loadProducts() {
  const productList = document.getElementById("product-list");
  if (!productList) {
    return;
  }

  try {
    const response = await fetch(`${BASE_URL}/products`);
    const products = await response.json();
    const uniqueProducts = [];
    const seenNames = new Set();
products.forEach((product) => { 
  const productName = product.name.trim().toLowerCase();

  if (!seenNames.has(productName)) {
    seenNames.add(productName);
    uniqueProducts.push(product);
  }
});

const allowedCategories = ["women", "men", "accessories", "clothing", "electronics", "gadgets"];

allProducts = uniqueProducts.filter((product) => {
  return product.category &&
         allowedCategories.includes(product.category.trim().toLowerCase());
});
    renderProducts(allProducts);
    return;
    productList.innerHTML = "";

    products.forEach((product) => {
      productList.innerHTML += `
                <div class="col-lg-3 col-md-6">
                    <div class="product-card">
                        <img src="${product.imageUrl}" class="product-image" alt="${product.name}">

                        <div class="product-info">
                            <p class="product-category">${product.category}</p>
                            <h3 class="product-name">${product.name}</h3>
                            <p class="product-price">₹ ${product.price}</p>
                            <button class="product-btn" onclick="addToCart(${product.id},'${product.name}',${product.price},'${product.imageUrl}')">
                            Add To Cart </button>
                        </div>
                    </div>
                </div>
            `;
    });
  } catch (error) {
    productList.innerHTML = `
            <div class="col-12 text-center">
                <p>Could not load products. Please check if backend is running.</p>
            </div>
        `;
    console.log(error);
  }
}
function addToCart(id, name, price, imageUrl) {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];
   
  const existingItem = cart.find((item) => item.id === id);

  if (existingItem) {
    existingItem.quantity += 1;
  } else {
    cart.push({
      id: id,
      name: name,
      price: price,
      imageUrl: imageUrl,
      quantity: 1,
    });
  }

  localStorage.setItem("cart", JSON.stringify(cart));
  updateCartCount();

  showMessage("Products added to cart");
}
function updateCartCount() {
  const cart = JSON.parse(localStorage.getItem("cart")) || [];
  const cartCount = document.querySelector(".cart-count");

  if (!cartCount) {
    return;
  }

  const totalQuantity = cart.reduce((total, item) => total + item.quantity, 0);

  cartCount.innerText = totalQuantity;
}
function loadCart() {
  const cartContainer = document.getElementById("cart-container");
  const cartTotal = document.getElementById("cart-total");

  if (!cartContainer || !cartTotal) {
    return;
  }

  const cart = JSON.parse(localStorage.getItem("cart")) || [];

  if (cart.length === 0) {
    cartContainer.innerHTML = `
            <div class="empty-cart">
                <p>Your cart is empty.</p>
                <a href="index.html#products" class="hero-btn">Continue Shopping</a>
            </div>
        `;

    cartTotal.innerText = 0;
    return;
  }

  let totalAmount = 0;
  cartContainer.innerHTML = "";

  cart.forEach((item, index) => {
    const itemTotal = item.price * item.quantity;
    totalAmount += itemTotal;

    cartContainer.innerHTML += `
            <div class="cart-item">
                <img src="${item.imageUrl}" alt="${item.name}">

                <div>
                    <h4>${item.name}</h4>
                    <p>₹ ${item.price}</p>
                </div>

                <div class="quantity-controls">
                    <button onclick="changeQuantity(${index}, -1)">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="changeQuantity(${index}, 1)">+</button>
                </div>

                <p>₹ ${itemTotal}</p>

                <button class="remove-btn" onclick="removeFromCart(${index})">×</button>
            </div>
        `;
  });

  cartTotal.innerText = totalAmount;
}
function changeQuantity(index, change) {
  const cart = JSON.parse(localStorage.getItem("cart")) || [];

  cart[index].quantity += change;

  if (cart[index].quantity <= 0) {
    cart.splice(index, 1);
  }

  localStorage.setItem("cart", JSON.stringify(cart));

  loadCart();
  updateCartCount();
}

function removeFromCart(index) {
  const cart = JSON.parse(localStorage.getItem("cart")) || [];

  cart.splice(index, 1);

  localStorage.setItem("cart", JSON.stringify(cart));

  loadCart();
  updateCartCount();
}
async function checkout() {
  const cart = JSON.parse(localStorage.getItem("cart")) || [];
 const token = localStorage.getItem("token");
  if (cart.length === 0) {
    showMessage("Your cart is empty", "error");
    return;
  }

  if (!token) {
    showMessage("Please login before checkout", "error");
    return;
  }


const productQuantities = {};

cart.forEach((item) => {
  productQuantities[item.id] = item.quantity;
});

const orderRequest = {
  productQuantities: productQuantities
};

  try {

    const response = await fetch(`${BASE_URL}/orders/place`, {
      method: "POST",
      headers: {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
  },
      body: JSON.stringify(orderRequest),
    });

    if (!response.ok) {
      throw new Error("Order failed");
    }

    await response.json();

    localStorage.removeItem("cart");

    loadCart();
    updateCartCount();

    showMessage("ordered placed successfully");
  } catch (error) {
    console.log(error);
    showMessage("Could not place order. Please check backend.", "error");
  }
}
function showLogin() {
  document.getElementById("loginForm").classList.remove("d-none");
  document.getElementById("registerForm").classList.add("d-none");

  document.querySelectorAll(".auth-tab")[0].classList.add("active");
  document.querySelectorAll(".auth-tab")[1].classList.remove("active");
}

function showRegister() {
  document.getElementById("registerForm").classList.remove("d-none");
  document.getElementById("loginForm").classList.add("d-none");

  document.querySelectorAll(".auth-tab")[1].classList.add("active");
  document.querySelectorAll(".auth-tab")[0].classList.remove("active");
}
function setupAuthForms() {
  const registerForm = document.getElementById("registerForm");

  if (!registerForm) {
    return;
  }

  registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const name = document.getElementById("registerName").value;
    const email = document.getElementById("registerEmail").value;
    const password = document.getElementById("registerPassword").value;

    const user = {
      name: name,
      email: email,
      password: password,
    };

    try {
      const response = await fetch(`${BASE_URL}/users/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(user),
      });

      if (!response.ok) {
        throw new Error("Register failed");
      }

      const savedUser = await response.json();

      // localStorage.setItem("user", JSON.stringify(savedUser));
      // updateAuthUI();
      showMessage("Registered successfully");
      registerForm.reset();
      closeLoginModal();
    } catch (error) {
      console.log(error);
      showMessage("Could not register. Please try again.", "error");
    }
  });
  const loginForm = document.getElementById("loginForm");

  if (loginForm) {
    loginForm.addEventListener("submit", async (event) => {
      event.preventDefault();

      const email = document.getElementById("loginEmail").value;
      const password = document.getElementById("loginPassword").value;

      const user = {
        email: email,
        password: password,
      };

      try {
        const response = await fetch(`${BASE_URL}/users/login`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(user),
        });

        if (!response.ok) {
          throw new Error("Login failed");
        }

        const loggedInUser = await response.json();

        if (!loggedInUser) {
          showMessage("Invalid email or password", "error");
          return;
        }

        localStorage.setItem("user", JSON.stringify(loggedInUser));
        localStorage.setItem("token", loggedInUser.token);
        updateAuthUI();
        showMessage("Login successfully");
        loginForm.reset();
        closeLoginModal();
      } catch (error) {
        console.log(error);
        showMessage("Could not login. Please try again.", "error");
      }
    });
  }
}
  function updateAuthUI() {
  const user = JSON.parse(localStorage.getItem("user"));
  const token = localStorage.getItem("token");
  const loginLink = document.getElementById("loginLink");
  const logoutBtn = document.getElementById("logoutBtn");
  const myOrdersNav = document.getElementById("myOrdersNav");

  if (!loginLink || !logoutBtn || !myOrdersNav) {
    return;
  }

  if (user) {
    loginLink.classList.add("d-none");
    logoutBtn.classList.remove("d-none");
    myOrdersNav.classList.remove("d-none");
  } else {
    loginLink.classList.remove("d-none");
    logoutBtn.classList.add("d-none");
    myOrdersNav.classList.add("d-none");
  }
}
function closeLoginModal() {
  const loginModalElement = document.getElementById("loginModal");

  if (!loginModalElement) {
    return;
  }

  const loginModal = bootstrap.Modal.getInstance(loginModalElement);

  if (loginModal) {
    loginModal.hide();
  }
}

function logout() {
  localStorage.removeItem("user");
   localStorage.removeItem("token");
  updateAuthUI();
  showMessage("Logged out");
}
async function loadOrders() {
  const ordersContainer = document.getElementById("orders-container");

  if (!ordersContainer) {
    return;
  }

  const user = JSON.parse(localStorage.getItem("user"));
  const token = localStorage.getItem("token");
  if (!user) {
    ordersContainer.innerHTML = `
            <div class="empty-cart">
                <p>Please login to view your orders.</p>
                <a href="index.html" class="hero-btn">Go To Home</a>
            </div>
        `;
    return;
  }
  if (!token) {
  ordersContainer.innerHTML = `
    <div class="empty-cart">
      <p>Please login to view your orders.</p>
      <a href="index.html" class="hero-btn">Go To Home</a>
    </div>
  `;
  return;
}

  try {
 const response = await fetch(`${BASE_URL}/orders/my`, {
  headers: {
    "Authorization": `Bearer ${token}`
  }
});

    if (!response.ok) {
      throw new Error("Could not load orders");
    }

    const orders = await response.json();

    if (orders.length === 0) {
      ordersContainer.innerHTML = `
                <div class="empty-cart">
                    <p>You have no orders yet.</p>
                    <a href="index.html#products" class="hero-btn">Start Shopping</a>
                </div>
            `;
      return;
    }

    ordersContainer.innerHTML = "";

    orders.forEach((order) => {
      ordersContainer.innerHTML += `
                <div class="order-card">
                   <div class="order-header">
                   <div>
                    <p>Order ID</p>
                    <h4>#${order.id}</h4>
                </div>
                  <div>
                  <p>Date</p>
               <h4>${formatOrderDate(order.orderDate)}</h4>
                 </div>
                  <div>
        <p>Status</p>
        <h4><span class="order-status">${order.status}</span></h4>
    </div>
    <div>
        <p>Total</p>
        <h4>₹ ${order.totalAmount}</h4>
    </div>
</div>

                    <div class="order-items">
                        ${order.orderItems
                          .map(
                            (item) => `
                            <div class="order-item-row">
                                <span>${item.productName}</span>
                                <span>Qty: ${item.quantity}</span>
                                <span>₹ ${item.productPrice}</span>
                            </div>
                        `,
                          )
                          .join("")}
                    </div>
                </div>
            `;
    });
  } catch (error) {
    console.log(error);

    ordersContainer.innerHTML = `
            <div class="empty-cart">
                <p>Could not load orders. Please check backend.</p>
            </div>
        `;
  }
}
function showMessage(message, type = "success") {
  const messageBox = document.getElementById("app-message");

  if (!messageBox) {
    return;
  }

  messageBox.innerText = message;
  messageBox.className = `app-message ${type}`;

  setTimeout(() => {
    messageBox.classList.add("d-none");
  }, 2500);
}
function formatOrderDate(orderDate) {
  if (!orderDate) {
    return "N/A";
  }

  const date = new Date(orderDate);

  return date.toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}
function renderProducts(products) {
  const productList = document.getElementById("product-list");
  if (!productList) {
    return;
  }

  productList.innerHTML = "";
  if (products.length === 0) {
    productList.innerHTML = `
        <div class="col-12 text-center">
            <p class="no-products">No products found.</p>
        </div>
    `;
    return;
  }
  products.forEach((product) => {
    productList.innerHTML += `
            <div class="col-lg-3 col-md-6">
                <div class="product-card">
                    <img src="${product.imageUrl}" class="product-image" alt="${product.name}">

                    <div class="product-info">
                        <p class="product-category">${product.category}</p>
                        <h3 class="product-name">${product.name}</h3>
                        <p class="product-price">₹ ${product.price}</p>
                        <button class="product-btn" onclick="addToCart(${product.id}, '${product.name}', ${product.price}, '${product.imageUrl}')">
                            Add To Cart
                        </button>
                    </div>
                </div>
            </div>
        `;
  });
}
function setupProductSearch() {
  const searchInput = document.getElementById("productSearch");

  if (!searchInput) {
    return;
  }

  searchInput.addEventListener("input", () => {
    const searchText = searchInput.value.toLowerCase();

    const filteredProducts = allProducts.filter((product) => {
      return (
        product.name.toLowerCase().includes(searchText) ||
        product.category.toLowerCase().includes(searchText)
      );
    });

    renderProducts(filteredProducts);
  });
}
function filterByCategory(category) {
  const searchInput = document.getElementById("productSearch");

  if (searchInput) {
    searchInput.value = category;
  }

  const filteredProducts = allProducts.filter((product) => {
    return product.category.toLowerCase() === category.toLowerCase();
  });

  renderProducts(filteredProducts);
}

function filterByGroup(group) {
  const searchInput = document.getElementById("productSearch");

  if (searchInput) {
    searchInput.value = group;
  }

  const filteredProducts = allProducts.filter((product) => {
    const name = product.name.toLowerCase();
    const category = product.category.toLowerCase();

    if (group === "men") {
      return name.includes("men") || category === "men";
    }

    if (group === "women") {
      return name.includes("women") || category === "women";
    }

    if (group === "gadgets") {
      return category === "electronics" || category === "gadgets" || category === "accessories";
    }

    return true;
  });

  renderProducts(filteredProducts);
}

document.addEventListener("DOMContentLoaded", () => {
  loadProducts();
  loadCart();
  loadOrders();
  updateCartCount();
  setupAuthForms();
  updateAuthUI();
  setupProductSearch();
});

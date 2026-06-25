let cartService;

class ShoppingCartService {

    cart = {
        items: [],
        total: 0
    };

    addToCart(productId) {
        const url = `${config.baseUrl}/cart/products/${productId}`;

        axios.post(url, null)
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();

                // prevents crashes if the backend returns an empty response, missing items, or null
                const errors = document.getElementById("errors");
                if (errors) {
                    errors.innerHTML = "";
                }

                const data = {
                    message: "Item added to cart."
                };

                templateBuilder.append("message", data, "errors");

                // If the cart page is currently open, refresh it
                const cartHeader = document.querySelector(".cart-header");
                if (cartHeader) {
                    this.loadCartPage();
                }
            })
            .catch(error => {
                const data = {
                    error: "Add to cart failed."
                };

                templateBuilder.append("error", data, "errors");
            });
    }

    updateCart(productId, quantity) {
        const url = `${config.baseUrl}/cart/products/${productId}`;

        const data = {
            quantity: quantity
        };

        axios.put(url, data)
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
                this.loadCartPage();
            })
            .catch(error => {
                const data = {
                    error: "Update cart failed."
                };

                templateBuilder.append("error", data, "errors");
            });
    }

    checkout() {
        const url = `${config.baseUrl}/orders`;

        axios.post(url, null) // update this line
            .then(response => {
                this.cart = {
                    items: [],
                    total: 0
                };

                this.updateCartDisplay();
                this.loadCartPage();

                const errors = document.getElementById("errors");
                if (errors) {
                    errors.innerHTML = "";
                }

                const data = {
                    message: "Item has been purchased."
                };

                templateBuilder.append("message", data, "errors");
            })
            .catch(error => {
                const data = {
                    error: "Checkout cart failed."
                };

                templateBuilder.append("error", data, "errors");
            });
    }

    setCart(data) {
        this.cart = {
            items: [],
            total: 0
        };

        if (!data) {
            return;
        }

        this.cart.total = data.total || 0;

        if (!data.items) {
            return;
        }

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }
    }

    loadCart() {
        const url = `${config.baseUrl}/cart`;

        return axios.get(url)
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
            })
            .catch(error => {
                const data = {
                    error: "Load cart failed."
                };

                templateBuilder.append("error", data, "errors");
            });
    }

    loadCartPage() {
        // templateBuilder.build("cart", this.cart, "main");
        const main = document.getElementById("main")
        main.innerHTML = "";

        let div = document.createElement("div");
        div.classList = "filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div")
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const cartHeader = document.createElement("div")
        cartHeader.classList.add("cart-header")

        const h1 = document.createElement("h1")
        h1.innerText = "Cart";
        cartHeader.appendChild(h1);

        const button = document.createElement("button");
        button.classList.add("btn")
        button.classList.add("btn-danger")
        button.innerText = "Clear";
        button.addEventListener("click", () => this.clearCart());
        cartHeader.appendChild(button)

        contentDiv.appendChild(cartHeader)
        main.appendChild(contentDiv);

        const checkoutButton = document.createElement("button");
        checkoutButton.classList.add("btn");
        checkoutButton.classList.add("btn-danger");
        checkoutButton.innerText = "Checkout";
        checkoutButton.addEventListener("click", () => this.checkout());
        cartHeader.appendChild(checkoutButton);



        // let parent = document.getElementById("cart-item-list");
        this.cart.items.forEach(item => {
            this.buildItem(item, contentDiv)
        });
    }

    buildItem(item, parent) {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        let div = document.createElement("div");
        outerDiv.appendChild(div);

        let h4 = document.createElement("h4");
        h4.innerText = item.product.name;
        div.appendChild(h4);

        let photoDiv = document.createElement("div");
        photoDiv.classList.add("photo");

        let img = document.createElement("img");
        img.src = `/images/products/${item.product.imageUrl}`;
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src);
        });

        photoDiv.appendChild(img);

        let priceH4 = document.createElement("h4");
        priceH4.classList.add("price");
        priceH4.innerText = `$${item.product.price}`;
        photoDiv.appendChild(priceH4);

        outerDiv.appendChild(photoDiv);

        let descriptionDiv = document.createElement("div");
        descriptionDiv.innerText = item.product.description;
        outerDiv.appendChild(descriptionDiv);

        let quantityDiv = document.createElement("div");
        quantityDiv.classList.add("quantity-control", "add-to-cart-button");

        let minusBtn = document.createElement("button");
        minusBtn.classList.add("btn", "btn-sm", "btn-secondary", "add-to-cart-button");
        minusBtn.innerText = "-";
        minusBtn.addEventListener("click", () => {
            this.updateCart(item.product.productId, item.quantity - 1);
        });

        let quantitySpan = document.createElement("span");
        quantitySpan.innerText = item.quantity;

        let plusBtn = document.createElement("button");
        plusBtn.classList.add("btn", "btn-sm", "btn-secondary", "add-to-cart-button");
        plusBtn.innerText = "+";
        plusBtn.addEventListener("click", () => {
            this.updateCart(item.product.productId, item.quantity + 1);
        });

        quantityDiv.appendChild(minusBtn);
        quantityDiv.appendChild(quantitySpan);
        quantityDiv.appendChild(plusBtn);

        outerDiv.appendChild(quantityDiv);

        parent.appendChild(outerDiv);
    }

    clearCart() {
        const url = `${config.baseUrl}/cart`;

        axios.delete(url)
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
                this.loadCartPage();

                // prevents crashes if the backend returns an empty response, missing items, or null
                const errors = document.getElementById("errors");
                if (errors) {
                    errors.innerHTML = "";
                }
            })
            .catch(error => {
                const data = {
                    error: "Empty cart failed."
                };

                templateBuilder.append("error", data, "errors");
            });
    }

    updateCartDisplay() {
        try {
            const itemCount = this.cart.items.length;
            const cartControl = document.getElementById("cart-items")

            cartControl.innerText = itemCount;
        } catch (e) {

        }
    }
}


document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();

    if (userService.isLoggedIn()) {
        cartService.loadCart();
    }

});

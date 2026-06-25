let productService;

class ProductService {

    photos = [];

    filter = {
        cat: undefined,
        minPrice: undefined,
        maxPrice: undefined,
        subCategory: undefined,
        // this version, if statements decide whether to include a filter in the URL.
        //     // checks if maxPrice is "truthy" and so if maxPrice is 0, this condition fails
        //     // That means this URL does not include max price:
        //     if (this.filter.maxPrice) {
        //         const maxP = `maxPrice=${this.filter.maxPrice}`;
        //         if (qs.length > 0) {
        //             qs += `&${maxP}`;
        //         } else {
        //             qs = maxP;
        //         }
        //     }

        queryString: () => {
            let qs = "";

            if (this.filter.cat !== undefined) {
                qs = `cat=${this.filter.cat}`;
            }

            if (this.filter.minPrice !== undefined) {
                const minP = `minPrice=${this.filter.minPrice}`;

                if (qs.length > 0) {
                    qs += `&${minP}`;
                } else {
                    qs = minP;
                }
            }

            if (this.filter.maxPrice !== undefined) {
                const maxP = `maxPrice=${this.filter.maxPrice}`;

                if (qs.length > 0) {
                    qs += `&${maxP}`;
                } else {
                    qs = maxP;
                }
            }

            if (this.filter.subCategory !== undefined) {
                const sub = `subCategory=${this.filter.subCategory}`;

                if (qs.length > 0) {
                    qs += `&${sub}`;
                } else {
                    qs = sub;
                }
            }

            return qs.length > 0 ? `?${qs}` : "";
        }
    }

    loadProductsByCategory(categoryId) {
        const url = `${config.baseUrl}/categories/${categoryId}/products`;

        axios.get(url)
            .then((response) => {
                this.products = response.data;
                this.showProducts();
            })
            .catch((error) => {
                const data = {
                    error: 'Load products by category failed.'
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    constructor() {

        //load list of photos into memory
        axios.get("./images/products/photos.json")
            .then(response => {
                this.photos = response.data;
            });
    }

    hasPhoto(photo) {
        return this.photos.filter(p => p == photo).length > 0;
    }

    addCategoryFilter(cat) {
        if (cat == 0) this.clearCategoryFilter();
        else this.filter.cat = cat;
    }

    addMinPriceFilter(price) {
        if (price == 0 || price == "") this.clearMinPriceFilter();
        else this.filter.minPrice = price;
    }

    addMaxPriceFilter(price) {
        // removing (price == 0 || price == "") since it means when max price is 0, it removes the max filter entirely.
        if (price == "") this.clearMaxPriceFilter();
        else this.filter.maxPrice = price;
    }

    addSubcategoryFilter(subCategory) {
        if (subCategory == "") this.clearSubcategoryFilter();
        else this.filter.subCategory = subCategory;
    }

    clearCategoryFilter() {
        this.filter.cat = undefined;
    }

    clearMinPriceFilter() {
        this.filter.minPrice = undefined;
    }

    clearMaxPriceFilter() {
        this.filter.maxPrice = undefined;
    }

    clearSubcategoryFilter() {
        this.filter.subCategory = undefined;
    }

    search() {
        const url = `${config.baseUrl}/products${this.filter.queryString()}`;

        axios.get(url)
            .then(response => {
                let data = {};
                data.products = response.data;

                data.products.forEach(product => {
                    if (!this.hasPhoto(product.imageUrl)) {
                        product.imageUrl = "no-image.jpg";
                    }
                })

                templateBuilder.build('product', data, 'content', this.enableButtons);

            })
            .catch(error => {

                const data = {
                    error: "Searching products failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    enableButtons() {
        const buttons = [...document.querySelectorAll(".add-button")];

        if (userService.isLoggedIn()) {
            buttons.forEach(button => {
                button.classList.remove("invisible")
            });
        } else {
            buttons.forEach(button => {
                button.classList.add("invisible")
            });
        }
    }

    setHighestPriceFromProducts() {
        const url = `${config.baseUrl}/products`;

        axios.get(url).then(response => {
            const products = response.data;

            if (products.length === 0) {
                return;
            }

            const highestPrice = Math.ceil(Math.max(...products.map(p => p.price)));

            const maxPriceInput = document.getElementById("max-price");
            const maxPriceDisplay = document.getElementById("max-price-display");

            // if the max price slider or display does not exist, stop the method instead of crashing
            if (!maxPriceInput || !maxPriceDisplay) {
                return;
            }

            maxPriceInput.max = highestPrice;
            maxPriceInput.value = highestPrice;
            maxPriceDisplay.innerText = highestPrice;

            // important: do NOT force a maxPrice filter on initial page load
            this.clearMaxPriceFilter();
        })
            .catch(error => {
                const data = {
                    error: "Load max price failed."
                };

                templateBuilder.append("error", data, "errors");
            });
    }

}

// ------------------------------------------------------------------

document.addEventListener('DOMContentLoaded', () => {
    productService = new ProductService();
    productService.setHighestPriceFromProducts();
});

function setCategory(control) {
    productService.addCategoryFilter(control.value);
    productService.search();
}

function setMinPrice(control) {
    document.getElementById("min-price-display").innerText = control.value;

    productService.addMinPriceFilter(control.value);
    productService.search();
}

function setMaxPrice(control) {
    document.getElementById("max-price-display").innerText = control.value;

    productService.addMaxPriceFilter(control.value);
    productService.search();
}

function setSubcategory(control) {
    productService.addSubcategoryFilter(control.value);
    productService.search();
}

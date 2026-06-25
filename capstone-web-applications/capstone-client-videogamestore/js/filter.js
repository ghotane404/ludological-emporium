function loadCategories(categories) {
    const select = document.getElementById('category-select');

    categories.forEach(c => {
        const option = document.createElement('option');
        option.setAttribute('value', c.categoryId);
        option.innerText = c.name;
        select.appendChild(option);
    })
}

document.addEventListener('DOMContentLoaded', () => {
    const select = document.getElementById('category-select');

    select.addEventListener('change', () => {
        const categoryId = select.value;

        if (categoryId === "") {
            productService.loadProducts();
        }
        else{
            productService.loadProductsByCategory(categoryId);
        }
    });

});

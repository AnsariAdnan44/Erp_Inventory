const API_URL = "http://localhost:8080/api/inventory";

// Load inventory on page load
function loadInventory() {
    fetch(API_URL)
        .then(res => res.json())
        .then(data => {
            const table = document.getElementById("inventoryTable");
            table.innerHTML = ""; // clear existing rows

            data.forEach(item => {
                table.innerHTML += `
                    <tr>
                        <td>${item.productName}</td>
                        <td>${item.quantity}</td>
                    </tr>
                `;
            });
        })
        .catch(err => console.error("Error fetching inventory:", err));
}

// Call loadInventory when page loads
loadInventory();

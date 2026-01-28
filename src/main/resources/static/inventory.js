const INVENTORY_API = "http://localhost:8080/api/inventory";

function loadInventory() {
    fetch(INVENTORY_API)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch inventory");
            }
            return response.json();
        })
        .then(data => {
            const tableBody = document.getElementById("inventoryTable");
            tableBody.innerHTML = "";

            if (data.length === 0) {
                tableBody.innerHTML = `
                    <tr>
                        <td colspan="2" style="text-align:center;color:#999;">
                            No inventory available
                        </td>
                    </tr>`;
                return;
            }

            data.forEach(item => {
                const qtyClass = item.quantity < 10 ? "low" : "ok";

                tableBody.innerHTML += `
                    <tr>
                        <td>${item.productName}</td>
                        <td class="${qtyClass}">
                            ${item.quantity}
                        </td>
                    </tr>
                `;
            });
        })
        .catch(error => {
            console.error("Inventory error:", error);
        });
}

// auto-refresh every 5 seconds
setInterval(loadInventory, 5000);
loadInventory();

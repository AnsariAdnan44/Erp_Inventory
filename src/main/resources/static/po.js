const API_URL = "http://localhost:8080/api/po";
const VENDOR_API = "http://localhost:8080/api/vendors";

let items = [];

// Load vendors for the dropdown
function loadVendors() {
    fetch(VENDOR_API)
        .then(res => res.json())
        .then(data => {
            const select = document.getElementById("vendorSelect");
            select.innerHTML = "";
            data.forEach(v => {
                select.innerHTML += `<option value="${v.id}">${v.name}</option>`;
            });
        });
}

// Add a row to items table
function addItem() {
    const tbody = document.querySelector("#itemsTable tbody");
    const row = document.createElement("tr");

    row.innerHTML = `
        <td><input type="text" placeholder="Product Name"></td>
        <td><input type="number" placeholder="Quantity"></td>
        <td><button type="button">Remove</button></td>
    `;

    // Remove row
    row.querySelector("button").addEventListener("click", () => {
        tbody.removeChild(row);
    });

    tbody.appendChild(row);
}

// Create PO
function createPO() {
    const vendorId = document.getElementById("vendorSelect").value;

    const itemRows = document.querySelectorAll("#itemsTable tbody tr");
    const poItems = [];

    itemRows.forEach(row => {
        const product = row.querySelector("td:nth-child(1) input").value;
        const qty = parseInt(row.querySelector("td:nth-child(2) input").value);
        if (product && qty > 0) {
            poItems.push({ productName: product, quantity: qty });
        }
    });

    if (!vendorId || poItems.length === 0) {
        alert("Select vendor and add at least one item!");
        return;
    }

    const po = {
        vendor: { id: Number(vendorId) },
        items: poItems
    };

    fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(po)
    }).then(() => {
        loadPOs();
        document.querySelector("#itemsTable tbody").innerHTML = "";
    });
}

// Load all existing POs
function loadPOs() {
    fetch(API_URL)
        .then(res => res.json())
        .then(data => {
            const table = document.getElementById("poTable");
            table.innerHTML = "";
            data.forEach(po => {
                table.innerHTML += `
                    <tr>
                        <td>${po.id}</td>
                        <td>${po.vendor ? po.vendor.name : po.vendorId}</td>
                        <td>${po.status}</td>
                        <td>
                            ${po.status === "CREATED" ? `<button onclick="approvePO(${po.id})">Approve</button>` : ""}
                            ${po.status === "APPROVED" ? `<button onclick="receiveGoods(${po.id})">Receive</button>` : ""}
                        </td>
                    </tr>
                `;
            });
        });
}

// Approve PO
function approvePO(id) {
    fetch(`${API_URL}/${id}/approve`, { method: "PUT" })
        .then(() => loadPOs());
}

// Receive Goods
function receiveGoods(id) {
    fetch(`${API_URL}/${id}/receive`, { method: "POST" })
        .then(() => loadPOs());
}

function receivePO(id) {
    fetch(`http://localhost:8080/api/po/${id}/receive`, {
        method: "POST"
    })
    .then(() => {
        alert("Goods received!");
        loadPOs();
    });
}


// Initial load
loadVendors();
loadPOs();

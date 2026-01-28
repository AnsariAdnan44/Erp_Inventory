const API_URL = "http://localhost:8080/api/vendors";

function loadVendors() {
    fetch(API_URL)
        .then(res => res.json())
        .then(data => {
            const table = document.getElementById("vendorTable");
            table.innerHTML = "";

            data.forEach(v => {
                table.innerHTML += `
                    <tr>
                        <td>${v.name}</td>
                        <td>${v.phone}</td>
                        <td>${v.email}</td>
                        <td>
                            <button onclick="editVendor(${v.id}, '${v.name}', '${v.phone}', '${v.email}')">Edit</button>
                            <button onclick="deleteVendor(${v.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });
        });
}

function saveVendor() {
    const id = document.getElementById("vendorId").value;

    const nameInput = document.getElementById("name");
    const phoneInput = document.getElementById("phone");
    const emailInput = document.getElementById("email");

    const vendor = {
        name: nameInput.value,
        phone: phoneInput.value,
        email: emailInput.value
    };

    if (id) {
        fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(vendor)
        }).then(loadVendors);
    } else {
        fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(vendor)
        }).then(loadVendors);
    }

    document.getElementById("vendorId").value = "";
    nameInput.value = "";
    phoneInput.value = "";
    emailInput.value = "";
}


function editVendor(id, nameVal, phoneVal, emailVal) {
    document.getElementById("vendorId").value = id;
    document.getElementById("vendorName").value = nameVal;
    document.getElementById("phone").value = phoneVal;
    document.getElementById("email").value = emailVal;
}

function deleteVendor(id) {
    fetch(`${API_URL}/${id}`, { method: "DELETE" })
        .then(loadVendors);
}

loadVendors();
